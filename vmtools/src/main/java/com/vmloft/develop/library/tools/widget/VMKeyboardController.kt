package com.vmloft.develop.library.tools.widget

import android.app.Activity
import android.view.View
import android.view.MotionEvent
import android.widget.EditText
import android.widget.LinearLayout

import com.vmloft.develop.library.tools.utils.VMDimen
import com.vmloft.develop.library.tools.utils.VMSP
import com.vmloft.develop.library.tools.utils.VMView

/**
 * Create by lzan13 on 2022/6/30
 * 描述：输入面板键盘控制器，主要用于扩展面板和输入法进行平滑切换
 * PS：使用此类需要将对应界面设置 android:windowSoftInputMode="adjustResize|stateHidden"
 */
class VMKeyboardController(val activity: Activity) {

    private lateinit var contentContainer: View // 内容布局，输入面板上方内容部分，用户固定输入面板高度，防止跳闪
    private lateinit var editText: EditText // 输入框，用于获取焦点触发面板切换
    private lateinit var extendContainer: View // 扩展面板布局

    /**
     * 绑定内容容器，用于固定输入面板的高度，防止跳闪
     */
    fun bindContentContainer(view: View): VMKeyboardController {
        contentContainer = view
        return this
    }

    /**
     * 绑定编辑框，用于获取焦点触发面板切换
     */
    fun bindEditText(editText: EditText): VMKeyboardController {
        this.editText = editText
        this.editText.requestFocus()
        this.editText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP && extendContainer.isShown) {
                hideExtendContainer(true, true) //隐藏表情布局，显示软件盘
            }
            false
        }
        return this
    }

    /**
     * 设置扩展容器
     */
    fun bindExtendContainer(view: View): VMKeyboardController {
        extendContainer = view
        return this
    }

    /**
     * 是否显示软件盘
     */
    fun isShowKeyboard(): Boolean {
        return VMView.getKeyboardHeight(activity) > 0
    }

    /**
     * 编辑框获取焦点，并显示软件盘
     */
    fun showKeyboard() {
        VMView.showKeyboard(activity, editText)
    }

    /**
     * 隐藏软件盘
     */
    fun hideKeyboard() {
        VMView.hideKeyboard(activity, editText)
    }

    /**
     * 显示扩展面板容器
     */
    fun showExtendContainer(lockContentHeight: Boolean) {
        if (lockContentHeight) lockContentHeight() // 显示软件盘时，锁定内容高度，防止跳闪。

        var keyboardHeight = VMView.getKeyboardHeight(activity)
        if (keyboardHeight == 0) {
            keyboardHeight = VMSP.getEntry("vmtools").get("keyboardHeight", VMDimen.dp2px(256)) as Int
        } else {
            hideKeyboard()
        }
        extendContainer.layoutParams.height = keyboardHeight
        extendContainer.visibility = View.VISIBLE

        if (lockContentHeight) unlockContentHeightDelayed() // 软件盘显示后，释放内容高度
    }

    /**
     * 隐藏扩展面板
     * @param showKeyboard 是否显示键盘
     */
    fun hideExtendContainer(showKeyboard: Boolean, lockContentHeight: Boolean) {
        if (lockContentHeight) lockContentHeight() // 显示软件盘时，锁定内容高度，防止跳闪。

        if (extendContainer.isShown) {
            extendContainer.visibility = View.GONE
            if (showKeyboard) showKeyboard()
        }
        if (lockContentHeight) unlockContentHeightDelayed() // 软件盘显示后，释放内容高度
    }

    /**
     * 锁定内容高度，防止跳闪
     */
    private fun lockContentHeight() {
        val params = contentContainer.layoutParams as LinearLayout.LayoutParams
        params.height = contentContainer.height
        params.weight = 0.0f
    }

    /**
     * 释放被锁定的内容高度
     */
    private fun unlockContentHeightDelayed() {
        editText.postDelayed({ (contentContainer.layoutParams as LinearLayout.LayoutParams).weight = 1.0f }, 200L)
    }
}