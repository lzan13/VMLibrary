package com.vmloft.develop.library.tools.widget.loading


import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable


/**
 * Create by lzan13 on 2020/6/28 17:48
 * 描述：自定义实现 Loading 控件绘制类
 */
class VMLoadingDrawable constructor(builder: VMLoadingBuilder) : Drawable(), Animatable, Drawable.Callback {

    private var mBuilder: VMLoadingBuilder = builder

    init {
        mBuilder.setDrawable(this)
    }

    fun initParams() {
        mBuilder.init()
        mBuilder.initParams()
    }

    /**
     * --------------------------------------------------------------
     * Drawable 方法
     */
    override fun draw(canvas: Canvas) {
        if (!bounds.isEmpty) {
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

    override fun getIntrinsicHeight(): Int {
        return mBuilder.getIntrinsicHeight()
    }

    override fun getIntrinsicWidth(): Int {
        return mBuilder.getIntrinsicWidth()
    }

    /**
     * --------------------------------------------------------------
     * Animatable 接口
     */
    override fun start() {
        mBuilder.start()
    }

    override fun stop() {
        mBuilder.stop()
    }

    override fun isRunning(): Boolean {
        return mBuilder.isRunning()
    }

    /**
     * --------------------------------------------------------------
     * Drawable.Callback 接口
     */
    override fun unscheduleDrawable(who: Drawable, what: Runnable) {
        unscheduleSelf(what)
    }

    override fun invalidateDrawable(who: Drawable) {
        invalidateSelf()
    }

    override fun scheduleDrawable(who: Drawable, what: Runnable, time: Long) {
        scheduleSelf(what, time)
    }

}