package com.vmloft.develop.library.tools.voice.recorder

import android.media.MediaRecorder
import android.media.MediaRecorder.AudioEncoder
import android.media.MediaRecorder.AudioSource
import android.media.MediaRecorder.OutputFormat

import com.vmloft.develop.library.tools.utils.VMDate
import com.vmloft.develop.library.tools.utils.VMFile
import com.vmloft.develop.library.tools.utils.logger.VMLog

import java.io.IOException

/**
 * Created by lzan13 on 2024/01/10.
 * 描述：定义的录音功能单例类，主要处理录音的相关操作
 */
object VMMediaRecorder : VMRecorderEngine() {
    private var mediaRecorder: MediaRecorder? = null // 媒体录影机，可以录制音频和视频

    private var samplingRate = 8000 // 音频采样率 单位 Hz
    private var encodingBitRate = 64 // 音频编码比特率

    private var decibelBase = 1 // 计算分贝基准值

    private var isRecording = false // 是否录制中
    private var filePath: String = "" // 录制文件保存路径

    /**
     * 初始化录制音频
     */
    override fun initVoiceRecorder() {
        // 实例化媒体录影机
        mediaRecorder = MediaRecorder()
        // 设置音频源为麦克风
        mediaRecorder?.setAudioSource(AudioSource.MIC)
        /**
         * 设置音频文件编码格式，这里设置默认
         * https://developer.android.com/reference/android/media/MediaRecorder.AudioEncoder.html
         */
        mediaRecorder?.setOutputFormat(OutputFormat.DEFAULT)
        /**
         * 设置音频文件输出格式
         * https://developer.android.com/reference/android/media/MediaRecorder.OutputFormat.html
         */
        mediaRecorder?.setAudioEncoder(AudioEncoder.AMR_NB)
        // 设置音频采样率
        mediaRecorder?.setAudioSamplingRate(samplingRate)
        // 设置音频编码比特率
        mediaRecorder?.setAudioEncodingBitRate(encodingBitRate)
        // 设置录音最大持续时间
        mediaRecorder?.setMaxDuration(VMRecorderManager.maxDuration)
    }

    /**
     * 开始录制声音文件
     *
     * @param path 录音文件保存地址，可以为空，默认保存到项目包名 files 下
     */
    override fun startRecord(path: String?): Int {
        // 判断录制系统是否空闲
        if (isRecording) {
            return VMRecorderManager.errorRecording
        }

        // 判断媒体录影机是否释放，没有则释放
        if (mediaRecorder != null) {
            mediaRecorder?.release()
            mediaRecorder = null
        }

        // 设置录制状态
        isRecording = true
        filePath = if (path.isNullOrEmpty()) {
            // 这里默认保存在 /sdcard/android/data/packagename/files/voice/下
            VMFile.filesPath("voice") + "VMVoice_" + VMDate.filenameDateTime() + ".amr"
        } else {
            path
        }

        // 释放之后重新初始化
        initVoiceRecorder()

        // 设置录制输出文件
        mediaRecorder?.setOutputFile(filePath)
        try {
            // 准备录制
            mediaRecorder?.prepare()
            // 开始录制
            mediaRecorder?.start()
        } catch (e: IOException) {
            releaseRecorder()
            VMLog.e("录音系统出现错误 ${e.message}")
            return VMRecorderManager.errorSystem
        }
        return VMRecorderManager.errorNone
    }

    /**
     * 停止录制
     */
    override fun stopRecord(): Int {
        // 停止录音，将录音状态设置为false
        isRecording = false
        // 释放媒体录影机
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
        // 释放媒体录影机
        releaseRecorder()
        // 取消录音，删除文件
        if (VMFile.isFileExists(filePath)) {
            VMFile.deleteFile(filePath)
        }
    }

    /**
     * 获取录制文件路径
     */
    override fun getRecordFile(): String {
        return filePath
    }

    /**
     * 获取声音分贝信息
     */
    override fun decibel(): Int {
        var decibel = 1
        if (mediaRecorder != null) {
            var ratio = 0
            try {
                // mediaRecorder?.maxAmplitude 每次只能调用一次
                ratio = mediaRecorder?.maxAmplitude ?: decibelBase / decibelBase
            } catch (e: IllegalStateException) {
                VMLog.e("decibel error ${e.message}")
            } catch (e: RuntimeException) {
                VMLog.e("decibel error ${e.message}")
            }
            if (ratio > 0) {
                // 根据麦克风采集到的声音振幅计算声音分贝大小
                decibel = (20 * Math.log10(ratio.toDouble())).toInt()
            }
        }
        if (decibel > 100) decibel = 100

        return decibel
    }

    /**
     * 释放录音机
     */
    override fun releaseRecorder(): Int {
        isRecording = false
//        filePath = ""

        if (mediaRecorder != null) {
            // 防止录音机 start 后马上调用 stop 出现异常
            mediaRecorder?.setOnErrorListener(null)
            try {
                // 停止录制
                mediaRecorder?.stop()
                mediaRecorder?.reset()
                mediaRecorder?.release()
                mediaRecorder = null
            } catch (e: IllegalStateException) {
                VMLog.e("录音系统出现错误 ${e.message}")
                return VMRecorderManager.errorSystem
            } catch (e: RuntimeException) {
                VMLog.e("录音系统出现错误 ${e.message}")
                return VMRecorderManager.errorSystem
            }
        }
        return VMRecorderManager.errorNone
    }
}