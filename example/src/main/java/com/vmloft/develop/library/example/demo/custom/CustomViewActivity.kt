package com.vmloft.develop.library.example.demo.custom

import android.view.View

import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.library.example.R.color
import com.vmloft.develop.library.example.R.drawable
import com.vmloft.develop.library.example.R.layout
import com.vmloft.develop.library.example.base.AppActivity
import com.vmloft.develop.library.tools.widget.record.VMRecordView.RecordListener
import com.vmloft.develop.library.tools.widget.toast.VMToast

import kotlinx.android.synthetic.main.activity_view_custom.customRatioLayout
import kotlinx.android.synthetic.main.activity_view_custom.customRecordView
import kotlinx.android.synthetic.main.activity_view_custom.customTimerBtn

/**
 * Created by lzan13 on 2017/4/1.
 * 描述：测试录音控件
 */
@Route(path = "/VMLoft/CustomView")
class CustomViewActivity : AppActivity() {

    override fun layoutId(): Int {
        return layout.activity_view_custom
    }

    override fun initUI() {
        super.initUI()
        setTopTitle("自定义控件演示")
        customRecordView.setRecordListener(object : RecordListener() {
            override fun onStart() {
                VMToast.make(mActivity, "录音开始").done()
            }

            override fun onCancel() {
                VMToast.make(mActivity, "录音取消").error()
            }

            override fun onError(code: Int, desc: String?) {
                VMToast.make(mActivity, "录音失败 %d %s", code, desc).error()
            }

            override fun onComplete(path: String?, time: Long) {
                VMToast.make(mActivity, "录音完成 %s %d", path, time).done()
            }
        })
    }

    override fun initData() {}

    fun startTimer(view: View) {
        customTimerBtn.startTimer()
    }

    fun toast1(view: View) {
        VMToast.make(mActivity, "测试自定义弹出 Toast 提醒功能，这是默认提醒样式！自定义颜色的")
                .setBGColor(color.app_bg_dark)
                .setIcon(drawable.emoji_dog)
                .setMsgColor(color.app_title_light)
                .show()
    }

    fun toast2(view: View) {
        VMToast.make(mActivity, "测试自定义表情雨控件").done()
    }

    fun toast3(view: View) {
        VMToast.make(mActivity, "测试自定义弹出 Toast 提醒功能，这是错误提醒默认样式！红色的").error()
    }

    fun setRatioLayout(view: View) {
        customRatioLayout.setFollowWidth(false)
    }
}