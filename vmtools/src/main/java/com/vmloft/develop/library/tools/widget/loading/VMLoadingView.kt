package com.vmloft.develop.library.tools.widget.loading

import android.R
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView


/**
 * Create by lzan13 on 2020/6/28 17:45
 * 描述：
 */
class VMLoadingView constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var mZLoadingDrawable: ZLoadingDrawable? = null
    protected var mZLoadingBuilder: ZLoadingBuilder? = null

    fun ZLoadingView(context: Context?) {
        this(context, null)
    }

    fun ZLoadingView(context: Context?, attrs: AttributeSet?) {
        this(context, attrs, -1)
    }

    fun ZLoadingView(context: Context, attrs: AttributeSet, defStyleAttr: Int) {
        super(context, attrs, defStyleAttr)
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        try {
            val ta: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ZLoadingView)
            val typeId = ta.getInt(R.styleable.ZLoadingView_z_type, 0)
            val color = ta.getColor(R.styleable.ZLoadingView_z_color, Color.BLACK)
            val durationTimePercent = ta.getFloat(R.styleable.ZLoadingView_z_duration_percent, 1.0f)
            ta.recycle()
            setLoadingBuilder(Z_TYPE.values().get(typeId), durationTimePercent.toDouble())
            setColorFilter(color)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setLoadingBuilder(builder: Z_TYPE) {
        mZLoadingBuilder = builder.newInstance()
        initZLoadingDrawable()
    }

    fun setLoadingBuilder(builder: Z_TYPE, durationPercent: Double) {
        this.setLoadingBuilder(builder)
        initDurationTimePercent(durationPercent)
    }

    private fun initZLoadingDrawable() {
        if (mZLoadingBuilder == null) {
            throw RuntimeException("mZLoadingBuilder is null.")
        }
        mZLoadingDrawable = ZLoadingDrawable(mZLoadingBuilder)
        mZLoadingDrawable.initParams(getContext())
        setImageDrawable(mZLoadingDrawable)
    }

    private fun initDurationTimePercent(durationPercent: Double) {
        if (mZLoadingBuilder == null) {
            throw RuntimeException("mZLoadingBuilder is null.")
        }
        mZLoadingBuilder.setDurationTimePercent(durationPercent)
    }

    protected fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation()
    }

    protected fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimation()
    }

    protected fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        val visible = visibility == VISIBLE && getVisibility() === VISIBLE
        if (visible) {
            startAnimation()
        } else {
            stopAnimation()
        }
    }

    private fun startAnimation() {
        if (mZLoadingDrawable != null) {
            mZLoadingDrawable.start()
        }
    }

    private fun stopAnimation() {
        if (mZLoadingDrawable != null) {
            mZLoadingDrawable.stop()
        }
    }
}