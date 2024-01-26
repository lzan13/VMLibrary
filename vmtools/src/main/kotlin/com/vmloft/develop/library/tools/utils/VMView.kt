package com.vmloft.develop.library.tools.utils

import android.app.Activity
import android.content.Context
import android.graphics.Outline
import android.graphics.Rect
import android.view.View
import android.view.ViewOutlineProvider
import android.view.inputmethod.InputMethodManager

import com.vmloft.develop.library.tools.utils.logger.VMLog

/**
 * Create by lzan13 on 2020/4/12 15:27
 * 描述：View 相关工具类
 */
object VMView {

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

    /**
     * ------------------------------- 键盘部分 -------------------------------
     * 显示键盘
     */
    fun showKeyboard(activity: Activity?, view: View?) {
        if (activity == null) {
            return
        }
        view?.requestFocus()
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // 切换软键盘的显示与隐藏
        // imm.toggleSoftInputFromWindow(mInputET.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN, InputMethodManager.HIDE_NOT_ALWAYS);
        // 显示软键盘
        imm.showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN)
    }

    /**
     * 隐藏键盘
     */
    fun hideKeyboard(activity: Activity?, view: View) {
        if (activity == null) {
            return
        }
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // 切换软键盘的显示与隐藏
        // imm.toggleSoftInputFromWindow(mInputET.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN, InputMethodManager.HIDE_NOT_ALWAYS);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * 获取软件盘的高度
     */
    fun getKeyboardHeight(activity: Activity): Int {
        val r = Rect()
        /**
         * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
         * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
         */
        activity.window.decorView.getWindowVisibleDisplayFrame(r)
        //获取屏幕的高度
        val screenHeight = activity.window.decorView.rootView.height
        //计算软件盘的高度
        var keyboardHeight = screenHeight - r.bottom
        /**
         * 某些Android版本下，没有显示软键盘时减出来的高度总是144，而不是零，
         * 这是因为高度是包括了虚拟按键栏的(例如华为系列)，所以在API Level高于20时，
         * 我们需要减去底部虚拟按键栏的高度（如果有的话）
         */
//            if (Build.VERSION.SDK_INT >= 20) {
//                // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
//            }
//        keyboardHeight -= VMDimen.navigationBarHeight

        if (keyboardHeight < 0) {
            VMLog.e("获取键盘高度异常")
        }
        // 保存键盘高度
        if (keyboardHeight > 0) {
            VMSP.getEntry("vmtools").putAsync("keyboardHeight", keyboardHeight)
        }
        return keyboardHeight
    }

}