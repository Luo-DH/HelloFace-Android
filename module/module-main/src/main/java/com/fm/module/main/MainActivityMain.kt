package com.fm.module.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.fm.library.common.constants.DBFaceMsg
import com.fm.library.common.constants.RouterPath
import com.fm.library.face.FaceSdk
import com.fm.module.main.databinding.MainActivityMainBinding
import kotlin.concurrent.thread

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
    }

    private fun setupObserver() {
        viewModel.dbFaceMap.observe(this) {
            Log.d(TAG, "setupObserver: dbFaceMap = $it")
            Toast.makeText(applicationContext, "人脸转换完毕! ${it.size}", Toast.LENGTH_SHORT).show()
            Thread.sleep(5000)
            viewModel.initFace(it)
        }
        viewModel.dbFeaMap.observe(this) {
            Log.d(TAG, "setupObserver: dbFeaMap = $it")
            Toast.makeText(applicationContext, "人脸库初始化完毕! ${it.size}", Toast.LENGTH_SHORT).show()
            DBFaceMsg.dbFaceMap = it
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

    private fun loadFaceSdk() {
        thread {
            FaceSdk
            FaceSdk.FindFace.init(assets)
            FaceSdk.CheckFace.init(assets)
            runOnUiThread {
                Toast.makeText(applicationContext, "人脸sdk加载完成!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}