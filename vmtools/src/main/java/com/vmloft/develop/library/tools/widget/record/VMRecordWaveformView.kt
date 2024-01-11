package com.vmloft.develop.library.tools.widget.record

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View

import com.vmloft.develop.library.tools.R
import com.vmloft.develop.library.tools.utils.VMColor
import com.vmloft.develop.library.tools.utils.VMDimen
import com.vmloft.develop.library.tools.utils.logger.VMLog


/**
 * Created by lzan13 on 2024/01/11
 * 描述：自定义录音波形控件，根据音量变化
 */
class VMRecordWaveformView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {
    private var mWidth = 0
    private var mHeight = 0

    // 画笔
    private var mPaint: Paint

    // 渐变色
    private var mStartColor = 0xfafafa
    private var mEndColor = 0xfafafa

    // 波形宽度
    private var mLineWidth = VMDimen.dp2px(10)

    // 波形空间
    private var spaceWidth = VMDimen.dp2px(30)

    private var recordDecibel = 1 // 分贝

    private var sampleTime: Long = 1000 // 分贝取样时间 毫秒值

    // 波形线集合
    private var lineList = mutableListOf<WaveformBean>()

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
        val array = context.obtainStyledAttributes(attrs, R.styleable.VMRecordWaveformView)

        mStartColor = array.getColor(R.styleable.VMRecordWaveformView_vm_start_color, mStartColor)
        mEndColor = array.getColor(R.styleable.VMRecordWaveformView_vm_end_color, mEndColor)
        mLineWidth = array.getDimensionPixelOffset(R.styleable.VMRecordWaveformView_vm_waveform_line_width, mLineWidth)
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
        val spaceWidth = (mWidth - VMDimen.dp2px(10) * 2 - mLineWidth * 19) / 18
        for (i in 0..18) {
            val waveformBean = WaveformBean()
            waveformBean.centerX = VMDimen.dp2px(10) + i * spaceWidth + mLineWidth / 2 + i * mLineWidth
            waveformBean.centerY = mHeight / 2
            if (i == 4 || i == 14) {
            } else if (i == 5 || i == 13) {
                waveformBean.height = VMDimen.dp2px(10)
            } else if (i == 6 || i == 12) {
                waveformBean.height = VMDimen.dp2px(16)
            } else if (i == 7 || i == 11) {
                waveformBean.height = VMDimen.dp2px(24)
            } else if (i == 8 || i == 10) {
                waveformBean.height = VMDimen.dp2px(36)
            } else if (i == 9) {
                waveformBean.height = VMDimen.dp2px(48)
            } else {
                waveformBean.height = VMDimen.dp2px(8)
            }
            lineList.add(waveformBean)
        }

    }

    /**
     * 更新分贝大小
     */
    fun updateDecibel(decibel: Int) {
        recordDecibel = decibel
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        // 绘制背景
        drawBackground(canvas)
        // 绘制波形线
//        drawLine(canvas)

    }

    /**
     * 绘制背景
     */
    private fun drawBackground(canvas: Canvas) {

        val sl = canvas.saveLayer(0f, 0f, mWidth.toFloat(), mHeight.toFloat(), mPaint)
        // 绘制渐变背景
        val gradient = LinearGradient(0f, 0f, mWidth / 2.0f, 0f, mStartColor, mEndColor, Shader.TileMode.MIRROR)
        mPaint.setShader(gradient)
        mPaint.style = Paint.Style.FILL
        canvas.drawRect(0f, 0f, mWidth.toFloat(), mHeight.toFloat(), mPaint)

//        mPaint.color = VMColor.byRes(R.color.vm_red)
//        mPaint.textSize = VMDimen.dp2px(14).toFloat()
//        val time = "current decibel: ${recordDecibel}"
//        VMLog.i(time)
//        val tWidth = VMDimen.getTextWidth(mPaint, time)
//        val tHeight = VMDimen.getTextHeight(mPaint, time)
//        val centerX = mWidth / 2 - tWidth / 2
//        val centerY = mHeight / 2 - tHeight / 2
//        canvas.drawText(time, centerX, centerY, mPaint)

        mPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        mPaint.color = VMColor.byRes(R.color.vm_green)
        lineList.forEach { waveformBean ->
            val left = waveformBean.centerX - waveformBean.width / 2f
            val right = waveformBean.centerX + waveformBean.width / 2f
            val top = waveformBean.centerY - waveformBean.height / 2f
            val bottom = waveformBean.centerY + waveformBean.height / 2f
            val rectF = RectF(left, top, right, bottom)
            canvas.drawRoundRect(rectF, mLineWidth.toFloat(), mLineWidth.toFloat(), mPaint)
        }
        mPaint.setXfermode(null)

        canvas.restoreToCount(sl)
    }

    /**
     * 绘制波形线
     */
    private fun drawLine(canvas: Canvas) {
//        mPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        mPaint.color = VMColor.byRes(R.color.vm_green)
//        val rectF1 = RectF(10f, 10f, 30f, 30f)
//        canvas.drawRoundRect(rectF1, 4.0f, 4.0f, mPaint)
//
        mPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_OUT))
//        val rectF2 = RectF(30f, 30f, 80f, 80f)
//        canvas.drawRoundRect(rectF2, 4.0f, 4.0f, mPaint)
        lineList.forEach { waveformBean ->
            val left = waveformBean.centerX - waveformBean.width / 2f
            val right = waveformBean.centerX + waveformBean.width / 2f
            val top = waveformBean.centerY - waveformBean.height / 2f
            val bottom = waveformBean.centerY + waveformBean.height / 2f
            val rectF = RectF(left, top, right, bottom)
            canvas.drawRoundRect(rectF, mLineWidth.toFloat(), mLineWidth.toFloat(), mPaint)
        }
    }
}

/**
 * 波形控件属性类
 */
data class WaveformBean(
    var centerX: Int = 0,
    var centerY: Int = 0,
    var width: Int = VMDimen.dp2px(5),
    var height: Int = VMDimen.dp2px(10),
) {}