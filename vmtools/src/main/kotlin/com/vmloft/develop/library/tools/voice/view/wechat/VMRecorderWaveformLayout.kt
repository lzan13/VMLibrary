package com.vmloft.develop.library.tools.voice.view.wechat

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout

import com.vmloft.develop.library.tools.R
import com.vmloft.develop.library.tools.utils.VMDimen
import com.vmloft.develop.library.tools.voice.view.VMRecorderFFTWaveformView
import com.vmloft.develop.library.tools.voice.view.VMRecorderWaveformView


/**
 * Created by lzan13 on 2024/01/11
 * 描述：自定义仿微信录音波形控件
 */
open class VMRecorderWaveformLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr) {
    protected var showWidth = 0
    protected var showHeight = 0

    protected var normalBGColor = 0
    protected var cancelBGColor = 0


    private var bubbleCL: View
    private var fftWaveformView: VMRecorderFFTWaveformView

    /**
     * 初始化控件
     */
    init {
        // 获取控件属性
        handleAttrs(attrs)

        LayoutInflater.from(context).inflate(R.layout.vm_recorder_waveform_layout, this)

        bubbleCL = findViewById(R.id.bubbleCL)
        fftWaveformView = findViewById(R.id.fftWaveformView)
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

        normalBGColor = array.getColor(R.styleable.VMRecorderLayout_vm_recorder_waveform_normal_bg, normalBGColor)
        cancelBGColor = array.getColor(R.styleable.VMRecorderLayout_vm_recorder_waveform_cancel_bg, cancelBGColor)

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
        showWidth = w
        showHeight = h
    }

    /**
     * 更新频谱数据
     */
    fun updateFFTData(data: DoubleArray) {
        fftWaveformView.updateFFTData(data)
    }

    /**
     * 更新录制状态
     */
    fun updateRecordStatus(isReadyCancel: Boolean) {
        if (isReadyCancel) {
            bubbleCL.backgroundTintList = ColorStateList.valueOf(cancelBGColor)
            fftWaveformView.layoutParams.width = VMDimen.dp2px(36)
        } else {
            bubbleCL.backgroundTintList = ColorStateList.valueOf(normalBGColor)
            fftWaveformView.layoutParams.width = VMDimen.dp2px(128)
        }
        fftWaveformView.requestLayout()
    }
}
