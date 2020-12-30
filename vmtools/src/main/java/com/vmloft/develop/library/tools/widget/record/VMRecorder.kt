package com.vmloft.develop.library.tools.widget.record

import android.media.MediaRecorder
import android.media.MediaRecorder.AudioEncoder
import android.media.MediaRecorder.AudioSource
import android.media.MediaRecorder.OutputFormat
import com.vmloft.develop.library.tools.utils.VMDate
import com.vmloft.develop.library.tools.utils.VMFile
import com.vmloft.develop.library.tools.utils.VMStr
import com.vmloft.develop.library.tools.utils.logger.VMLog.e
import java.io.IOException

/**
 * Created by lz on 2016/8/20.
 * 定义的录音功能单例类，主要处理录音的相关操作
 */
class VMRecorder
/**
 * 单例类的私有构造方法
 */
private constructor() {
    // 媒体录影机，可以录制音频和视频
    private var mMediaRecorder: MediaRecorder? = null

    // 音频采样率 单位 Hz
    protected var mSamplingRate = 8000

    // 音频编码比特率
    protected var mEncodingBitRate = 64

    // 录音最大持续时间 10 分钟
    protected var mMaxDuration = 10 * 60 * 1000

    // 计算分贝基准值
    protected var mDecibelBase = 200

    /**
     * 判断录音机是否正在录制中
     */
    // 是否录制中
    var isRecording = false

    /**
     * 获取录制的语音文件
     */
    // 录制文件保存路径
    var recordFile: String? = null

    /**
     * 内部类实现单例模式
     */
    object InnerHolder {
        var INSTANCE = VMRecorder()
    }

    /**
     * 初始化录制音频
     */
    private fun initVoiceRecorder() {
        // 实例化媒体录影机
        mMediaRecorder = MediaRecorder()
        // 设置音频源为麦克风
        mMediaRecorder!!.setAudioSource(AudioSource.MIC)
        /**
         * 设置音频文件编码格式，这里设置默认
         * https://developer.android.com/reference/android/media/MediaRecorder.AudioEncoder.html
         */
        mMediaRecorder!!.setOutputFormat(OutputFormat.DEFAULT)
        /**
         * 设置音频文件输出格式
         * https://developer.android.com/reference/android/media/MediaRecorder.OutputFormat.html
         */
        mMediaRecorder!!.setAudioEncoder(AudioEncoder.AMR_NB)
        // 设置音频采样率
        mMediaRecorder!!.setAudioSamplingRate(mSamplingRate)
        // 设置音频编码比特率
        mMediaRecorder!!.setAudioEncodingBitRate(mEncodingBitRate)
        // 设置录音最大持续时间
        mMediaRecorder!!.setMaxDuration(mMaxDuration)
    }

    /**
     * 开始录制声音文件
     *
     * @param path 录音文件保存地址，可以为空，默认保存到项目包名 files 下
     */
    fun startRecord(path: String?): Int {
        // 判断录制系统是否空闲
        if (isRecording) {
            return ERROR_RECORDING
        }

        // 判断媒体录影机是否释放，没有则释放
        if (mMediaRecorder != null) {
            mMediaRecorder!!.release()
            mMediaRecorder = null
        }

        // 设置录制状态
        isRecording = true
        if (VMStr.isEmpty(path)) {
            // 这里默认保存在 /sdcard/android/data/packagename/files/下
            //File file = VMFile.createFile(VMFile.getFilesFromSDCard(), "VMVoice_", ".amr");
            recordFile = VMFile.filesFromSDCard("voice") + "VMVoice_" + VMDate.filenameDateTime() + ".amr"
        } else {
            //File file = VMFile.createFile(path, "VMVoice_", ".amr");
            recordFile = path
        }

        // 释放之后重新初始化
        initVoiceRecorder()

        // 设置录制输出文件
        mMediaRecorder!!.setOutputFile(recordFile)
        try {
            // 准备录制
            mMediaRecorder!!.prepare()
            // 开始录制
            mMediaRecorder!!.start()
        } catch (e: IOException) {
            reset()
            e("录音系统出现错误 " + e.message)
            return ERROR_SYSTEM
        }
        return ERROR_NONE
    }

    /**
     * 停止录制
     */
    fun stopRecord(): Int {
        // 停止录音，将录音状态设置为false
        isRecording = false
        // 释放媒体录影机
        if (mMediaRecorder != null) {
            // 防止录音机 start 后马上调用 stop 出现异常
            mMediaRecorder!!.setOnErrorListener(null)
            try {
                // 停止录制
                mMediaRecorder!!.stop()
            } catch (e: IllegalStateException) {
                e("录音系统出现错误 " + e.message)
                reset()
                return ERROR_SYSTEM
            } catch (e: RuntimeException) {
                e("录音系统出现错误 " + e.message)
                reset()
                return ERROR_SYSTEM
            }
        }
        // 根据录制结果判断录音是否成功
        if (!VMFile.isFileExists(recordFile)) {
            e("录音失败没有生成文件")
            return ERROR_FAILED
        }
        return ERROR_NONE
    }

    /**
     * 取消录音
     */
    fun cancelRecord() {
        // 停止录音，将录音状态设置为false
        isRecording = false
        // 释放媒体录影机
        if (mMediaRecorder != null) {
            try {
                // 停止录制
                mMediaRecorder!!.stop()
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
     * 获取声音分贝信息
     */
    val decibel: Int
        get() {
            var decibel = 1
            if (mMediaRecorder != null) {
                var ratio = 0
                try {
                    ratio = mMediaRecorder!!.maxAmplitude / mDecibelBase
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                } catch (e: RuntimeException) {
                    e.printStackTrace()
                }
                if (ratio > 0) {
                    // 根据麦克风采集到的声音振幅计算声音分贝大小
                    decibel = (20 * Math.log10(ratio.toDouble())).toInt() / 10
                }
            }
            return decibel
        }

    /**
     * 重置录音机
     */
    fun reset() {
        isRecording = false
        recordFile = null
        if (mMediaRecorder != null) {
            // 重置媒体录影机
            mMediaRecorder!!.reset()
            // 释放媒体录影机
            mMediaRecorder!!.release()
            mMediaRecorder = null
        }
    }

    companion object {
        const val ERROR_NONE = 0 // 没有错误
        const val ERROR_SYSTEM = 1 // 系统错误
        const val ERROR_FAILED = 2 // 录制失败
        const val ERROR_RECORDING = 3 // 正在录制
        const val ERROR_CANCEL = 4 // 录音取消
        const val ERROR_SHORT = 5 // 录音时间过短

        /**
         * 获取单例类的实例
         */
        val instance: VMRecorder
            get() = InnerHolder.INSTANCE
    }
}