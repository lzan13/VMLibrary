package com.vmloft.develop.library.tools.widget

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView

import com.vmloft.develop.library.tools.R
import com.vmloft.develop.library.tools.base.VMBaseDialog
import kotlinx.android.synthetic.main.vm_widget_default_dialog.*

/**
 * Create by lzan13 on 2021/5/13 11:41
 * 描述：自定义通用对话框
 */
open class VMDefaultDialog(context: Context) : VMBaseDialog(context) {

    override fun layoutId() = R.layout.vm_widget_default_dialog

    override fun getTitleTV(): TextView?  = vmDialogTitleTV

    override fun getContentTV(): TextView? = vmDialogContentTV

    override fun getContainerLL(): LinearLayout? = vmDialogContainerLL

    override fun getNegativeTV(): TextView? = vmDialogPositiveTV

    override fun getPositiveTV(): TextView? = vmDialogConfirmTV

}