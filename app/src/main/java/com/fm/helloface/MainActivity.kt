package com.fm.helloface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.alibaba.android.arouter.launcher.ARouter
import com.fm.library.common.constants.RouterPath

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testModule()

        ARouter.getInstance().inject(this)
    }

    private fun testModule() {
        findViewById<Button>(R.id.test_module_login).setOnClickListener {
            ARouter.getInstance().build(RouterPath.Login.PAGE_LOGIN).navigation()
        }
        findViewById<Button>(R.id.test_module_main).setOnClickListener {
            ARouter.getInstance().build(RouterPath.Main.PAGE_MAIN).navigation()
        }
        findViewById<Button>(R.id.test_module_manager).setOnClickListener {
            ARouter.getInstance().build(RouterPath.Manager.PAGE_MANAGER).navigation()
        }
        findViewById<Button>(R.id.test_module_record).setOnClickListener {
            ARouter.getInstance().build(RouterPath.Record.PAGE_RECORD).navigation()
        }
        findViewById<Button>(R.id.test_module_setting).setOnClickListener {
            ARouter.getInstance().build(RouterPath.Setting.PAGE_SETTING).navigation()
        }
    }
}