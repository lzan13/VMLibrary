package com.vmloft.develop.library.tools.voice.recorder

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder

import com.vmloft.develop.library.tools.utils.VMDate
import com.vmloft.develop.library.tools.utils.VMFile
import com.vmloft.develop.library.tools.utils.VMSystem
import com.vmloft.develop.library.tools.utils.logger.VMLog
import com.vmloft.develop.library.tools.voice.encoder.EncodeTask
import com.vmloft.develop.library.tools.voice.encoder.VMLame

import java.io.File
import java.io.IOException

/**
 * Created by lzan13 on 2024/1/22 18:52
 * 描述：AudioRecord 实现录音
 */
class VMAudioRecorder : VMRecorderEngine() {
    var audioRecord: AudioRecord? = null

    private var samplingRate = 8000 // 音频采样率 单位 Hz
    private var samplingOutRate = 8000 // 音频转mp3采样率 单位 Hz
    private var mp3BitRate = 32 // 输出比特率
    private var mp3Quality = 7 // mp3 质量
    private var channel = AudioFormat.CHANNEL_IN_MONO // 音频的声道类型
    private var encodeFormat = AudioFormat.ENCODING_PCM_16BIT // 音频的编码格式
    private var encodingBitRate = 64 // 音频编码比特率

    private var frameCount = 160 // 缓冲采样周期
    private var bufferSize = 0 // 缓冲区大小
    lateinit var bufferData: ShortArray // 缓冲区数据

    private var maxDuration = 60 * 60 * 1000 // 录音最大持续时间 60 分钟

    // 缓冲区大小
//    private var bufferSize = AudioRecord.getMinBufferSize(samplingRate, channel, encodeFormat)

    // 计算分贝基准值
    private var decibelBase = 1

    // 最后一次获取时间
    private var lastTime = 0L

    // 当前分贝
    private var currentDecibel = 1

    private var isRecording = false // 是否录制中
    private var filePath: String = "" // 录制文件保存路径

    /**
     * 初始化录音机
     */
    override fun initVoiceRecorder() {
        VMLame.init(samplingRate, channel, samplingOutRate, mp3BitRate, mp3Quality)
        bufferSize = calculateBufferSize()
        bufferData = ShortArray(bufferSize)
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC, // 音频源
            samplingRate, // 音频的采样频率
            channel, // 音频的声道类型
            encodeFormat, // 音频的编码格式
            bufferSize
        )
        // 设置通知周期
        audioRecord?.setPositionNotificationPeriod(frameCount)
    }

    /**
     * 开始录音
     */
    override fun startRecord(path: String?): Int {
        // 判断录制系统是否空闲
        if (isRecording) {
            return VMRecorderManager.errorRecording
        }

        // 判断媒体录影机是否释放，没有则释放
        if (audioRecord != null) {
            audioRecord?.release()
            audioRecord = null
        }

        // 设置录制状态
        isRecording = true
        filePath = if (path.isNullOrEmpty()) {
            // 这里默认保存在 /sdcard/android/data/packagename/files/voice/下
            VMFile.filesPath("voice") + "VMVoice_" + VMDate.filenameDateTime() + ".mp3"
        } else {
            path
        }

        // 释放之后重新初始化
        initVoiceRecorder()

        // 设置录制位置变化监听器
        val file = File(filePath)
        val encodeTask = EncodeTask(file, bufferSize)
        encodeTask.startTask()

        audioRecord?.setRecordPositionUpdateListener(encodeTask, encodeTask.getHandler())
        try {
            // 开始录制
            audioRecord?.startRecording()
            // 读取数据要在子线程进行
            VMSystem.runTask { readData(encodeTask) }
        } catch (e: IOException) {
            releaseRecorder()
            VMLog.e("录音系统出现错误 " + e.message)
            return VMRecorderManager.errorSystem
        }
        return VMRecorderManager.errorNone
    }

    /**
     * 读取缓存数据
     */
    private fun readData(encodeTask: EncodeTask) {
        while (isRecording) {
            val readSize = audioRecord?.read(bufferData, 0, bufferSize) ?: 0
            if (readSize > 0) {
                encodeTask.addTask(bufferData, readSize)
                // 计算声音分贝
                calculateDecibel(readSize)
            }
        }
        // 停止编码任务
        encodeTask.stopTask()
    }

    /**
     * 停止录音
     */
    override fun stopRecord(): Int {
        // 停止录音，将录音状态设置为 false
        isRecording = false
        // 释放录音机
        var result = releaseRecorder()
        if (result != VMRecorderManager.errorNone) {
            return result
        }
        // 根据录制结果判断录音是否成功
        if (!VMFile.isFileExists(filePath)) {
            VMLog.e("录音失败没有生成文件")
            result = VMRecorderManager.errorFailed
        }
        return result
    }

    /**
     * 取消录音
     */
    override fun cancelRecord() {
        // 停止录音，将录音状态设置为false
        isRecording = false
        // 释放录音机
        releaseRecorder()
        // 取消录音，删除文件
        if (VMFile.isFileExists(filePath)) {
            VMFile.deleteFile(filePath)
        }
    }

    /**
     * 获取录音文件
     */
    override fun getRecordFile(): String {
        return filePath
    }

    /**
     * 获取声音分贝大小
     */
    override fun decibel(): Int {
        return currentDecibel
    }

    /**
     * 计算分贝
     */
    private fun calculateDecibel(readSize: Int) {
        // 声音大小 100 毫秒采样一次
        if (System.currentTimeMillis() - lastTime < VMRecorderManager.sampleTime) return
        lastTime = System.currentTimeMillis()
        var v: Long = 0
        // 将 buffer 内容取出，进行平方和运算
        for (value in bufferData) {
            v += (value * value).toLong()
        }
        // 平方和除以数据总长度，得到音量大小。
        val mean = v.toDouble() / readSize
        currentDecibel = (12 * Math.log10(mean)).toInt()
        if (currentDecibel > 100) currentDecibel = 100
        if (currentDecibel < 1) currentDecibel = 1
    }

    /**
     * 根据样本数重新计算缓冲区大小
     */
    private fun calculateBufferSize(): Int {
        // 根据定义好的几个配置，来获取合适的缓冲大小
        var bufferSize = AudioRecord.getMinBufferSize(samplingRate, channel, encodeFormat)
        val bytesPerFrame = 2
        // 通过样本数重新计算缓冲区大小（能够整除样本数），以便周期性通知
        var frameSize = bufferSize / bytesPerFrame
        if (frameSize % frameCount != 0) {
            frameSize += frameCount - frameSize % frameCount
            bufferSize = frameSize * bytesPerFrame
        }
        return bufferSize
    }

    /**
     * 释放录音机
     */
    override fun releaseRecorder(): Int {
        isRecording = false
//        filePath = ""

        // 停止并释放录音机
        if (audioRecord != null) {
            try {
                audioRecord?.stop()
                audioRecord?.release()
            } catch (e: IllegalStateException) {
                VMLog.e("录音系统出现错误 " + e.message)
                return VMRecorderManager.errorSystem
            } catch (e: RuntimeException) {
                VMLog.e("录音系统出现错误 " + e.message)
                return VMRecorderManager.errorSystem
            }
            audioRecord = null
        }
        return VMRecorderManager.errorNone
    }

}