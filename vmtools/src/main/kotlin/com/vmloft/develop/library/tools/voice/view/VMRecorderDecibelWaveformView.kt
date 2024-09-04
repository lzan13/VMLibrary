package com.vmloft.develop.library.tools.voice.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.animation.LinearInterpolator

import com.vmloft.develop.library.tools.utils.VMDimen
import com.vmloft.develop.library.tools.utils.VMSystem
import com.vmloft.develop.library.tools.voice.bean.VMWaveformBean
import com.vmloft.develop.library.tools.voice.recorder.VMRecorderManager


/**
 * Created by lzan13 on 2024/01/11
 * 描述：自定义录音波形控件，根据录音分贝变化
 */
class VMRecorderDecibelWaveformView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    VMRecorderWaveformView(context, attrs, defStyleAttr) {

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

    /**
     * 更新分贝大小
     */
    override fun updateDecibel(decibel: Int) {
//        VMLog.i("updateDecibel $decibel")
        if (lineList.isEmpty()) {
            val spaceWidth = (showWidth - VMDimen.dp2px(10) * 2 - lineWidth * 19) / 18
            for (i in 0..18) {
                val waveformBean = VMWaveformBean()
                waveformBean.width = lineWidth.toFloat()
                waveformBean.height = lineHeight.toFloat()
                waveformBean.centerX = VMDimen.dp2px(10) + i * spaceWidth + lineWidth / 2f + i * lineWidth
                waveformBean.centerY = showHeight / 2f
                when (i) {
                    4, 14 -> waveformBean.scale = 0.5f
                    5, 13 -> waveformBean.scale = 1f
                    6, 12 -> waveformBean.scale = 1.5f
                    7, 11 -> waveformBean.scale = 3f
                    8, 10 -> waveformBean.scale = 5f
                    9 -> waveformBean.scale = 8f
                    else -> waveformBean.scale = 0f
                }
                lineList.add(waveformBean)
            }
        }
        startLineAnim(decibel)
    }

    /**
     * 更新频谱数据
     */
    override fun updateFFTData(data: DoubleArray) {
//        VMLog.i("updateFSData $data")
    }

    /**
     * 绘制波形线
     */
    override fun drawWaveformLine(canvas: Canvas) {
        // 开启离屏缓冲
        val saveLayer = canvas.saveLayer(0f, 0f, showWidth.toFloat(), showHeight.toFloat(), linePaint)

        val bgBitmap = Bitmap.createBitmap(showWidth, showHeight, Bitmap.Config.ARGB_8888)
        val bgCanvas = Canvas(bgBitmap)
        val bgPaint = Paint()
        // 绘制渐变背景
        val gradient = LinearGradient(0f, 0f, showWidth / 2.0f, 0f, startColor, endColor, Shader.TileMode.MIRROR)
        bgPaint.setShader(gradient)
        bgPaint.style = Paint.Style.FILL
        bgCanvas.drawRect(0f, 0f, showWidth.toFloat(), showHeight.toFloat(), bgPaint)
        // 将背景绘制到 canvas
        canvas.drawBitmap(bgBitmap, 0f, 0f, linePaint)

        // 绘制线条
        linePaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_IN))
        linePaint.color = startColor

        val lineBitmap = Bitmap.createBitmap(showWidth, showHeight, Bitmap.Config.ARGB_8888)
        val lineCanvas = Canvas(lineBitmap)
        val linePaint = Paint()
        lineList.forEach { waveformBean ->
//            val animHeight = waveformBean.height + recordDecibel * waveformBean.scale
            val left = waveformBean.centerX - waveformBean.width / 2f
            val right = waveformBean.centerX + waveformBean.width / 2f
            val top = waveformBean.centerY - waveformBean.animHeight / 2f
            val bottom = waveformBean.centerY + waveformBean.animHeight / 2f
            val rectF = RectF(left, top, right, bottom)
            lineCanvas.drawRoundRect(rectF, lineWidth.toFloat(), lineWidth.toFloat(), linePaint)
        }
        canvas.drawBitmap(lineBitmap, 0f, 0f, linePaint)

        // 清除 Xfermode
        linePaint.setXfermode(null)
        // 关闭离屏缓冲
        canvas.restoreToCount(saveLayer)
    }

    /**
     * 波形平滑变换动画
     */
    private fun startLineAnim(decibel: Int) {
        VMSystem.runInUIThread({
            lineList.forEach {
                // 计算波形振幅最大高度
                val maxHeight = it.height + if (decibel > 45) (decibel - 45) * it.scale else decibel / 2 * it.scale
                val mAnimator = ValueAnimator.ofFloat(it.animHeight, maxHeight)
                mAnimator.duration = VMRecorderManager.sampleTime.toLong()
                mAnimator.repeatCount = 0
                mAnimator.interpolator = LinearInterpolator()
                mAnimator.addUpdateListener { a: ValueAnimator ->
                    // 动画大小根据回调变化，不能超过控件高度
                    val value = a.animatedValue as Float
                    it.animHeight = if (value >= showHeight) showHeight.toFloat() else value
                    invalidate()
                }
                mAnimator.start()
            }
        })
    }
}