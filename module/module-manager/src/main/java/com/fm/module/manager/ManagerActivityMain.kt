package com.fm.module.manager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.fm.library.common.constants.RouterPath
import com.fm.module.manager.history.ManagerActivityHistory
import com.fm.module.manager.store.ManagerActivityStore

@Route(path = RouterPath.Manager.PAGE_MANAGER)
class ManagerActivityMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manager_activity_main)

        findViewById<TextView>(R.id.manager_main_btn_to_store).setOnClickListener {
            startActivity(Intent(this, ManagerActivityStore::class.java))
        }

        findViewById<TextView>(R.id.manager_main_btn_to_history).setOnClickListener {
            startActivity(Intent(this, ManagerActivityHistory::class.java))
        }
    }
}