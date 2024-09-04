package com.vmloft.develop.library.tools.voice.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

import com.vmloft.develop.library.tools.R
import com.vmloft.develop.library.tools.utils.VMDimen
import com.vmloft.develop.library.tools.voice.bean.VMWaveformBean


/**
 * Created by lzan13 on 2024/01/11
 * 描述：自定义录音波形控件，根据录音分贝变化
 */
open class VMRecorderWaveformView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {
    protected var showWidth = 0
    protected var showHeight = 0

    // 画笔
    protected var linePaint: Paint

    protected var lineBGColor = 0

    // 渐变色
    protected var startColor = 0xfafafa
    protected var endColor = 0xfafafa


    protected var lineWidth = VMDimen.dp2px(2) // 波形宽度
    protected var lineSpace = VMDimen.dp2px(1) // 波形间隔
    protected var lineHeight = VMDimen.dp2px(2) // 波形高度

    protected val sideSpaceSize = VMDimen.dp2px(4) // 侧边空间

    // 波形线集合
    protected var lineList = mutableListOf<VMWaveformBean>()

    /**
     * 初始化控件
     */
    init {
        //禁用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        // 获取控件属性
        handleAttrs(attrs)

        // 画笔
        linePaint = Paint()
        // 设置抗锯齿
        linePaint.isAntiAlias = true
    }

    /**
     * 获取控件属性
     */
    private fun handleAttrs(attrs: AttributeSet?) {
        // 获取控件的属性值
        if (attrs == null) {
            return
        }
        val array = context.obtainStyledAttributes(attrs, R.styleable.VMVoiceView)

        lineBGColor = array.getColor(R.styleable.VMVoiceView_vm_bg_color, lineBGColor)

        startColor = array.getColor(R.styleable.VMVoiceView_vm_anim_start_color, startColor)
        endColor = array.getColor(R.styleable.VMVoiceView_vm_anim_end_color, endColor)

        lineWidth = array.getDimensionPixelOffset(R.styleable.VMVoiceView_vm_anim_line_width, lineWidth)
        lineSpace = array.getDimensionPixelOffset(R.styleable.VMVoiceView_vm_anim_line_space, lineSpace)
        lineHeight = array.getDimensionPixelOffset(R.styleable.VMVoiceView_vm_anim_line_height, lineHeight)
        array.recycle()
    }

    /**
     * 重写父类的 onSizeChanged 方法，检测控件宽高的变化
     *
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 绘制背景
        drawBackground(canvas)
        // 绘制波形线
        drawWaveformLine(canvas)
    }

    /**
     * 更新分贝大小
     */
    open fun updateDecibel(decibel: Int) {
    }

    /**
     * 更新频谱数据
     */
    open fun updateFFTData(data: DoubleArray) {
    }

    /**
     * 绘制背景
     */
    open fun drawBackground(canvas: Canvas) {
        if (lineBGColor > 0) {
            canvas.drawColor(lineBGColor)
        }
    }

    /**
     * 绘制波形线
     */
    open fun drawWaveformLine(canvas: Canvas) {

    }
}
