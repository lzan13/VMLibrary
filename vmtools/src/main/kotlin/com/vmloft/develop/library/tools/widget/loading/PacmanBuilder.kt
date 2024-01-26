package com.vmloft.develop.library.tools.widget.loading

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.*
import androidx.annotation.FloatRange


/**
 * Create by lzan13 on 2020/6/30 15:06
 * 描述：吃豆人构建器
 */
class PacmanBuilder : VMLoadingBuilder() {
    //最终阶段
    private val finalState = 9
    private val maxMouthAngle = 45f
    private var mDurationTime: Long = 333
    private lateinit var mFullPaint: Paint
    private lateinit var mOuterCircleRectF: RectF
    private var mMouthAngle = 0f
    private var mMoveDistance = 0f
    //当前动画阶段
    private var mCurrAnimatorState = 0
    private var mHorizontalAngle = 0f
    private var mMaxMoveRange = 0f
    private var mLastMoveDistance = 0f
    private var mDefaultStartMoveX = 0f

    override fun initParams() {
        val outR = getMaxSize()
        val inR = outR * 0.7f
        mMaxMoveRange = getIntrinsicWidth() + 2 * inR //移动距离范围
        initPaint() //圆范围
        mMouthAngle = maxMouthAngle //嘴度数
        mHorizontalAngle = 0f //水平翻转度数
        mDefaultStartMoveX = -mMaxMoveRange * 0.5f //默认偏移量
        mMoveDistance = 0f //移动距离
        mOuterCircleRectF = RectF(getViewCenterX() - inR, getViewCenterY() - inR, getViewCenterX() + inR, getViewCenterY() + inR)
    }

    private fun initPaint() {
        mFullPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mFullPaint.style = Paint.Style.FILL
        mFullPaint.color = Color.WHITE
        mFullPaint.isDither = true
        mFullPaint.isFilterBitmap = true
        mFullPaint.strokeCap = Paint.Cap.ROUND
        mFullPaint.strokeJoin = Paint.Join.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.translate(mDefaultStartMoveX + mMoveDistance, 0f)
        canvas.rotate(mHorizontalAngle, getViewCenterX(), getViewCenterY())
        canvas.drawArc(mOuterCircleRectF, mMouthAngle, 360 - mMouthAngle * 2, true, mFullPaint)
        canvas.restore()
    }

    override fun setAlpha(alpha: Int) {
        mFullPaint.alpha = alpha
    }

    override fun prepareStart(animator: ValueAnimator) {
        mDurationTime = (getAnimationDuration() * 0.3).toLong()
        animator.duration = mDurationTime
    }

    override fun prepareEnd() {}

    override fun computeUpdateValue(animation: ValueAnimator, @FloatRange(from = 0.0, to = 1.0) animatedValue: Float) {
        val half = finalState / 2 + 1
        val step = mMaxMoveRange / half
        if (mCurrAnimatorState < half) //以下分为两个阶段
        { //向右
            mHorizontalAngle = 0f
            mMoveDistance = mLastMoveDistance + step * animatedValue
        } else { //向左
            mHorizontalAngle = 180f
            mMoveDistance = mLastMoveDistance - step * animatedValue
        }
        //嘴张开度数
        mMouthAngle = if (mCurrAnimatorState % 2 == 0) {
            maxMouthAngle * animatedValue + 5
        } else {
            maxMouthAngle * (1 - animatedValue) + 5
        }
    }

    override fun onAnimationRepeat(animation: Animator) {
        if (++mCurrAnimatorState > finalState) { //还原到第一阶段
            mCurrAnimatorState = 0
        }
        //矫正
        val half = finalState / 2 + 1
        val stepRange = mMaxMoveRange / half
        mLastMoveDistance = if (mCurrAnimatorState < half) {
            stepRange * mCurrAnimatorState
        } else {
            stepRange * (half - mCurrAnimatorState % half)
        }
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mFullPaint.colorFilter = colorFilter
    }
}