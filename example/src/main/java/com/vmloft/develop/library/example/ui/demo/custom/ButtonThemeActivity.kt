package com.vmloft.develop.library.example.ui.demo.custom

import com.alibaba.android.arouter.facade.annotation.Route
import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.base.BaseActivity
import com.vmloft.develop.library.example.router.AppRouter

/**
 * Created by lzan13 on 2017/4/7.
 * 测试控件样式主题界面
 */
@Route(path = AppRouter.appStyle)
class ButtonThemeActivity : BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_view_button
    }

    override fun initUI() {
        super.initUI()
        setTopTitle("展示自定义按钮")
    }

    override fun initData() {

    }

}