package com.vmloft.develop.library.tools.widget.behavior

import android.animation.ValueAnimator
import android.app.Service
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.util.TypedValue.*
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
import androidx.coordinatorlayout.widget.CoordinatorLayout.DefaultBehavior
import androidx.core.animation.doOnEnd
import androidx.core.view.ViewCompat
import androidx.core.view.children
import com.vmloft.develop.library.tools.R

import com.vmloft.develop.library.tools.widget.behavior.VMHeaderLayout.ScrollState
import com.vmloft.develop.library.tools.widget.behavior.transformation.*

/**
 * Create by lzan13 2021/07/14
 * 来自开源项目 https://github.com/imurluck/HeaderLayout
 *
 * 头部布局，定义了头部布局的五中状态， 状态信息请看[ScrollState],
 * 在滑动过程中会将滑动的一些信息传递给[Transformation],[Transformation]是子View需要设置的，
 * 设置了[Transformation]的子 View 会接收到[VMHeaderLayout]滑动时的一些信息，
 * 如滑动距离dy等。子 View 则可以根据这些信息来做相应的改变以达到联动的效果
 */
@DefaultBehavior(VMHeaderLayout.HeaderLayoutBehavior::class)
class VMHeaderLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr), CoordinatorLayout.AttachedBehavior {

    private val defaultExtendHeight = 200

    var maxHeight = 0

    var minHeight = 0

    var extendHeight = defaultExtendHeight

    var extendHeightFraction = 0.0f

    private var scrollState = ScrollState.stateMaxHeight

    var hasLayouted = false

