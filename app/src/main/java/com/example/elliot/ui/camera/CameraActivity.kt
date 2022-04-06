package com.example.elliot.ui.camera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
import com.example.elliot.MainActivity
import com.example.elliot.R
import com.example.elliot.databinding.ActivityCameraBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
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
                cameraViewModel.ingredientsListUiState.collect {
                    if (it.ingredients.isNotEmpty()) {
                        showConfirmationDialog(it)
                    }
                }
            }
        }

        // Set up the listener for take photo button
        binding.cameraCaptureButton.setOnClickListener {
            takePhoto()
            cameraViewModel.onEvent(CameraEvent.OnCameraButtonClick)
            showPredictionCheckDialog()
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
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun showPredictionCheckDialog() {
        MaterialAlertDialogBuilder(this, R.style.AlertDialog)
            .setTitle("Prediction Check")
            .setMessage("Is the food ${cameraViewModel.foodName} the correct prediction?")
            .setPositiveButton("Yes") { _, _ ->
                cameraViewModel.onEvent(CameraEvent.OnPredictionCheckDialogYesClick)
            }
            .setNegativeButton("No") { _, _ ->
                showMealEntryDialog()
            }
            .show()
    }

    private fun showConfirmationDialog(state: CameraViewModel.IngredientListUiState) {
        MaterialAlertDialogBuilder(this, R.style.AlertDialog)
            .setTitle("Confirm Food Ingredients")
            .setPositiveButton("OK") { _, _ ->
                // Store the selected ingredients in database.
                cameraViewModel.onEvent(CameraEvent.OnConfirmationDialogOkClick)
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .setMultiChoiceItems(
                state.ingredients,
                state.checked
            ) { _, _, _ -> }
            .show()
    }

    private fun showMealEntryDialog() {
        val dialogTextsCamera = LayoutInflater.from(this)
            .inflate(R.layout.dialog_texts_camera, null)
//        val mealInsertButton = dialogTextsCamera.findViewById<Button>(R.id.buttonInsertFood)
//        val ingredientText = dialogTextsCamera.findViewById<EditText>(R.id.editTextIngredient1)
        val foodEntry = dialogTextsCamera.findViewById<EditText>(R.id.editTextFoodName)

//        mealInsertButton.setOnClickListener {
//            if (ingredientText.text.toString() != "") {
//                cameraViewModel.ingredients.add(ingredientText.text.toString())
//                ingredientText.setText("")
//            } else {
//                Toast.makeText(
//                    this,
//                    "Please enter an ingredient before trying to enter a new one.",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }

        MaterialAlertDialogBuilder(this, R.style.AlertDialog)
            .setTitle("Meal Entry")
            .setView(dialogTextsCamera)
            .setPositiveButton("OK") { _, _ ->
                if (foodEntry.text.toString().isBlank()) {
                    Toast.makeText(
                        this,
                        "Please enter a food and try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    cameraViewModel.foodName = foodEntry.text.toString()
                    cameraViewModel.onEvent(CameraEvent.OnMealEntryDialogOkClick)
                }
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .show()
    }

    private fun takePhoto() {
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

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
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