package com.fm.module.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.fm.library.common.constants.RouterPath
import com.fm.library.face.FaceSdk
import com.fm.module.main.databinding.MainActivityMainBinding
import kotlin.concurrent.thread

@Route(path = RouterPath.Main.PAGE_MAIN)
class MainActivityMain : AppCompatActivity() {

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
    }

    private fun setupViewModel() {
        val repository = MainRepositoryMain()
        val viewModelFactory = MainViewModelFactoryMain(repository)
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
        }
    }
}