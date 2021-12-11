package com.fm.module.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.fm.library.common.constants.RouterPath

@Route(path = RouterPath.Login.PAGE_LOGIN)
class LoginActivityMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity_main)

        ARouter.getInstance().inject(this)
    }
}