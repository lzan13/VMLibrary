package com.vmloft.develop.library.tools.utils

import android.app.Activity
import android.graphics.Outline
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.view.View
import android.view.ViewOutlineProvider
import android.view.Window
import android.view.WindowManager.LayoutParams
import androidx.appcompat.app.AppCompatDelegate

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
     * 状态栏深色模式，设置状态栏黑色文字、图标
     *
     * @return 1-MIUI 2=-Flyme 3-Android 6.0 原生
     */
    @JvmStatic
    fun setDarkStatusBar(activity: Activity, dark: Boolean): Int {
        var result = 0
        when {
            statusBarDarkModeFromMIUI(activity, dark) -> {
                result = 1
            }
            statusBarDarkModeFromFlyme(activity.window, dark) -> {
                result = 2
            }
            VERSION.SDK_INT >= VERSION_CODES.M -> {
                if (dark) {
                    activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                }
                result = 3
            }
        }
        return result
    }

    /**
     * 针对魅族系统设置状态栏图标为深色和魅族特定的文字风格
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏文字及图标颜色设置为深色
     * @return 成功执行返回 true
     */
    private fun statusBarDarkModeFromFlyme(window: Window?, dark: Boolean): Boolean {
        var result = false
        if (window != null) {
            try {
                val lp = window.attributes
                val darkFlag = LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val mzFlags = LayoutParams::class.java.getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                mzFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = mzFlags.getInt(lp)
                value = if (dark) {
                    value or bit
                } else {
                    value and bit.inv()
                }
                mzFlags.setInt(lp, value)
                window.attributes = lp
                result = true
            } catch (e: Exception) {
            }
        }
        return result
    }

    /**
     * 针对 MIUI 系统设置深色状态栏
     *
     * @param dark 是否把状态栏文字及图标颜色设置为深色
     * @return 成功执行返回 true
     */
    private fun statusBarDarkModeFromMIUI(activity: Activity, dark: Boolean): Boolean {
        var result = false
        val window = activity.window
        if (window != null) {
            val clazz: Class<*> = window.javaClass
            try {
                var darkModeFlag = 0
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag) //状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag) //清除黑色字体
                }
                result = true
                if (VERSION.SDK_INT >= VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    } else {
                        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                    }
                }
            } catch (e: Exception) {
            }
        }
        return result
    }
    /**
     * 改变 View 背景阴影透明度
     */
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