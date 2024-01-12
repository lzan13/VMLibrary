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

import com.vmloft.develop.library.base.widget.CommonDialog
import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.tools.utils.VMColor
import com.vmloft.develop.library.tools.utils.VMStr
import com.vmloft.develop.library.tools.utils.logger.VMLog

/**
 * Create by lzan13 on 2021/07/10 21:41
 * 描述：用户政策与隐私协议对话框
 */
class AgreementPolicyDialog(context: Context) : CommonDialog(context) {

    init {
        val agreementContent = VMStr.byRes(R.string.agreement_policy_dialog_content)
        val userAgreement = VMStr.byRes(R.string.user_agreement)
        val privacyPolicy = VMStr.byRes(R.string.privacy_policy)
        //创建一个 SpannableString对象
        val sp = SpannableString(agreementContent)
        var start = 0
        var end = 0

        start = agreementContent.indexOf(userAgreement)
        end = start + userAgreement.length
        //设置超链接
        sp.setSpan(CustomURLSpan("agreement"), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        //设置高亮样式
        sp.setSpan(ForegroundColorSpan(VMColor.byRes(com.vmloft.develop.library.base.R.color.app_accent)), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //设置高亮样式二

        start = agreementContent.indexOf(privacyPolicy)
        end = start + privacyPolicy.length
        //设置超链接
        sp.setSpan(CustomURLSpan("policy"), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        //设置高亮样式
        sp.setSpan(ForegroundColorSpan(VMColor.byRes(com.vmloft.develop.library.base.R.color.app_accent)), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        //设置斜体
//        sp.setSpan(StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 19, 21, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //设置下划线
//        sp.setSpan(UnderlineSpan(), 22, 25, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        setContent(sp)
        getContentTV()?.highlightColor = VMColor.byRes(com.vmloft.develop.library.tools.R.color.vm_transparent)
        getContentTV()?.movementMethod = LinkMovementMethod.getInstance()
    }

    class CustomURLSpan(val type: String) : URLSpan(type) {
        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            //设置超链接文本的颜色
//            ds.color = Color.RED
            //这里可以去除点击文本的默认的下划线
            ds.isUnderlineText = false
            ds.bgColor = VMColor.byRes(com.vmloft.develop.library.tools.R.color.vm_transparent)
        }

        override fun onClick(widget: View) {
            VMLog.d("点击 span")
            // 去除点击后字体出现的背景色
//            (widget as? TextView)?.highlightColor = VMColor.byRes(R.color.vm_transparent)
//            AppRouter.goAgreementPolicy(type)
        }
    }
}