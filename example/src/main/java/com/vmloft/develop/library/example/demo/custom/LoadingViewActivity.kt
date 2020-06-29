package com.vmloft.develop.library.example.demo.custom


import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.library.example.R.layout
import com.vmloft.develop.library.example.base.AppActivity

/**
 * Created by lzan13 on 2017/4/1.
 * 描述：测试 Loading 控件
 */
@Route(path = "/VMLoft/Loading")
class LoadingViewActivity : AppActivity() {

    override fun layoutId(): Int = layout.activity_view_loading

    override fun initUI() {
        super.initUI()
        setTopTitle("自定义 Loading 控件演示")

    }

    override fun initData() {}

}