package com.fm.module.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.fm.library.common.constants.RouterPath

@Route(path = RouterPath.Setting.PAGE_SETTING)
class SettingActivityMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_activity_main)
    }
}