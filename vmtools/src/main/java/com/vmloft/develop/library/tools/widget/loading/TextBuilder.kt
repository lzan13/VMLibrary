package com.vmloft.develop.library.tools.widget.loading

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.*
import android.text.TextUtils
import android.view.animation.AccelerateInterpolator
import androidx.annotation.FloatRange


/**
 * Create by lzan13 on 2020/6/30 16:13
 * 描述：
 */
class TextBuilder : VMLoadingBuilder() {

    private val baseAlpha = 100
    private val defaultText = "Loading"
    private lateinit var mTextPaint: Paint
    private lateinit var mTextChars: String
    private var mDrawTextCount = 0

    override fun initParams() {
        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint.color = Color.BLACK
        mTextPaint.isDither = true
        mTextPaint.isFilterBitmap = true
        mTextPaint.textSize = getMaxSize().toFloat()
        mTextPaint.style = Paint.Style.FILL
        mTextPaint.textAlign = Paint.Align.LEFT
        //默认值
        mTextChars = defaultText
    }

    override fun onDraw(canvas: Canvas) {
        val length = mTextChars.toCharArray().size
        val measureText: Float = mTextPaint.measureText(mTextChars, 0, length)
        val paint = Paint(mTextPaint)
        paint.alpha = baseAlpha
        paint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText(mTextChars, 0, length, getViewCenterX() - measureText / 2, getViewCenterY(), paint)
        canvas.drawText(mTextChars, 0, mDrawTextCount, getViewCenterX() - measureText / 2, getViewCenterY(), mTextPaint)
    }

    override fun setAlpha(alpha: Int) {
        mTextPaint.alpha = alpha
    }

    override fun prepareStart(animator: ValueAnimator) {
        animator.duration = (getAnimationDuration() * 0.3f).toLong()
        animator.interpolator = AccelerateInterpolator()
    }

    override fun prepareEnd() {}

    override fun computeUpdateValue(animation: ValueAnimator, @FloatRange(from = 0.0, to = 1.0) animatedValue: Float) {
        mTextPaint.alpha = (animatedValue * 155).toInt() + baseAlpha
    }

    override fun onAnimationRepeat(animation: Animator) {
        if (++mDrawTextCount > mTextChars.toCharArray().size) { //还原到第一阶段
            mDrawTextCount = 0
        }
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mTextPaint.colorFilter = colorFilter
    }

    fun setText(text: String) {
        if (TextUtils.isEmpty(text)) {
            return
        }
        val measureText: Float = mTextPaint.measureText(text)
        if (measureText >= getIntrinsicWidth()) {
            val v: Float = measureText / getMaxSize()
            mTextPaint.textSize = getIntrinsicWidth() / v
        }
        mTextChars = text
    }

}