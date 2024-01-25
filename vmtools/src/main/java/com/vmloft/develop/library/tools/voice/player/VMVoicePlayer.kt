package com.vmloft.develop.library.tools.voice.player

import android.net.Uri
import androidx.media3.common.AudioAttributes
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.Listener
import androidx.media3.exoplayer.ExoPlayer

import com.vmloft.develop.library.tools.VMTools
import com.vmloft.develop.library.tools.utils.VMSystem

import java.util.Timer
import java.util.TimerTask

/**
 * Created by lzan13 on 2024/1/24 20:56
 * 描述：语音播放器，使用ExoPlayer实现
 */
object VMVoicePlayer {

    const val statusIdle = 0 // 空闲
    const val statusPrepare = 1 // 准备播放
    const val statusPlaying = 2 // 播放中
    const val statusPause = 3 // 暂停中

    // 定时器
    private var timer: Timer? = null

    private var exoPlayer: ExoPlayer? = null

    // 当前播放文件地址
    private var currentUrl: String = ""
    private var currentStatus: Int = statusIdle

    // 是否需要拖动进度
    private var isSeek = false

    // 当前进度
    private var currentProgress: Float = 0f

    // 进度动画时间
    var progressAnimDuration = 100L

    // 播放动作监听接口
    private var onPlayActionListener: IOnPlayActionListener? = null

    /**
     * 初始化播放器
     */
    private fun initPlayer() {
        if (exoPlayer == null) {
            // 初始化播放器实例
            exoPlayer = ExoPlayer.Builder(VMTools.context)
                .setAudioAttributes(AudioAttributes.DEFAULT, true) // 设置自动处理音频焦点
                .setHandleAudioBecomingNoisy(true) // 设置输出设备变化自动暂停播放
                .build()
            // 添加播放器监听
            exoPlayer?.addListener(playerListener)
        }
    }

    /**
     * 设置播放监听接口
     */
    fun setOnPlayActionListener(listener: IOnPlayActionListener) {
        onPlayActionListener = listener
    }

    /**
     * 判断是否在播放当前
     */
    fun isCurrentPlay(url: String): Boolean {
        return currentUrl == url && currentStatus != statusIdle
    }

    /**
     * 开始播放
     */
    fun start(url: String) {
        if (currentUrl != url) {
            // 换源，停止再播放
            stop()

            currentUrl = url
            preparePlay()
        } else {
            if (currentStatus == statusIdle) {
                // 空闲直接播放
                preparePlay()
            } else if (currentStatus == statusPause) {
                resume()
            } else {
                // 不处理
            }
        }
    }

    /**
     * 真正准备播放方法
     */
    private fun preparePlay() {
        initPlayer()

        currentStatus = statusPrepare
        // 设置播放资源
        val mediaItem = MediaItem.fromUri(Uri.parse(currentUrl))
        exoPlayer?.setMediaItem(mediaItem)
        // 准备播放
        exoPlayer?.prepare()
        // 开始播放
        exoPlayer?.play()
    }

    /**
     * 继续播放
     */
    fun resume() {
        currentStatus = statusPlaying
        exoPlayer?.playWhenReady = true
    }

    /**
     * 暂停播放
     */
    fun pause() {
        currentStatus = statusPause
        exoPlayer?.pause()
    }

    /**
     * 更新播放进度
     */
    fun updateProgress(progress: Float) {
        isSeek = true
        currentProgress = progress
        if (currentStatus == statusPlaying) {
            exoPlayer?.seekTo(currentProgress.toLong())
            isSeek = false
        }
    }

    /**
     * 停止播放
     */
    fun stop() {
        if (currentStatus != statusIdle) {
            exoPlayer?.stop()
            exoPlayer?.release()
            exoPlayer?.removeListener(playerListener)
            exoPlayer = null
        }

        currentStatus = statusIdle
        currentProgress = 0f
    }

    /**
     * 开启定时器
     */
    private fun startTimer() {
        // 开始录音，记录开始录制时间
        timer = Timer()
        timer?.purge()
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                VMSystem.runInUIThread({
                    currentProgress = (exoPlayer?.currentPosition ?: 0L).toFloat()
                    // 回调当前播放位置 ms
                    onPlayActionListener?.onProgressChange(currentProgress)
                })
            }
        }
        timer?.scheduleAtFixedRate(task, 0, progressAnimDuration)
    }

    /**
     * 停止定时器
     */
    private fun stopTimer() {
        timer?.purge()
        timer?.cancel()
        timer = null
    }


    // 播放监听
    private val playerListener = object : Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            if (isPlaying) {
                currentStatus = statusPlaying
                if (isSeek) {
                    isSeek = false
                    exoPlayer?.seekTo(currentProgress.toLong())
                }
                onPlayActionListener?.onStart()
                startTimer()
            } else {
                currentStatus = statusPause
                onPlayActionListener?.onPause()
                stopTimer()
            }
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            // 播放结束
            if (playbackState == Player.STATE_ENDED) {
                onPlayActionListener?.onComplete()
                stop()
            }
        }
    }

    interface IOnPlayActionListener {

        /**
         * 播放开始
         */
        fun onStart()

        /**
         * 播放暂定
         */
        fun onPause()

        /**
         * 播放完成
         */
        fun onComplete()

        /**
         * 播放进度监听，需注意，这里为了和 [VMWaveformView] 联动，进度都以毫秒值为基准，最大进度为当前音频时间毫秒值，当前进度是当前播放毫秒值
         */
        fun onProgressChange(progress: Float)
    }
}
