package com.vmloft.develop.app.example.ui.demo.draw


import android.view.View
import com.didi.drouter.annotation.Router

import com.vmloft.develop.library.base.BActivity
import com.vmloft.develop.app.example.databinding.ActivityDemoDrawBinding
import com.vmloft.develop.app.example.router.AppRouter


/**
 * Create by lzan13 on 2021/8/13
 * 描述：测试 绘画 View
 */
@Router(path = AppRouter.appCustomDrawView)
class DrawActivity : BActivity<ActivityDemoDrawBinding>() {

    override fun initVB() = ActivityDemoDrawBinding.inflate(layoutInflater)

    override fun initUI() {
        super.initUI()

        mBinding.paintBtn.setOnClickListener { mBinding.drawView.isEraser = false }
        mBinding.eraserBtn.setOnClickListener { mBinding.drawView.isEraser = true }
        mBinding.clearBtn.setOnClickListener { mBinding.drawView.reset() }
        mBinding.imageBtn.setOnClickListener { getDrawContent() }
        mBinding.drawLL.setOnClickListener { mBinding.drawLL.visibility = View.GONE }


    }

    override fun initData() {

    }

    private fun getDrawContent() {
        mBinding.drawLL.visibility = View.VISIBLE
        val bitmap = mBinding.drawView.getBitmap()
        mBinding.drawIV.setImageBitmap(bitmap)
    }

}