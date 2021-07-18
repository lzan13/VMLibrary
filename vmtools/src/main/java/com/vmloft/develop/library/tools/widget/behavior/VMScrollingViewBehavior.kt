package com.vmloft.develop.library.tools.widget.behavior

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.*

import com.vmloft.develop.library.tools.widget.behavior.VMHeaderLayout.ScrollState
import com.vmloft.develop.library.tools.widget.behavior.transformation.Transformation

/**
 * Create by lzan13 2021/07/14
 * 来自开源项目 https://github.com/unixzii/android-FancyBehaviorDemo
 * @author zzx
 *
 * 头部布局，定义了头部布局的五中状态， 状态信息请看[ScrollState],
 * 在滑动过程中会将滑动的一些信息传递给[Transformation],[Transformation]是子View需要设置的，
 * 设置了[Transformation]的子 View 会接收到[VMHeaderLayout]滑动时的一些信息，
 * 如滑动距离dy等。子 View 则可以根据这些信息来做相应的改变以达到联动的效果
 */
class VMScrollingViewBehavior<V : View>(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<V>(context, attrs) {

    private var scrollAnimation: ValueAnimator? = null

    override fun layoutDependsOn(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
        return if (dependency is VMHeaderLayout) {
            dependency.onFlingMaxHeight = {
                onFlingMaxHeight(it, child)
            }
            true
        } else {
            false
        }
    }

    /**
     * 子类可以重写这个方法来改变[headerLayout]fling到maxHeight时的行为
     * @param headerLayout {@see [VMHeaderLayout]]}
     * @param child 目标子View
     */
    fun onFlingMaxHeight(headerLayout: VMHeaderLayout, child: V) {
        scrollAnimation?.apply {
            if (isRunning) {
                cancel()
            }
        }
        scrollAnimation = ValueAnimator.ofInt(child.scrollY, -50, 0).apply {
            addUpdateListener {
                child.scrollY = it.animatedValue as Int
            }
            duration = 200
            interpolator = LinearInterpolator()
        }
        scrollAnimation!!.start()
    }

    override fun onLayoutChild(parent: CoordinatorLayout, child: V, layoutDirection: Int): Boolean {
        return layoutChild(parent, child, layoutDirection)
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
        offsetChildAsNeed(child, dependency)
        return false
    }

    override fun onMeasureChild(
        parent: CoordinatorLayout,
        child: V,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int,
    ): Boolean {
        val headerLayout = findFirstDependency(parent.getDependencies(child))
        if (headerLayout != null) {
            val headerLayoutParams = headerLayout.layoutParams as CoordinatorLayout.LayoutParams
            val childLayoutParams = child.layoutParams as CoordinatorLayout.LayoutParams
            val childWidthMeasureSpec = CoordinatorLayout.getChildMeasureSpec(
                parentWidthMeasureSpec,
                parent.paddingLeft + parent.paddingRight + child.marginLeft + child.marginRight,
                childLayoutParams.width
            )
            val childHeightMeasureSpec = CoordinatorLayout.getChildMeasureSpec(
                parentHeightMeasureSpec,
                parent.paddingTop + parent.paddingBottom + child.marginTop + child.marginBottom +
                        headerLayout.minHeight + headerLayoutParams.topMargin + headerLayoutParams.bottomMargin,
                childLayoutParams.height
            )
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
            return true
        }
        return false
    }

    private fun offsetChildAsNeed(child: V, dependency: View) {
        val dependencyBehavior = (dependency.layoutParams as CoordinatorLayout.LayoutParams).behavior
        if (dependencyBehavior is VMHeaderLayout.HeaderLayoutBehavior) {
            val dependencyParams = dependency.layoutParams as CoordinatorLayout.LayoutParams
            val childLayoutParams = child.layoutParams as CoordinatorLayout.LayoutParams
            val offset = (dependency.bottom + dependencyParams.bottomMargin +
                    childLayoutParams.topMargin) - child.top
            ViewCompat.offsetTopAndBottom(child, offset)
        }
    }

    private fun layoutChild(parent: CoordinatorLayout, child: V, layoutDirection: Int): Boolean {
        val headerLayout = findFirstDependency(parent.getDependencies(child))
        return if (headerLayout != null) {
            val headerLayoutParams = headerLayout.layoutParams as CoordinatorLayout.LayoutParams
            val childLayoutParams = child.layoutParams as CoordinatorLayout.LayoutParams
            val out = Rect()
            out.left = childLayoutParams.leftMargin
            out.top = headerLayout.bottom + headerLayoutParams.bottomMargin + childLayoutParams.topMargin
            out.right = out.left + child.measuredWidth
            out.bottom = out.top + child.measuredHeight
            child.layout(out.left, out.top, out.right, out.bottom)
            true
        } else {
            super.onLayoutChild(parent, child, layoutDirection)
        }
    }


    private fun findFirstDependency(dependencies: List<View>): VMHeaderLayout? {
        for (dependency in dependencies) {
            if (dependency is VMHeaderLayout) {
                return dependency
            }
        }
        return null
    }
}