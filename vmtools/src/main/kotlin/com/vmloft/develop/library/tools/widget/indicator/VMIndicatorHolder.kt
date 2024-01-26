package com.vmloft.develop.library.tools.widget.indicator

import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable

/**
 * Created by lzan13 on 2019/04/10
 *
 * 自定义指示器小圆点对象
 */
class VMIndicatorHolder(var shape: ShapeDrawable) {
    var x = 0f
    var y = 0f
    var color = 0
        set(value) {
            shape.paint.color = value
            field = value
        }
    var paint: Paint? = null
    private var alpha = 1f

    fun setAlpha(alpha: Float) {
        this.alpha = alpha
        shape.alpha = (alpha * 255f + .5f).toInt()
    }

    var width: Float
        get() = shape.shape.width
        set(width) {
            val s = shape.shape
            s.resize(width, s.height)
        }

    var height: Float
        get() = shape.shape.height
        set(height) {
            val s = shape.shape
            s.resize(s.width, height)
        }

    fun resizeShape(width: Float, height: Float) {
        shape.shape.resize(width, height)
    }

}