package com.vmloft.develop.library.example.ui.demo.media


import com.alibaba.android.arouter.facade.annotation.Route
import com.vmloft.develop.library.common.base.BaseActivity

import com.vmloft.develop.library.example.R.layout
import com.vmloft.develop.library.example.router.AppRouter


/**
 * Created by lzan13 on 2017/6/14.
 * 测试系统播放音频相关界面
 */
@Route(path = AppRouter.appMediaPlay)
class MediaPlayActivity : BaseActivity() {

    lateinit var videoUrl: String

    override fun layoutId() = layout.activity_demo_media_play

    override fun initUI() {
        super.initUI()
        setTopTitle("媒体播放")

    }

    override fun initData() {
        videoUrl = "http://200024424.vod.myqcloud.com/200024424_709ae516bdf811e6ad39991f76a4df69.f20.mp4"
        setupPlayer()
    }

    private fun setupPlayer() {

    }

}