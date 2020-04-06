package com.vmloft.develop.library.tools.widget

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.animation.Interpolator
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.vmloft.develop.library.tools.R.styleable
import java.util.ArrayList

/**
 * Create by lzan13 2018/3/21
 *
 * 可以伸缩布局控件
 */
class VMExpandableLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    private var wms = 0
    private var hms = 0
    private var expandableViews: MutableList<View>? = null
    private var duration = DEFAULT_DURATION
    private var isExpanded = false
    private val interpolator: Interpolator = FastOutSlowInInterpolator()
    private var animatorSet: AnimatorSet? = null
    private var listener: OnExpansionUpdateListener? = null

    /**
     * 初始化
     */
    init {
        handleAttrs(context, attrs)
        expandableViews = ArrayList()

        orientation = VERTICAL
    }

    /**
     * 获取资源属性
     */
    private fun handleAttrs(context: Context, attrs: AttributeSet?) {
        // 获取控件的属性值
        if (attrs == null) {
            return
        }
        val a = context.obtainStyledAttributes(attrs, styleable.VMExpandableLayout)
        duration = a.getInt(styleable.VMExpandableLayout_vm_el_duration, DEFAULT_DURATION)
        isExpanded = a.getBoolean(styleable.VMExpandableLayout_vm_el_expanded, false)
        a.recycle()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val bundle = Bundle()
        bundle.putBoolean(KEY_EXPANDED, isExpanded)
        bundle.putParcelable(KEY_SUPER_STATE, superState)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val bundle = state as Bundle
        isExpanded = bundle.getBoolean(KEY_EXPANDED)
        val superState = bundle.getParcelable<Parcelable>(KEY_SUPER_STATE)
        for (expandableView in expandableViews!!) {
            expandableView.visibility = if (isExpanded) View.VISIBLE else View.GONE
        }
        super.onRestoreInstanceState(superState)
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        val lp = params as LayoutParams
        if (lp.expandable) {
            expandableViews!!.add(child)
            child.visibility = if (isExpanded) View.VISIBLE else View.GONE
        }
        super.addView(child, index, params)
    }

    override fun removeView(child: View) {
        val lp = child.layoutParams as LayoutParams
        if (lp.expandable) {
            expandableViews!!.remove(child)
        }
        super.removeView(child)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        wms = widthMeasureSpec
        hms = heightMeasureSpec
    }

    override fun generateLayoutParams(attrs: AttributeSet): LinearLayout.LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        animatorSet?.cancel()
        super.onConfigurationChanged(newConfig)
    }

    @JvmOverloads
    fun toggle(animate: Boolean = true) {
        if (isExpanded) {
            collapse(animate)
        } else {
            expand(animate)
        }
    }

    fun expand() {
        expand(true)
    }

    @SuppressLint("WrongCall")
    fun expand(animate: Boolean) {
        if (isExpanded) {
            return
        }
        animatorSet?.cancel()
        animatorSet = null
        isExpanded = true
        for (expandableView in expandableViews!!) {
            val lp = expandableView.layoutParams as LayoutParams

            // Calculate view's original height
            expandableView.visibility = View.VISIBLE
            lp.weight = lp.originalWeight
            lp.height = lp.originalHeight
            super.onMeasure(wms, hms)
        }
        for (expandableView in expandableViews!!) {
            if (animate) {
                animateHeight(expandableView, expandableView.measuredHeight)
            } else {
                setHeight(expandableView, expandableView.measuredHeight)
            }
        }
        if (animate) {
            animatorSet?.start()
        }
    }

    @JvmOverloads
    fun collapse(animate: Boolean = true) {
        if (!isExpanded) {
            return
        }
        animatorSet?.cancel()
        isExpanded = false
        for (expandableView in expandableViews!!) {
            if (animate) {
                animateHeight(expandableView, 0)
            } else {
                setHeight(expandableView, 0)
            }
        }
        if (animate) {
            animatorSet?.start()
        }
    }

    fun setOnExpansionUpdateListener(listener: OnExpansionUpdateListener?) {
        this.listener = listener
    }

    private fun animateHeight(view: View, targetHeight: Int) {
        if (animatorSet == null) {
            animatorSet = AnimatorSet()
            animatorSet?.interpolator = interpolator
            animatorSet?.duration = duration.toLong()
        }
        val lp = view.layoutParams as LayoutParams
        lp.weight = 0f
        val height = view.height
        val animator = ValueAnimator.ofInt(height, targetHeight)
        animator.addUpdateListener { valueAnimator ->
            view.layoutParams.height = (valueAnimator.animatedValue as Int)
            view.requestLayout()
            if (listener != null) {
                val fraction = if (targetHeight == 0) 1 - valueAnimator.animatedFraction else valueAnimator.animatedFraction
                listener!!.onExpansionUpdate(fraction)
            }
        }
        animator.addListener(object : AnimatorListener {
            override fun onAnimationStart(animator: Animator) {
                view.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animator: Animator) {
                if (targetHeight == 0) {
                    view.visibility = View.GONE
                } else {
                    lp.height = lp.originalHeight
                    lp.weight = lp.originalWeight
                }
            }

            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
        animatorSet?.playTogether(animator)
    }

    private fun setHeight(view: View, targetHeight: Int) {
        val lp = view.layoutParams as LayoutParams
        if (targetHeight == 0) {
            view.visibility = View.GONE
        } else {
            lp.height = lp.originalHeight
            lp.weight = lp.originalWeight
            view.requestLayout()
        }
        if (listener != null) {
            listener!!.onExpansionUpdate(if (targetHeight == 0) 0f else 1f)
        }
    }

    class LayoutParams(c: Context, attrs: AttributeSet?) : LinearLayout.LayoutParams(c, attrs) {
        val expandable: Boolean
        val originalHeight: Int
        val originalWeight: Float

        init {
            val a = c.obtainStyledAttributes(attrs, styleable.VMExpandableLayout)
            expandable = a.getBoolean(styleable.VMExpandableLayout_vm_el_expandable, false)
            originalHeight = height
            originalWeight = weight
            a.recycle()
        }
    }

    interface OnExpansionUpdateListener {
        fun onExpansionUpdate(expansionFraction: Float)
    }

    companion object {
        const val KEY_SUPER_STATE = "super_state"
        const val KEY_EXPANDED = "expanded"
        private const val DEFAULT_DURATION = 300
    }

}