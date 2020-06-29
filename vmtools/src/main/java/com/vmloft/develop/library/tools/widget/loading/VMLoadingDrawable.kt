package com.vmloft.develop.library.tools.widget.loading

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.solver.widgets.WidgetContainer.getBounds


/**
 * Create by lzan13 on 2020/6/28 17:48
 * 描述：自定义实现 Loading 控件绘制类
 */
class VMLoadingDrawable constructor(builder: VMLoadingBuilder) : Drawable(), Animatable {

    private lateinit var mBuilder: VMLoadingBuilder

    init {
        mBuilder = builder
        mBuilder.setCallback(object : Callback {
            override fun unscheduleDrawable(who: Drawable, what: Runnable) {
                unscheduleSelf(what)
            }

            override fun invalidateDrawable(who: Drawable) {
                invalidateSelf()
            }

            override fun scheduleDrawable(who: Drawable, what: Runnable, time: Long) {
                scheduleSelf(what, time)
            }
        })
    }

    fun initParams(context: Context) {
        mBuilder.init(context)
        mBuilder.initParams(context)
    }

    override fun draw(canvas: Canvas) {
        if (!getBounds().isEmpty()) {
            mBuilder.draw(canvas)
        }
    }

    override fun setAlpha(alpha: Int) {
        mBuilder.setAlpha(alpha)
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mBuilder.setColorFilter(colorFilter)
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun start() {
        mBuilder.start()
    }

    override fun stop() {
        mBuilder.stop()
    }

    override fun isRunning(): Boolean {
        return mBuilder.isRunning()
    }

    override fun getIntrinsicHeight(): Int {
        return mBuilder.getIntrinsicHeight().toInt()
    }

    override fun getIntrinsicWidth(): Int {
        return mBuilder.getIntrinsicWidth().toInt()
    }
}