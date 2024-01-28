package com.vmloft.develop.app.example.ui.demo.custom

import com.didi.drouter.annotation.Router

import com.vmloft.develop.app.example.databinding.ActivityDemoViewButtonBinding
import com.vmloft.develop.app.example.router.AppRouter
import com.vmloft.develop.library.base.BActivity

/**
 * Created by lzan13 on 2017/4/7.
 * 测试控件样式主题界面
 */
@Router(path = AppRouter.appStyle)
class ButtonThemeActivity : BActivity<ActivityDemoViewButtonBinding>() {
    override fun initVB() = ActivityDemoViewButtonBinding.inflate(layoutInflater)

    override fun initUI() {
        super.initUI()
        setTopTitle("展示自定义按钮")
    }

    override fun initData() {

    }

}