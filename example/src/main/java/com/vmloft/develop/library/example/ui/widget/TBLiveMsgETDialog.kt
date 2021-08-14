package com.vmloft.develop.library.example.ui.widget

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.common.widget.CommonDialog
import com.vmloft.develop.library.tools.utils.VMColor
import com.vmloft.develop.library.tools.utils.VMStr

import kotlinx.android.synthetic.main.widget_agreement_policy_dialog.*
import kotlinx.android.synthetic.main.widget_agreement_policy_dialog.dialogNegativeTV
import kotlinx.android.synthetic.main.widget_agreement_policy_dialog.dialogPositiveTV
import kotlinx.android.synthetic.main.widget_tb_live_msg_et_dialog.*


/**
 * Create by lzan13 on 2021/07/10 21:41
 * 描述：淘宝直播消息输入对话框
 */
class TBLiveMsgETDialog(context: Context) : CommonDialog(context) {

    override fun layoutId() = R.layout.widget_tb_live_msg_et_dialog

    override fun getNegativeTV(): TextView? = dialogNegativeTV

    override fun getPositiveTV(): TextView? = dialogPositiveTV

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