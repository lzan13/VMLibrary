package com.vmloft.develop.library.tools.base

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
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

        getPositiveTV()?.setOnClickListener { v ->
            dismiss()
            positiveListener?.onClick(v)
        }
        getConfirmTV()?.setOnClickListener { v ->
            dismiss()
            confirmListener?.onClick(v)
        }
    }

    abstract fun layoutId(): Int

    abstract fun getPositiveTV(): TextView?

    abstract fun getConfirmTV(): TextView?

    /**
     * 消极按钮，可设置隐藏
     */
    fun setPositive(content: String = VMStr.byRes(R.string.vm_btn_cancel), listener: View.OnClickListener? = null) {
        getPositiveTV()?.visibility = if (content.isNullOrEmpty()) View.GONE else View.VISIBLE

        getPositiveTV()?.text = content
        positiveListener = listener
    }

    /**
     * 确认按钮
     */
    fun setConfirm(content: String = VMStr.byRes(R.string.vm_btn_confirm), listener: View.OnClickListener? = null) {
        getConfirmTV()?.text = content
        confirmListener = listener
    }
}