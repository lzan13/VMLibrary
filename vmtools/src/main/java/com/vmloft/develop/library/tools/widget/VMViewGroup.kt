package com.vmloft.develop.library.tools.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.annotation.AttrRes

/**
 * Created by lzan13 on 2017/3/25
 *
 * 自定义ViewGroup类，会根据子控件的宽度自动换行，
 */
class VMViewGroup @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {
    private lateinit var childView: View
    private var childListener: OnChildListener? = null
    private var position = 0

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        //子控件的个数
        val count = childCount
        //ViewParent宽度(包含padding)
        val width = width
        //ViewParent 的右边x的布局限制值
        val rightLimit = width - paddingRight

        //存储基准的left top (子类.layout(),里的坐标是基于父控件的坐标，所以 x应该是从0+父控件左内边距开始，y从0+父控件上内边距开始)
        val baseLeft = 0 + paddingLeft
        val baseTop = 0 + paddingTop
        //存储现在的left top
        var curLeft = baseLeft
        var curTop = baseTop

        //子View
        var child: View
        //子view用于layout的 l t r b
        var viewL: Int
        var viewT: Int
        var viewR: Int
        var viewB: Int
        //子View的LayoutParams
        var params: MarginLayoutParams
        //子View Layout需要的宽高(包含margin)，用于计算是否越界
        var childWidth: Int
        var childHeight: Int
        //子View 本身的宽高
        var childW: Int
        var childH: Int

