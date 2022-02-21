package com.vmloft.develop.library.example.ui.widget

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView

import com.vmloft.develop.library.example.databinding.WidgetTbLiveMsgEtDialogBinding
import com.vmloft.develop.library.tools.base.VMBDialog

/**
 * Create by lzan13 on 2021/07/10 21:41
 * 描述：淘宝直播消息输入对话框
 */
class TBLiveMsgETDialog(context: Context) : VMBDialog<WidgetTbLiveMsgEtDialogBinding>(context) {

    override fun initVB() = WidgetTbLiveMsgEtDialogBinding.inflate(LayoutInflater.from(context))

    override fun getNegativeTV(): TextView = mBinding.dialogNegativeTV

    override fun getPositiveTV(): TextView = mBinding.dialogPositiveTV

    override fun setContent(content: String) {
        mBinding.dialogContentET.setText(content)
    }

    fun setTime(time: String) {
        mBinding.dialogTimeET.setText(time)
    }

    fun getContent(): String {
        val content = mBinding.dialogContentET.text.trim().toString()
        positiveDismissSwitch = content.isNotEmpty()
        return content
    }

    fun getTime(): String {
        val time = mBinding.dialogTimeET.text.trim().toString()
        positiveDismissSwitch = time.isNotEmpty()
        return time
    }

}