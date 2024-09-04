package com.vmloft.develop.library.tools.voice.view.wechat

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.vmloft.develop.library.tools.R
import com.vmloft.develop.library.tools.permission.VMPermission
import com.vmloft.develop.library.tools.utils.VMDimen
import com.vmloft.develop.library.tools.utils.VMSystem
import com.vmloft.develop.library.tools.voice.bean.VMVoiceBean
import com.vmloft.develop.library.tools.voice.recorder.VMRecorderActionListener
import com.vmloft.develop.library.tools.voice.recorder.VMRecorderEngine
import com.vmloft.develop.library.tools.voice.recorder.VMRecorderManager

import java.util.Timer
import java.util.TimerTask

/**
 * Created by lzan13 on 2024/09/03
 * 描述：自定义仿微信录音控件
 */
class VMRecorderLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr) {

    // 默认宽高
    private var normalWidth = 0
    private var normalHeight = 0

    // 激活后宽高
    private var activateWidth = 0
    private var activateHeight = VMDimen.dp2px(220)

    // 实际显示宽高
    private var showWidth = 0
    private var showHeight = 0

    // 录音按钮的颜色、大小
    private var touchActivateBG = 0
    private var touchCancelBG = 0
    private var touchHeight = VMDimen.dp2px(128)

    // 控件取消相关颜色
    private var cancelNormalBG = 0
    private var cancelActivateBG = 0

    // 提示文本
    private var activateTips: String = "松开 发送"
    private var cancelTips: String = "松开 取消"

    // 倒计时文案
    private var countDownTips: String = "%d秒后停止"

    // 不可用描述文案
    private var unusableTips = "点击授予权限"

    private var tipsColor = 0x44407a
    private var tipsFontSize = VMDimen.sp2px(14)


    // 画笔
    var paint: Paint

    // 是否开始录制
    var isStart = false
    var isReadyCancel = false // 取消状态
    var isFirst = true // 首次触发

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

    private var rootCL: View
    private var cancelCL: View
    private var cancelActivateTV: TextView
    private var cancelActivateIV: ImageView
    private var cancelNormalIV: ImageView
    private var touchTV: TextView
    private var touchActivateIV: ImageView
    private var touchCancelIV: ImageView

    // 录音控件回调接口
    private var recorderActionListener: VMRecorderActionListener? = null

    // 录音联动动画控件
    private var recorderWaveformLayout: VMRecorderWaveformLayout? = null

    // 录音引擎
    private var recorderEngine: VMRecorderEngine

    /**
     * 初始化控件
     */
    init {
        // 获取控件属性
        handleAttrs(attrs)

        LayoutInflater.from(context).inflate(R.layout.vm_recorder_layout, this)

        recorderEngine = VMRecorderManager.getRecorderEngine()

        rootCL = findViewById(R.id.rootCL)
        cancelCL = findViewById(R.id.cancelCL)
        cancelActivateTV = findViewById(R.id.cancelActivateTV)
        cancelActivateIV = findViewById(R.id.cancelActivateIV)
        cancelNormalIV = findViewById(R.id.cancelNormalIV)
        touchTV = findViewById(R.id.touchTV)
        touchActivateIV = findViewById(R.id.touchActivateIV)
        touchCancelIV = findViewById(R.id.touchCancelIV)

        rootCL.layoutParams.height = activateHeight
        cancelActivateTV.text = cancelTips
        cancelActivateIV.setImageResource(cancelActivateBG)
        cancelNormalIV.setImageResource(cancelNormalBG)
        touchTV.text = activateTips
        touchActivateIV.setImageResource(touchActivateBG)
        touchCancelIV.setImageResource(touchCancelBG)

        // 画笔
        paint = Paint()
        // 设置抗锯齿
        paint.isAntiAlias = true
    }

