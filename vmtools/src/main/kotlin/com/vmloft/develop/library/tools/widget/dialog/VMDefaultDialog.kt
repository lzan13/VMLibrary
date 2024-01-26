package com.vmloft.develop.library.tools.widget.dialog

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView

import com.vmloft.develop.library.tools.widget.dialog.VMBDialog
import com.vmloft.develop.library.tools.databinding.VmWidgetDefaultDialogBinding

/**
 * Create by lzan13 on 2021/5/13 11:41
 * 描述：自定义通用对话框
 */
open class VMDefaultDialog(context: Context) : VMBDialog<VmWidgetDefaultDialogBinding>(context) {

    override fun initVB() = VmWidgetDefaultDialogBinding.inflate(LayoutInflater.from(context))

    override fun getTitleTV(): TextView? = mBinding.vmDialogTitleTV

    override fun getContentTV(): TextView? = mBinding.vmDialogContentTV

    override fun getContainerLL(): LinearLayout? = mBinding.vmDialogContainerLL

    override fun getNegativeTV(): TextView? = mBinding.vmDialogPositiveTV

    override fun getPositiveTV(): TextView? = mBinding.vmDialogConfirmTV

}