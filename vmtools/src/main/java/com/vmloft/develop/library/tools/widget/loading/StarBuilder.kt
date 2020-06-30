package com.vmloft.develop.library.tools.widget.loading

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.graphics.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import androidx.annotation.FloatRange
import com.vmloft.develop.library.tools.utils.VMDimen


/**
 * Create by lzan13 on 2020/6/30 15:41
 * 描述：跳动的星星
 */
class StarBuilder : VMLoadingBuilder() {

    private lateinit var mFullPaint: Paint
    private var mStarOutR = 0f
    private var mStarInR = 0f
    private var mStarOutMidR = 0f
    private var mStarInMidR = 0f
    //开始偏移角度
    private var mStartAngle = 0f
    private lateinit var mStarPath: Path
    private var mOffsetTranslateY = 0f
    private lateinit var mOvalRectF: RectF
    private var mShadowWidth = 0f
    private lateinit var mShadowAnimator: ValueAnimator

    private val mAnimatorUpdateListener = AnimatorUpdateListener { animation ->
        val value = animation.animatedValue as Float
        mOffsetTranslateY = getViewCenterY() * 0.4f * value
        mShadowWidth = (mOffsetTranslateY + 10) * 0.9f
    }

    override fun initParams() {
        mFullPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mFullPaint.style = Paint.Style.FILL_AND_STROKE
        mFullPaint.strokeWidth = 2f
        mFullPaint.color = Color.BLACK
        mFullPaint.isDither = true
        mFullPaint.isFilterBitmap = true

        initValue()
        initAnimator()
    }

    private fun initAnimator() {
        mShadowAnimator = ValueAnimator.ofFloat(0.0f, 1.0f, 0.0f)
        mShadowAnimator.repeatCount = Animation.INFINITE
        mShadowAnimator.duration = getAnimationDuration()
        mShadowAnimator.startDelay = getAnimationStartDelay()
        mShadowAnimator.interpolator = AccelerateDecelerateInterpolator()
    }

    private fun initValue() {
        val maxSize = getMaxSize()
        mStarOutR = (maxSize - VMDimen.dp2px(5)).toFloat()
        mStarOutMidR = mStarOutR * 0.9f
        mStarInR = mStarOutMidR * 0.6f
        mStarInMidR = mStarInR * 0.9f
        mStartAngle = 0f
        mOffsetTranslateY = 0f
        //星路径
        mStarPath = createStarPath(5, -18)
        //阴影宽度
        mShadowWidth = mStarOutR
        mOvalRectF = RectF()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.translate(0f, mOffsetTranslateY)
        canvas.rotate(mStartAngle, getViewCenterX(), getViewCenterY())
        canvas.drawPath(mStarPath, mFullPaint)
        canvas.restore()
        mOvalRectF[getViewCenterX() - mShadowWidth, getIntrinsicHeight() - 20.toFloat(), getViewCenterX() + mShadowWidth] = getIntrinsicHeight() - 10.toFloat()
        canvas.drawOval(mOvalRectF, mFullPaint)
    }

    /**
     * 绘制五角星
     *
     * @param num        角数量
     * @param startAngle 初始角度
     * @return
     */
    private fun createStarPath(num: Int, startAngle: Int): Path {
        val path = Path()
        val angle = 360 / num
        val roundSize = 5 //圆角弧度
        val offsetAngle = angle / 2
        path.moveTo(getViewCenterX() + mStarOutMidR * cos(startAngle - roundSize), getViewCenterY() + mStarOutMidR * sin(startAngle - roundSize))
        for (i in 0 until num) {
            val value = angle * i + startAngle
            path.lineTo(getViewCenterX() + mStarOutMidR * cos(value - roundSize), getViewCenterY() + mStarOutMidR * sin(value - roundSize))
            //圆角
            path.quadTo(getViewCenterX() + mStarOutR * cos(value), getViewCenterY() + mStarOutR * sin(value), getViewCenterX() + mStarOutMidR * cos(value + roundSize), getViewCenterY() + mStarOutMidR * sin(value + roundSize))
            path.lineTo(getViewCenterX() + mStarInR * cos(value + offsetAngle - roundSize), getViewCenterY() + mStarInR * sin(value + offsetAngle - roundSize))
            //内圆角
            path.quadTo(getViewCenterX() + mStarInMidR * cos(value + offsetAngle), getViewCenterY() + mStarInMidR * sin(value + offsetAngle), getViewCenterX() + mStarInR * cos(value + offsetAngle + roundSize), getViewCenterY() + mStarInR * sin(value + offsetAngle + roundSize))
        }
        path.close()
        return path
    }

    override fun setAlpha(alpha: Int) {
        mFullPaint.setAlpha(alpha)
    }

    override fun prepareStart(animator: ValueAnimator) {
        animator.interpolator = DecelerateInterpolator()
        mShadowAnimator.repeatCount = Animation.INFINITE
        mShadowAnimator.duration = getAnimationDuration()
        mShadowAnimator.startDelay = getAnimationStartDelay()
        mShadowAnimator.addUpdateListener(mAnimatorUpdateListener)
        mShadowAnimator.start()
    }

    override fun prepareEnd() {
        mShadowAnimator.removeAllUpdateListeners()
        mShadowAnimator.removeAllListeners()
        mShadowAnimator.repeatCount = 0
        mShadowAnimator.duration = 0
        mShadowAnimator.end()
    }

    override fun computeUpdateValue(animation: ValueAnimator, @FloatRange(from = 0.0, to = 1.0) animatedValue: Float) {
        mStartAngle = 360 * animatedValue
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mFullPaint.colorFilter = colorFilter
    }

    private fun cos(num: Int): Float {
        return kotlin.math.cos(num * Math.PI / 180).toFloat()
    }

    private fun sin(num: Int): Float {
        return kotlin.math.sin(num * Math.PI / 180).toFloat()
    }
}