package com.vmloft.develop.library.example.ui.widget

import android.content.Context
import android.widget.EditText
import android.widget.TextView

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.common.widget.CommonDialog

/**
 * Create by lzan13 on 2021/07/10 21:41
 * 描述：淘宝直播消息输入对话框
 */
class TBLiveMsgETDialog(context: Context) : CommonDialog(context) {
    private var dialogContentET: EditText = this.findViewById(R.id.dialogContentET)
    private var dialogTimeET: EditText = this.findViewById(R.id.dialogTimeET)

    override fun layoutId() = R.layout.widget_tb_live_msg_et_dialog

    override fun getNegativeTV(): TextView? = findViewById(R.id.dialogNegativeTV)

    override fun getPositiveTV(): TextView? = findViewById(R.id.dialogPositiveTV)

    override fun setContent(content: String) {
        dialogContentET.setText(content)
    }

    fun setTime(time: String) {
        dialogTimeET.setText(time)
    }

    fun getContent(): String {
        val content = dialogContentET.text.trim().toString()
        positiveDismissSwitch = content.isNotEmpty()
        return content
    }

    fun getTime(): String {
        val time = dialogTimeET.text.trim().toString()
        positiveDismissSwitch = time.isNotEmpty()
        return time
    }

}