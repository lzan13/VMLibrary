package com.vmloft.develop.library.tools.base

import android.app.Application
import android.content.Context
import kotlin.properties.Delegates

/**
 * Created by lzan13 on 2017/4/13.
 * 这里主要是定义一个全局的上下文对象，以后所有引用此库的项目直接使用
 */
open class VMApp : Application() {

    companion object {
        var appContext: Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

}