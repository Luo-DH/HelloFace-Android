package com.fm.module.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.fm.library.common.constants.DBFaceMsg
import com.fm.library.common.constants.RouterPath
import com.fm.library.common.constants.net.GlobalServiceCreator
import com.fm.library.face.FaceSdk
import com.fm.module.main.databinding.MainActivityMainBinding
import com.fm.module.main.net.MainNetApi
import com.wildma.pictureselector.PictureSelector
import kotlin.concurrent.thread
import com.wildma.pictureselector.PictureBean
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


@Route(path = RouterPath.Main.PAGE_MAIN)
class MainActivityMain : AppCompatActivity() {

    private val TAG = "MainActivityMain"

    private lateinit var binding: MainActivityMainBinding

    private lateinit var viewModel: MainViewModelMain

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()

        // 加载数据库中的人脸数据
        loadFace()

        // 加载人脸识别sdk
        loadFaceSdk()

        // 设置数据观察
        setupObserver()

        // 设置点击监听
        setupClickListener()
    }

    private fun setupClickListener() {
        binding.mainBtnToRecord.setOnClickListener {
            ARouter.getInstance().build(RouterPath.Record.PAGE_RECORD).navigation()
        }
        binding.mainBtnToHistory.setOnClickListener {
            ARouter.getInstance().build(RouterPath.Manager.PAGE_HISTORY).navigation()
        }
        binding.mainBtnToManager.setOnClickListener {
            ARouter.getInstance().build(RouterPath.Manager.PAGE_MANAGER).navigation()
        }
        binding.mainBtnToSetting.setOnClickListener {
            ARouter.getInstance().build(RouterPath.Setting.PAGE_SETTING).navigation()
        }
        binding.mainBtnToInitPic.setOnClickListener {
            ARouter.getInstance().build(RouterPath.Init.PAGE_INIT).navigation()
        }
        binding.mainBtnToQrcode.setOnClickListener {
            ARouter.getInstance().build(RouterPath.QRCode.PAGE_QRCODE).navigation()
        }
        binding.mainBtnToSelectPic.setOnClickListener {
            PictureSelector
                .create(this, PictureSelector.SELECT_REQUEST_CODE)
                .selectPicture(true, 200, 200, 1, 1);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*结果回调*/if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                val pictureBean: PictureBean =
                    data.getParcelableExtra(PictureSelector.PICTURE_RESULT)!!
                Log.d(TAG, "onActivityResult: ${pictureBean.path}")
                val file = File(pictureBean.path)

                // 创建 RequestBody，用于封装构建RequestBody
                 val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file);
//                val requestFile = RequestBody.create("image/jpg".toMediaTypeOrNull(), file);

                // MultipartBody.Part  和后端约定好Key，这里的partName是用file
                val body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

                runBlocking {
                    val api = GlobalServiceCreator.create<MainNetApi>()
                    val res = api.upload(body)
                    Log.d(TAG, "onActivityResult: ${res.body()!!.msg}")
                }

            }
        }
    }

    private fun setupObserver() {
        viewModel.dbFaceMap.observe(this) {
            Log.d(TAG, "setupObserver: dbFaceMap = $it")
            Toast.makeText(applicationContext, "人脸转换完毕! ${it.size}", Toast.LENGTH_SHORT).show()
            mThread.join()
            viewModel.initFace(it)
        }
        viewModel.dbFeaMap.observe(this) {
            Log.d(TAG, "setupObserver: dbFeaMap = $it")
            Toast.makeText(applicationContext, "人脸库初始化完毕! ${it.size}", Toast.LENGTH_SHORT).show()
            DBFaceMsg.dbFaceMap = it
            DBFaceMsg.dbFaceMap2 = it
        }
    }

    private fun setupViewModel() {
        val repository = MainRepositoryMain()
        val viewModelFactory = MainViewModelFactoryMain(this.applicationContext, repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModelMain::class.java)
    }

    private fun loadFace() {
        viewModel.loadFace()
    }

    lateinit var mThread: Thread

    private fun loadFaceSdk() {
        mThread = thread {
            FaceSdk
            FaceSdk.FindFace.init(assets)
            FaceSdk.CheckFace.init(assets)
            runOnUiThread {
                Toast.makeText(applicationContext, "人脸sdk加载完成!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}