package com.vmloft.develop.library.tools.widget.record

import android.media.MediaRecorder
import android.media.MediaRecorder.AudioEncoder
import android.media.MediaRecorder.AudioSource
import android.media.MediaRecorder.OutputFormat
import com.vmloft.develop.library.tools.utils.VMDate
import com.vmloft.develop.library.tools.utils.VMFile
import com.vmloft.develop.library.tools.utils.logger.VMLog.e
import java.io.IOException

/**
 * Created by lzan13 on 2024/01/10.
 * 描述：定义的录音功能单例类，主要处理录音的相关操作
 */
object VMRecorder {
    const val errorNone = 0 // 没有错误
    const val errorSystem = 1 // 系统错误
    const val errorFailed = 2 // 录制失败
    const val errorRecording = 3 // 正在录制
    const val errorCancel = 4 // 录音取消
    const val errorShort = 5 // 录音时间过短

    private var mediaRecorder: MediaRecorder? = null // 媒体录影机，可以录制音频和视频

    private var recordSamplingRate = 8000 // 音频采样率 单位 Hz
    private var recordEncodingBitRate = 64 // 音频编码比特率
    private var recordMaxDuration = 60 * 60 * 1000 // 录音最大持续时间 60 分钟

    private var recordDecibelBase = 200 // 计算分贝基准值

    private var isRecording = false // 是否录制中
    private var recordFile: String = "" // 录制文件保存路径

    /**
     * 初始化录制音频
     */
    private fun initVoiceRecorder() {
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
        mediaRecorder?.setAudioSamplingRate(recordSamplingRate)
        // 设置音频编码比特率
        mediaRecorder?.setAudioEncodingBitRate(recordEncodingBitRate)
        // 设置录音最大持续时间
        mediaRecorder?.setMaxDuration(recordMaxDuration)
    }

    /**
     * 开始录制声音文件
     *
     * @param path 录音文件保存地址，可以为空，默认保存到项目包名 files 下
     */
    fun startRecord(path: String?): Int {
        // 判断录制系统是否空闲
        if (isRecording) {
            return errorRecording
        }

        // 判断媒体录影机是否释放，没有则释放
        if (mediaRecorder != null) {
            mediaRecorder?.release()
            mediaRecorder = null
        }

        // 设置录制状态
        isRecording = true
        recordFile = if (path.isNullOrEmpty()) {
            // 这里默认保存在 /sdcard/android/data/packagename/files/voice/下
            VMFile.filesPath("voice") + "VMVoice_" + VMDate.filenameDateTime() + ".amr"
        } else {
            path
        }

        // 释放之后重新初始化
        initVoiceRecorder()

        // 设置录制输出文件
        mediaRecorder?.setOutputFile(recordFile)
        try {
            // 准备录制
            mediaRecorder?.prepare()
            // 开始录制
            mediaRecorder?.start()
        } catch (e: IOException) {
            reset()
            e("录音系统出现错误 " + e.message)
            return errorSystem
        }
        return errorNone
    }

    /**
     * 停止录制
     */
    fun stopRecord(): Int {
        // 停止录音，将录音状态设置为false
        isRecording = false
        // 释放媒体录影机
        if (mediaRecorder != null) {
            // 防止录音机 start 后马上调用 stop 出现异常
            mediaRecorder?.setOnErrorListener(null)
            try {
                // 停止录制
                mediaRecorder?.stop()
            } catch (e: IllegalStateException) {
                e("录音系统出现错误 " + e.message)
                reset()
                return errorSystem
            } catch (e: RuntimeException) {
                e("录音系统出现错误 " + e.message)
                reset()
                return errorSystem
            }
        }
        // 根据录制结果判断录音是否成功
        if (!VMFile.isFileExists(recordFile)) {
            e("录音失败没有生成文件")
            return errorFailed
        }
        return errorNone
    }

    /**
     * 取消录音
     */
    fun cancelRecord() {
        // 停止录音，将录音状态设置为false
        isRecording = false
        // 释放媒体录影机
        if (mediaRecorder != null) {
            try {
                // 停止录制
                mediaRecorder?.stop()
            } catch (e: IllegalStateException) {
                e("录音系统出现错误 " + e.message)
                reset()
            } catch (e: RuntimeException) {
                e("录音系统出现错误 " + e.message)
                reset()
            }
        }
        // 取消录音，删除文件
        if (VMFile.isFileExists(recordFile)) {
            VMFile.deleteFile(recordFile)
        }
    } // 根据麦克风采集到的声音振幅计算声音分贝大小

    /**
     * 获取录制文件路径
     */
    fun getRecordFile(): String {
        return recordFile
    }

    /**
     * 获取声音分贝信息
     */
    fun decibel(): Int {
        var decibel = 1
        if (mediaRecorder != null) {
            var ratio = 0
            try {
                ratio = mediaRecorder?.maxAmplitude ?: recordDecibelBase / recordDecibelBase
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: RuntimeException) {
                e.printStackTrace()
            }
            if (ratio > 0) {
                // 根据麦克风采集到的声音振幅计算声音分贝大小
                decibel = (20 * Math.log10(ratio.toDouble()) / 5).toInt()
            }
        }
        return decibel
    }

    /**
     * 重置录音机
     */
    fun reset() {
        isRecording = false
        recordFile = ""
        if (mediaRecorder != null) {
            // 重置媒体录影机
            mediaRecorder?.reset()
            // 释放媒体录影机
            mediaRecorder?.release()
            mediaRecorder = null
        }
    }
}