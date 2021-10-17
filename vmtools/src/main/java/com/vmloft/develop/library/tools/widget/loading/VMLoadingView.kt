package com.vmloft.develop.library.tools.widget.loading

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView

import com.vmloft.develop.library.tools.R


/**
 * Create by lzan13 on 2020/6/28 17:45
 * 描述：自定义 Loading 控件
 */
class VMLoadingView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr) {

    private lateinit var mDrawable: VMLoadingDrawable
    private lateinit var mBuilder: VMLoadingBuilder


    init {
        try {
            val ta: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.VMLoadingView)
            val type = ta.getInt(R.styleable.VMLoadingView_vm_loading_type, 0)
            val color = ta.getColor(R.styleable.VMLoadingView_vm_loading_color, Color.BLACK)
            val speed = ta.getFloat(R.styleable.VMLoadingView_vm_loading_speed, 1.0f)
            ta.recycle()

            initLoadingDrawable(type, speed)

            setColorFilter(color)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 初始化 Loading 绘制
     */
    private fun initLoadingDrawable(type: Int, speed:Float) {
        mBuilder = when (type) {
            0 -> DoubleCircleBuilder()
            1 -> PacmanBuilder()
            2 -> SnakeCircleBuilder()
            3 -> StarBuilder()
            4 -> TextBuilder()
            else -> DoubleCircleBuilder()
        }
        mBuilder.setSpeed(speed)

        mDrawable = VMLoadingDrawable(mBuilder)
        mDrawable.initParams()
        setImageDrawable(mDrawable)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimation()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        val visible = (visibility == VISIBLE && getVisibility() == VISIBLE)
        if (visible) {
            startAnimation()
        } else {
            stopAnimation()
        }
        invalidate()
    }

    private fun startAnimation() {
        mDrawable.start()
    }

    private fun stopAnimation() {
        mDrawable.stop()
    }
}