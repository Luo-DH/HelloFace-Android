package com.fm.module.record

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.fm.library.common.constants.RouterPath

@Route(path = RouterPath.Record.PAGE_RECORD)
class RecordActivityMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.record_activity_main)
    }
}