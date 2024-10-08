package com.vmloft.develop.library.tools.voice.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.graphics.drawable.toBitmap

import com.vmloft.develop.library.tools.R
import com.vmloft.develop.library.tools.R.styleable
import com.vmloft.develop.library.tools.permission.VMPermission
import com.vmloft.develop.library.tools.utils.VMDimen
import com.vmloft.develop.library.tools.utils.VMStr
import com.vmloft.develop.library.tools.utils.VMSystem
import com.vmloft.develop.library.tools.voice.bean.VMVoiceBean
import com.vmloft.develop.library.tools.voice.recorder.VMRecorderEngine
import com.vmloft.develop.library.tools.voice.recorder.VMRecorderManager

import java.util.*

/**
 * Created by lzan13 on 2024/01/10
 * 描述：自定义录音控件
 */
class VMRecorderCircleView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {
    private var mWidth = 0
    private var mHeight = 0

    // 是否可用
    private var isUsable = false

    // 不可用描述文案
    private val mUnusableDesc = "点击授予权限"

    //是否开始录制
    private var isStart = false
    private var isReadyCancel = false

    // 画笔
    private var mPaint: Paint

    // 背景色
    private var mBGColor = 0xfafafa

    // 控件取消相关颜色
    private var mCancelColor = 0xf2f2f4
    private var mCancelColorActivate = 0xf0578e
    private var mCancelIcon = R.drawable.ic_close_gray
    private var mCancelIconActivate = R.drawable.ic_close_white
    private var mCancelSize = VMDimen.dp2px(48)
    private var mCancelMargin = VMDimen.dp2px(48)

    // 倒计时文案
    private var mCountDownDesc: String = "%d秒后停止"

    // 触摸区域提示文本
    private var mDescNormal: String = "按住 说话"
    private var mDescCancel: String = "松开 取消"
    private var mDescColor = 0x44407a
    private var mDescFontSize = VMDimen.dp2px(14)

    // 内圈颜色、大小
    private var mInnerSize = VMDimen.dp2px(128)
    private var mInnerColor = 0xffffff
    private var mInnerAnimSize = 0
    private var mInnerAnimAlpha = 100

    // 外圈的颜色、大小
    private var mOuterColor = 0xffffff
    private var mOuterSize = VMDimen.dp2px(220)
    private var mOuterAnimSize = 0
    private var mOuterAnimAlpha = 100

    // 录音按钮的颜色、大小
    private var mTouchSize = VMDimen.dp2px(60)
    private var mTouchColor = 0x6457f0
    private var mTouchColorCancel = 0xc7c6d4
    private var mTouchIcon = R.drawable.ic_voice_record_mic_white
    private var mTouchIconCancel = R.drawable.ic_voice_record_mic_black

    // 时间字体的大小、颜色
    private var mTimeColor = 0x44407a
    private var mTimeFontSize = VMDimen.dp2px(14)
    private var mTimeMargin = VMDimen.dp2px(48)

    private var startTime: Long = 0 // 录制开始时间
    private var durationTime: Int = 0 // 录制持续时间
    private var voiceDecibel = 1 // 声音分贝

    private var countDownTime: Int = 0 // 录制倒计时

    // 录音声音分贝集合
    private var decibelList = mutableListOf<Int>()
    private var decibelCount: Int = 0 // 声音分贝总数，用来计算抽样

    // 定时器
    private var timer: Timer? = null

    // 录音控件回调接口
    private var mRecordActionListener: RecordActionListener? = null

    // 录音联动动画控件
    private var mVoiceAnimView: VMRecorderWaveformView? = null

    private lateinit var recorderEngine: VMRecorderEngine

    /**
     * 初始化控件
     */
    init {
        // 获取控件属性
        handleAttrs(attrs)
        recorderEngine = VMRecorderManager.getRecorderEngine()
        // 画笔
        mPaint = Paint()
        // 设置抗锯齿
        mPaint.isAntiAlias = true
    }