    var onFlingMaxHeight: ((VMHeaderLayout) -> Unit)? = null

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.VMHeaderLayout)
        val extendHeightValue = TypedValue()
        val hasValue = a.getValue(R.styleable.VMHeaderLayout_vm_extend_height, extendHeightValue)
        if (hasValue) {
            extendHeightValue.apply {
                when (type) {
                    TYPE_FRACTION -> extendHeightFraction = getFraction(1.0f, 1.0f)
                    TYPE_FLOAT -> extendHeightFraction = float
                    TYPE_DIMENSION -> extendHeight = getDimension(getDisplayMetrics(context)).toInt()
                }
            }
        }
        a.recycle()
        post {
            hasLayouted = true
        }
    }

    private fun getDisplayMetrics(context: Context): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        (context.getSystemService(Service.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics
    }

    enum class ScrollState {
        /**
         * 收缩到了最小高度
         */
        stateMinHeight,

        /**
         * 在最小高度与最大高度之间
         */
        stateNormalProcess,

        /**
         * 伸展到了最大高度
         */
        stateMaxHeight,

        /**
         * 拓高，在最大高度与拓展最大高度之间
         */
        stateExtendProcess,

        /**
         * 到了拓展最大高度
         */
        stateExtendMaxEnd
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        maxHeight = measuredHeight
        if (extendHeightFraction != 0.0f) {
            extendHeight = (maxHeight * extendHeightFraction).toInt()
        }
        if (maxHeight == 0) {
            throw IllegalStateException("the height of HeaderLayout can't be 0")
        }

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (hasLayouted) {
            //表示是由requestLayout引发的，不需要重新布局子View
            return
        }
        super.onLayout(changed, left, top, right, bottom)
        minHeight = 0
        for (child in children) {
            (child.layoutParams as LayoutParams).apply {
                if (stickyUntilExit) {
                    minTop = minHeight
                    minHeight += child.height
                }
            }
        }
    }

    fun offsetChild(child: View, dy: Int) {
        (child.layoutParams as LayoutParams).apply {
            if (stickyUntilExit) {
                var unConsumedDy = dy
                if (dy < 0) {
                    if (minTopOffset != 0) {
                        if (dy + minTopOffset < 0) {
                            unConsumedDy = dy + minTopOffset
                            minTopOffset = 0
                        } else {
                            minTopOffset += dy
                            unConsumedDy = 0
                        }
                    }
                    if (unConsumedDy != 0) {
                        ViewCompat.offsetTopAndBottom(child, -unConsumedDy)
                    }
                } else {
                    if (child.top == minTop) {
                        minTopOffset += dy
                        unConsumedDy = 0
                    } else if (child.top - dy < minTop) {
                        unConsumedDy = child.top - minTop
                        minTopOffset = dy - (child.top - minTop)
                    }
                    ViewCompat.offsetTopAndBottom(child, -unConsumedDy)
                }
            } else {
                ViewCompat.offsetTopAndBottom(child, -dy)
            }
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams = LayoutParams(context, attrs)
    override fun generateDefaultLayoutParams(): LayoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
    override fun generateLayoutParams(lp: ViewGroup.LayoutParams?): ViewGroup.LayoutParams =
        LayoutParams(super.generateLayoutParams(lp))


    override fun getBehavior(): Behavior<*> = HeaderLayoutBehavior(context)

    private fun dispatchTransformationBehaviors(scrollState: ScrollState, dy: Int, percent: Float = 1.0f) {
        for (child in children) {
            val layoutParams = child.layoutParams as LayoutParams
            if (layoutParams.transformations != null && layoutParams.transformations!!.size > 0) {
                for (behavior in layoutParams.transformations!!) {
                    when (scrollState) {
                        ScrollState.stateMinHeight -> behavior.onStateMinHeight(child, this, dy)
                        ScrollState.stateNormalProcess -> behavior.onStateNormalProcess(child, this, percent, dy)
                        ScrollState.stateMaxHeight -> behavior.onStateMaxHeight(child, this, dy)
                        ScrollState.stateExtendProcess -> behavior.onStateExtendProcess(child, this, percent, dy)
                        ScrollState.stateExtendMaxEnd -> behavior.onStateExtendMaxEnd(child, this, dy)
                    }
                }
            }
        }
    }

    /**
     * 头部控件 behavior
     */
    open class HeaderLayoutBehavior(
        context: Context? = null,
        attrs: AttributeSet? = null,
    ) : Behavior<VMHeaderLayout>(context, attrs) {

        private var childUnConsumedDy = 0

        private var isBackAnimationDo = false

        private var backAnimation: ValueAnimator? = null

        private var canAcceptFling = false

        private var canAcceptFlingCallback = false

        private var canAcceptScroll = false

        override fun onStartNestedScroll(
            coordinatorLayout: CoordinatorLayout,
            child: VMHeaderLayout,
            directTargetChild: View,
            target: View,
            axes: Int,
            type: Int,
        ): Boolean {
            if (ViewCompat.SCROLL_AXIS_VERTICAL and axes != 0) {
                childUnConsumedDy = 0
                canAcceptFling = true
                canAcceptScroll = true
                resetBackAnimation()
                return true
            }

            return false
        }

        private fun resetBackAnimation() {
            backAnimation?.apply {
                if (isRunning) {
                    backAnimation!!.cancel()
                    isBackAnimationDo = false
                }
            }
        }

        override fun onLayoutChild(parent: CoordinatorLayout, child: VMHeaderLayout, layoutDirection: Int): Boolean {
            return if (child.hasLayouted) {
                // 第一次布局加载完成之后，因requestLayout造成的再次layout应是上一次的位置，
                // 需要保持滑动后的原样
                child.layout(child.left, child.top, child.right, child.bottom)
                true
            } else {
                super.onLayoutChild(parent, child, layoutDirection)
            }
        }

        override fun onNestedPreScroll(
            coordinatorLayout: CoordinatorLayout,
            child: VMHeaderLayout,
            target: View,
            dx: Int,
            dy: Int,
            consumed: IntArray,
            type: Int,
        ) {
            if (dy > 0 && child.scrollState > ScrollState.stateMinHeight) {
                //手指上滑时，HeaderLayout的scrollState大于最小高度状态才有效果
                consumed[1] = preScrollUp(child, dy)
            } else if (dy < 0 && childUnConsumedDy < 0 && canScrollDown()) {
                //手指下滑时，需要等target view滑到顶部且还有未消耗完的dy,才将滑动交给HeaderLayout处理
                if (type == ViewCompat.TYPE_NON_TOUCH && canAcceptFling) {
                    consumed[1] = preScrollDown(child, dy, true)
                } else if (type == ViewCompat.TYPE_TOUCH && canAcceptScroll) {
                    consumed[1] = preScrollDown(child, dy, false)
                }
            }
        }

        private fun canScrollDown(): Boolean {
            return !isBackAnimationDo
        }

        /**
         * 手指下滑
         */
        private fun preScrollDown(headerLayout: VMHeaderLayout, dy: Int, fling: Boolean): Int {
            if (headerLayout.scrollState >= ScrollState.stateExtendMaxEnd) {
                return 0
            }
            if (fling && headerLayout.scrollState >= ScrollState.stateMaxHeight) {
                return 0
            }
            var consumedDy = 0
            headerLayout.apply {
                //上滑到了最大拓展高度
                if (bottom - dy >= maxHeight + extendHeight) {
                    if (fling && Math.abs(dy) >= (maxHeight + extendHeight - minHeight)) {
                        bottom = maxHeight
                        consumedDy = bottom - minHeight
                        scrollState = ScrollState.stateMaxHeight
                        dispatchTransformationBehaviors(scrollState, consumedDy)
                    } else {
                        consumedDy = bottom - (maxHeight + extendHeight)
                        bottom = maxHeight + extendHeight
                        scrollState = ScrollState.stateExtendMaxEnd
                        dispatchTransformationBehaviors(scrollState, consumedDy)
                    }
                } else {
                    var unConsumedDy = dy
                    if (bottom < maxHeight && bottom - dy < maxHeight) {
                        consumedDy = dy
                        bottom -= dy
                        scrollState = ScrollState.stateNormalProcess
                        val percent = (bottom - minHeight).toFloat() / (maxHeight - minHeight).toFloat()
                        dispatchTransformationBehaviors(scrollState, consumedDy, percent)
                    }
                    if (bottom < maxHeight && bottom - dy >= maxHeight) {
                        consumedDy = bottom - maxHeight
                        unConsumedDy = dy - consumedDy
                        bottom = maxHeight
                        scrollState = ScrollState.stateMaxHeight
                        dispatchTransformationBehaviors(scrollState, consumedDy)
                    }
                    if (bottom >= maxHeight && bottom - unConsumedDy > maxHeight) {
                        if (fling) {
                            //告诉fling监听者，已fling到maxHeight处
                            if (canAcceptFlingCallback && headerLayout.onFlingMaxHeight != null) {
                                headerLayout.onFlingMaxHeight!!.invoke(headerLayout)
                                canAcceptFlingCallback = false
                            }
                            return 0
                            return@apply
                        }
                        consumedDy += unConsumedDy
                        bottom -= unConsumedDy
                        scrollState = ScrollState.stateExtendProcess
                        val percent = (bottom - maxHeight).toFloat() / extendHeight
                        dispatchTransformationBehaviors(scrollState, unConsumedDy, percent)
                    }
                }
            }
            return consumedDy
        }

        /**
         * 手指上滑
         */
        private fun preScrollUp(headerLayout: VMHeaderLayout, dy: Int): Int {
            if (headerLayout.scrollState == ScrollState.stateMinHeight) {
                return 0
            }
            var consumedDy: Int = 0
            headerLayout.apply {
                //上滑之后到了最小高度
                if (bottom - dy <= minHeight) {
                    consumedDy = bottom - minHeight
                    bottom = minHeight
                    scrollState = ScrollState.stateMinHeight
                    dispatchTransformationBehaviors(scrollState, consumedDy)
                } else {
                    var unConsumedDy = dy
                    //之前在最大伸展高度与拓展的高度之间，上滑之后还在此区间
                    if (bottom > maxHeight && bottom - dy > maxHeight) {
                        consumedDy = dy
                        bottom -= dy
                        val percent = (bottom - maxHeight).toFloat() / extendHeight.toFloat()
                        scrollState = ScrollState.stateExtendProcess
                        dispatchTransformationBehaviors(scrollState, consumedDy, percent)
                    }
                    //在最大伸展高度与拓展的高度之间, 上滑之后小于了最大高度
                    if (bottom > maxHeight && bottom - dy <= maxHeight) {
                        consumedDy = bottom - maxHeight
                        unConsumedDy = dy - consumedDy
                        bottom = maxHeight
                        scrollState = ScrollState.stateMaxHeight
                        dispatchTransformationBehaviors(scrollState, consumedDy)
                    }
                    //在最小高度和最大高度之间
                    if (bottom <= maxHeight && bottom - unConsumedDy < maxHeight) {
                        consumedDy += unConsumedDy
                        bottom -= unConsumedDy
                        val percent = (bottom - minHeight).toFloat() / (maxHeight - minHeight).toFloat()
                        scrollState = ScrollState.stateNormalProcess
                        dispatchTransformationBehaviors(scrollState, unConsumedDy, percent)
                    }
                }

            }
            return consumedDy
        }

        private fun backToMaxHeight(headerLayout: VMHeaderLayout) {
            if (headerLayout.scrollState <= ScrollState.stateMaxHeight) {
                return
            }
            isBackAnimationDo = true
            canAcceptFling = false
            canAcceptScroll = false
            backAnimation = ValueAnimator.ofFloat(headerLayout.bottom.toFloat(), headerLayout.maxHeight.toFloat()).apply {
                addUpdateListener { animator ->
                    val dy = (headerLayout.bottom - animator.animatedValue as Float).toInt()
                    preScrollUp(headerLayout, dy)
                }
                doOnEnd {
                    isBackAnimationDo = false
                }
                duration = 200L
            }
            backAnimation!!.start()
        }

        override fun onNestedPreFling(
            coordinatorLayout: CoordinatorLayout,
            child: VMHeaderLayout,
            target: View,
            velocityX: Float,
            velocityY: Float,
        ): Boolean {
            if (child.scrollState < ScrollState.stateMaxHeight) {
                canAcceptFlingCallback = true
            }
            return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)
        }

        override fun onNestedScroll(
            coordinatorLayout: CoordinatorLayout,
            child: VMHeaderLayout,
            target: View,
            dxConsumed: Int,
            dyConsumed: Int,
            dxUnconsumed: Int,
            dyUnconsumed: Int,
            type: Int,
        ) {
            childUnConsumedDy = dyUnconsumed
        }

        override fun onStopNestedScroll(
            coordinatorLayout: CoordinatorLayout,
            child: VMHeaderLayout,
            target: View,
            type: Int,
        ) {
            if (type == ViewCompat.TYPE_TOUCH) {
                backToMaxHeight(child)
            }
            if (type == ViewCompat.TYPE_NON_TOUCH) {
                canAcceptFlingCallback = false
            }
        }
    }

    class LayoutParams : FrameLayout.LayoutParams {

        private val transformationNothing = 0x00
        private val transformationScroll = 0x01
        private val transformationAlpha = 0x02
        private val transformationAlphaContrary = 0x04
        private val transformationExtendScale = 0x08
        private val transformationCommonToolbar = 0x10

        private var transformationFlags = transformationNothing
        private var customTransformation: String? = null

        var transformations: MutableList<Transformation<View>>? = null

        var minTop = 0

        var minTopOffset = 0

        var stickyUntilExit = false

        constructor(width: Int, height: Int) : super(width, height)
        constructor(width: Int, height: Int, gravity: Int) : super(width, height, gravity)
        constructor(source: ViewGroup.LayoutParams) : super(source)
        constructor(source: ViewGroup.MarginLayoutParams) : super(source)
        constructor(source: FrameLayout.LayoutParams) : super(source)

        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.VMHeaderLayout)
            transformationFlags = a.getInt(R.styleable.VMHeaderLayout_vm_transformation, 0x00)
            stickyUntilExit = a.getBoolean(R.styleable.VMHeaderLayout_vm_sticky_until_exit, false)
            customTransformation = a.getString(R.styleable.VMHeaderLayout_vm_custom_transformation)
            parseTransformationBehaviors(transformationFlags, customTransformation)
            a.recycle()
        }

        /**
         * 解析在xml中设置的transformation_behavior,解析成[Transformation]存储在[transformations]中，
         * 在behavior分发时会遍历[transformations]进行分发
         */
        private fun parseTransformationBehaviors(transformationFlags: Int, customTransformation: String?) {
            if (transformationFlags and transformationNothing != 0) {
                return
            }
            transformations = mutableListOf<Transformation<View>>().apply {
                if (transformationFlags and transformationAlpha != 0) {
                    add(AlphaTransformation())
                }
                if (transformationFlags and transformationExtendScale != 0) {
                    add(ExtendScaleTransformation())
                }
                if (transformationFlags and transformationAlphaContrary != 0) {
                    add(AlphaContraryTransformation())
                }
                if (transformationFlags and transformationScroll != 0) {
                    add(ScrollTransformation())
                }
                if (transformationFlags and transformationCommonToolbar != 0) {
                    add(CommonToolbarTransformation())
                }
                if (!TextUtils.isEmpty(customTransformation)) {
                    add(reflectCustomTransformation(customTransformation!!))
                }
            }
        }

        /**
         * 反射创建Transformation, 并且不捕获异常
         * @param customTransformation Transformation子类的全路径
         */
        private fun reflectCustomTransformation(customTransformation: String): Transformation<View> {
            val clazz = Class.forName(customTransformation)
            return clazz.getDeclaredConstructor().newInstance() as Transformation<View>
        }

    }

}