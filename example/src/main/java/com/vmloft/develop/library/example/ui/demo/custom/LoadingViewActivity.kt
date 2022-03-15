package com.vmloft.develop.library.example.ui.demo.custom


import com.alibaba.android.arouter.facade.annotation.Route
import com.vmloft.develop.library.base.BActivity

import com.vmloft.develop.library.example.databinding.ActivityDemoViewLoadingBinding
import com.vmloft.develop.library.example.router.AppRouter

/**
 * Created by lzan13 on 2017/4/1.
 * 描述：测试 Loading 控件
 */
@Route(path = AppRouter.appLoading)
class LoadingViewActivity : BActivity<ActivityDemoViewLoadingBinding>() {

    override fun initVB() = ActivityDemoViewLoadingBinding.inflate(layoutInflater)

    override fun initUI() {
        super.initUI()
        setTopTitle("自定义 Loading 控件演示")

    }

    override fun initData() {}

}