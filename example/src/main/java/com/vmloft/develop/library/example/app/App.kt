package com.vmloft.develop.library.example.app

import com.alibaba.android.arouter.launcher.ARouter
import com.vmloft.develop.library.example.BuildConfig
import com.vmloft.develop.library.tools.VMTools
import com.vmloft.develop.library.tools.base.VMApp
import com.vmloft.develop.library.tools.utils.logger.VMLog

/**
 * Created by lzan13 on 2017/7/7.
 * 程序入口
 */
class App : VMApp() {
    override fun onCreate() {
        super.onCreate()
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