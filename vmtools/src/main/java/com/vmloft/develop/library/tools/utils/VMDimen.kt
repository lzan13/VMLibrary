package com.vmloft.develop.library.tools.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Paint
import android.graphics.Point
import android.view.WindowManager
import com.vmloft.develop.library.tools.VMTools
import com.vmloft.develop.library.tools.utils.logger.VMLog

/**
 * Created by lzan13 on 2015/4/15.
 * 尺寸转化工具类
 */
object VMDimen {
    private const val RES_STATUS_BAR_HEIGHT = "status_bar_height"
    private const val RES_NAV_BAR_HEIGHT = "navigation_bar_height"

    /**
     * 获取屏幕宽高
     */
     val screenWidth: Int
        get() = screenSize.x

     val screenHeight: Int
        get() = screenSize.y

    /**
     * 获取屏幕大小
     */
     val screenSize: Point
        get() = getScreenSize(VMTools.context)

    /**
     * 获取屏幕大小
     */
    fun getScreenSize(context: Context): Point {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val outSize = Point()
        display.getSize(outSize)
        return outSize
    }

    /**
     * 将控件尺寸的资源转换为像素尺寸
     *
     * @param resId 尺寸资源id
     */
    fun getDimenPixel(resId: Int): Int {
        val res = VMTools.context.resources
        return res.getDimensionPixelSize(resId)
    }

    /**
     * 将控件尺寸大小转为当前设备下的像素大小
     *
     * @param dp 控件尺寸大小
     */
    fun dp2px(dp: Int): Int {
        val res = VMTools.context.resources
        val density = res.displayMetrics.density
        return (dp * density + 0.5f).toInt()
    }

    /**
     * 将字体尺寸大小转为当前设备下的像素尺寸大小
     *
     * @param sp 字体的尺寸大小
     */
    fun sp2px(sp: Int): Float {
        val res = VMTools.context.resources
        val density = res.displayMetrics.scaledDensity
        return sp * density + 0.5f
    }

    /**
     * 获取文字的宽度
     *
     * @param paint 绘制文字的画笔
     * @param str   需要计算宽度的字符串
     * @return 返回字符串宽度
     */
    fun getTextWidth(paint: Paint, str: String?): Float {
        var textWidth = 0f
        if (str != null && str.isNotEmpty()) {
            // 记录字符串中每个字符宽度的数组
            val widths = FloatArray(str.length)
            // 获取字符串中每个字符的宽度到数组
            paint.getTextWidths(str, widths)
            for (element in str) {
                textWidth += Math.ceil(element.toDouble()).toFloat()
            }
        }
        return textWidth
    }

    /**
     * 计算文字的高度
     *
     * @param paint 绘制文字的画笔
     * @return 返回字符串高度
     */
    fun getTextHeight(paint: Paint): Float {
        val fm = paint.fontMetrics
        return Math.ceil(fm.descent - fm.ascent.toDouble()).toFloat()
    }

    /**
     * 获取状态栏高度
     */
     val statusBarHeight: Int
        get() {
            var height = 0
            val res = VMTools.context.resources
            val resId = res.getIdentifier(RES_STATUS_BAR_HEIGHT, "dimen", "android")
            if (resId > 0) {
                height = res.getDimensionPixelSize(resId)
            }
            return height
        }

    /**
     * 获取导航栏的高度（在 NavigationBar 存在的情况下）
     */
     val navigationBarHeight: Int
        get() {
            var height = 0
            val res = VMTools.context.resources
            if (hasNavigationBar()) {
                val key = RES_NAV_BAR_HEIGHT
                height = getInternalDimensionSize(res, key)
            }
            return height
        }

    /**
     * 判断是否有虚拟导航栏NavigationBar，
     */
    fun hasNavigationBar(): Boolean {
        var hasNavigationBar = false
        val rs = VMTools.context.resources
        val id = rs.getIdentifier("config_showNavigationBar", "bool", "android")
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id)
        }
        try {
            val systemPropertiesClass = Class.forName("android.os.SystemProperties")
            val m = systemPropertiesClass.getMethod("get", String::class.java)
            val navBarOverride = m.invoke(systemPropertiesClass, "qemu.hw.mainkeys") as String
            if ("1" == navBarOverride) {
                hasNavigationBar = false
            } else if ("0" == navBarOverride) {
                hasNavigationBar = true
            }
        } catch (e: Exception) {
            VMLog.e(e.message ?: "获取导航栏高度错误")
        }
        return hasNavigationBar
    }

    /**
     * 获取系统内部尺寸
     *
     * @param res 资源管理
     * @param key 内部资源的 key
     * @return
     */
    private fun getInternalDimensionSize(res: Resources, key: String): Int {
        var result = 0
        val resourceId = res.getIdentifier(key, "dimen", "android")
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId)
        }
        return result
    }

}