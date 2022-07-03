package com.vmloft.develop.library.tools.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

import com.vmloft.develop.library.tools.R
import com.vmloft.develop.library.tools.utils.VMColor
import com.vmloft.develop.library.tools.utils.VMDimen

import java.util.*


/**
 * Create by lzan13 on 2022/7/3
 * 描述：自定义波纹控件
 */
class VMWaveView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var mWidth = 0
    private var mHeight = 0

    // 画笔
    private var paint: Paint

    // 波纹颜色
    private var waveLineColor = VMColor.byRes(R.color.vm_green)
    private var waveLineWidth = VMDimen.dp2px(1)
    private var waveMaxHeight = 50

    // 是否开始动画
    private var isStart = false

    private val paths = mutableListOf<Path>()

    private var lastTime: Long = 0
    private val lineSpeed = 50
    private var translateX = 0f

    private var amplitude = 5f // 振幅

    private var waveHeight = 1f // 波形高
    private val fineness = 1

    /**
     * 初始化控件
     */
    init {
        // 获取控件属性
        handleAttrs(attrs)
        for (i in 0..19) {
            paths.add(Path())
        }
        // 获得绘制文本的宽和高
        paint = Paint()
        paint.isAntiAlias = true // 设置抗锯齿
        paint.color = waveLineColor
        paint.strokeWidth = waveLineWidth.toFloat()
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
    }

    /**
     * 获取控件属性
     */
    private fun handleAttrs(attrs: AttributeSet?) {
        // 获取控件的属性值
        if (attrs == null) {
            return
        }
        val array = context.obtainStyledAttributes(attrs, R.styleable.VMWaveView)
        waveLineColor = array.getColor(R.styleable.VMWaveView_vm_wave_line_color, waveLineColor)
        waveLineWidth = array.getDimensionPixelOffset(R.styleable.VMWaveView_vm_wave_line_width, waveLineWidth)
        waveMaxHeight = array.getDimensionPixelOffset(R.styleable.VMWaveView_vm_wave_max_width, waveMaxHeight)
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

        // 绘制波纹
        drawWave(canvas)
    }

    /**
     * 绘制波形
     */
    private fun drawWave(canvas: Canvas) {
        lineChange()
        canvas.save()
        val moveY = height / 2
        for (i in 0 until paths.size) {
            paths.get(i).reset()
            paths.get(i).moveTo(width.toFloat(), height / 2f)
        }
        var j = width - 1f
        while (j >= 1) {
            val i = j - 1
            //这边必须保证起始点和终点的时候amplitude = 0;
            amplitude = waveHeight * i / width - waveHeight * i / width * i / (width - 2)
            for (n in 1..paths.size) {
                val sin: Float = amplitude * Math.sin((i - Math.pow(1.22, n.toDouble())) * Math.PI / 180 - translateX).toFloat()
                paths.get(n - 1).lineTo(j, 2 * n * sin / paths.size - 15 * sin / paths.size + moveY)
            }
            j -= fineness
        }
        for (n in 0 until paths.size) {
            if (n == paths.size - 1) {
                paint.alpha = 200
            } else {
                paint.alpha = n * 130 / paths.size
            }
            if (paint.alpha > 0) {
                canvas.drawPath(paths[n], paint)
            }
        }
        canvas.restore()
    }

    /**
     * 线条改变
     */
    private fun lineChange() {
        if (!isStart) return
        translateX += 5f
//        if (lastTime == 0L) {
//            lastTime = System.currentTimeMillis()
//            translateX += 5f
//        } else {
//            if (System.currentTimeMillis() - lastTime > lineSpeed) {
//                lastTime = System.currentTimeMillis()
//                translateX += 5f
//            } else {
//                return
//            }
//        }
        if (waveHeight < waveMaxHeight) {
            waveHeight += height / 10
        }
    }

    var waveTimer: Timer? = null

    /**
     * 开始波形动画定时器
     */
    fun start() {
        if (isStart) return
        isStart = true
        waveTimer = Timer()
        waveTimer?.purge()
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                postInvalidate()
            }
        }
        waveTimer?.scheduleAtFixedRate(task, 100, 100)
    }

    /**
     * 停止波形动画定时器
     */
    fun stop() {
        waveTimer?.purge()
        waveTimer?.cancel()
        waveTimer = null
        isStart = false
        waveHeight = 1f
        postInvalidate()
    }
}