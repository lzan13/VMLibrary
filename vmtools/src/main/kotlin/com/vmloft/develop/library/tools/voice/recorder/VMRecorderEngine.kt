package com.vmloft.develop.library.tools.voice.recorder

/**
 * Created by lzan13 on 2024/01/10.
 * 描述：定义的录音功能引擎接口，主要定义录音的相关方法
 */
abstract class VMRecorderEngine {

    /**
     * 初始化录制音频
     */
    abstract fun initVoiceRecorder()

    /**
     * 开始录制声音文件
     *
     * @param path 录音文件保存地址，可以为空，默认保存到项目包名 files 下
     */
    abstract fun startRecord(path: String?): Int

    /**
     * 停止录制
     */
    abstract fun stopRecord(): Int

    /**
     * 取消录音
     */
    abstract fun cancelRecord()

    /**
     * 获取录制文件路径
     */
    abstract fun getRecordFile(): String

    /**
     * 获取声音分贝信息
     */
    abstract fun decibel(): Int

    /**
     * 获取傅里叶转换频谱数据
     */
    abstract fun getFFTData(): DoubleArray

    /**
     * 释放录音机
     */
    abstract fun releaseRecorder(): Int
}