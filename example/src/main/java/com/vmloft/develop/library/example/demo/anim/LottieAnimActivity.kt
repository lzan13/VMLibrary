package com.vmloft.develop.library.example.demo.anim

import com.alibaba.android.arouter.facade.annotation.Route
import com.vmloft.develop.library.example.R.layout
import com.vmloft.develop.library.example.base.AppActivity

/**
 * Created by lzan13 on 2017/4/7.
 * 描述：测试 Lottie 动画
 */
@Route(path = "/VMLoft/LottieAnim")
class LottieAnimActivity : AppActivity() {

    override fun layoutId(): Int = layout.activity_anim_lottie

    override fun initUI() {
        super.initUI()
        setTopTitle("Lottie 动画")
    }

    override fun initData() {

    }

}