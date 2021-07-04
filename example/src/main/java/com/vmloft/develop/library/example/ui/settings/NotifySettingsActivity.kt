package com.vmloft.develop.library.example.ui.settings

import android.view.View

import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.library.common.base.BaseActivity
import com.vmloft.develop.library.common.common.CConstants
import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.common.SPManager
import com.vmloft.develop.library.example.notify.NotifyManager
import com.vmloft.develop.library.example.router.AppRouter
import com.vmloft.develop.library.tools.utils.VMFile

import kotlinx.android.synthetic.main.activity_settings_notify.*


/**
 * Create by lzan13 on 2020/06/07 21:06
 * 描述：设置
 */
@Route(path = AppRouter.appSettingsNotify)
class NotifySettingsActivity : BaseActivity() {
    // 缓存地址
    private val cachePath = "${VMFile.cachePath}${CConstants.cacheImageDir}"


    override fun layoutId(): Int = R.layout.activity_settings_notify

    override fun initUI() {
        super.initUI()
        setTopTitle(R.string.settings_notify)

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
        notifyMsgLV.isActivated = SPManager.instance.isNotifyMsgSwitch()
        notifyMsgDetailLV.isActivated = SPManager.instance.isNotifyMsgDetailSwitch()
        notifyMsgSystemLL.visibility = if (notifyMsgLV.isActivated) View.VISIBLE else View.GONE
    }

}