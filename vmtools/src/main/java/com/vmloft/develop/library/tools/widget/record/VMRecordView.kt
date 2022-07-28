package com.vmloft.develop.library.tools.widget.record

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator

import com.vmloft.develop.library.tools.R.color
import com.vmloft.develop.library.tools.R.styleable
import com.vmloft.develop.library.tools.permission.VMPermission
import com.vmloft.develop.library.tools.utils.VMColor
import com.vmloft.develop.library.tools.utils.VMDimen
import com.vmloft.develop.library.tools.utils.VMStr
import com.vmloft.develop.library.tools.utils.VMSystem

import java.util.*

/**
 * Created by lzan13 on 2019/06/06 14:30
 *
 * 自定义录音控件
 */
class VMRecordView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var mWidth = 0
    private var mHeight = 0

    protected var isUsable = false // 是否可用
    private val mUnusableDesc = "授予权限" // 不可用描述

    // 画笔
    private var mPaint: Paint

    // 控件背景颜色
    private var mCancelColor = VMColor.byRes(color.vm_red_87)

    // 外圈的颜色、大小
    private var mOuterColor = VMColor.byRes(color.vm_green_38)
    private var mOuterSize = VMDimen.dp2px(96)

    // 内圈录音按钮的颜色、大小
    private var mInnerColor = VMColor.byRes(color.vm_green)
    private var mInnerSize = VMDimen.dp2px(96)

    // 触摸区域提示文本
    private var mDescNormal: String = "触摸录音"
    private var mDescCancel: String = "松开取消"
    private var mDescColor = VMColor.byRes(color.vm_white)
    private var mDescSize = VMDimen.dp2px(14)

    // 时间字体的大小、颜色
    private var mTimeColor = VMColor.byRes(color.vm_green)
    private var mTimeSize = VMDimen.dp2px(14)

    //是否开始录制
    private var isStartRecord = false
    private var isCancelRecord = false

    protected var recordStartTime: Long = 0 // 录制开始时间
    protected var recordTime: Int = 0 // 录制持续时间

    protected var recordDecibel = 1 // 分贝

    protected var sampleTime: Long = 500 // 分贝取样时间 毫秒值

    /**
     * 初始化控件
     */
    init {
        // 获取控件属性
        handleAttrs(attrs)

        // 获得绘制文本的宽和高
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
        val array = context.obtainStyledAttributes(attrs, styleable.VMRecordView)
        mCancelColor = array.getColor(styleable.VMRecordView_vm_cancel_color, mCancelColor)
        mOuterColor = array.getColor(styleable.VMRecordView_vm_outer_color, mOuterColor)
        mInnerColor = array.getColor(styleable.VMRecordView_vm_inner_color, mInnerColor)
        mInnerSize = array.getDimensionPixelOffset(styleable.VMRecordView_vm_inner_size, mInnerSize)
        mDescNormal = array.getString(styleable.VMRecordView_vm_touch_normal_desc) ?: mDescCancel
        mDescCancel = array.getString(styleable.VMRecordView_vm_touch_cancel_desc) ?: mDescCancel
        if (mDescNormal.isNullOrEmpty()) {
            mDescNormal = "触摸录音"
        }
        if (mDescCancel.isNullOrEmpty()) {
            mDescCancel = "松开取消"
        }
        mDescColor = array.getColor(styleable.VMRecordView_vm_desc_color, mDescColor)
        mDescSize = array.getDimensionPixelOffset(styleable.VMRecordView_vm_desc_size, mDescSize)
        mTimeColor = array.getColor(styleable.VMRecordView_vm_time_color, mTimeColor)
        mTimeSize = array.getDimensionPixelOffset(styleable.VMRecordView_vm_time_size, mTimeSize)
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
        drawBackground(canvas)
        if (!isUsable) {
            drawUnusable(canvas)
            return
        }

        // 绘制触摸区域
        drawTouch(canvas)

        // 绘制时间
        drawTime(canvas)
    }

    /**
     * 绘制背景
     */
    private fun drawBackground(canvas: Canvas) {
        if (isCancelRecord) {
            mPaint.color = mCancelColor
        } else {
            mPaint.color = VMColor.byRes(color.vm_transparent)
        }
        // 绘制背景
        canvas.drawRect(0f, 0f, mWidth.toFloat(), mHeight.toFloat(), mPaint)
    }

    /**
     * 绘制不可用时的 UI
     */
    private fun drawUnusable(canvas: Canvas) {
        // 绘制触摸区域
        mPaint.color = mInnerColor
        canvas.drawCircle(mWidth / 2.toFloat(), mHeight / 2.toFloat(), mInnerSize / 2.toFloat(), mPaint)

        // 绘制提示文本
        mPaint.color = mDescColor
        mPaint.textSize = mDescSize.toFloat()
        val tWidth = VMDimen.getTextWidth(mPaint, mUnusableDesc)
        val tHeight = VMDimen.getTextHeight(mPaint, mUnusableDesc)
        canvas.drawText(mUnusableDesc, mWidth / 2 - tWidth / 2, mHeight / 2 + tHeight / 3, mPaint)
    }

    /**
     * 绘制触摸区域
     */
    private fun drawTouch(canvas: Canvas) {
        // 绘制外圈
        if (isStartRecord && !isCancelRecord) {
            mPaint.color = mOuterColor
            canvas.drawCircle(mWidth / 2.toFloat(), mHeight / 2.toFloat(), mOuterSize / 2.toFloat(), mPaint)
        }
        val innerColor: Int
        val descColor: Int
        var desc: String
        if (isCancelRecord) {
            innerColor = VMColor.byRes(color.vm_white)
            descColor = mCancelColor
            desc = mDescCancel
        } else {
            innerColor = mInnerColor
            descColor = mDescColor
            desc = mDescNormal
        }
        // 绘制触摸区域
        mPaint.color = innerColor
        canvas.drawCircle(mWidth / 2.toFloat(), mHeight / 2.toFloat(), mInnerSize / 2.toFloat(), mPaint)

        // 绘制提示文本
        mPaint.color = descColor
        mPaint.textSize = mDescSize.toFloat()
        val tWidth = VMDimen.getTextWidth(mPaint, desc)
        val tHeight = VMDimen.getTextHeight(mPaint, desc)
        canvas.drawText(desc, mWidth / 2 - tWidth / 2, mHeight / 2 + tHeight / 3, mPaint)
    }

    /**
     * 绘制录音时间
     */
    private fun drawTime(canvas: Canvas) {
        mPaint.color = if (isCancelRecord) VMColor.byRes(color.vm_white) else mTimeColor
        mPaint.strokeWidth = 1f
        mPaint.textSize = mTimeSize.toFloat()
        val minute = recordTime / 1000 / 60
        val seconds = recordTime / 1000 % 60
//        val millisecond = recordTime % 1000 / 100
        val time = VMStr.byArgs("%02d:%02d", minute, seconds)
        val tWidth = VMDimen.getTextWidth(mPaint, time)
        val tHeight = VMDimen.getTextHeight(mPaint, time)
        canvas.drawText(time, mWidth / 2 - tWidth / 2, mHeight / 6f, mPaint)
    }

    /**
     * 外圈动画
     */
    private fun startOuterAnim() {
        VMSystem.runInUIThread({
            val mAnimator = ValueAnimator.ofInt(mInnerSize, (mInnerSize + recordDecibel * 1.1f * mHeight / 10).toInt(), mInnerSize)
            mAnimator.duration = sampleTime
            mAnimator.repeatCount = 0
            mAnimator.interpolator = LinearInterpolator()
            mAnimator.addUpdateListener { a: ValueAnimator ->
                mOuterSize = a.animatedValue as Int
                invalidate()
            }
            mAnimator.start()
        })
    }

    var timer: Timer? = null

    /**
     * 启动录音时间记录
     */
    private fun startRecordTimer() {
        // 开始录音，记录开始录制时间
        recordStartTime = System.currentTimeMillis()
        timer = Timer()
        timer?.purge()
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                recordTime = (System.currentTimeMillis() - recordStartTime).toInt()
                recordDecibel = VMRecorder.decibel()
                startOuterAnim()
                postInvalidate()
            }
        }
        timer?.scheduleAtFixedRate(task, 500, 500)
    }

    /**
     * 开始录音
     */
    fun startRecord() {
        isStartRecord = true
        // 调用录音机开始录制音频
        val code: Int = VMRecorder.startRecord(null)
        if (code == VMRecorder.errorNone) {
            startRecordTimer()
            recordStart()
        } else if (code == VMRecorder.errorRecording) {
            // TODO 正在录制中，不做处理
        } else if (code == VMRecorder.errorSystem) {
            isStartRecord = false
            recordError(code, "录音系统错误")
            reset()
        }
        postInvalidate()
    }

    /**
     * 停止录音
     *
     * @param cancel 是否为取消
     */
    fun stopRecord(cancel: Boolean) {
        stopTimer()
        if (cancel) {
            VMRecorder.cancelRecord()
            recordCancel()
        } else {
            val code: Int = VMRecorder.stopRecord()
            if (code == VMRecorder.errorFailed) {
                recordError(code, "录音失败")
            } else if (code == VMRecorder.errorSystem || recordTime < 1000) {
                if (recordTime < 1000) {
                    // 录制时间太短
                    recordError(VMRecorder.errorShort, "录音时间过短")
                } else {
                    recordError(VMRecorder.errorShort, "录音系统出现错误")
                }
            } else {
                recordComplete()
            }
        }
        reset()
        postInvalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        val x = event.x
        val y = event.y
        when (action) {
            MotionEvent.ACTION_DOWN -> if (isUsable && x > mWidth / 2 - mInnerSize / 2 && x < mWidth / 2 + mInnerSize / 2 && y > mHeight / 2 - mInnerSize / 2 && y < mHeight / 2 + mInnerSize / 2) {
                startRecord()
            }
            MotionEvent.ACTION_MOVE -> if (isUsable && isStartRecord) {
                isCancelRecord = x < mWidth / 2 - mInnerSize / 2 || x > mWidth / 2 + mInnerSize / 2 || y < mHeight / 2 - mInnerSize / 2 || y > mHeight / 2 + mInnerSize / 2
                postInvalidate()
            }
            MotionEvent.ACTION_UP -> if (isUsable && isStartRecord) {
                if (isCancelRecord) {
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
     * 录音开始
     */
    private fun recordStart() {
        mRecordListener?.onStart()
    }

    /**
     * 录音取消
     */
    private fun recordCancel() {
        mRecordListener?.onCancel()
    }

    /**
     * 录音出现错误
     *
     * @param code
     * @param desc
     */
    private fun recordError(code: Int, desc: String) {
        mRecordListener?.onError(code, desc)
    }

    /**
     * 录音完成
     */
    private fun recordComplete() {
        mRecordListener?.onComplete(VMRecorder.getRecordFile(), recordTime)
    }

    /**
     * 停止计时
     */
    fun stopTimer() {
        timer?.purge()
        timer?.cancel()
        timer = null
    }

    /**
     * 重置控件
     */
    private fun reset() {
        isStartRecord = false
        isCancelRecord = false
        recordStartTime = 0L
        recordTime = 0
        VMRecorder.reset()
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
                    postInvalidate()
                } else {
                    isUsable = false
                    postInvalidate()
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
     * ---------------------------------- 定义录音回调 ----------------------------------
     */
    // 录音控件回调接口
    protected var mRecordListener: RecordListener? = null

    /**
     * 设置录音回调
     */
    fun setRecordListener(listener: RecordListener?) {
        mRecordListener = listener
    }

    /**
     * 定义录音控件的回调接口，用于回调给调用者录音结果
     */
    abstract class RecordListener {
        /**
         * 录音开始，默认空实现，有需要可重写
         */
        open fun onStart() {}

        /**
         * 录音取消，默认空实现，有需要可重写
         */
        open fun onCancel() {}

        /**
         * 录音错误
         *
         * @param code 错误码
         * @param desc 错误描述
         */
        abstract fun onError(code: Int, desc: String)

        /**
         * 录音成功
         *
         * @param path 录音文件的路径
         * @param time 录音时长
         */
        abstract fun onComplete(path: String, time: Int)
    }

}