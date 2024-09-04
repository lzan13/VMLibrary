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

        binding.paintBtn.setOnClickListener { binding.drawView.isEraser = false }
        binding.eraserBtn.setOnClickListener { binding.drawView.isEraser = true }
        binding.clearBtn.setOnClickListener { binding.drawView.reset() }
        binding.imageBtn.setOnClickListener { getDrawContent() }
        binding.drawLL.setOnClickListener { binding.drawLL.visibility = View.GONE }


    }

    override fun initData() {

    }

    private fun getDrawContent() {
        binding.drawLL.visibility = View.VISIBLE
        val bitmap = binding.drawView.getBitmap()
        binding.drawIV.setImageBitmap(bitmap)
    }

}