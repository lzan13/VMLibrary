package com.vmloft.develop.library.example.ui.demo.custom

import android.view.View

import com.alibaba.android.arouter.facade.annotation.Route
import com.vmloft.develop.library.common.base.BActivity

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.databinding.ActivityDemoViewCustomBinding
import com.vmloft.develop.library.example.router.AppRouter
import com.vmloft.develop.library.example.utils.darkBar
import com.vmloft.develop.library.example.utils.showBar
import com.vmloft.develop.library.example.utils.errorBar
import com.vmloft.develop.library.example.utils.show
import com.vmloft.develop.library.tools.widget.record.VMRecordView.RecordListener
import com.vmloft.develop.library.tools.widget.tips.VMTips

/**
 * Created by lzan13 on 2017/4/1.
 * 描述：测试录音控件
 */
@Route(path = AppRouter.appCustomView)
class CustomViewActivity : BActivity<ActivityDemoViewCustomBinding>() {

    override fun initVB() = ActivityDemoViewCustomBinding.inflate(layoutInflater)

    override fun initUI() {
        super.initUI()
        setTopTitle("自定义控件演示")
        mBinding.customRecordView.setRecordListener(object : RecordListener() {
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
        mBinding.customTimerBtn.startTimer()
    }

    fun tips1(view: View) {
        VMTips.showBar(mActivity, "测试自定义图标提示条", VMTips.durationLong, R.drawable.emoji_dog)
    }

    fun tips2(view: View) {
        showBar("测试自定义默认提示条")
    }

    fun tips3(view: View) {
        darkBar("测试自定义暗色提示条")
    }

    fun tips4(view: View) {
        errorBar("测试自定义错误提示条，这是错误提醒默认样式！红色的")
    }

    fun tips5(view: View) {
        this.show("测试系统提示")
    }

    fun setRatioLayout(view: View) {
        mBinding.customRatioLayout.setFollowWidth(false)
    }
}