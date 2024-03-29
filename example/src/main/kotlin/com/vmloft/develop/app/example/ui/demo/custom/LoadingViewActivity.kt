package com.vmloft.develop.app.example.ui.demo.custom


import com.didi.drouter.annotation.Router
import com.vmloft.develop.library.base.BActivity

import com.vmloft.develop.app.example.databinding.ActivityDemoViewLoadingBinding
import com.vmloft.develop.app.example.router.AppRouter

/**
 * Created by lzan13 on 2017/4/1.
 * 描述：测试 Loading 控件
 */
@Router(path = AppRouter.appLoading)
class LoadingViewActivity : BActivity<ActivityDemoViewLoadingBinding>() {

    override fun initVB() = ActivityDemoViewLoadingBinding.inflate(layoutInflater)

    override fun initUI() {
        super.initUI()
        setTopTitle("自定义 Loading 控件演示")

    }

    override fun initData() {}

}