    /**
     * 获取控件属性
     */
    private fun handleAttrs(attrs: AttributeSet?) {
        // 获取控件的属性值
        if (attrs == null) {
            return
        }
        val array = context.obtainStyledAttributes(attrs, R.styleable.VMRecorderLayout)

        normalHeight = array.getDimensionPixelOffset(R.styleable.VMRecorderLayout_vm_recorder_normal_height, normalHeight)
        activateHeight = array.getDimensionPixelOffset(R.styleable.VMRecorderLayout_vm_recorder_activate_height, activateHeight)

        touchActivateBG = array.getResourceId(R.styleable.VMRecorderLayout_vm_recorder_touch_activate_bg, touchActivateBG)
        touchCancelBG = array.getResourceId(R.styleable.VMRecorderLayout_vm_recorder_touch_cancel_bg, touchCancelBG)
        touchHeight = array.getDimensionPixelOffset(R.styleable.VMRecorderLayout_vm_recorder_touch_height, touchHeight)

        cancelNormalBG = array.getResourceId(R.styleable.VMRecorderLayout_vm_recorder_cancel_normal_bg, cancelNormalBG)
        cancelActivateBG = array.getResourceId(R.styleable.VMRecorderLayout_vm_recorder_cancel_activate_bg, cancelActivateBG)

        activateTips = array.getString(R.styleable.VMRecorderLayout_vm_recorder_tips_activate_text) ?: activateTips
        cancelTips = array.getString(R.styleable.VMRecorderLayout_vm_recorder_tips_cancel_text) ?: cancelTips
        countDownTips = array.getString(R.styleable.VMRecorderLayout_vm_recorder_tips_count_down_text) ?: countDownTips
        unusableTips = array.getString(R.styleable.VMRecorderLayout_vm_recorder_tips_unusable_text) ?: unusableTips

        tipsColor = array.getColor(R.styleable.VMRecorderLayout_vm_recorder_tips_color, tipsColor)
        tipsFontSize = array.getDimensionPixelOffset(R.styleable.VMRecorderLayout_vm_recorder_tips_font_size, tipsFontSize.toInt()).toFloat()

        array.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 获取测量到的高度
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec))
        var childWidthSize = measuredWidth
        var childHeightSize = measuredHeight

        // 计算真实高度，并设置回去
        if (showHeight == 0) {
            showHeight = normalHeight
        }
        var realWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY)
        var realHeightMeasureSpec = MeasureSpec.makeMeasureSpec(showHeight, MeasureSpec.EXACTLY)
        //设定宽高
        super.onMeasure(realWidthMeasureSpec, realHeightMeasureSpec)
    }

    /**
     * 重写父类的 onSizeChanged 方法，检测控件宽高的变化
     *
     * @param w    控件当前宽
     * @param h    控件当前高
     * @param oldw 控件原来的宽
     * @param oldh 控件原来的高
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        showWidth = w
        showHeight = h
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        val x = event.x
        val y = event.y
        when (action) {
            MotionEvent.ACTION_DOWN -> if (isUsable
                && x > 0
                && x < showWidth
                && y > 0
                && y < showHeight
            ) {
                startRecord()
            }

            MotionEvent.ACTION_MOVE -> if (isUsable && isStart) {
                val cancelYThreshold = showHeight - touchHeight
                val status = y < cancelYThreshold
                if (isReadyCancel != status) {
                    isReadyCancel = status
                    recorderWaveformLayout?.updateRecordStatus(isReadyCancel)
                    if (!isFirst) {
                        updateUIStatus()
                    }
                    invalidate()
                }
            }

            MotionEvent.ACTION_UP -> if (isUsable && isStart) {
                if (isReadyCancel) {
                    stopRecord(true)
                } else {
                    stopRecord(false)
                }
            } else {
                checkPermission()
            }
        }
        return true
    }

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
//                // 将声音分贝添加到集合，这里防止过大，进行抽样保存
//                if (decibelCount % 2 == 1) {
//                    decibelList.add(voiceDecibel)
//                }
//                onRecordDecibel(voiceDecibel)

                // 获取傅里叶变换后的频谱数据
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
    fun startRecord() {
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
    fun stopRecord(cancel: Boolean = false) {
        // 停止录音，恢复控件高度
        showHeight = normalHeight
        setMeasuredDimension(showWidth, showHeight)

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

    private fun updateUIStatus() {
        if (isStart) {
            if (isReadyCancel) {
                cancelNormalIV.visibility = View.INVISIBLE
                cancelActivateIV.visibility = View.VISIBLE
                cancelActivateTV.visibility = View.VISIBLE

                touchTV.visibility = View.INVISIBLE
                touchActivateIV.visibility = View.INVISIBLE
                touchCancelIV.visibility = View.VISIBLE
            } else {
                cancelNormalIV.visibility = View.VISIBLE
                cancelActivateIV.visibility = View.INVISIBLE
                cancelActivateTV.visibility = View.INVISIBLE

                touchTV.visibility = View.VISIBLE
                touchActivateIV.visibility = View.VISIBLE
                touchCancelIV.visibility = View.INVISIBLE
            }
        } else {
            cancelNormalIV.visibility = View.INVISIBLE
            cancelActivateIV.visibility = View.INVISIBLE
            cancelActivateTV.visibility = View.INVISIBLE

            touchTV.visibility = View.INVISIBLE
            touchActivateIV.visibility = View.INVISIBLE
            touchCancelIV.visibility = View.INVISIBLE
        }
    }

    /**
     * 重置控件
     */
    fun reset() {
        isFirst = true
        isStart = false
        isReadyCancel = false

        startTime = 0L
        durationTime = 0
        countDownTime = 0

        decibelList.clear()

        updateUIStatus()
    }


    // 录音开始
    fun onRecordStart() {
        recorderWaveformLayout?.visibility = VISIBLE
        recorderActionListener?.onStart()

        // 开始录音，调整控件激活高度
        showHeight = activateHeight
        setMeasuredDimension(showWidth, showHeight)

        postDelayed({
            updateUIStatus()
            isFirst = false
        }, 200)
    }

    // 录音取消
    fun onRecordCancel() {
        recorderWaveformLayout?.visibility = GONE
        recorderActionListener?.onCancel()
    }

    // 录音完成
    fun onRecordComplete() {
        recorderWaveformLayout?.visibility = GONE
        val bean = VMVoiceBean(duration = durationTime, path = recorderEngine.getRecordFile())
        // 这里在录制完成后，需要清空 decibelList
        bean.decibelList.addAll(decibelList)

        recorderActionListener?.onComplete(bean)
    }

    // 录音分贝变化
    fun onRecordDecibel(decibel: Int) {
//        recorderWaveformLayout?.updateDecibel(decibel)
//        recorderActionListener?.onDecibel(voiceDecibel)
    }

    // 录音分贝变化
    fun onRecordFFTData(data: DoubleArray) {
        recorderWaveformLayout?.updateFFTData(data)
        recorderActionListener?.onRecordFFTData(data)
    }

    // 录音出现错误
    fun onRecordError(code: Int, desc: String) {
        recorderWaveformLayout?.visibility = GONE
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
    fun setRecorderWaveformLayout(view: VMRecorderWaveformLayout?) {
        recorderWaveformLayout = view
    }

    /**
     * 设置录音回调
     */
    fun setRecorderActionListener(listener: VMRecorderActionListener?) {
        recorderActionListener = listener
    }

}
