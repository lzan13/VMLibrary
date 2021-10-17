package com.vmloft.develop.library.tools.widget.loading

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import androidx.annotation.FloatRange

import com.vmloft.develop.library.tools.utils.VMDimen


/**
 * Create by lzan13 on 2020/6/28 17:53
 * 描述：构建 Loading 控件基类
 */
abstract class VMLoadingBuilder : ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

    // 大小
    var defaultSize = 56
    // 延迟
    private val animationStartDelay: Long = 320
    // 持续时间
    private val animationDuration: Long = 1200
    // 速度
    private var mSpeed = 1.0f

    private var mMaxSize = 0
    private var mViewWidth = 0
    private var mViewHeight = 0

    private lateinit var mDrawable: Drawable
    private lateinit var mFloatValueAnimator: ValueAnimator

    fun init() {
        mMaxSize = VMDimen.dp2px((defaultSize * 0.5f - 12).toInt())
        mViewWidth = VMDimen.dp2px(defaultSize)
        mViewHeight = VMDimen.dp2px(defaultSize)
        initAnimators()
    }

    private fun initAnimators() {
        mFloatValueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
        mFloatValueAnimator.repeatCount = Animation.INFINITE
        mFloatValueAnimator.duration = getAnimationDuration()
        mFloatValueAnimator.startDelay = getAnimationStartDelay()
        mFloatValueAnimator.interpolator = LinearInterpolator()
    }

    abstract fun initParams()

    abstract fun onDraw(canvas: Canvas)

    abstract fun setAlpha(alpha: Int)

    abstract fun prepareStart(animation: ValueAnimator)

    abstract fun prepareEnd()

    abstract fun computeUpdateValue(animation: ValueAnimator, @FloatRange(from = 0.0, to = 1.0) animatedValue: Float)

    abstract fun setColorFilter(colorFilter: ColorFilter?)

    fun setDrawable(drawable: Drawable) {
        mDrawable = drawable
    }

    fun draw(canvas: Canvas) {
        onDraw(canvas)
    }

    fun start() {
        if (mFloatValueAnimator.isStarted) {
            return
        }
        mFloatValueAnimator.addUpdateListener(this)
        mFloatValueAnimator.addListener(this)
        mFloatValueAnimator.repeatCount = Animation.INFINITE
        mFloatValueAnimator.duration = getAnimationDuration()
        prepareStart(mFloatValueAnimator)
        mFloatValueAnimator.start()
    }

    fun stop() {
        mFloatValueAnimator.removeAllUpdateListeners()
        mFloatValueAnimator.removeAllListeners()
        mFloatValueAnimator.repeatCount = 0
        mFloatValueAnimator.duration = 0
        prepareEnd()
        mFloatValueAnimator.end()
    }

    fun isRunning(): Boolean {
        return mFloatValueAnimator.isRunning
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        computeUpdateValue(animation, animation.animatedValue as Float)
        mDrawable.invalidateSelf()
    }

    override fun onAnimationStart(animation: Animator) {}

    override fun onAnimationEnd(animation: Animator) {}

    override fun onAnimationCancel(animation: Animator) {}

    override fun onAnimationRepeat(animation: Animator) {}

    fun setSpeed(speed: Float) {
        mSpeed = if (speed <= 0) {
            1.0f
        } else {
            speed
        }
    }

    fun getAnimationStartDelay(): Long {
        return (animationStartDelay * mSpeed).toLong()
    }

    fun getAnimationDuration(): Long {
        return animationDuration
    }

    fun getIntrinsicHeight(): Int {
        return mViewHeight
    }

    fun getIntrinsicWidth(): Int {
        return mViewWidth
    }

    fun getViewCenterX(): Float {
        return getIntrinsicWidth() * 0.5f
    }

    fun getViewCenterY(): Float {
        return getIntrinsicHeight() * 0.5f
    }

    fun getMaxSize(): Int {
        return mMaxSize
    }
}