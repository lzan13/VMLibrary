package com.vmloft.develop.library.example.app

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

import com.alibaba.android.arouter.launcher.ARouter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.vmloft.develop.library.base.event.LDEventBus
import com.vmloft.develop.library.base.notify.NotifyManager
import com.vmloft.develop.library.common.widget.refresh.DoubleCircleFooter
import com.vmloft.develop.library.common.widget.refresh.DoubleCircleHeader

import com.vmloft.develop.library.example.BuildConfig
import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.common.Constants
import com.vmloft.develop.library.example.common.SPManager
import com.vmloft.develop.library.tools.VMTools
import com.vmloft.develop.library.tools.utils.VMFile
import com.vmloft.develop.library.tools.utils.VMTheme
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

        initApp()
    }

    private fun initApp() {
        initCommon()

//        initKoin()

        initRouter()

        // 初始化通知管理
        initNotify()

        // 初始化上报，这里包含 bugly 错误日志上报以及 UMeng 统计上报
        initReport()

        initRefresh()

        // 初始化短信
//        SMSManager.instance.init()

        // 初始化广告管理
        // ADSManager.instance.init(appContext)

        // 初始化事件总线
        LDEventBus.init()
    }


    /**
     * 初始化通用工具
     */
    private fun initCommon() {
        VMTools.init(appContext)
        val level = if (BuildConfig.DEBUG) VMLog.Level.DEBUG else VMLog.Level.ERROR
        VMLog.init(level, "vm_template")

        // 设置暗色主题模式
        if (SPManager.isDarkModeSystemSwitch()) {
            VMTheme.setDarkTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        } else {
            VMTheme.setDarkTheme(SPManager.getDarkModeManual())
        }

        val path = "${VMFile.pictures}${Constants.projectDir}"
        if (!VMFile.isDirExists(path)) {
            VMFile.createDirectory(path)
        }
    }


    /**
     * 初始化 Koin
     */
//    private fun initKoin() {
//        startKoin {
//            androidContext(this@App)
//            modules(appModule)
//        }
//    }

    /**
     * 初始化注解路由
     */
    private fun initRouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
    }

    /**
     * 初始化通知
     */
    private fun initNotify() {
        NotifyManager.init()
    }

    /**
     * 初始化上报
     */
    private fun initReport() {
//        ReportManager.instance.init()
    }

    /**
     * 初始化下拉刷新控件
     */
    private fun initRefresh() {
        // 设置全局默认配置（优先级最低，会被其他设置覆盖）
        SmartRefreshLayout.setDefaultRefreshInitializer { _, layout ->
            layout.setPrimaryColorsId(com.vmloft.develop.library.base.R.color.app_primary, com.vmloft.develop.library.base.R.color.app_accent) // 全局设置主题颜色

            layout.setDragRate(0.6f) // 显示拖动高度/真实拖动高度（默认0.5，阻尼效果）

            layout.setReboundDuration(300) // 设置回弹时间

            layout.setHeaderHeight(96.0f) // Header标准高度（显示下拉高度>=标准高度 触发刷新）
            layout.setFooterHeight(56.0f) // Footer标准高度（显示上拉高度>=标准高度 触发加载）

            layout.setHeaderMaxDragRate(2.0f) // 最大显示下拉高度/Header标准高度
            layout.setHeaderTriggerRate(1.0f) // 触发刷新距离 与 HeaderHeight 的比率1.0.4
            layout.setFooterMaxDragRate(2.0f) // 最大显示上拉高度/Footer标准高度
            layout.setFooterTriggerRate(1.0f) // 触发加载距离 与 FooterHeight 的比率1.0.4

            //            layout.setEnableNestedScroll(true) // 是否开启嵌套滚动NestedScrolling（默认false-智能开启）
            layout.setEnableOverScrollBounce(false) // 是否启用越界回弹
            layout.setEnablePureScrollMode(false) // 是否启用纯滚动模式

            layout.setEnableScrollContentWhenRefreshed(false) // 是否在刷新成功之后滚动内容显示新数据（默认-true）
            layout.setEnableScrollContentWhenLoaded(true) // 是否在加载完成之后滚动内容显示新数据（默认-true）

            layout.setEnableRefresh(true) // 是否启用下拉刷新功能
            layout.setEnableLoadMore(true) // 是否启用上拉加载功能
        }
        // 设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            // 设置顶部刷新头
            DoubleCircleHeader(context)
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            // 设置底部加载更多，默认是 BallPulseFooter
            DoubleCircleFooter(context)
        }
    }
}