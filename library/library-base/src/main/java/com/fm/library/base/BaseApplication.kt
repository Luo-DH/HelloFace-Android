package com.fm.library.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter

/**
 * 创建日期：2021/12/11 10:48 下午
 * @author vansingluo
 * 包名： com.fm.library.base
 * @desc：
 */
class BaseApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        if (BuildConfig.DEBUG) {
            // 日志开启
            ARouter.openLog()
            // 调试模式开启，如果在install run模式下运行，则必须开启调试模式
            ARouter.openDebug()
        }
        ARouter.init(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        ARouter.getInstance().destroy()
    }
}