package com.fm.module.record

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.RectF
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.fm.library.common.constants.RouterPath
import com.fm.module.record.databinding.RecordActivityMainBinding
import java.util.concurrent.Executors
import androidx.camera.core.ImageCapture

import android.util.Size
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.fm.library.common.constants.DBFaceMsg
import com.fm.library.common.constants.ext.toRotaBitmap
import com.fm.library.face.FaceSdk
import com.fm.library.face.module.Box
import com.fm.library.face.Face
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import kotlin.concurrent.thread
import kotlin.math.min


@Route(path = RouterPath.Record.PAGE_RECORD)
class RecordActivityMain : AppCompatActivity() {

    private lateinit var viewBinding: RecordActivityMainBinding

    private var imageCapture: ImageCapture? = null

    private lateinit var viewModel: RecordViewModelMain

    private var mImageAnalysisExecutor = Executors.newFixedThreadPool(10)

    private val mImageAnalysis = setUpImageAnalysis()

    // 选择镜头
    private var mLensFacing = CameraSelector.LENS_FACING_FRONT

    // 用于上传
    private var mBitmap: Bitmap? = null

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
        setupObserver()

        setAnalysis()
    }

    private fun setAnalysis() {
        viewBinding.boxPrediction.visibility = View.GONE
        viewBinding.recordTvRes.visibility = View.GONE
        viewBinding.recordImShowRes.visibility = View.GONE
        mImageAnalysis.setAnalyzer(mImageAnalysisExecutor) { image ->
            image.use {
                val bitmap = viewModel.imageToBitmap(image).toRotaBitmap()
                mBitmap = bitmap // 存一下，用于上传
                viewModel.detectFace(bitmap)
            }
        }
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
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
            val cameraSelector = setupCameraSelector()

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

    /**
     * 选择镜头
     */
    private fun setupCameraSelector() = CameraSelector.Builder()
        .requireLensFacing(mLensFacing).build()

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


    private fun setUpImageAnalysis(): ImageAnalysis =
        ImageAnalysis.Builder()
            .setTargetResolution(Size(960, 1280))
//            .setTargetRotation(binding.pvFinder.display.rotation)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//            .setImageQueueDepth(20)
            .build()

    private fun setupObserver() {

        // 人脸检测获得数据
        viewModel.detectBox.observe(this) {
            synchronized(this) {
                analysisBitmap(it)
            }
        }

        // 获取人脸识别数据
        viewModel.feature.observe(this) {
            synchronized(this) {
                viewModel.checkFace(feature = it)
            }
        }

        // 人脸比对结果
        viewModel.recognizeRes.observe(this) { name ->
            viewModel.vote(name)
        }

        viewModel.imgUrl.observe(this) { imgUrl ->
            viewBinding.recordImShowRes.visibility = View.VISIBLE
            val a = viewBinding.recordImShowRes.load(imgUrl)
            val anim = AnimationUtils.loadAnimation(applicationContext, R.anim.alpha_anim)
            viewBinding.recordImShowRes.animation = anim

        }

        // 投票结果
        viewModel.voteRes.observe(this) { name ->
            viewBinding.recordTvRes.apply {
                viewBinding.recordTvRes.visibility = View.VISIBLE
                text = name
            }
            viewModel.clearVoteMap()
            mImageAnalysis.clearAnalyzer()

            viewModel.requestBitmap(name)

            viewModel.updateData(name)

            mHandler.postDelayed({
                viewBinding.boxPrediction.visibility = View.GONE
            }, 500)

            mHandler.postDelayed(Runnable {
                setAnalysis()
            }, 3500)
        }

    }

    private val mHandler = Handler(Looper.getMainLooper())

    /**
     * 处理人脸检测后得到的人脸框
     */
    private fun analysisBitmap(result: List<Box>) =
        viewBinding.viewFinder.post {
            viewBinding.boxPrediction.visibility = View.GONE
            if (result.isEmpty()) {
                return@post
            }

            val box = result.maxByOrNull { (it.x2 - it.x1) * (it.y2 - it.y1) }!!

            // 记录当前帧人脸框信息

            val location = mapOutputCoordinates(
                RectF().also {
                    it.left = box.x1.toFloat() / box.width
                    it.right = box.x2.toFloat() / box.width
                    it.top = box.y1.toFloat() / box.height
                    it.bottom = box.y2.toFloat() / box.height
                }
            )

            (viewBinding.boxPrediction.layoutParams as ViewGroup.MarginLayoutParams).apply {
                topMargin = location.top.toInt()
                leftMargin = location.left.toInt()
                width = min(
                    viewBinding.viewFinder.width,
                    location.right.toInt() - location.left.toInt()
                )
                height =
                    min(
                        viewBinding.viewFinder.height,
                        location.bottom.toInt() - location.top.toInt()
                    )
            }

            // Make sure all UI elements are visible
            viewBinding.boxPrediction.visibility = View.VISIBLE

        }

    /**
     * Helper function used to map the coordinates for objects coming out of
     * the model into the coordinates that the user sees on the screen.
     */
    private fun mapOutputCoordinates(location: RectF): RectF {
        val view_finder = viewBinding.viewFinder

        // Step 1: map location to the preview coordinates
        val previewLocation = RectF(
            location.left * view_finder.width,
            location.top * view_finder.height,
            location.right * view_finder.width,
            location.bottom * view_finder.height
        )

        // Step 2: compensate for camera sensor orientation and mirroring
        val isFrontFacing = mLensFacing == CameraSelector.LENS_FACING_FRONT
        val correctedLocation = if (isFrontFacing) {
            RectF(
                view_finder.width - previewLocation.right,
                previewLocation.top,
                view_finder.width - previewLocation.left,
                previewLocation.bottom
            )
        } else {
            previewLocation
        }

        // Step 3: compensate for 1:1 to 4:3 aspect ratio conversion + small margin
        val margin = 0.1f
        val requestedRatio = 18.7f / 15f
        val midX = (correctedLocation.left + correctedLocation.right) / 2f
        val midY = (correctedLocation.top + correctedLocation.bottom) / 2f
        return if (view_finder.width < view_finder.height) {
            RectF(
                midX - (1f + margin) * requestedRatio * correctedLocation.width() / 2f,
                midY - (1f - margin) * correctedLocation.height() / 2f,
                midX + (1f + margin) * requestedRatio * correctedLocation.width() / 2f,
                midY + (1f - margin) * correctedLocation.height() / 2f
            )
        } else {
            RectF(
                midX - (1f - margin) * correctedLocation.width() / 2f,
                midY - (1f + margin) * requestedRatio * correctedLocation.height() / 2f,
                midX + (1f - margin) * correctedLocation.width() / 2f,
                midY + (1f + margin) * requestedRatio * correctedLocation.height() / 2f
            )
        }
    }
    private fun saveBitmap(bitmap: Bitmap) {
        val sdCardDir =
            Environment.getExternalStorageDirectory().toString() + "/fingerprintimages/";
        val fileName = "test"
        val file = File(sdCardDir, "abc".toString() + ".jpg")
        try {
            val dirFile = File(sdCardDir)
            if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                dirFile.mkdirs()
            }
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // 把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(
                this.getContentResolver(),
                file.getAbsolutePath(), fileName, null
            )
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        // 通知图库更新
        sendBroadcast(
            Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + "/sdcard/namecard/")
            )
        )
    }

}