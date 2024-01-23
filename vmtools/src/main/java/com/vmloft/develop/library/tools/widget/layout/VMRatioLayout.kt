package com.vmloft.develop.library.tools.widget.layout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout

import com.vmloft.develop.library.tools.R.styleable

/**
 * Create by lzan13 on 2019/9/20 18:50
 *
 * 自定义正方形布局s
 */
class VMRatioLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {
    // 正方形布局大小为统一宽度
    private var ratio = 1.0f

    // 跟随宽度计算
    private var isFollowWidth = true

    init {
        handleAttrs(context, attrs)
    }

    /**
     * 获取资源属性
     */
    private fun handleAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        val array = context.obtainStyledAttributes(attrs, styleable.VMRatioLayout)
        ratio = array.getFloat(styleable.VMRatioLayout_vm_layout_ratio, ratio)
        isFollowWidth = array.getBoolean(styleable.VMRatioLayout_vm_follow_width, isFollowWidth)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(View.getDefaultSize(0, widthMeasureSpec), View.getDefaultSize(0, heightMeasureSpec))
        var childWidthSize = measuredWidth
        var childHeightSize = measuredHeight
        // 根据属性计算高度和宽度
        if (isFollowWidth) {
            childHeightSize = (childWidthSize * ratio).toInt()
        } else {
            childWidthSize = (childHeightSize * ratio).toInt()
        }
        var realWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY)
        var realHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY)

        //设定宽高比例
        super.onMeasure(realWidthMeasureSpec, realHeightMeasureSpec)
    }

    /**
     * 设置比例
     */
    fun setRatio(ratio: Float) {
        this.ratio = ratio
        requestLayout()
    }

    /**
     * 设置跟随宽度
     */
    fun setFollowWidth(unify: Boolean) {
        isFollowWidth = unify
        requestLayout()
    }
}