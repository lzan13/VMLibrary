package com.vmloft.develop.library.example.demo.style

import com.alibaba.android.arouter.facade.annotation.Route
import com.vmloft.develop.library.example.R.layout
import com.vmloft.develop.library.example.base.BaseActivity

/**
 * Created by lzan13 on 2017/4/7.
 * 测试控件样式主题界面
 */
@Route(path = "/VMLoft/BtnStyle")
class BtnStyleActivity : BaseActivity() {
    override fun layoutId(): Int {
        return layout.activity_theme
    }

    override fun initUI() {
        super.initUI()
        setTopTitle("展示自定义按钮")
    }

    override fun initData() {

    }

}