package com.vmloft.develop.library.example.app

import android.app.Application
import android.content.Context

import com.alibaba.android.arouter.BuildConfig
import com.alibaba.android.arouter.launcher.ARouter

import com.vmloft.develop.library.tools.VMTools
import com.vmloft.develop.library.tools.utils.logger.VMLog

import kotlin.properties.Delegates

/**
 * Created by lzan13 on 2017/7/7.
 * 程序入口
 */
class App : Application() {

    companion object {
        var appContext: Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        init()
    }

    private fun init() {
        VMTools.init(appContext)
        val level = if (BuildConfig.DEBUG) VMLog.Level.DEBUG else VMLog.Level.ERROR
        VMLog.init(level, "vm_example")

        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this)
    }
}