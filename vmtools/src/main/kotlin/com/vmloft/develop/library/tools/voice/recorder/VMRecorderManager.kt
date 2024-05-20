package com.vmloft.develop.library.tools.voice.recorder

/**
 * Created by lzan13 on 2024/1/19 14:58
 * 描述：语音管理类
 */
object VMRecorderManager {
    // 声音采样时间间隔
    var sampleTime: Int = 100

    // 触摸动画时间
    var touchAnimTime: Long = 1000L

    // 声音播放动画时间间隔
    var playAnimTime: Long = 100L

    const val errorNone = 0 // 没有错误
    const val errorSystem = 1 // 系统错误
    const val errorFailed = 2 // 录制失败
    const val errorRecording = 3 // 正在录制
    const val errorCancel = 4 // 录音取消
    const val errorShort = 5 // 录音时间过短

    var maxDuration = 60 * 1000 // 录音最大持续时间 60 秒
//    var maxDuration = 10 * 1000 // 录音最大持续时间 60 秒

    // 参考 https://www.cnblogs.com/yongdaimi/p/10722355.html
//    8,000 Hz - 电话所用采样率, 对于人的说话已经足够
//    11,025 Hz - AM调幅广播所用采样率
//    22,050 Hz和24,000 Hz - FM调频广播所用采样率
//    32,000 Hz - miniDV 数码视频 camcorder、DAT (LP mode)所用采样率
//    44,100 Hz - 音频 CD, 也常用于 MPEG-1 音频（VCD, SVCD, MP3）所用采样率
//    47,250 Hz - 商用 PCM 录音机所用采样率
//    48,000 Hz - miniDV、数字电视、DVD、DAT、电影和专业音频所用的数字声音所用采样率
//    50,000 Hz - 商用数字录音机所用采样率
//    96,000 或者 192,000 Hz - DVD-Audio、一些 LPCM DVD 音轨、BD-ROM（蓝光盘）音轨、和 HD-DVD （高清晰度 DVD）音轨所用所用采样率
//    2.8224 MHz - Direct Stream Digital 的 1 位 sigma-delta modulation 过程所用采样率。
    const val samplingRate = 32000 // 音频输入采样率 单位 Hz
    const val samplingOutRate = 32000 // 音频输出采样率 单位 Hz

    private var recorderEngine: VMRecorderEngine? = null

    /**
     * 设置录音机引擎
     */
    fun setRecorderEngine(engine: VMRecorderEngine) {
        recorderEngine = engine
    }

    fun getRecorderEngine(): VMRecorderEngine {
        if (recorderEngine == null) {
            recorderEngine = VMAudioRecorder()
        }
        return recorderEngine!!
    }
}