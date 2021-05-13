package com.vmloft.develop.library.tools.base

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.vmloft.develop.library.tools.R
import com.vmloft.develop.library.tools.utils.VMStr

/**
 * Create by lzan13 on 2020/12/10 21:41
 * 描述：自定义通用对话框基类
 */
abstract class VMBaseDialog : Dialog {

    private var positiveListener: View.OnClickListener? = null
    private var confirmListener: View.OnClickListener? = null

    constructor(context: Context) : this(context, R.style.VMDialog)

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {
        setContentView(layoutId())
        window?.setGravity(Gravity.CENTER)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        getNegativeTV()?.setOnClickListener { v ->
            dismiss()
            positiveListener?.onClick(v)
        }
        getPositiveTV()?.setOnClickListener { v ->
            if (positiveDismiss()) {
                dismiss()
            }
            confirmListener?.onClick(v)
        }
    }

    /**
     * 点击积极按钮时检查是否关闭，主要是在一些对话框判断数据时使用，默认为 true
     */
    open fun positiveDismiss(): Boolean {
        return true
    }

    /**
     * 触摸空白处以及返回是否关闭 默认 true
     */
    open fun touchDismiss(): Boolean {
        return true
    }

    /**
     * 触摸点击返回是否关闭 默认 true
     */
    open fun backDismiss(): Boolean {
        return true
    }

    abstract fun layoutId(): Int

    abstract fun getNegativeTV(): TextView?

    abstract fun getPositiveTV(): TextView?

    /**
     * 获取标题控件
     */
    open fun getTitleTV(): TextView? {
        return null
    }

    /**
     * 获取内容控件
     */
    open fun getContentTV(): TextView? {
        return null
    }

    /**
     * 获取内容容器控件
     */
    open fun getContainerLL(): LinearLayout? {
        return null
    }

    /**
     * 设置标题
     */
    fun setTitle(title: String?) {
        getTitleTV()?.text = title
        getTitleTV()?.visibility = if (title.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    /**
     * 设置内容
     */
    fun setContent(resId: Int) {
        getContentTV()?.setText(resId)
        getContentTV()?.visibility = if (resId == 0) View.GONE else View.VISIBLE
    }

    /**
     * 设置内容
     */
    fun setContent(content: String) {
        getContentTV()?.text = content
        getContentTV()?.visibility = if (content.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    /**
     * 设置内容
     */
    fun setView(view: View?) {
        getContainerLL()?.addView(view)
        getContentTV()?.visibility = if (view == null) View.GONE else View.VISIBLE
    }

    /**
     * 消极按钮，可设置隐藏
     */
    fun setNegative(negative: String = VMStr.byRes(R.string.vm_btn_cancel), listener: View.OnClickListener? = null) {
        getNegativeTV()?.visibility = if (negative.isNullOrEmpty()) View.GONE else View.VISIBLE

        getNegativeTV()?.text = negative
        positiveListener = listener
    }

    /**
     * 确认按钮
     */
    fun setPositive(positive: String = VMStr.byRes(R.string.vm_btn_confirm), listener: View.OnClickListener? = null) {
        getPositiveTV()?.text = positive
        confirmListener = listener
    }
}