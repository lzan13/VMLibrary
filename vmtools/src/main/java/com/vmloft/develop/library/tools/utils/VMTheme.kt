package com.vmloft.develop.library.tools.utils

import android.app.Activity
import android.graphics.Outline
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import android.view.Window
import android.view.WindowManager

import androidx.appcompat.app.AppCompatDelegate
import com.vmloft.develop.library.tools.R

/**
 * Created by lzan13 on 2018/4/17.
 * 主题工具类
 */
object VMTheme {
    /**
     * 设置是否使用夜间模式，这里只是切换主题设置，具体的主题样式资源等还需要自己配置，
     * 参考项目的 values-night 配置
     *
     * @param night 是否设置为夜间模式
     */
    @JvmStatic
    fun setNightTheme(night: Boolean) {
        if (night) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
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
                if (dark) {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 改变 View 背景阴影透明度
     */
    fun changeShadow(view: View, alpha: Float = 0.36f) {
        view.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                if (view.background != null) {
                    view.background.getOutline(outline)
                    outline.alpha = alpha
                }
            }
        }
    }
}