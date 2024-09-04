package com.vmloft.develop.library.tools.voice.view

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

import com.vmloft.develop.library.tools.permission.VMPermission
import com.vmloft.develop.library.tools.utils.VMSystem
import com.vmloft.develop.library.tools.voice.bean.VMVoiceBean
import com.vmloft.develop.library.tools.voice.recorder.VMRecorderActionListener
import com.vmloft.develop.library.tools.voice.recorder.VMRecorderEngine
import com.vmloft.develop.library.tools.voice.recorder.VMRecorderManager

import java.util.*

/**
 * Created by lzan13 on 2024/01/10
 * 描述：自定义录音控件
 */
open class VMRecorderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {

    // 画笔
    lateinit var paint: Paint

    // 是否开始录制
    var isStart = false
    var isReadyCancel = false // 取消状态

    var isUsable = false // 是否可用

    var startTime: Long = 0 // 录制开始时间
    var durationTime: Int = 0 // 录制持续时间
    var voiceDecibel = 1 // 声音分贝

    var countDownTime: Int = 0 // 录制倒计时

    // 录音声音分贝集合
    var decibelList = mutableListOf<Int>()
    var decibelCount: Int = 0 // 声音分贝总数，用来计算抽样

    // 定时器
    var timer: Timer? = null

    // 录音控件回调接口
    private var recorderActionListener: VMRecorderActionListener? = null

    // 录音联动动画控件
    private var recorderWaveformView: VMRecorderWaveformView? = null

    // 录音引擎
    lateinit var recorderEngine: VMRecorderEngine

    /**
     * 启动录音时间记录
     */
    fun startRecordTimer() {
        // 开始录音，记录开始录制时间
        startTime = System.currentTimeMillis()
        timer = Timer()
        timer?.purge()
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                if (!isStart) return
                durationTime = (System.currentTimeMillis() - startTime).toInt()
                countDownTime = VMRecorderManager.maxDuration - durationTime

                voiceDecibel = recorderEngine.decibel()
                if (durationTime > VMRecorderManager.maxDuration) {
                    VMSystem.runInUIThread({ stopRecord(false) })
                }
                // 将声音分贝添加到集合，这里防止过大，进行抽样保存
                if (decibelCount % 2 == 1) {
                    decibelList.add(voiceDecibel)
                }
                onRecordDecibel(voiceDecibel)

                val fftData = recorderEngine.getFFTData()
                onRecordFFTData(fftData)

                val simple = decibelCount % (VMRecorderManager.touchAnimTime / VMRecorderManager.sampleTime).toInt()
                if (simple == 0) {
//                    startInnerAnim()
//                    startOuterAnim()
                }

                decibelCount++
                postInvalidate()
            }
        }
        timer?.scheduleAtFixedRate(task, 10, VMRecorderManager.sampleTime.toLong())
    }

    /**
     * 开始录音
     */
    open fun startRecord() {
        isStart = true
        // 调用录音机开始录制音频
        val code: Int = recorderEngine.startRecord(null)
        if (code == VMRecorderManager.errorNone) {
            startRecordTimer()
            onRecordStart()
        } else if (code == VMRecorderManager.errorRecording) {
            // TODO 正在录制中，不做处理
        } else if (code == VMRecorderManager.errorSystem) {
            isStart = false
            onRecordError(code, "录音系统错误")
            reset()
        }
        invalidate()
    }

    /**
     * 停止录音
     *
     * @param cancel 是否为取消
     */
    open fun stopRecord(cancel: Boolean = false) {
        stopTimer()
        if (cancel) {
            recorderEngine.cancelRecord()
            onRecordCancel()
        } else {
            val code: Int = recorderEngine.stopRecord()
            if (code == VMRecorderManager.errorFailed) {
                onRecordError(code, "录音失败")
            } else if (code == VMRecorderManager.errorSystem || durationTime < 1000) {
                if (durationTime < 1000) {
                    // 录制时间太短
                    onRecordError(VMRecorderManager.errorShort, "录音时间过短")
                } else {
                    onRecordError(VMRecorderManager.errorShort, "录音系统出现错误")
                }
            } else {
                onRecordComplete()
            }
        }
        reset()
        invalidate()
    }

    /**
     * 停止计时
     */
    private fun stopTimer() {
        timer?.purge()
        timer?.cancel()
        timer = null
    }

    /**
     * 重置控件
     */
    fun reset() {
        isStart = false
        isReadyCancel = false

        startTime = 0L
        durationTime = 0
        countDownTime = 0

        decibelList.clear()
    }


    // 录音开始
    open fun onRecordStart() {
        recorderWaveformView?.visibility = VISIBLE
        recorderActionListener?.onStart()
    }

    // 录音取消
    fun onRecordCancel() {
        recorderWaveformView?.visibility = GONE
        recorderActionListener?.onCancel()
    }

    // 录音完成
    fun onRecordComplete() {
        recorderWaveformView?.visibility = GONE
        val bean = VMVoiceBean(duration = durationTime, path = recorderEngine.getRecordFile())
        // 这里在录制完成后，需要清空 decibelList
        bean.decibelList.addAll(decibelList)

        recorderActionListener?.onComplete(bean)
    }

    // 录音分贝变化
    fun onRecordDecibel(decibel: Int) {
        recorderWaveformView?.updateDecibel(decibel)
        recorderActionListener?.onDecibel(voiceDecibel)
    }

    // 录音分贝变化
    fun onRecordFFTData(data: DoubleArray) {
        recorderWaveformView?.updateFFTData(data)
        recorderActionListener?.onRecordFFTData(data)
    }

    // 录音出现错误
    fun onRecordError(code: Int, desc: String) {
        recorderWaveformView?.visibility = GONE
        recorderActionListener?.onError(code, desc)
    }

    /**
     * 检查权限
     */
    fun checkPermission() {
        // 检查录音权限
        if (VMPermission.checkRecord()) {
            isUsable = true
        } else {
            isUsable = false
            VMPermission.requestRecord(context) {
                if (it) {
                    isUsable = true
                    invalidate()
                } else {
                    isUsable = false
                    invalidate()
                }
            }
        }
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == 0) {
            // View 可见 检查下权限
            isUsable = VMPermission.checkRecord()
        }
    }


    /**
     * 设置录音联动波形控件
     */
    fun setRecorderWaveformView(view: VMRecorderWaveformView?) {
        recorderWaveformView = view
    }

    /**
     * 设置录音回调
     */
    fun setRecorderActionListener(listener: VMRecorderActionListener?) {
        recorderActionListener = listener
    }

}
