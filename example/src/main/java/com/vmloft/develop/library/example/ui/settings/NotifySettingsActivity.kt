package com.vmloft.develop.library.example.ui.settings

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.base.BaseActivity
import com.vmloft.develop.library.example.common.Constants
import com.vmloft.develop.library.example.common.SPManager
import com.vmloft.develop.library.template.notify.NotifyManager
import com.vmloft.develop.library.tools.utils.VMFile

import kotlinx.android.synthetic.main.activity_settings_notify.*
import kotlinx.android.synthetic.main.widget_common_top_bar.*

/**
 * Create by lzan13 on 2020/06/07 21:06
 * 描述：设置
 */
@Route(path = "/VMLoft/SettingsNotify")
class NotifySettingsActivity : BaseActivity() {
    // 缓存地址
    private val cachePath = "${VMFile.cacheFromSDCard}${Constants.cacheImageDir}"

    override fun layoutId(): Int = R.layout.activity_settings_notify

    override fun initUI() {
        super.initUI()
        setTopTitle(R.string.settings_notify)

        commonTopBar.setOnClickListener {
            NotifyManager.sendNotify("这是测试通知栏提醒的内容", "这是测试通知标题")
        }

        // 通知开关
        notifyMsgLV.setOnClickListener {
            notifyMsgLV.isActivated = !notifyMsgLV.isActivated
            notifyMsgSystemLL.visibility = if (notifyMsgLV.isActivated) View.VISIBLE else View.GONE
            SPManager.instance.setNotifyMsgSwitch(notifyMsgLV.isActivated)
        }
        notifyMsgDetailLV.setOnClickListener {
            notifyMsgDetailLV.isActivated = !notifyMsgDetailLV.isActivated
            notifyMsgSystemLL.visibility = if (notifyMsgLV.isActivated) View.VISIBLE else View.GONE
            SPManager.instance.setNotifyMsgDetailSwitch(notifyMsgDetailLV.isActivated)
        }

        // 打开设置
        notifyMsgSystemLV.setOnClickListener { NotifyManager.openNotifySetting() }

    }

    override fun initData() {
        notifyMsgLV.isActivated = SPManager.instance.getNotifyMsgSwitch()
        notifyMsgDetailLV.isActivated = SPManager.instance.getNotifyMsgDetailSwitch()
        notifyMsgSystemLL.visibility = if (notifyMsgLV.isActivated) View.VISIBLE else View.GONE
    }

}