package com.vmloft.develop.library.tools.voice.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet

import com.vmloft.develop.library.tools.voice.bean.VMWaveformBean


/**
 * Created by lzan13 on 2024/01/11
 * 描述：自定义录音波形控件，根据录音FFT数据变化
 */
class VMRecorderFFTWaveformView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
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
     *
     * 更新分贝大小
     */
    override fun updateDecibel(decibel: Int) {
//        VMLog.i("updateDecibel $decibel")
    }


    /**
     * 更新频谱数据
     */
    override fun updateFFTData(data: DoubleArray) {
        if (data.isEmpty()) {
            return
        }
        val simpleCount = ((showWidth - sideSpaceSize * 2f) / (lineWidth + lineSpace)).toInt()

        // 用数组是为了方便补0
        var tempArr = DoubleArray(simpleCount) { 0.0 }
        val list = data.filter { it > 0.02 }.toMutableList()
        if (list.size > simpleCount) {
            tempArr = list.subList(0, simpleCount).toDoubleArray()
        } else {
            for (i in list.indices) {
                tempArr[i] = list[i]
            }
        }
        list.clear()
        list.addAll(tempArr.toList())

        lineList.clear()
        val spaceWidth = (showWidth - sideSpaceSize * 2f - lineWidth * list.size) / (list.size - 1)
        for (i in list.indices) {
            val waveformBean = VMWaveformBean()
            waveformBean.width = lineWidth.toFloat()
            val value = list[i]
            // 根据位置计算高度百分比，使波形内高外低过渡更自然一些
            val percentage = if (i < list.size / 2f) {
                i / (list.size / 2f)
            } else {
                (list.size - i) / (list.size / 2f)
            }
            // 计算波形高度
            if (value > showHeight) {
                waveformBean.height = showHeight * percentage
            } else if (value > showHeight / 5 * 4) {
                waveformBean.height = lineHeight + showHeight * percentage
            } else if (value > showHeight / 3 * 2) {
                waveformBean.height = lineHeight + showHeight * percentage
            } else if (value > 5) {
                waveformBean.height = (lineHeight + value * 10 * 2f * percentage).toFloat()
            } else if (value > 4) {
                waveformBean.height = (lineHeight + value * 20 * 2f * percentage).toFloat()
            } else if (value > 3) {
                waveformBean.height = (lineHeight + value * 30 * 2f * percentage).toFloat()
            } else if (value > 2) {
                waveformBean.height = (lineHeight + value * 50 * 2f * percentage).toFloat()
            } else if (value > 1) {
                waveformBean.height = (lineHeight + value * 80 * 2f * percentage).toFloat()
            } else {
                waveformBean.height = (lineHeight + value * 100 * 2f * percentage).toFloat()
            }
            if (waveformBean.height > showHeight) {
                waveformBean.height = showHeight - lineHeight.toFloat()
            }
            waveformBean.centerX = sideSpaceSize + i * spaceWidth + lineWidth / 2f + i * lineWidth
            waveformBean.centerY = showHeight / 2f

            lineList.add(waveformBean)
        }
        invalidate()
    }

    /**
     * 绘制波形线
     */
    override fun drawWaveformLine(canvas: Canvas) {
        // 绘制线条
        linePaint.color = startColor

        linePaint.strokeWidth = lineWidth.toFloat()
        linePaint.strokeCap = Paint.Cap.ROUND // 线头 圆润
        lineList.forEach { bean ->
            canvas.drawLine(bean.centerX, bean.centerY - bean.height / 2f, bean.centerX, bean.centerY + bean.height / 2f, linePaint)
        }
    }
}
