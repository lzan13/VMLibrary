package com.vmloft.develop.library.tools.utils.behavior

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.MotionEvent.PointerCoords
import android.view.MotionEvent.PointerProperties
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.core.view.NestedScrollingParent2
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat
import com.vmloft.develop.library.tools.R

/**
 * Create by lzan13 2021/01/05
 * 自定义联动 Layout
 */
class VMBehaviorFrameLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : FrameLayout(context, attrs, defStyle), NestedScrollingParent2 {
    private var mHeaderView: View? = null
    private var mScrollingParentHelper: NestedScrollingParentHelper
    private var mNeedHackDispatchTouch = false
    private var mTouchDownOnHeader = false
    private var mRevertAnimation: ValueAnimator? = null
    private var mMinHeaderHeight = 0
    private var mOldTop = 0

    // recyclerView 现在 loadMore 需要特别处理
    var canScrollUp = true

    private var mHeaderScrollListener: HeaderScrollListener? = null
    private var mNeedDragOver = false
    private var mStickHeaderHeight = 0
    private var mMaxHeaderHeight = 0
    private var mOnHeaderClickListener: OnClickListener? = null

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.VMBehaviorLayout, 0, 0)
        mNeedDragOver = a.getBoolean(R.styleable.VMBehaviorLayout_vm_drag_over, false)
        mStickHeaderHeight = a.getDimensionPixelSize(R.styleable.VMBehaviorLayout_vm_stick_section_height, 0)
        a.recycle()
        val gestureDetector = GestureDetector(getContext(), object : SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean {
                return mTouchDownOnHeader
            }

            override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
                if (mTouchDownOnHeader) {
                    mNeedHackDispatchTouch = true
                    this@VMBehaviorFrameLayout.dispatchTouchEvent(e1)
                    this@VMBehaviorFrameLayout.dispatchTouchEvent(e2)
                }
                return mTouchDownOnHeader
            }

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                if (mOnHeaderClickListener != null) {
                    mOnHeaderClickListener?.onClick(mHeaderView)
                    return true
                }
                return super.onSingleTapConfirmed(e)
            }
        })
        mScrollingParentHelper = NestedScrollingParentHelper(this)

        setOnTouchListener { v, event -> gestureDetector.onTouchEvent(event) }
    }

    fun reset() {
        scrollByOffsetTop(top)
    }

    fun setNeedDragOver(needDragOver: Boolean) {
        mNeedDragOver = needDragOver
    }

    fun setHeaderScrollListener(headerScrollListener: HeaderScrollListener) {
        mHeaderScrollListener = headerScrollListener
    }

    fun setStickHeaderHeight(height: Int) {
        mStickHeaderHeight = height
    }

    fun setMaxHeaderHeight(maxHeaderHeight: Int) {
        mMaxHeaderHeight = maxHeaderHeight
        if (mMinHeaderHeight != 0) {
            onMaxHeaderHeightChange(maxHeaderHeight)
        }
    }

    fun setOnHeaderClickListener(onHeaderClickListener: OnClickListener) {
        mOnHeaderClickListener = onHeaderClickListener
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.actionMasked
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mNeedHackDispatchTouch = false
            if (mNeedDragOver && scrollY < 0) {
                if (mRevertAnimation == null || !mRevertAnimation!!.isRunning) {
                    mRevertAnimation = getRevertAnimation()
                    mRevertAnimation?.start()
                }
                return true
            }
        } else if (action == MotionEvent.ACTION_DOWN) {
            mTouchDownOnHeader = ev.y < getMaxNeedHideHeight()
        }
        return if (mTouchDownOnHeader && mNeedHackDispatchTouch) {
            super.dispatchTouchEvent(obtainNewMotionEvent(ev))
        } else super.dispatchTouchEvent(ev)
    }

    private fun getRevertAnimation(): ObjectAnimator {
        val animator: ObjectAnimator = ObjectAnimator.ofInt(this, "scrollY", scrollY, 0)
        animator.duration = 300
        animator.interpolator = DecelerateInterpolator()
        return animator
    }

    private fun obtainNewMotionEvent(event: MotionEvent): MotionEvent? {
        val pointerProperties = arrayOfNulls<PointerProperties>(event.pointerCount)
        for (i in 0 until event.pointerCount) {
            pointerProperties[i] = PointerProperties()
            event.getPointerProperties(i, pointerProperties[i])
        }
        val pointerCoords = arrayOfNulls<PointerCoords>(event.pointerCount)
        for (i in 0 until event.pointerCount) {
            pointerCoords[i] = PointerCoords()
            event.getPointerCoords(i, pointerCoords[i])
            pointerCoords[i]!!.y = pointerCoords[i]!!.y + mMinHeaderHeight
        }
        return MotionEvent.obtain(event.downTime, event.eventTime, event.action, event.pointerCount, pointerProperties, pointerCoords, event.metaState, event.buttonState, event.xPrecision, event.yPrecision, event.deviceId, event.edgeFlags, event.source, event.flags)
    }

    private fun onMaxHeaderHeightChange(maxHeaderHeight: Int) {
        mHeaderView!!.layoutParams.height = maxHeaderHeight
        (mHeaderView!!.layoutParams as MarginLayoutParams).topMargin = mMinHeaderHeight - maxHeaderHeight
        setHeaderViewPaddingTop(getMaxDragOverHeight())
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mHeaderView = getChildAt(0)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (mOldTop != 0) {
            offsetTopAndBottom(mOldTop - t)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (mMinHeaderHeight == 0) {
            mHeaderView!!.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            mMinHeaderHeight = mHeaderView!!.layoutParams.height
            if (mMaxHeaderHeight != 0 && mNeedDragOver) {
                onMaxHeaderHeightChange(mMaxHeaderHeight)
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec + getMaxNeedHideHeight())
    }

    override fun scrollTo(x: Int, y: Int) {
        var y = y
        if (y < -getMaxDragOverHeight()) {
            y = -getMaxDragOverHeight()
        }
        if (y > 0) {
            y = 0
        }
        super.scrollTo(x, y)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mRevertAnimation?.cancel()
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int): Boolean {
        return onStartNestedScroll(child, target, axes, ViewCompat.TYPE_TOUCH)
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
        onNestedScrollAccepted(child, target, axes, ViewCompat.TYPE_TOUCH)
    }

    override fun onStopNestedScroll(target: View) {
        onStopNestedScroll(target, ViewCompat.TYPE_TOUCH)
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {}

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        onNestedPreScroll(target, dx, dy, consumed, ViewCompat.TYPE_TOUCH)
    }

    override fun onNestedFling(target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return false
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        return false
    }

    override fun getNestedScrollAxes(): Int {
        return mScrollingParentHelper.nestedScrollAxes
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int, type: Int): Boolean {
        return nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedScrollAccepted(child: View, target: View, nestedScrollAxes: Int, type: Int) {
        mScrollingParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes, type)
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        mScrollingParentHelper.onStopNestedScroll(target, type)
        canScrollUp = true
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {}

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if (dy > 0) {
            if (top > -getMaxNeedHideHeight()) {
                consumed[1] = dy
                if (scrollY < 0) {
                    scrollBy(0, dy)
                } else {
                    scrollByOffsetTop(dy)
                }
            }
            if (!canScrollUp || !target.canScrollVertically(1)) {
                canScrollUp = false
                consumed[1] = dy
            }
        } else {
            canScrollUp = true
            val canScrollDown = target.canScrollVertically(-1)
            if (!canScrollDown) {
                consumed[1] = dy
                if (top < 0) {
                    scrollByOffsetTop(dy)
                } else if (mNeedDragOver && type == ViewCompat.TYPE_TOUCH) {
                    if (scrollY > -getMaxDragOverHeight()) {
                        scrollBy(0, dy)
                    }
                }
            }
        }
    }

    private fun scrollByOffsetTop(dy: Int) {
        var dy = dy
        val oldTop = top
        val maxNeedHideHeight = getMaxNeedHideHeight()
        var newTop = oldTop - dy
        if (newTop > 0) {
            dy = oldTop
        }
        if (newTop < -maxNeedHideHeight) {
            dy = maxNeedHideHeight + oldTop
        }
        newTop = oldTop - dy
        if (mHeaderScrollListener != null) {
            if (oldTop < 0 && newTop >= 0) {
                mHeaderScrollListener!!.onHeaderTotalShow()
            } else if (newTop == -maxNeedHideHeight) {
                mHeaderScrollListener!!.onHeaderTotalHide()
            }
            mHeaderScrollListener!!.onScroll(-newTop, -newTop * 1f / maxNeedHideHeight)
        }
        mOldTop = newTop
        offsetTopAndBottom(-dy)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (t <= 0) {
            setHeaderViewPaddingTop(getMaxDragOverHeight() + t)
        }
    }

    private fun setHeaderViewPaddingTop(top: Int) {
        mHeaderView!!.setPadding(mHeaderView!!.paddingLeft, top, mHeaderView!!.paddingRight, mHeaderView!!.paddingBottom)
    }

    private fun getMaxNeedHideHeight(): Int {
        return mMinHeaderHeight - mStickHeaderHeight
    }

    private fun getMaxDragOverHeight(): Int {
        return mMaxHeaderHeight - mMinHeaderHeight
    }

    open interface HeaderScrollListener {
        fun onScroll(dy: Int, percent: Float)
        fun onHeaderTotalHide()
        fun onHeaderTotalShow()
    }

    open class SimpleHeaderScrollListener : HeaderScrollListener {
        override fun onScroll(dy: Int, fraction: Float) {}
        override fun onHeaderTotalHide() {}
        override fun onHeaderTotalShow() {}
    }
}