package com.fm.module.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.fm.library.common.constants.RouterPath

@Route(path = RouterPath.Manager.PAGE_MANAGER)
class ManagerActivityMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manager_activity_main)
    }
}