package com.vmloft.develop.library.tools.widget.draw

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

import com.vmloft.develop.library.tools.R
import com.vmloft.develop.library.tools.utils.VMColor
import com.vmloft.develop.library.tools.utils.VMDimen

/**
 * Create by lzan13 on 2022/3/6
 * 描述：自定义绘画板 View
 */
class VMDrawView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    // 画板
    private lateinit var drawCanvas: Canvas
    private lateinit var cacheBitmap: Bitmap

    // 画笔
    private var pathPaint: Paint = Paint()
    private var eraserPaint: Paint = Paint()
    private var drawPaint: Paint = Paint()

    // 颜色与宽度
    private var pathColor = VMColor.byRes(R.color.vm_black)
    private var pathWidth = VMDimen.dp2px(2)

    //    private var eraserColor = VMColor.byRes(R.color.vm_black)
    private var eraserWidth = VMDimen.dp2px(16)

    // 背景颜色
    private var drawColor = VMColor.byRes(R.color.vm_white)

    private var drawPath = Path()
    private var preX = 0.0f
    private var endX = 0.0f
    private var preY = 0.0f
    private var endY = 0.0f


    //是否橡皮擦
    var isEraser: Boolean = false

    /**
     * 初始化控件
     */
    init {
        // 设置画笔属性
        pathPaint.isAntiAlias = true //抗锯齿功能
        pathPaint.isDither = true // 消除拉动，使画面圓滑
        pathPaint.color = pathColor  //设置画笔颜色
        pathPaint.style = Paint.Style.STROKE //设置填充样式
        pathPaint.strokeWidth = pathWidth.toFloat() //设置画笔宽度
        pathPaint.strokeJoin = Paint.Join.ROUND // 结合方式，平滑
        pathPaint.strokeCap = Paint.Cap.ROUND // 线头 圆润

        // 设置橡皮擦属性
        eraserPaint.isAntiAlias = true //抗锯齿功能
        eraserPaint.isDither = true // 消除拉动，使画面圓滑
//        eraserPaint.color = eraserColor  //设置画笔颜色
        eraserPaint.style = Paint.Style.STROKE //设置填充样式
        eraserPaint.strokeWidth = eraserWidth.toFloat() //设置画笔宽度
        eraserPaint.strokeJoin = Paint.Join.ROUND // 结合方式，平滑
        eraserPaint.strokeCap = Paint.Cap.ROUND // 线头 圆润

        eraserPaint.alpha = 0
        eraserPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

        // 设置绘图板
        drawPaint.isAntiAlias = true //抗锯齿功能
        drawPaint.isDither = true // 消除拉动，使画面圓滑
        drawPaint.color = drawColor  //设置画笔颜色

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

        cacheBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(cacheBitmap)
        drawCanvas.drawColor(drawColor)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawBitmap(cacheBitmap, 0.0f, 0.0f, drawPaint)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                drawPath.reset()
                preX = event.x
                preY = event.y
                drawPath.moveTo(preX, preY)
                drawPath()
            }
            MotionEvent.ACTION_MOVE -> {
                endX = (preX + event.x) / 2
                endY = (preY + event.y) / 2
                drawPath.quadTo(preX, preY, endX, endY)

                preX = event.x
                preY = event.y
                drawPath()
            }
            MotionEvent.ACTION_UP -> {
                endX = (preX + event.x) / 2
                endY = (preY + event.y) / 2
                drawPath.quadTo(preX, preY, endX, endY)

                preX = event.x
                preY = event.y
                drawPath()
            }
        }
        invalidate()

        return true
    }

    /**
     * 绘制路径到缓存
     */
    private fun drawPath() {
        drawCanvas.drawPath(drawPath, if (isEraser) eraserPaint else pathPaint)
    }

    /**
     * 设置画笔颜色
     */
    fun setPaintColor(color: Int) {
        if (color != 0) {
            pathColor = color
        }
        pathPaint.color = pathColor
    }

    /**
     * 设置画笔宽度
     */
    fun setPaintWidth(width: Int) {
        if (width != 0) {
            pathWidth = width
        }
        pathPaint.strokeWidth = pathWidth.toFloat()
    }

    /**
     * 设置橡皮擦宽度
     */
    fun setEraserWidth(width: Int) {
        if (width != 0) {
            eraserWidth = width
        }
        eraserPaint.strokeWidth = eraserWidth.toFloat()
    }

    /**
     * 获取绘图板绘制的内容
     */
    fun getBitmap(): Bitmap {
        return cacheBitmap
    }

    /**
     * 重置控件
     */
    fun reset() {
        isEraser = false

        drawCanvas.drawColor(drawColor, PorterDuff.Mode.CLEAR)

        drawCanvas.drawColor(drawColor)

        drawPath.reset()

        invalidate()
    }

}