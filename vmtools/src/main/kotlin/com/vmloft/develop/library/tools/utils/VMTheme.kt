package com.vmloft.develop.library.tools.utils

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate

import com.vmloft.develop.library.tools.R
import com.vmloft.develop.library.tools.VMTools

/**
 * Created by lzan13 on 2018/4/17.
 * 主题工具类
 */
object VMTheme {

    /**
     * 设置使用黑暗模式方式，这里只是切换主题设置，具体的主题样式资源等还需要自己配置，
     * 参考项目的 values-night 配置
     * [AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM] 亮色
     * [AppCompatDelegate.MODE_NIGHT_YES] 黑暗
     * [AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM] 跟随系统
     * [AppCompatDelegate.MODE_NIGHT_AUTO_TIME] 跟随日落
     * [AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY] 跟随电量
     * @param dark 设置黑暗模式方式
     */
    fun setDarkTheme(dark: Int) {
        AppCompatDelegate.setDefaultNightMode(dark)
    }

    /**
     * 判断当前是否为暗色主题模式
     */
    fun isDarkMode(): Boolean {
        val mode = VMTools.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return mode == Configuration.UI_MODE_NIGHT_YES
    }

    /**
     * 设置黑色状态栏标题
     */
    fun setDarkStatusBar(activity: Activity, dark: Boolean) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window: Window = activity.window
                //去除半透明状态栏
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = VMColor.byRes(R.color.vm_transparent)
                if (dark && !isDarkMode()) {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}