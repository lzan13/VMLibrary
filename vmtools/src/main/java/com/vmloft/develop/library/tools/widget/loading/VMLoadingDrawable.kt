package com.vmloft.develop.library.tools.widget.loading

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import androidx.constraintlayout.solver.widgets.WidgetContainer.getBounds


/**
 * Create by lzan13 on 2020/6/28 17:48
 * 描述：
 */
class VMLoadingDrawable : Drawable(), Animatable {

    private var mBuilder: VMLoadingBuilder? = null

    fun VMLoadingDrawable(builder: VMLoadingBuilder?) {
        mBuilder = builder
        mBuilder?.setCallback(object : Callback{
//            override fun invalidateDrawable(d: Drawable?) {
//                invalidateSelf()
//            }
//
//            override fun scheduleDrawable(d: Drawable?, what: Runnable?, time: Long) {
//                scheduleSelf(what, time)
//            }
//
//            override fun unscheduleDrawable(d: Drawable?, what: Runnable?) {
//                unscheduleSelf(what)
//            }

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
        if (mBuilder != null) {
            mBuilder?.init(context)
            mBuilder?.initParams(context)
        }
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
        return mBuilder.getIntrinsicHeight()
    }

    override fun getIntrinsicWidth(): Int {
        return mBuilder.getIntrinsicWidth()
    }
}