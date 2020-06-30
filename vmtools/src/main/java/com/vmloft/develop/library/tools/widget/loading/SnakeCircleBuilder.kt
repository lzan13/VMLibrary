package com.vmloft.develop.library.tools.widget.loading

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.*
import androidx.annotation.FloatRange


/**
 * Create by lzan13 on 2020/6/30 15:24
 * 描述：圈 蛇加载动画
 */
class SnakeCircleBuilder : VMLoadingBuilder() {

    //最终阶段
    private val FINAL_STATE = 1
    //当前动画阶段
    private var mCurrAnimatorState = 0
    private lateinit var mStrokePaint: Paint
    private var mOuterRF = 0f
    private var mInterRF = 0f
    private lateinit var mOuterCircleRectF: RectF
    private lateinit var mInterCircleRectF: RectF
    private var mAlpha = 255
    //旋转角度
    private var mRotateAngle = 0f
    private var mAntiRotateAngle = 0f
    //路径
    private lateinit var mPath: Path
    private lateinit var mPathMeasure: PathMeasure
    private lateinit var mDrawPath: Path

    override fun initParams() {
        //最大尺寸
        mOuterRF = getMaxSize() * 1.0f
        //小圆尺寸
        mInterRF = mOuterRF * 0.7f
        //初始化画笔
        initPaint(mInterRF * 0.4f)
        //旋转角度
        mRotateAngle = 0f
        //圆范围
        mOuterCircleRectF = RectF()
        mOuterCircleRectF[getViewCenterX() - mOuterRF, getViewCenterY() - mOuterRF, getViewCenterX() + mOuterRF] = getViewCenterY() + mOuterRF
        mInterCircleRectF = RectF()
        mInterCircleRectF[getViewCenterX() - mInterRF, getViewCenterY() - mInterRF, getViewCenterX() + mInterRF] = getViewCenterY() + mInterRF
        initPathMeasure()
        initPaths()
    }

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

    private fun initPathMeasure() {
        mDrawPath = Path()
        mPathMeasure = PathMeasure()
    }

    private fun initPaths() { //中心折线
        mPath = Path()
        val pointOffset = mOuterRF * 0.3f
        val pointOffsetHalf = mOuterRF * 0.3f * 0.5f
        mPath.moveTo(getViewCenterX() - mOuterRF * 0.8f, getViewCenterY())
        mPath.lineTo(getViewCenterX() - pointOffset, getViewCenterY())
        mPath.lineTo(getViewCenterX() - pointOffsetHalf, getViewCenterY() + pointOffsetHalf)
        mPath.lineTo(getViewCenterX() + pointOffsetHalf, getViewCenterY() - pointOffsetHalf)
        mPath.lineTo(getViewCenterX() + pointOffset, getViewCenterY())
        mPath.lineTo(getViewCenterX() + mOuterRF * 0.8f, getViewCenterY())
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        //外圆
        mStrokePaint.strokeWidth = mOuterRF * 0.05f
        mStrokePaint.alpha = (mAlpha * 0.6f).toInt()
        canvas.drawCircle(getViewCenterX(), getViewCenterY(), mOuterRF, mStrokePaint)
        canvas.drawCircle(getViewCenterX(), getViewCenterY(), mInterRF, mStrokePaint)
        canvas.restore()
        canvas.save()
        mStrokePaint.strokeWidth = mOuterRF * 0.1f
        mStrokePaint.alpha = mAlpha
        canvas.rotate(mRotateAngle, getViewCenterX(), getViewCenterY())
        canvas.drawArc(mOuterCircleRectF, 0f, 120f, false, mStrokePaint)
        canvas.drawArc(mOuterCircleRectF, 180f, 120f, false, mStrokePaint)
        canvas.restore()
        // 蛇
        canvas.save()
        mStrokePaint.alpha = (mAlpha * 0.6f).toInt()
        canvas.drawPath(mDrawPath, mStrokePaint)
        canvas.restore()
        canvas.save()
        mStrokePaint.strokeWidth = mOuterRF * 0.1f
        mStrokePaint.alpha = mAlpha
        canvas.rotate(mAntiRotateAngle, getViewCenterX(), getViewCenterY())
        canvas.drawArc(mInterCircleRectF, 60f, 60f, false, mStrokePaint)
        canvas.drawArc(mInterCircleRectF, 180f, 180f, false, mStrokePaint)
        canvas.restore()
    }

    override fun setAlpha(alpha: Int) {
        mAlpha = alpha
    }

    override fun prepareStart(floatValueAnimator: ValueAnimator) {}

    override fun prepareEnd() {}

    override fun computeUpdateValue(animation: ValueAnimator, @FloatRange(from = 0.0, to = 1.0) animatedValue: Float) {
        mRotateAngle = 360 * animatedValue
        mAntiRotateAngle = 360 * (1 - animatedValue)
        var start = 0f
        var stop = 0f
        when (mCurrAnimatorState) {
            0 -> {
                resetDrawPath()
                mPathMeasure.setPath(mPath, false)
                stop = mPathMeasure.length * animatedValue
                mPathMeasure.getSegment(start, stop, mDrawPath, true)
            }
            1 -> {
                resetDrawPath()
                mPathMeasure.setPath(mPath, false)
                stop = mPathMeasure.length
                start = mPathMeasure.length * animatedValue
                mPathMeasure.getSegment(start, stop, mDrawPath, true)
            }
            else -> {
            }
        }
    }

    override fun onAnimationRepeat(animation: Animator) {
        if (++mCurrAnimatorState > FINAL_STATE) { //还原到第一阶段
            mCurrAnimatorState = 0
        }
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mStrokePaint.colorFilter = colorFilter
    }

    private fun resetDrawPath() {
        mDrawPath.reset()
        mDrawPath.lineTo(0f, 0f)
    }
}