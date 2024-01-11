package com.vmloft.develop.library.tools.widget.record

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
import com.vmloft.develop.library.tools.utils.logger.VMLog

import java.util.*

/**
 * Created by lzan13 on 2024/01/10
 * 描述：自定义录音控件
 */
class VMRecordView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
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

    // 触摸区域提示文本
    private var mDescNormal: String = "按下 说话"
    private var mDescCancel: String = "松开 取消"
    private var mDescColor = 0x44407a
    private var mDescFontSize = VMDimen.dp2px(14)

    // 内圈录音按钮的颜色、大小
    private var mInnerSize = VMDimen.dp2px(60)
    private var mInnerColor = 0x6457f0
    private var mInnerColorCancel = 0xc7c6d4
    private var mInnerIcon = R.drawable.ic_voice_record_mic_white
    private var mInnerIconCancel = R.drawable.ic_voice_record_mic_black

    // 外圈的颜色、大小
    private var mOuterColor = 0xffffff
    private var mOuterSize = VMDimen.dp2px(128)
    private var mOuterAnimSize = 0
    private var mOuterAnimAlpha = 128

    // 时间字体的大小、颜色
    private var mTimeColor = 0x44407a
    private var mTimeFontSize = VMDimen.dp2px(14)


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
        val array = context.obtainStyledAttributes(attrs, styleable.VMRecordView)

        mBGColor = array.getColor(styleable.VMRecordView_vm_bg_color, mBGColor)

        mCancelColor = array.getColor(styleable.VMRecordView_vm_cancel_color, mCancelColor)
        mCancelColorActivate = array.getColor(styleable.VMRecordView_vm_cancel_color_activate, mCancelColorActivate)
        mCancelIcon = array.getResourceId(styleable.VMRecordView_vm_cancel_icon, mCancelIcon)
        mCancelIconActivate = array.getResourceId(styleable.VMRecordView_vm_cancel_icon_activate, mCancelIconActivate)
        mCancelSize = array.getDimensionPixelOffset(styleable.VMRecordView_vm_cancel_size, mCancelSize)

        mOuterColor = array.getColor(styleable.VMRecordView_vm_outer_color, mOuterColor)
        mOuterSize = array.getDimensionPixelOffset(styleable.VMRecordView_vm_outer_size, mOuterSize)
        mOuterAnimSize = (mOuterSize * 1.5f).toInt()

        mInnerColor = array.getColor(styleable.VMRecordView_vm_inner_color, mInnerColor)
        mInnerColorCancel = array.getColor(styleable.VMRecordView_vm_inner_color_cancel, mInnerColorCancel)
        mInnerSize = array.getDimensionPixelOffset(styleable.VMRecordView_vm_inner_size, mInnerSize)

        mInnerIcon = array.getResourceId(styleable.VMRecordView_vm_inner_icon, mCancelIcon)
        mInnerIconCancel = array.getResourceId(styleable.VMRecordView_vm_inner_icon_cancel, mCancelIconActivate)

        mDescNormal = array.getString(styleable.VMRecordView_vm_desc_normal) ?: mDescCancel
        mDescCancel = array.getString(styleable.VMRecordView_vm_desc_cancel) ?: mDescCancel
        if (mDescNormal.isNullOrEmpty()) {
            mDescNormal = "触摸录音"
        }
        if (mDescCancel.isNullOrEmpty()) {
            mDescCancel = "松开取消"
        }
        mDescColor = array.getColor(styleable.VMRecordView_vm_desc_color, mDescColor)
        mDescFontSize = array.getDimensionPixelOffset(styleable.VMRecordView_vm_desc_font_size, mDescFontSize)
        mTimeColor = array.getColor(styleable.VMRecordView_vm_time_color, mTimeColor)
        mTimeFontSize = array.getDimensionPixelOffset(styleable.VMRecordView_vm_time_font_size, mTimeFontSize)
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
        // 绘制触摸区域
        mPaint.color = mInnerColor
        canvas.drawCircle(mWidth / 2.0f, mHeight / 2.0f, mInnerSize / 2.0f, mPaint)
        val bitmap = context.resources.getDrawable(mInnerIcon).toBitmap()
        canvas.drawBitmap(bitmap, mWidth / 2.0f - bitmap.width / 2, mHeight / 2.0f - bitmap.height / 2, mPaint)

        // 绘制提示文本
        mPaint.color = mDescColor
        mPaint.textSize = mDescFontSize.toFloat()
        val tWidth = VMDimen.getTextWidth(mPaint, mUnusableDesc)
        val tHeight = VMDimen.getTextHeight(mPaint, mUnusableDesc)
        canvas.drawText(mUnusableDesc, mWidth / 2 - tWidth / 2, mHeight / 2 + tHeight / 3, mPaint)
    }

    /**
     * 绘制录制按钮动画
     */
    private fun drawRecordAnim(canvas: Canvas) {
        if (isReadyCancel) return
        // 绘制外圈
        mPaint.color = mOuterColor
        // 设置透明度，这里透明度要放在设置颜色之后
        mPaint.alpha = mOuterAnimAlpha
        canvas.drawCircle(mWidth / 2.0f, mHeight / 2.0f, mOuterAnimSize / 2.0f, mPaint)
    }

    /**
     * 绘制录制按钮
     */
    private fun drawRecordBtn(canvas: Canvas) {
        if (!isReadyCancel) {
            // 绘制外圈
            mPaint.color = mOuterColor
            canvas.drawCircle(mWidth / 2.0f, mHeight / 2.0f, mOuterSize / 2.0f, mPaint)
        }

        // 绘制触摸区域
        mPaint.color = if (isReadyCancel) mInnerColorCancel else mInnerColor
        canvas.drawCircle(mWidth / 2.0f, mHeight / 2.0f, mInnerSize / 2.0f, mPaint)

        // 绘制麦克风图标
        val bitmap = context.resources.getDrawable(if (isReadyCancel) mInnerIconCancel else mInnerIcon).toBitmap()
        canvas.drawBitmap(bitmap, mWidth / 2.0f - bitmap.width / 2, mHeight / 2.0f - bitmap.height / 2, mPaint)
    }

    /**
     * 绘制录制按钮
     */
    private fun drawCancelBtn(canvas: Canvas) {
        // 绘制取消按钮背景
        mPaint.color = if (isReadyCancel) mCancelColorActivate else mCancelColor

        val cancelX = mWidth - VMDimen.dp2px(36) - mCancelSize / 2.0f
        val cancelY = mHeight / 2.0f
        canvas.drawCircle(cancelX, cancelY, mCancelSize / 2.0f, mPaint)

        // 绘制麦克风图标
        val bitmap = context.resources.getDrawable(if (isReadyCancel) mCancelIconActivate else mCancelIcon).toBitmap()
        val cancelIconX = mWidth - VMDimen.dp2px(36) - mCancelSize / 2 - bitmap.width / 2.0f
        val cancelIconY = mHeight / 2 - bitmap.height / 2.0f
        canvas.drawBitmap(bitmap, cancelIconX, cancelIconY, mPaint)
    }

    /**
     * 绘制文本
     */
    private fun drawDesc(canvas: Canvas) {
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
        val centerY = mHeight / 2 - mInnerSize / 2 - tHeight * 2
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
        val minute = recordTime / 1000 / 60
        val seconds = recordTime / 1000 % 60
//        val millisecond = recordTime % 1000 / 100

        val time = VMStr.byArgs("%02d:%02d", minute, seconds)
        val tWidth = VMDimen.getTextWidth(mPaint, time)
        val tHeight = VMDimen.getTextHeight(mPaint, time)
        val centerX = VMDimen.dp2px(36) + tWidth / 2
        canvas.drawText(time, centerX, mHeight / 2 + tHeight / 2, mPaint)
    }

    /**
     * 外圈动画
     */
    private fun startOuterAnim() {
        VMSystem.runInUIThread({
            val mAnimator = ValueAnimator.ofInt(mOuterSize, mOuterSize * 2)
            mAnimator.duration = sampleTime
            mAnimator.repeatCount = 0
            mAnimator.interpolator = LinearInterpolator()
            mAnimator.addUpdateListener { a: ValueAnimator ->
                if (isStart) {
                    // 动画大小根据回调变化
                    mOuterAnimSize = a.animatedValue as Int
                    mOuterAnimAlpha = (mOuterSize * 2 - mOuterAnimSize) * 128 / mOuterSize

//                VMLog.i("alpha $mOuterAnimAlpha")
                    invalidate()
                }
            }
            mAnimator.start()
        })
        // 原来的内圈动画
//        VMSystem.runInUIThread({
//            val mAnimator = ValueAnimator.ofInt(mInnerSize, (mInnerSize + recordDecibel * 1.2f * mHeight / 10).toInt(), mInnerSize)
//            mAnimator.duration = sampleTime
//            mAnimator.repeatCount = 0
//            mAnimator.interpolator = LinearInterpolator()
//            mAnimator.addUpdateListener { a: ValueAnimator ->
//                mOuterSize = a.animatedValue as Int
//                invalidate()
//            }
//            mAnimator.start()
//        })
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
                mRecordListener?.onDecibel(recordDecibel)
                postInvalidate()
            }
        }
        timer?.scheduleAtFixedRate(task, 100, sampleTime)
    }

    /**
     * 开始录音
     */
    fun startRecord() {
        isStart = true
        // 调用录音机开始录制音频
        val code: Int = VMRecorder.startRecord(null)
        if (code == VMRecorder.errorNone) {
            startRecordTimer()
            recordStart()
        } else if (code == VMRecorder.errorRecording) {
            // TODO 正在录制中，不做处理
        } else if (code == VMRecorder.errorSystem) {
            isStart = false
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

            MotionEvent.ACTION_MOVE -> if (isUsable && isStart) {
                val cancelX = mWidth - VMDimen.dp2px(36) - mCancelSize / 2.0f
                val cancelY = mHeight / 2.0f
                val cancelLeft = cancelX - mCancelSize / 2
                val cancelRight = cancelX + mCancelSize / 2
                val cancelTop = cancelY - mCancelSize / 2
                val cancelBottom = cancelY + mCancelSize / 2
                isReadyCancel = x > cancelLeft && x < cancelRight && y > cancelTop && y < cancelBottom

//                VMLog.i("$isReadyCancel ($cancelLeft-$x-$cancelRight) ($cancelTop-$y-$cancelBottom)")

                postInvalidate()
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
        isStart = false
        isReadyCancel = false

        mOuterAnimSize = (mOuterSize * 1.5).toInt()
        mOuterAnimAlpha = 128

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

        /**
         * 录音分贝
         */
        abstract fun onDecibel(decibel: Int)
    }

}