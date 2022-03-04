package com.example.elliot.ui.camera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
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
import androidx.camera.view.PreviewView
import androidx.lifecycle.lifecycleScope
import com.example.elliot.MainActivity
import com.example.elliot.R
import com.example.elliot.domain.model.Food
import com.example.elliot.util.UiEvent
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        // Set up the listener for take photo button
        val cameraButton = findViewById<ImageButton>(R.id.camera_capture_button)
        cameraButton.setOnClickListener { takePhoto() }

        val backButton = findViewById<ImageButton>(R.id.back_button_camera)
        backButton.setOnClickListener {
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
        MaterialAlertDialogBuilder(this)
            .setTitle("Prediction Check")
            .setMessage("Is the food '...' the correct prediction?")
            .setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
                showConfirmationDialog()
                // It needs to update the prediction value stored in database
//                cameraViewModel.onEvent(CameraEvent.OnDialogYesClick(Food("noo")))
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()    // Close previous dialog
                showMealEntryDialog()
            }
            .show()
    }

    private fun showConfirmationDialog() {
        val multiItems = arrayOf("Item 1", "Item 2", "Item 3")
        val checkedItems = booleanArrayOf(false, false, false)

        MaterialAlertDialogBuilder(this)
            .setTitle("Confirm Food Ingredients")

            .setPositiveButton("OK") { _, _ ->
                for ((counter, checker) in checkedItems.withIndex()) {
                    if(checker) {
                        Log.d(TAG, multiItems[counter])
                    }
                }
            }

            .setNegativeButton("Cancel") { _, _ -> }

            .setMultiChoiceItems(multiItems, checkedItems) { _, _, checked ->
            }
            .show()
    }

    private fun showMealEntryDialog() {
        val dialogTextsCamera = LayoutInflater.from(this)
            .inflate(R.layout.dialog_texts_camera, null)
        val answers : MutableList<String> = ArrayList()
        answers.add("empty")

        val ingredientButton = dialogTextsCamera.findViewById<Button>(R.id.buttonInsertIngredient)
        val ingredientText = dialogTextsCamera.findViewById<EditText>(R.id.editTextIngredient1)

        ingredientButton.setOnClickListener {
            if (ingredientText.text.toString() != "") {
                answers.add(ingredientText.text.toString())
                ingredientText.setText("")
            } else {
                Toast.makeText(
                    this,
                    "Please enter an ingredient before trying to enter a new one.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Meal Entry")
            .setView(dialogTextsCamera)
            .setPositiveButton("OK") { _, _ ->
                answers[0] =
                    dialogTextsCamera.findViewById<EditText>(R.id.editTextFoodName).text.toString()

                if (answers[0] == "empty" || answers.size < 2) {
                    Toast.makeText(
                        this,
                        "Please enter a food and at least one ingredient and try again.",
                        Toast.LENGTH_SHORT
                    ).show()
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
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }
            })

        showPredictionCheckDialog()
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
                        findViewById<PreviewView>(R.id.view_finder)
                            .surfaceProvider
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
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "dd-MM-yyyy-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}