        //临时增加一个temp 存储上一个View的高度 解决过长的两行View导致显示不正确的bug
        var lastChildHeight = 0
        //
        for (i in 0 until count) {
            child = getChildAt(i)
            //如果gone，不布局了
            if (View.GONE == child.visibility) {
                continue
            }
            //获取子View本身的宽高:
            childW = child.measuredWidth
            childH = child.measuredHeight
            //获取子View的LayoutParams，用于获取其margin
            params = child.layoutParams as MarginLayoutParams
            //子View需要的宽高 为 本身宽高+marginLeft + marginRight
            childWidth = childW + params.leftMargin + params.rightMargin
            childHeight = childH + params.topMargin + params.bottomMargin

            //这里要考虑padding，所以右边界为 ViewParent宽度(包含padding) -ViewParent右内边距
            if (curLeft + childWidth > rightLimit) {
                //如果当前行已经放不下该子View了 需要换行放置：
                //在新的一行布局子View，左x就是baseLeft，上y是 top +前一行高(这里假设的是每一行行高一样)，
                curTop = curTop + lastChildHeight
                //layout时要考虑margin
                viewL = baseLeft + params.leftMargin
                viewT = curTop + params.topMargin
                viewR = viewL + childW
                viewB = viewT + childH
                //child.layout(baseLeft + params.leftMargin, curTop + params.topMargin, baseLeft + params.leftMargin + child.getMeasuredWidth(), curTop + params.topMargin + child.getMeasuredHeight());
                //Log.i(TAG,"新的一行:" +"   ,baseLeft:"+baseLeft +"  curTop:"+curTop+"  baseLeft+childWidth:"+(baseLeft+childWidth)+"  curTop+childHeight:"+ ( curTop+childHeight));
                curLeft = baseLeft + childWidth
            } else {
                //当前行可以放下子View:
                viewL = curLeft + params.leftMargin
                viewT = curTop + params.topMargin
                viewR = viewL + childW
                viewB = viewT + childH

                //child.layout(curLeft + params.leftMargin, curTop + params.topMargin, curLeft + params.leftMargin + child.getMeasuredWidth(), curTop + params.topMargin + child.getMeasuredHeight());
                //Log.i(TAG,"当前行:"+changed +"   ,curLeft:"+curLeft +"  curTop:"+curTop+"  curLeft+childWidth:"+(curLeft+childWidth)+"  curTop+childHeight:"+(curTop+childHeight));
                curLeft = curLeft + childWidth
            }
            lastChildHeight = childHeight
            //布局子View
            child.layout(viewL, viewT, viewR, viewB)
        }
    }

    /**
     * 重写onMeasure方法，这里循环设置当前自定义控件的子控件的大小
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //获取系统传递过来测量出的宽度 高度，以及相应的测量模式。
        //如果测量模式为 EXACTLY( 确定的dp值，match_parent)，则可以调用setMeasuredDimension()设置，
        //如果测量模式为 AT_MOST(wrap_content),则需要经过计算再去调用setMeasuredDimension()设置
        val widthMeasure = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMeasure = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        //计算宽度 高度 //wrap_content测量模式下会使用到:
        //存储最后计算出的宽度，
        var maxLineWidth = 0
        //存储最后计算出的高度
        var totalHeight = 0
        //存储当前行的宽度
        var curLineWidth = 0
        //存储当前行的高度
        var curLineHeight = 0

        // 得到内部元素的个数
        val count = childCount

        //存储子View
        var child: View
        //存储子View的LayoutParams
        var params: MarginLayoutParams
        //子View Layout需要的宽高(包含margin)，用于计算是否越界
        var childWidth: Int
        var childHeight: Int

        //遍历子View 计算父控件宽高
        for (i in 0 until count) {
            child = getChildAt(i)
            //如果gone，不测量了
            if (View.GONE == child.visibility) {
                continue
            }
            //先测量子View
            measureChild(child, widthMeasureSpec, heightMeasureSpec)

            //获取子View的LayoutParams，(子View的LayoutParams的对象类型，取决于其ViewGroup的generateLayoutParams()方法的返回的对象类型，这里返回的是MarginLayoutParams)
            params = child.layoutParams as MarginLayoutParams
            //子View需要的宽度 为 子View 本身宽度+marginLeft + marginRight
            childWidth = child.measuredWidth + params.leftMargin + params.rightMargin
            childHeight = child.measuredHeight + params.topMargin + params.bottomMargin

            //如果当前的行宽度大于 父控件允许的最大宽度 则要换行
            //父控件允许的最大宽度 如果要适配 padding 这里要- getPaddingLeft() - getPaddingRight()
            //即为测量出的宽度减去父控件的左右边距
            if (curLineWidth + childWidth > widthMeasure - paddingLeft - paddingRight) {
                //通过比较 当前行宽 和以前存储的最大行宽,得到最新的最大行宽,用于设置父控件的宽度
                maxLineWidth = Math.max(maxLineWidth, curLineWidth)
                //父控件的高度增加了，为当前高度+当前行的高度
                totalHeight += curLineHeight
                //换行后 刷新 当前行 宽高数据： 因为新的一行就这一个View，所以为当前这个view占用的宽高(要加上View 的 margin)
                curLineWidth = childWidth
                curLineHeight = childHeight
            } else {
                //不换行：叠加当前行宽 和 比较当前行高:
                curLineWidth += childWidth
                curLineHeight = Math.max(curLineHeight, childHeight)
            }
            //如果已经是最后一个View,要比较当前行的 宽度和最大宽度，叠加一共的高度
            if (i == count - 1) {
                maxLineWidth = Math.max(maxLineWidth, curLineWidth)
                totalHeight += childHeight
            }
        }

        //适配padding,如果是wrap_content,则除了子控件本身占据的控件，还要在加上父控件的padding
        setMeasuredDimension(
            if (widthMode != MeasureSpec.EXACTLY) maxLineWidth + paddingLeft + paddingRight else widthMeasure, if (heightMode != MeasureSpec.EXACTLY) totalHeight + paddingTop + paddingBottom else heightMeasure
        )
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateLayoutParams(lp: LayoutParams): LayoutParams {
        return MarginLayoutParams(lp)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    /**
     * 设置子控件的点击监听
     */
    fun setChileOnClick(listener: OnChildListener?) {
        childListener = listener
        for (i in 0 until childCount) {
            childView = getChildAt(i)
            position = i
            childView.setOnClickListener { childListener?.onItemClick(childView, position) }
        }
    }

    interface OnChildListener {
        fun onItemClick(view: View?, position: Int)
    }
}