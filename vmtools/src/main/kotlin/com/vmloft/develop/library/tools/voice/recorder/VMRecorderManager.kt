package com.vmloft.develop.library.tools.voice.recorder

/**
 * Created by lzan13 on 2024/1/19 14:58
 * 描述：语音管理类
 */
object VMRecorderManager {
    // 声音采样时间间隔
    var sampleTime: Long = 100L

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