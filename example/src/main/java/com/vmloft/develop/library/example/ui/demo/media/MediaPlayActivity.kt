package com.vmloft.develop.library.example.ui.demo.media

import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener

import com.alibaba.android.arouter.facade.annotation.Route
import com.shuyu.gsyvideoplayer.GSYVideoManager

import com.vmloft.develop.library.example.R.layout
import com.vmloft.develop.library.example.base.BaseActivity
import com.vmloft.develop.library.example.router.AppRouter

import kotlinx.android.synthetic.main.activity_media_play.*


/**
 * Created by lzan13 on 2017/6/14.
 * 测试系统播放音频相关界面
 */
@Route(path = AppRouter.appMediaPlay)
class MediaPlayActivity : BaseActivity() {

    lateinit var videoUrl: String

    override fun layoutId() = layout.activity_media_play

    override fun initUI() {
        super.initUI()
        setTopTitle("媒体播放")

    }

    override fun initData() {
        videoUrl = "http://200024424.vod.myqcloud.com/200024424_709ae516bdf811e6ad39991f76a4df69.f20.mp4"
        setupPlayer()
    }

    private fun setupPlayer() {
        videoPlayer.setUp(videoUrl,true,"")

        //增加封面
//        val imageView = ImageView(this)
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP)
//        imageView.setImageResource(R.mipmap.xxx1)
//        videoPlayer.setThumbImageView(imageView)
        //增加title
//        videoPlayer.titleTextView.visibility = View.VISIBLE
        //设置返回键
//        videoPlayer.backButton.visibility = View.VISIBLE
        //设置旋转
//        orientationUtils = OrientationUtils(this, videoPlayer)
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
//        videoPlayer.fullscreenButton.setOnClickListener { orientationUtils.resolveByClick() }
        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true)
        //设置返回按键功能
//        videoPlayer.backButton.setOnClickListener { onBackPressed() }
        videoPlayer.startPlayLogic()
    }

    /**
     *
     */
    override fun onDestroy() {
        GSYVideoManager.releaseAllVideos()
        super.onDestroy()
    }
}