package com.iprism.elliot.ui.camera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.concurrent.Executors
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.iprism.elliot.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iprism.elliot.R
import com.iprism.elliot.databinding.ActivityCameraBinding
import com.iprism.elliot.domain.cnn.NNModel
import com.iprism.elliot.util.ModelUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService

// TODO: 10/26/2021 Delete Log statements after development

@AndroidEntryPoint
class CameraActivity : AppCompatActivity() {
    private var imageCapture: ImageCapture? = null
    private val cameraViewModel: CameraViewModel by viewModels()

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var binding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cameraProgressCircle.hide()

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        // Collecting ingredients to show with checkboxes on dialog.
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                cameraViewModel.oneTimeIngredientsListUiState.collect {
                    if (it.error is CameraViewModel.IngredientListUiState.Error.NoSuchFood) showToast(
                        getString(R.string.not_supported_food)
                    )
                    if (it.ingredients?.isNotEmpty() == true) {
                        binding.cameraProgressCircle.hide()
                        showConfirmationDialog(it)
                    }
                }
            }
        }

        // Set up the listener for take photo button
        binding.cameraCaptureButton.setOnClickListener {
            cameraViewModel.onEvent(
                CameraEvent.OnCameraButtonClick
            )
            takePhoto()
        }

        binding.backButtonCamera.setOnClickListener {
            startActivity(Intent(baseContext, MainActivity::class.java))
        }

        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                showToast(getString(R.string.permissions_denied))
                finish()
            }
        }
    }

    private fun showToast(message: String) = Toast.makeText(
        this,
        message,
        Toast.LENGTH_SHORT
    ).show()

    private fun showPredictionCheckDialog(foodNames: Array<String>) {
        var foodNameIndex = 0
        var foodName = foodNames[foodNameIndex]

        MaterialAlertDialogBuilder(this, R.style.AlertDialog)
            .setTitle(getString(R.string.food_prediction_question))
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                cameraViewModel.onEvent(CameraEvent.OnPredictionCheckDialogYesClick(foodName))
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                showMealEntryDialog()
            }
            .setSingleChoiceItems(foodNames, foodNameIndex) { _, which ->
                foodNameIndex = which
                foodName = foodNames[foodNameIndex]
            }
            .show()
    }

    private fun showConfirmationDialog(state: CameraViewModel.IngredientListUiState) {
        MaterialAlertDialogBuilder(this, R.style.AlertDialog)
            .setTitle(getString(R.string.food_ingredients_confirmation))
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                // Store the selected ingredients in database.
                cameraViewModel.onEvent(CameraEvent.OnConfirmationDialogOkClick)
                showToast(getString(R.string.ingredients_confirmation_toast))
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .setMultiChoiceItems(
                state.ingredients,
                state.checked
            ) { _, _, _ -> }
            .show()
    }

    private fun showMealEntryDialog() {
        val dialogTextsCamera = LayoutInflater.from(this)
            .inflate(R.layout.dialog_texts_camera, null)

        val foodEntry = dialogTextsCamera.findViewById<EditText>(R.id.editTextFoodName)

        MaterialAlertDialogBuilder(this, R.style.AlertDialog)
            .setTitle(getString(R.string.meal_entry))
            .setView(dialogTextsCamera)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                if (foodEntry.text.toString().isBlank()) {
                    showToast(getString(R.string.enter_food_again))
                } else {
                    cameraViewModel.foodName = foodEntry.text.toString()
                    cameraViewModel.onEvent(CameraEvent.OnMealEntryDialogOkClick)
                }
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .show()
    }

    private fun startCNNModel(imagePath: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val nnModel = NNModel(this@CameraActivity)
            val pyObj = nnModel.init()
            val foodNames = pyObj.callAttr(
                "classify",
                imagePath,
                ModelUtils.assetFilePath(
                    this@CameraActivity,
                    "resnet18_classifier.pth"
                ),
                ModelUtils.assetFilePath(this@CameraActivity, "classes.json")
            ).asList().joinToString(",").split(",").toTypedArray()

            // Translate the foodNames to the correct locale if necessary

            runOnUiThread {
                binding.cameraProgressCircle.hide()
                showPredictionCheckDialog(foodNames)
            }
        }
    }

    private fun takePhoto() {
        binding.cameraProgressCircle.show()

        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.getDefault()
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    startCNNModel(photoFile.absolutePath)
                }
            })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(
                        binding.viewFinder.surfaceProvider
                    )
                }

            imageCapture = ImageCapture.Builder()
                .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    companion object {
        private const val TAG = "Elliot"
        private const val FILENAME_FORMAT = "dd-MM-yyyy-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}
