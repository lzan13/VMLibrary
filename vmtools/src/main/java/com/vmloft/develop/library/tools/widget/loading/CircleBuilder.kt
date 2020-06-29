package com.vmloft.develop.library.tools.widget.loading

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import androidx.annotation.FloatRange

/**
 * Created by zyao89 on 2017/3/19.
 * Contact me at 305161066@qq.com or zyao89@gmail.com
 * For more projects: https://github.com/zyao89
 * My Blog: http://zyao89.me
 */
class CircleBuilder : VMLoadingBuilder() {
    private var mInnerCircleRectF: RectF? = null
    private var paint: Paint? = null
    private var mStartAngle = 0f
    private var mEndAngle = 0f
    private var mIsFirstState = true
    private fun initValues(context: Context) {
        val allSize = getAllSize()
        val innerRadius = allSize - dip2px(context, 3f)
        mInnerCircleRectF = RectF()
        mStartAngle = DEFAULT_ANGLE
        mEndAngle = DEFAULT_ANGLE
        val viewCenterX = getViewCenterX()
        val viewCenterY = getViewCenterY()
        mInnerCircleRectF!![viewCenterX - innerRadius, viewCenterY - innerRadius, viewCenterX + innerRadius] = viewCenterY + innerRadius
    }

    override fun initParams(context: Context) {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint!!.color = Color.BLACK
        initValues(context)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.drawArc(mInnerCircleRectF!!, mStartAngle, mEndAngle - mStartAngle, true, paint!!)
        canvas.restore()
    }

    override fun setAlpha(alpha: Int) {
        paint!!.alpha = alpha
    }

    override fun prepareStart(floatValueAnimator: ValueAnimator) {}
    override fun prepareEnd() {}
    override fun computeUpdateValue(animation: ValueAnimator, @FloatRange(from = 0.0, to = 1.0) animatedValue: Float) {
        if (mIsFirstState) {
            mEndAngle = animatedValue * 360 + DEFAULT_ANGLE
        } else {
            mStartAngle = animatedValue * 360 + DEFAULT_ANGLE
        }
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint!!.colorFilter = colorFilter
    }

    override fun onAnimationStart(animation: Animator?) {
        mStartAngle = DEFAULT_ANGLE
        mEndAngle = DEFAULT_ANGLE
    }

    override fun onAnimationEnd(animation: Animator?) {
        mStartAngle = DEFAULT_ANGLE
        mEndAngle = DEFAULT_ANGLE
    }

    override fun onAnimationCancel(animation: Animator?) {
        mStartAngle = DEFAULT_ANGLE
        mEndAngle = DEFAULT_ANGLE
    }

    override fun onAnimationRepeat(animation: Animator?) {
        mIsFirstState = !mIsFirstState
        if (mIsFirstState) {
            mStartAngle = DEFAULT_ANGLE
            mEndAngle = DEFAULT_ANGLE
        } else {
            mStartAngle = DEFAULT_ANGLE
            mEndAngle = 360 + DEFAULT_ANGLE
        }
    }

    companion object {
        private const val DEFAULT_ANGLE = -90f
    }
}