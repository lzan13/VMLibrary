package com.vmloft.develop.library.example.ui.widget

import android.content.Context
import android.view.LayoutInflater

import com.vmloft.develop.library.example.databinding.WidgetCustomGravityDialogBinding
import com.vmloft.develop.library.tools.widget.dialog.VMBDialog

/**
 * Create by lzan13 on 2022/02/21
 * 描述：自定义位置弹窗
 */
class CustomGravityDialog(context: Context) : VMBDialog<WidgetCustomGravityDialogBinding>(context) {

    override fun initVB() = WidgetCustomGravityDialogBinding.inflate(LayoutInflater.from(context))

}