    /**
     * 获取控件属性
     */
    private fun handleAttrs(attrs: AttributeSet?) {
        // 获取控件的属性值
        if (attrs == null) {
            return
        }
        val array = context.obtainStyledAttributes(attrs, styleable.VMVoiceView)

        mBGColor = array.getColor(styleable.VMVoiceView_vm_bg_color, mBGColor)

        mCancelColor = array.getColor(styleable.VMVoiceView_vm_cancel_color, mCancelColor)
        mCancelColorActivate = array.getColor(styleable.VMVoiceView_vm_cancel_color_activate, mCancelColorActivate)
        mCancelIcon = array.getResourceId(styleable.VMVoiceView_vm_cancel_icon, mCancelIcon)
        mCancelIconActivate = array.getResourceId(styleable.VMVoiceView_vm_cancel_icon_activate, mCancelIconActivate)
        mCancelSize = array.getDimensionPixelOffset(styleable.VMVoiceView_vm_cancel_size, mCancelSize)
        mCancelMargin = array.getDimensionPixelOffset(styleable.VMVoiceView_vm_cancel_margin, mCancelMargin)

        mCountDownDesc = array.getString(styleable.VMVoiceView_vm_count_down_desc) ?: mCountDownDesc
        mDescNormal = array.getString(styleable.VMVoiceView_vm_desc_normal) ?: mDescCancel
        mDescCancel = array.getString(styleable.VMVoiceView_vm_desc_cancel) ?: mDescCancel

        mDescColor = array.getColor(styleable.VMVoiceView_vm_desc_color, mDescColor)
        mDescFontSize = array.getDimensionPixelOffset(styleable.VMVoiceView_vm_desc_font_size, mDescFontSize)

        mInnerColor = array.getColor(styleable.VMVoiceView_vm_inner_color, mInnerColor)
        mInnerSize = array.getDimensionPixelOffset(styleable.VMVoiceView_vm_inner_size, mInnerSize)

        mOuterColor = array.getColor(styleable.VMVoiceView_vm_outer_color, mOuterColor)
        mOuterSize = array.getDimensionPixelOffset(styleable.VMVoiceView_vm_outer_size, mOuterSize)

        mTouchColor = array.getColor(styleable.VMVoiceView_vm_touch_color, mTouchColor)
        mTouchColorCancel = array.getColor(styleable.VMVoiceView_vm_touch_color_cancel, mTouchColorCancel)
        mTouchSize = array.getDimensionPixelOffset(styleable.VMVoiceView_vm_touch_size, mTouchSize)

        mTouchIcon = array.getResourceId(styleable.VMVoiceView_vm_touch_icon, mCancelIcon)
        mTouchIconCancel = array.getResourceId(styleable.VMVoiceView_vm_touch_icon_cancel, mCancelIconActivate)

        mTimeColor = array.getColor(styleable.VMVoiceView_vm_time_color, mTimeColor)
        mTimeFontSize = array.getDimensionPixelOffset(styleable.VMVoiceView_vm_time_font_size, mTimeFontSize)
        mTimeMargin = array.getDimensionPixelOffset(styleable.VMVoiceView_vm_time_margin, mTimeMargin)

        array.recycle()
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
        mWidth = w
        mHeight = h
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 绘制背景
        drawBackground(canvas)

        if (!isUsable) {
            drawUnusable(canvas)
            return
        } else {
            // 绘制录制按钮动画
            drawRecordAnim(canvas)
            // 绘制录制按钮
            drawRecordBtn(canvas)
            // 绘制取消按钮
            drawCancelBtn(canvas)
            // 绘制文本
            drawDesc(canvas)
            // 绘制时间
            drawTime(canvas)
        }
    }

    /**
     * 绘制背景
     */
    private fun drawBackground(canvas: Canvas) {
        if (isReadyCancel) {
            mPaint.color = mBGColor
        } else {
            mPaint.color = mBGColor
        }
        // 绘制背景
        canvas.drawRect(0f, 0f, mWidth.toFloat(), mHeight.toFloat(), mPaint)
    }

