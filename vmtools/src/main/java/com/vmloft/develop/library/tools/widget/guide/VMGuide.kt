package com.vmloft.develop.library.tools.widget.guide

import android.widget.FrameLayout
import android.app.Activity
import android.graphics.*
import android.view.View

import com.vmloft.develop.library.tools.R
import com.vmloft.develop.library.tools.utils.VMColor
import com.vmloft.develop.library.tools.widget.guide.VMGuideView.GuideListener

/**
 * 此引导组件参考自一下开源项目修改优化而来，感谢作者的无私开源
 * https://github.com/15018777629/guide
 *
 * Create by lzan13 on 2022/4/23
 * 描述：遮罩引导入口类
 */
class VMGuide private constructor() {

    // 根视图，通过当前 activity 获取，后续 GuideView 就是添加在这个视图之上
    private lateinit var rootView: FrameLayout

    // 遮罩引导视图
    private lateinit var guideView: VMGuideView

    // 引导回调
    private var guideListener: GuideListener? = null

    /**
     * 关闭引导层
     */
    fun close() {
        rootView.removeView(guideView)
    }

    /**
     * 展示引导层
     */
    fun show() {
        rootView.addView(guideView)
    }

    /**
     * 引导层构造器
     */
    class Builder(activity: Activity) {
        private var params = GuideParams(activity)

        /**
         * 设置引导层背景颜色
         */
        fun setBGColor(color: Int): Builder {
            params.bgColor = color
            return this
        }

        /**
         * 设置引导层除 targetView（需要挖洞的控件）外是否可以点击
         */
        fun setOutsideTouchable(outsideTouchable: Boolean): Builder {
            params.outsideTouchable = outsideTouchable
            return this
        }

        /**
         * 设置引导步骤是否一个接一个
         */
        fun setOneByOne(oneByOne: Boolean): Builder {
            params.oneByOne = oneByOne
            return this
        }

        /**
         * 设置引导层控件，单个targetView和多个互斥，那个后设置用哪个
         */
        fun setGuideView(view: GuideItem): Builder {
            params.guideItems.add(view)
            return this
        }

        /**
         * 设置引导层控件，多个 targetView 的参数集合和单个互斥，哪个后设置用哪个
         */
        fun setGuideViews(views: MutableList<GuideItem>): Builder {
            params.guideItems.addAll(views)
            return this
        }

        /**
         * 引导层宽度
         */
        fun setWidth(width: Int): Builder {
            params.width = width
            return this
        }

        /**
         * 引导层高度
         */
        fun setHeight(height: Int): Builder {
            params.height = height
            return this
        }

        /**
         * 设置引导层监听
         */
        fun setGuideListener(listener: GuideListener): Builder {
            params.guideListener = listener
            return this
        }

        /**
         * 遮罩引导构造器
         */
        fun build(): VMGuide {
            val guide = VMGuide()
            guide.rootView = params.activity.window.decorView as FrameLayout
            guide.guideView = VMGuideView(params)
            return guide
        }

    }
}

object VMShape {
    const val guideShapeRect = 0
    const val guideShapeCircle = 1
}

/**
 * 引导层总参数，包括引导逻辑，遮罩背景，引导控件集合等
 */
data class GuideParams(
    val activity: Activity,
    // 引导层宽度，不设置默认为match_parent
    var width: Int = 0,
    // 引导层高度，不设置默认为match_parent
    var height: Int = 0,
    // 是否按顺序一个引导接一个引导
    var oneByOne: Boolean = false,
    // 是否允许 targetView 外的事件
    var outsideTouchable: Boolean = true,
    // 引导层背景颜色
    var bgColor: Int = VMColor.byRes(R.color.vm_translucent),
    // 引导集合，oneByOne 顺序按这个执行
    var guideItems: MutableList<GuideItem> = mutableListOf(),
    // 引导回调
    var guideListener: GuideListener? = null,
) {}

/**
 * 引导参数 Item 包括目标控件，挖洞模式，引导文案及资源，偏移量等
 */
data class GuideItem(
    // 目标控件 引导遮罩蒙层以这个控件挖洞
    var targetView: View,
    // 引导文案，如果不设置 guideRes 则显示文字引导
    var guideTips: String = "",
    // 引导图资源文件Id
    var guideRes: Int = 0,
    // 引导控件挖洞的模式 0-矩形，1-圆形
    var shape: Int = VMShape.guideShapeRect,
    // 引导图在 X 轴上的偏移，虽然会自动根据 targetView 位置添加引导图，不能满足用户需求情况下提供这个属性进行引导图位置矫正
    var offX: Int = 0,
    // 引导图在 Y 轴上的偏移，虽然会自动根据 targetView 位置添加引导图，不能满足用户需求情况下提供这个属性进行引导图位置矫正
    var offY: Int = 0,

    /**
     * targetView 所处位置矩形信息，用来确定绘制引导图片位置，虽然会根据 targetView 位置自动调整
     * 如果 targetView 左边空间较大引导图绘制在 targetView 左边，否则相反
     * 如果 targetView 上边空间较大引导图绘制在 targetView 上边，否则反之
     */
    var rect: Rect = Rect(0, 0, 0, 0),
) {}