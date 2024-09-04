package com.vmloft.develop.app.example.ui.demo.custom

import android.view.View

import com.didi.drouter.annotation.Router

import com.vmloft.develop.app.example.R
import com.vmloft.develop.app.example.databinding.ActivityDemoViewCustomBinding
import com.vmloft.develop.app.example.router.AppRouter
import com.vmloft.develop.app.example.utils.darkBar
import com.vmloft.develop.app.example.utils.errorBar
import com.vmloft.develop.library.base.BActivity
import com.vmloft.develop.library.base.widget.CommonDialog
import com.vmloft.develop.library.base.utils.showBar
import com.vmloft.develop.library.base.utils.show
import com.vmloft.develop.library.tools.widget.tips.VMTips

/**
 * Created by lzan13 on 2017/4/1.
 * 描述：测试录音控件
 */
@Router(path = AppRouter.appCustomView)
class CustomViewActivity : BActivity<ActivityDemoViewCustomBinding>() {

    override fun initVB() = ActivityDemoViewCustomBinding.inflate(layoutInflater)

    override fun initUI() {
        super.initUI()
        setTopTitle("自定义控件演示")

    }

    override fun initData() {}

    fun startTimer(view: View) {
        binding.customTimerBtn.startTimer()
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

    fun showDialog(view: View) {
        mDialog = CommonDialog(this)
        (mDialog as CommonDialog).let { dialog ->
            dialog.setTitle("测试对话框标题")
            dialog.setContent("测试对话框内容测试对话框内容测试对话框内容测试对话框内容")
            dialog.setNegative("消极按钮") {
                this.darkBar("点击了 消极按钮")
            }
            dialog.setPositive("积极按钮") {
                this.showBar("点击了 积极按钮")
            }
            dialog.show()
        }
    }

}