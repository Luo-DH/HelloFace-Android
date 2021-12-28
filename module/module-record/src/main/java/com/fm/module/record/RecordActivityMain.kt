package com.fm.module.record

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.fm.library.common.constants.RouterPath
import com.fm.module.record.databinding.RecordActivityMainBinding
import java.io.File
import java.util.concurrent.Executors
import androidx.camera.core.ImageCapture

import android.util.Size
import androidx.lifecycle.ViewModelProvider
import com.fm.library.common.constants.ext.toRotaBitmap


@Route(path = RouterPath.Record.PAGE_RECORD)
class RecordActivityMain : AppCompatActivity() {

    private lateinit var viewBinding: RecordActivityMainBinding

    private var imageCapture: ImageCapture? = null

    private lateinit var viewModel: RecordViewModelMain

    private var mImageAnalysisExecutor = Executors.newFixedThreadPool(5)

    private val mImageAnalysis = setUpImageAnalysis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = RecordActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        setupViewModel()

        mImageAnalysis.setAnalyzer(mImageAnalysisExecutor) { image ->
            var bitmap = Bitmap.createBitmap(
                image.width, image.height, Bitmap.Config.ARGB_8888
            )
            image.use {
                bitmap = viewModel.imageToBitmap(image).toRotaBitmap()
                Log.d(TAG, "onCreate: ${bitmap.width}==${bitmap.height} ${Thread.currentThread()} ")
            }
        }
    }

    private fun setupViewModel() {
        val repository = RecordRepositoryMain()
        val viewModelFactory = RecordViewModelFactoryMain(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RecordViewModelMain::class.java)
    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, mImageAnalysis
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

    override fun onDestroy() {
        super.onDestroy()
        mImageAnalysisExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    private fun setUpImageAnalysis(): ImageAnalysis =
        ImageAnalysis.Builder()
            .setTargetResolution(Size(960, 1280))
//            .setTargetRotation(binding.pvFinder.display.rotation)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_BLOCK_PRODUCER)
//            .setImageQueueDepth(20)
            .build()
}