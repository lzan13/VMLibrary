package com.vmloft.develop.library.example.demo.media

import android.media.MediaPlayer
import android.media.audiofx.Visualizer
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.library.example.R.layout
import com.vmloft.develop.library.example.base.BaseActivity

import java.io.IOException

/**
 * Created by lzan13 on 2017/6/14.
 * 测试系统播放音频相关界面
 */
@Route(path = "/VMLoft/MediaPlay")
class MediaPlayActivity : BaseActivity() {
    private val mediaStatusNormal = 0
    private val mediaStatusPausing = 1
    private val mediaStatusPlaying = 2

    // 媒体播放器
    private var mMediaPlayer: MediaPlayer? = null

    // 音频可视化工具，主要是为了获取音频波形信息
    private var mVisualizer: Visualizer? = null

    // 是否有音频正在播放中
    private var playStatus = mediaStatusNormal
    override fun layoutId(): Int {
        return layout.activity_media_play
    }

    override fun initUI() {
        super.initUI()
        setTopTitle("媒体播放")

    }

    override fun initData() {
    }

    /**
     * 最终播放操作
     */
    fun startPlayVoice(view: View) {
        try {
            /**
             * 通过 new() 的方式实例化 MediaPlayer ，也可以调用 create 方法，不过 create 必须传递Uri指向的文件
             * 这里为了方便可以直接根据文件地址加载，使用 setDataSource() 的方法
             *
             * 当使用 new() 或者调用 reset() 方法时 MediaPlayer 会进入 Idle 状态
             * 这两种方法的一个重要差别就是：如果在这个状态下调用了getDuration()等方法（相当于调用时机不正确），
             * 通过reset()方法进入idle状态的话会触发OnErrorListener.onFailed()，并且MediaPlayer会进入Error状态；
             * 如果是新创建的MediaPlayer对象，则并不会触发onError(),也不会进入Error状态；
             */
            mMediaPlayer = MediaPlayer()
            // 设置数据源，即要播放的音频文件，MediaPlayer 进入 Initialized 状态，必须在 Idle 状态下调用
            mMediaPlayer!!.setDataSource("/sdcard/Download/voice.amr")
            // 准备 MediaPlayer 进入 Prepared 状态
            mMediaPlayer!!.prepare()
            // 设置是否循环播放 默认为 false，必须在 Prepared 状态下调用
            mMediaPlayer!!.isLooping = false

            // 初始化设置 Visualizer
            onSetupVisualizer()

            // 开始播放状态将变为 Started，必须在 Prepared 状态下进行
            mMediaPlayer!!.start()

            // 设置当前状态为播放中
            playStatus = mediaStatusPlaying

            // 媒体播放结束监听
            mMediaPlayer!!.setOnCompletionListener { // 调用停止播放方法，主要是为了释放资源
                stopPlayVoice(view)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 停止播放，并释放资源
     */
    fun stopPlayVoice(view: View) {
        // 设置当前状态为正常
        playStatus = mediaStatusNormal

        // 释放 Visualizer
        if (mVisualizer != null) {
            // 释放音频可视化采集器
            mVisualizer!!.release()
            mVisualizer = null
        }
        // 释放 MediaPlayer
        if (mMediaPlayer != null) {
            // 停止播放
            mMediaPlayer!!.stop()
            // 释放 MediaPlayer
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

    /**
     * 初始化 Visualizer
     */
    private fun onSetupVisualizer() {
        //mBarVisualizerView.setAudioSessionId(mMediaPlayer.getAudioSessionId());
        //mWaveVisualizerView.setAudioSessionId(mMediaPlayer.getAudioSessionId());
    }
}