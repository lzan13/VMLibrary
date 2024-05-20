package com.vmloft.develop.library.tools.voice.recorder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Shader
import android.util.AttributeSet

import com.vmloft.develop.library.tools.utils.VMDimen


/**
 * Created by lzan13 on 2024/01/11
 * 描述：自定义录音波形控件，根据录音分贝变化
 */
class VMRecorderFFTAnimView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    VMRecorderAnimView(context, attrs, defStyleAttr) {

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

    /**
     * 更新分贝大小
     */
    override fun updateDecibel(decibel: Int) {
//        VMLog.i("updateDecibel $decibel")
    }

    private val sideSpaceSize = VMDimen.dp2px(4)

    /**
     * 更新频谱数据
     */
    override fun updateFFTData(data: DoubleArray) {
        if (data.isEmpty()) {
            return
        }

        var tempArr = DoubleArray(60, { 0.0 })
        var list = data.filter { it > 0.02 }.toMutableList()
        if (list.size > 60) {
            tempArr = list.subList(0, 60).toDoubleArray()
        } else {
            for (i in list.indices) {
                tempArr[i] = list[i]
            }
        }
        list.clear()
        list.addAll(tempArr.toList())
        list.addAll(0, tempArr.reversed().toList())
//        tempList.reversed()
//        val list = mutableListOf<Double>()
//        // 这里将数据倒序插入数据集合中，实现
//        list.addAll(tempList.reversed())
//        list.addAll(tempList)

        lineList.clear()
        val spaceWidth = (mWidth - sideSpaceSize * 2f - mLineWidth * list.size) / (list.size - 1)
        for (i in list.indices) {
            val waveformBean = WaveformBean()
            waveformBean.width = mLineWidth.toFloat()
            val value = list[i]
//            waveformBean.height = (mLineHeight + value * 100 * 2f).toFloat()

            if (value > mHeight) {
                waveformBean.height = mHeight.toFloat()
            } else if (value > mHeight / 5 * 4) {
                waveformBean.height = (mLineHeight + mHeight).toFloat()
            } else if (value > mHeight / 3 * 2) {
                waveformBean.height = (mLineHeight + mHeight).toFloat()
            } else if (value > 5) {
                waveformBean.height = (mLineHeight + value * 10 * 2f).toFloat()
            } else if (value > 4) {
                waveformBean.height = (mLineHeight + value * 20 * 2f).toFloat()
            } else if (value > 3) {
                waveformBean.height = (mLineHeight + value * 30 * 2f).toFloat()
            } else if (value > 2) {
                waveformBean.height = (mLineHeight + value * 50 * 2f).toFloat()
            } else if (value > 1) {
                waveformBean.height = (mLineHeight + value * 80 * 2f).toFloat()
            } else {
                waveformBean.height = (mLineHeight + value * 100 * 2f).toFloat()
            }
            waveformBean.animHeight = if (waveformBean.height > mHeight) mHeight.toFloat() else waveformBean.height
            waveformBean.centerX = sideSpaceSize + i * spaceWidth + mLineWidth / 2f + i * mLineWidth
            waveformBean.centerY = mHeight / 2f

            lineList.add(waveformBean)
        }
        invalidate()
    }

    /**
     * 绘制波形线
     */
    override fun drawWaveformLine(canvas: Canvas) {
        // 开启离屏缓冲
        val saveLayer = canvas.saveLayer(0f, 0f, mWidth.toFloat(), mHeight.toFloat(), mPaint)

        val bgBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888)
        val bgCanvas = Canvas(bgBitmap)
        val bgPaint = Paint()
        // 绘制渐变背景
        val gradient = LinearGradient(0f, 0f, mWidth / 2.0f, 0f, mStartColor, mEndColor, Shader.TileMode.MIRROR)
        bgPaint.setShader(gradient)
        bgPaint.style = Paint.Style.FILL
        bgCanvas.drawRect(0f, 0f, mWidth.toFloat(), mHeight.toFloat(), bgPaint)
        // 将背景绘制到 canvas
        canvas.drawBitmap(bgBitmap, 0f, 0f, mPaint)

        // 绘制线条
        mPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_IN))
        mPaint.color = mStartColor

        val lineBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888)
        val lineCanvas = Canvas(lineBitmap)
        val linePaint = Paint()
        linePaint.strokeWidth = mLineWidth.toFloat()
//        linePaint.strokeJoin = Paint.Join.ROUND // 结合方式，平滑
        linePaint.strokeCap = Paint.Cap.ROUND // 线头 圆润
        lineList.forEach { bean ->
            lineCanvas.drawLine(bean.centerX, bean.centerY - bean.height / 2f, bean.centerX, bean.centerY + bean.height / 2f, linePaint)
        }
        canvas.drawBitmap(lineBitmap, 0f, 0f, mPaint)

        // 清除 Xfermode
        mPaint.setXfermode(null)
        // 关闭离屏缓冲
        canvas.restoreToCount(saveLayer)
    }
}
