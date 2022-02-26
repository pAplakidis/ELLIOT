package com.example.elliot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.concurrent.Executors
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService

// TODO: 10/26/2021 Delete Log statements after development

class CameraActivity : AppCompatActivity() {
    private var imageCapture: ImageCapture? = null

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

        MaterialAlertDialogBuilder(this)
            .setTitle("Prediction Check")
            .setMessage("Is the food '...' the correct prediction?")

            .setPositiveButton("Yes") { _, _ ->
                // ΒΑΛΕ ΠΡΟΒΛΕΨΗ ΣΤΗΝ ΒΑΣΗ
            }

            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss() // ΚΛΕΙΣΕ ΠΡΟΗΓΟΥΜΕΝΟ DIALOG ΓΙΑ ΝΑ ΜΠΕΙ ΑΥΤΟ

                val dialogTextsCamera = LayoutInflater.from(this)
                    .inflate(R.layout.dialog_texts_camera, null) // INFLATE TO VIEW ΓΙΑ ΕΝΩΣΗ

                MaterialAlertDialogBuilder(this)
                    .setTitle("Meal Entry")

                    .setView(dialogTextsCamera)

                    .setPositiveButton("OK") { _, _ ->
                        val answers = Array<String?>(4) { null }    // ΠΕΡΑΣΜΑ ΑΠΑΝΤΗΣΕΩΝ
                        answers[0] =
                            dialogTextsCamera.findViewById<EditText>(R.id.editTextFoodName).text.toString()
                        answers[1] =
                            dialogTextsCamera.findViewById<EditText>(R.id.editTextIngredient1).text.toString()
                        answers[2] =
                            dialogTextsCamera.findViewById<EditText>(R.id.editTextIngredient2).text.toString()
                        answers[3] =
                            dialogTextsCamera.findViewById<EditText>(R.id.editTextIngredient3).text.toString()
                        if (answers[0] == "" || answers[1] == "") {
                            val failureMessage =
                                "Please enter a food and at least one ingredient and try again."
                            val toast = Toast.makeText(this, failureMessage, Toast.LENGTH_SHORT)
                            toast.show()
                        }
                    }
                    .setNegativeButton("Cancel") { _, _ -> }
                    .show()
            }
            .show()
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
