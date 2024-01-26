package com.vmloft.develop.library.tools.widget.loading

import android.animation.ValueAnimator
import android.graphics.*
import androidx.annotation.FloatRange

/**
 * Create by lzan13 on 2020/6/30 13:45
 * 描述：嵌套的圆形加载动画
 */
class DoubleCircleBuilder : VMLoadingBuilder() {

    private val outerCircleAngle = 270f
    private val interCircleAngle = 90f
    private lateinit var mStrokePaint: Paint
    private lateinit var mOuterCircleRectF: RectF
    private lateinit var mInnerCircleRectF: RectF
    //旋转角度
    private var mRotateAngle = 0f

    /**
     * 初始化画笔
     */
    private fun initPaint(lineWidth: Float) {
        mStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mStrokePaint.style = Paint.Style.STROKE
        mStrokePaint.strokeWidth = lineWidth
        mStrokePaint.color = Color.WHITE
        mStrokePaint.isDither = true
        mStrokePaint.isFilterBitmap = true
        mStrokePaint.strokeCap = Paint.Cap.ROUND
        mStrokePaint.strokeJoin = Paint.Join.ROUND
    }

    override fun initParams() {
        //外圆尺寸
        val outR = getMaxSize()
        //小圆尺寸
        val inR = outR * 0.6f
        //初始化画笔
        initPaint(inR * 0.3f)
        //旋转角度
        mRotateAngle = 0f
        //圆范围
        mOuterCircleRectF = RectF()
        mOuterCircleRectF[getViewCenterX() - outR, getViewCenterY() - outR, getViewCenterX() + outR] = getViewCenterY() + outR
        mInnerCircleRectF = RectF()
        mInnerCircleRectF[getViewCenterX() - inR, getViewCenterY() - inR, getViewCenterX() + inR] = getViewCenterY() + inR
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        //外圆
        canvas.drawArc(mOuterCircleRectF, mRotateAngle % 360, outerCircleAngle, false, mStrokePaint)
        //内圆
        canvas.drawArc(mInnerCircleRectF, 270 - mRotateAngle % 360, interCircleAngle, false, mStrokePaint)
        canvas.restore()
    }

    override fun setAlpha(alpha: Int) {
        mStrokePaint.alpha = alpha
    }

    override fun prepareStart(animation: ValueAnimator) {
    }

    override fun prepareEnd() {}

    override fun computeUpdateValue(animation: ValueAnimator, @FloatRange(from = 0.0, to = 1.0) animatedValue: Float) {
        mRotateAngle = 360 * animatedValue
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mStrokePaint.colorFilter = colorFilter
    }
}