    /**
     * 绘制不可用时的 UI
     */
    private fun drawUnusable(canvas: Canvas) {
        drawRecordAnim(canvas)
        drawRecordBtn(canvas)
        // 绘制提示文本
        mPaint.color = mDescColor
        mPaint.textSize = mDescFontSize.toFloat()
        val tWidth = VMDimen.getTextWidth(mPaint, mUnusableDesc)
        val tHeight = VMDimen.getTextHeight(mPaint, mUnusableDesc)
        val centerY = mHeight / 2 - mTouchSize / 2 - tHeight * 2
        canvas.drawText(mUnusableDesc, mWidth / 2 - tWidth / 2, centerY, mPaint)
    }

    /**
     * 绘制录制按钮动画
     */
    private fun drawRecordAnim(canvas: Canvas) {
        if (!isStart || isReadyCancel) return
        // 绘制外圈
        mPaint.color = mOuterColor
        // 设置透明度，这里透明度要放在设置颜色之后
        mPaint.alpha = mOuterAnimAlpha
        canvas.drawCircle(mWidth / 2.0f, mHeight / 2.0f, mOuterAnimSize / 2.0f, mPaint)

        // 绘制内圈
        mPaint.color = mInnerColor
        // 设置透明度，这里透明度要放在设置颜色之后
        mPaint.alpha = mInnerAnimAlpha
        canvas.drawCircle(mWidth / 2.0f, mHeight / 2.0f, mInnerAnimSize / 2.0f, mPaint)
    }

    /**
     * 绘制录制按钮
     */
    private fun drawRecordBtn(canvas: Canvas) {
        if (!isStart && !isReadyCancel) {
            // 绘制内外圈
            mPaint.color = mOuterColor
            canvas.drawCircle(mWidth / 2.0f, mHeight / 2.0f, mOuterSize / 2.0f, mPaint)

            mPaint.color = mInnerColor
            canvas.drawCircle(mWidth / 2.0f, mHeight / 2.0f, mInnerSize / 2.0f, mPaint)
        }

        // 绘制触摸区域
        mPaint.color = if (isReadyCancel) mTouchColorCancel else mTouchColor
        canvas.drawCircle(mWidth / 2.0f, mHeight / 2.0f, mTouchSize / 2.0f, mPaint)

        // 绘制麦克风图标
        val bitmap = context.resources.getDrawable(if (isReadyCancel) mTouchIconCancel else mTouchIcon).toBitmap()
        canvas.drawBitmap(bitmap, mWidth / 2.0f - bitmap.width / 2, mHeight / 2.0f - bitmap.height / 2, mPaint)
    }

    /**
     * 绘制录制按钮
     */
    private fun drawCancelBtn(canvas: Canvas) {
        // 开始后再绘制取消按钮
        if (!isStart) return

        // 绘制取消按钮背景
        mPaint.color = if (isReadyCancel) mCancelColorActivate else mCancelColor
        // 取消激活时放大一些
//        val size = if (isReadyCancel) mCancelSize * 5 / 4 else mCancelSize
        val size = mCancelSize
        val cancelX = mWidth - mCancelMargin - mCancelSize / 2.0f
        val cancelY = mHeight / 2.0f
        canvas.drawCircle(cancelX, cancelY, size / 2.0f, mPaint)

        // 绘制取消图标
        val bitmap = context.resources.getDrawable(if (isReadyCancel) mCancelIconActivate else mCancelIcon).toBitmap()
        val cancelIconX = mWidth - mCancelMargin - mCancelSize / 2 - bitmap.width / 2.0f
        val cancelIconY = mHeight / 2 - bitmap.height / 2.0f
        canvas.drawBitmap(bitmap, cancelIconX, cancelIconY, mPaint)
    }

