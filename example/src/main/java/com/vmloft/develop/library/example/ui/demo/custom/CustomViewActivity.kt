package com.vmloft.develop.library.example.ui.demo.custom

import android.view.View

import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.base.BaseActivity
import com.vmloft.develop.library.example.router.AppRouter
import com.vmloft.develop.library.example.utils.showBar
import com.vmloft.develop.library.example.utils.errorBar
import com.vmloft.develop.library.tools.widget.record.VMRecordView.RecordListener
import com.vmloft.develop.library.tools.widget.toast.VMToast

import kotlinx.android.synthetic.main.activity_view_custom.customRatioLayout
import kotlinx.android.synthetic.main.activity_view_custom.customRecordView
import kotlinx.android.synthetic.main.activity_view_custom.customTimerBtn

/**
 * Created by lzan13 on 2017/4/1.
 * 描述：测试录音控件
 */
@Route(path = AppRouter.appCustomView)
class CustomViewActivity : BaseActivity() {

    override fun layoutId() = R.layout.activity_view_custom

    override fun initUI() {
        super.initUI()
        setTopTitle("自定义控件演示")
        customRecordView.setRecordListener(object : RecordListener() {
            override fun onStart() {
                showBar("录音开始")
            }

            override fun onCancel() {
                errorBar("录音取消")
            }

            override fun onError(code: Int, desc: String?) {
                errorBar("录音失败 $code $desc")
            }

            override fun onComplete(path: String?, time: Long) {
                showBar("录音完成 $path $time")
            }
        })
    }

    override fun initData() {}

    fun startTimer(view: View) {
        customTimerBtn.startTimer()
    }

    fun toast1(view: View) {
        VMToast.showBar(mActivity,
                "测试自定义弹出 Toast 提醒功能，这是默认提醒样式！自定义颜色的",
                VMToast.durationLong,
                R.drawable.emoji_dog,
                R.color.app_bg,
                R.color.app_title)
    }

    fun toast2(view: View) {
        showBar("测试自定义提醒功能")
    }

    fun toast3(view: View) {
        errorBar("测试自定义提醒功能，这是错误提醒默认样式！红色的")
    }

    fun setRatioLayout(view: View) {
        customRatioLayout.setFollowWidth(false)
    }
}