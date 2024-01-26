package com.vmloft.develop.library.tools.widget.tips

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.RelativeLayout.LayoutParams
import android.widget.Toast

import com.vmloft.develop.library.tools.R.color
import com.vmloft.develop.library.tools.R.id

/**
 * Created by lzan13 on 2018/4/16.
 *
 * 自定义封装提示类
 */
object VMTips {

    // 显示时长
    const val durationLong = 3000L
    const val durationShort = 1500L

    /**
     * 自定义弹出提示条
     *
     * @param activity 需要展示提醒界面的 Activity 对象
     * @param msg      要展示的提醒内容
     * @param duration 显示时长，毫秒值
     * @param iconId 显示图标资源Id
     * @param bgId 显示背景资源 Id
     * @param colorId 显示文本颜色 Id
     */
    fun showBar(activity: Activity, msg: String, duration: Long = durationShort, iconId: Int = 0, bgId: Int = color.vm_toast_light_bg, colorId: Int = color.vm_toast_light_color, hideTop: Boolean = false) {
        val barLayout = activity.findViewById<View>(id.vmContainerLL)
        var barView: VMTipsBar
        if (barLayout == null) {
            barView = VMTipsBar(activity)
            val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            activity.addContentView(barView, lp)
        } else {
            barView = barLayout.parent as VMTipsBar
        }
        barView.setHideTop(hideTop)
        barView.show(msg, duration, iconId, bgId, colorId)
    }

    /**
     * 自定义暗色提示条
     */
    fun darkBar(activity: Activity, msg: String, duration: Long = durationShort, hideTop: Boolean = false) {
        showBar(activity, msg, duration, bgId = color.vm_toast_bg, colorId = color.vm_toast_dark_color, hideTop = hideTop)
    }

    /**
     * 自定义错误提示条
     */
    fun errorBar(activity: Activity, msg: String, duration: Long = durationShort, hideTop: Boolean = false) {
        showBar(activity, msg, duration, bgId = color.vm_toast_error_bg, colorId = color.vm_toast_dark_color, hideTop = hideTop)
    }

    /**
     * 系统 Toast 样式
     */
    fun show(context: Context, content: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, content, duration).show()
    }

    fun show(context: Context, resId: Int, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, resId, duration).show()
    }

}