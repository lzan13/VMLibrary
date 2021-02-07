package com.vmloft.develop.library.example.ui.demo.notify

import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.base.BaseActivity
import com.vmloft.develop.library.example.router.AppRouter
import com.vmloft.develop.library.example.utils.showBar
import com.vmloft.develop.library.example.utils.errorBar
import com.vmloft.develop.library.template.notify.NotifyManager

import kotlinx.android.synthetic.main.activity_notify.*


/**
 * Created by lzan13 on 2020/5/18.
 * 描述：测试通知提醒界面
 */
@Route(path = AppRouter.appNotifyTest)
class NotifyActivity : BaseActivity() {

    private var count: Int = 0

    override fun layoutId(): Int {
        return R.layout.activity_notify
    }

    override fun initUI() {
        super.initUI()
        setTopTitle("通知提醒")

        notifyMsgLV.setOnClickListener { NotifyManager.openNotifySetting() }
        notifyOtherLV.setOnClickListener { NotifyManager.openNotifySetting() }
        notifyCheckBtn.setOnClickListener {
            if (NotifyManager.checkNotifySetting()) {
                showBar("通知已打开")
            } else {
                errorBar("通知未打开")

            }
        }
        notifySendBtn.setOnClickListener {
            NotifyManager.sendNotify("这是通知栏提醒的内容 " + count++, "这是通知标题")
        }
    }

    override fun initData() {

    }


}