    /**
     * 绘制文本
     */
    private fun drawDesc(canvas: Canvas) {
        // 倒计时，倒计时优先级最高
        if (countDownTime < 5 * 1000 && countDownTime > 0) {
            mPaint.color = mCancelColorActivate

            val desc = VMStr.byArgs(mCountDownDesc, (countDownTime / 1000) + 1)

            mPaint.textSize = mDescFontSize.toFloat()
            val tWidth = VMDimen.getTextWidth(mPaint, desc)
            val tHeight = VMDimen.getTextHeight(mPaint, desc)
            val centerY = mHeight / 2 - mTouchSize / 2 - tHeight * 2
            canvas.drawText(desc, mWidth / 2 - tWidth / 2, centerY, mPaint)
            return
        }

        if (isStart && !isReadyCancel) return

        val descColor: Int
        var desc: String
        if (isReadyCancel) {
            descColor = mCancelColorActivate
            desc = mDescCancel
        } else {
            descColor = mDescColor
            desc = mDescNormal
        }
        // 绘制提示文本
        mPaint.color = descColor
        mPaint.textSize = mDescFontSize.toFloat()
        val tWidth = VMDimen.getTextWidth(mPaint, desc)
        val tHeight = VMDimen.getTextHeight(mPaint, desc)
        val centerY = mHeight / 2 - mTouchSize / 2 - tHeight * 2
        canvas.drawText(desc, mWidth / 2 - tWidth / 2, centerY, mPaint)
    }

    /**
     * 绘制录音时间
     */
    private fun drawTime(canvas: Canvas) {
        // 取消状态不绘制时间
        if (!isStart || isReadyCancel) return
        //
        mPaint.color = mTimeColor
        mPaint.strokeWidth = 1f
        mPaint.textSize = mTimeFontSize.toFloat()
        val minute = durationTime / 1000 / 60
        val seconds = durationTime / 1000 % 60
//        val millisecond = recordTime % 1000 / 100

        val time = VMStr.byArgs("%02d:%02d", minute, seconds)
        val tWidth = VMDimen.getTextWidth(mPaint, time)
        val tHeight = VMDimen.getTextHeight(mPaint, time)
        val centerX = mTimeMargin + tWidth / 2
        canvas.drawText(time, centerX, mHeight / 2 + tHeight / 2, mPaint)
    }

    /**
     * 内圈动画
     */
    private fun startInnerAnim() {
        VMSystem.runInUIThread({
            val mAnimator = ValueAnimator.ofInt(mTouchSize, mInnerSize * 2)
            mAnimator.duration = VMRecorderManager.touchAnimTime
            mAnimator.repeatCount = 0
            mAnimator.interpolator = LinearInterpolator()
            mAnimator.addUpdateListener { a: ValueAnimator ->
                if (isStart) {
                    // 动画大小根据回调变化
                    mInnerAnimSize = a.animatedValue as Int
                    mInnerAnimAlpha = (mInnerSize * 2 - mInnerAnimSize) * 100 / (mInnerSize * 2 - mTouchSize)

                    invalidate()
                }
            }
            mAnimator.start()
        })
    }

    /**
     * 外圈动画
     */
    private fun startOuterAnim() {
        VMSystem.runInUIThread({
            val mAnimator = ValueAnimator.ofInt(mInnerSize, mOuterSize * 2)
            mAnimator.duration = VMRecorderManager.touchAnimTime
            mAnimator.repeatCount = 0
            mAnimator.interpolator = LinearInterpolator()
            mAnimator.addUpdateListener { a: ValueAnimator ->
                if (isStart) {
                    // 动画大小根据回调变化
                    mOuterAnimSize = a.animatedValue as Int
                    mOuterAnimAlpha = (mOuterSize * 2 - mOuterAnimSize) * 100 / (mOuterSize * 2 - mInnerSize)

                    invalidate()
                }
            }
            mAnimator.start()
        })
    }

