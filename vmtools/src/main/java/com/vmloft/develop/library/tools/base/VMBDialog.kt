package com.vmloft.develop.library.tools.base

import android.app.Dialog
import android.content.Context
import android.text.SpannableString
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewbinding.ViewBinding

import com.vmloft.develop.library.tools.R
import com.vmloft.develop.library.tools.utils.VMStr

/**
 * Create by lzan13 on 2020/12/10 21:41
 * 描述：自定义通用对话框基类
 */
abstract class VMBDialog<VB : ViewBinding> : Dialog {

    // 子类可以覆盖以下属性
    // 点击积极按钮时检查是否关闭，主要是在一些对话框判断数据时使用，默认为 true
    open var positiveDismissSwitch: Boolean = true

    // 触摸空白处以及返回是否关闭 默认 true
    open var touchDismissSwitch: Boolean = true
        set(value) {
            field = value
            setCanceledOnTouchOutside(value)
        }

    // 触摸点击返回是否关闭 默认 true
    open var backDismissSwitch: Boolean = true
        set(value) {
            field = value
            setCancelable(value)
        }

    private var positiveListener: View.OnClickListener? = null
    private var confirmListener: View.OnClickListener? = null

    private var _binding: VB
    protected val mBinding get() = _binding

    constructor(context: Context) : this(context, R.style.VMDialog)

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {
        _binding = initVB()
        setContentView(_binding.root)
        window?.setGravity(Gravity.CENTER)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        getNegativeTV()?.setOnClickListener { v ->
            dismiss()
            positiveListener?.onClick(v)
        }
        getPositiveTV()?.setOnClickListener { v ->
            if (positiveDismissSwitch) {
                dismiss()
            }
            confirmListener?.onClick(v)
        }

        // 设置触摸空白处取消对话框
        setCanceledOnTouchOutside(touchDismissSwitch)
        // 设置点击返回取消对话框
        setCancelable(backDismissSwitch)
    }

    abstract fun initVB(): VB

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
    open fun setTitle(title: String?) {
        getTitleTV()?.text = title
        getTitleTV()?.visibility = if (title.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    /**
     * 设置内容
     */
    open fun setContent(resId: Int) {
        getContentTV()?.setText(resId)
        getContentTV()?.visibility = if (resId == 0) View.GONE else View.VISIBLE
    }

    /**
     * 设置内容
     */
    open fun setContent(content: String) {
        getContentTV()?.text = content
        getContentTV()?.visibility = if (content.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    /**
     * 设置内容
     */
    open fun setContent(content: CharSequence) {
        getContentTV()?.text = content
        getContentTV()?.visibility = if (content.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    /**
     * 设置内容
     */
    open fun setView(view: View?) {
        getContainerLL()?.addView(view)
        getContainerLL()?.visibility = if (view == null) View.GONE else View.VISIBLE
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