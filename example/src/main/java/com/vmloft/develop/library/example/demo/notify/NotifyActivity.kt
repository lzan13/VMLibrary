package com.vmloft.develop.library.example.demo.notify

import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.base.BaseActivity
import com.vmloft.develop.library.template.notify.NotifyManager
import com.vmloft.develop.library.tools.widget.toast.VMToast

import kotlinx.android.synthetic.main.activity_notify.*


/**
 * Created by lzan13 on 2020/5/18.
 * 描述：测试通知提醒界面
 */
@Route(path = "/VMLoft/Notify")
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
                VMToast.make(mActivity, "通知已打开").done()
            } else {
                VMToast.make(mActivity, "通知未打开").error()

            }
        }
        notifySendBtn.setOnClickListener {
            NotifyManager.sendNotify("这是通知栏提醒的内容 " + count++, "这是通知标题")
        }
    }

    override fun initData() {

    }


}