    /**
     * 启动录音时间记录
     */
    private fun startRecordTimer() {
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
                    startInnerAnim()
                    startOuterAnim()
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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        val x = event.x
        val y = event.y
        when (action) {
            MotionEvent.ACTION_DOWN -> if (isUsable
                && x > mWidth / 2 - mTouchSize / 2
                && x < mWidth / 2 + mTouchSize / 2
                && y > mHeight / 2 - mTouchSize / 2
                && y < mHeight / 2 + mTouchSize / 2
            ) {
                startRecord()
            }

            MotionEvent.ACTION_MOVE -> if (isUsable && isStart) {
                val cancelX = mWidth - VMDimen.dp2px(36) - mCancelSize / 2.0f
                val cancelY = mHeight / 2.0f
                val cancelLeft = cancelX - mCancelSize / 2
                val cancelRight = cancelX + mCancelSize / 2
                val cancelTop = cancelY - mCancelSize / 2
                val cancelBottom = cancelY + mCancelSize / 2
                isReadyCancel = x > cancelLeft && x < cancelRight && y > cancelTop && y < cancelBottom
                invalidate()
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
    private fun reset() {
        isStart = false
        isReadyCancel = false

        mOuterAnimSize = 0
        mOuterAnimAlpha = 100

        mInnerAnimSize = 0
        mInnerAnimAlpha = 100

        startTime = 0L
        durationTime = 0
        countDownTime = 0

        decibelList.clear()
    }


    // 录音开始
    private fun onRecordStart() {
        mVoiceAnimView?.visibility = VISIBLE
        mRecordActionListener?.onStart()
    }

    // 录音取消
    private fun onRecordCancel() {
        mVoiceAnimView?.visibility = GONE
        mRecordActionListener?.onCancel()
    }

    // 录音完成
    private fun onRecordComplete() {
        mVoiceAnimView?.visibility = GONE
        val bean = VMVoiceBean(duration = durationTime, path = recorderEngine.getRecordFile())
        // 这里在录制完成后，需要清空 decibelList
        bean.decibelList.addAll(decibelList)

        mRecordActionListener?.onComplete(bean)
    }

    // 录音分贝变化
    private fun onRecordDecibel(decibel: Int) {
        mVoiceAnimView?.updateDecibel(decibel)
        mRecordActionListener?.onDecibel(voiceDecibel)
    }

    // 录音分贝变化
    private fun onRecordFFTData(data: DoubleArray) {
        mVoiceAnimView?.updateFFTData(data)
        mRecordActionListener?.onRecordFFTData(data)
    }

    // 录音出现错误
    private fun onRecordError(code: Int, desc: String) {
        mVoiceAnimView?.visibility = GONE
        mRecordActionListener?.onError(code, desc)
    }

    /**
     * 检查权限
     */
    private fun checkPermission() {
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
     * 设置录音联动动画控件
     */
    fun setRecordAnimView(animView: VMRecorderWaveformView?) {
        mVoiceAnimView = animView
    }

    /**
     * 设置录音回调
     */
    fun setRecordActionListener(listener: RecordActionListener?) {
        mRecordActionListener = listener
    }

    /**
     * 录音控件的回调接口，用于回调给调用者录音结果
     */
    abstract class RecordActionListener {
        /**
         * 录音开始，默认空实现，有需要可重写
         */
        open fun onStart() {}

        /**
         * 录音取消，默认空实现，有需要可重写
         */
        open fun onCancel() {}

        /**
         * 录音成功
         * @param bean 录音数据 bean
         */
        abstract fun onComplete(bean: VMVoiceBean)

        /**
         * 录音分贝
         */
        open fun onDecibel(decibel: Int) {}

        /**
         * 录音分贝
         */
        open fun onRecordFFTData(data: DoubleArray) {}

        /**
         * 录音错误
         *
         * @param code 错误码
         * @param desc 错误描述
         */
        abstract fun onError(code: Int, desc: String)

    }

}
