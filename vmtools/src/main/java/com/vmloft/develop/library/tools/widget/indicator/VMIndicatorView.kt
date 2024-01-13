package com.vmloft.develop.library.tools.widget.indicator

import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff.Mode.SRC
import android.graphics.PorterDuff.Mode.SRC_ATOP
import android.graphics.PorterDuff.Mode.SRC_OVER
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback

import com.vmloft.develop.library.tools.R.color
import com.vmloft.develop.library.tools.R.styleable
import com.vmloft.develop.library.tools.utils.VMColor
import com.vmloft.develop.library.tools.utils.VMDimen
import com.vmloft.develop.library.tools.widget.indicator.VMIndicatorView.Gravity.CENTER
import com.vmloft.develop.library.tools.widget.indicator.VMIndicatorView.Gravity.LEFT
import com.vmloft.develop.library.tools.widget.indicator.VMIndicatorView.Mode.INSIDE
import com.vmloft.develop.library.tools.widget.indicator.VMIndicatorView.Mode.OUTSIDE
import com.vmloft.develop.library.tools.widget.indicator.VMIndicatorView.Mode.SOLO

/**
 * Created by lzan13 on 2019/04/10
 *
 * 自定义指示器控件
 */
class VMIndicatorView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {
    // 指示器半径
    private var mIndicatorRadius = 0

    // 指示器之间的距离
    private var mIndicatorMargin = 0

    // 指示器正常颜色
    private var mIndicatorNormal = 0

    // 指示器选中颜色
    private var mIndicatorSelected = 0

    // 指示器当前位置
    private var mCurrentPosition = 0

    // 指示器当前位置偏移量
    private var mCurrentPositionOffset = 0f

    // 指示器对齐方式
    private var mIndicatorLayoutGravity: Gravity = CENTER

    // 指示器模式
    private var mIndicatorMode: Mode = INSIDE

    // 可以动的指示器对象
    private lateinit var moveHolder: VMIndicatorHolder
    private var mHolders: MutableList<VMIndicatorHolder> = mutableListOf()
    private var mViewPager: ViewPager? = null
    private var mViewPager2: ViewPager2? = null


    /**
     * 初始化
     */
    init {
        // 初始化默认值
        mIndicatorRadius = VMDimen.dp2px(4)
        mIndicatorMargin = VMDimen.dp2px(8)
        mIndicatorNormal = VMColor.byRes(color.vm_indicator_normal)
        mIndicatorSelected = VMColor.byRes(color.vm_indicator_selected)
        mCurrentPosition = 0
        mCurrentPositionOffset = 0f
        handleAttrs(context, attrs)
    }

    /**
     * 获取资源属性
     *
     * @param context
     * @param attrs
     */
    private fun handleAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, styleable.VMIndicatorView)
        mIndicatorRadius = typedArray.getDimensionPixelSize(styleable.VMIndicatorView_vm_indicator_radius, mIndicatorRadius)
        mIndicatorMargin = typedArray.getDimensionPixelSize(styleable.VMIndicatorView_vm_indicator_margin, mIndicatorMargin)
        mIndicatorNormal = typedArray.getColor(styleable.VMIndicatorView_vm_indicator_normal, mIndicatorNormal)
        mIndicatorSelected = typedArray.getColor(styleable.VMIndicatorView_vm_indicator_selected, mIndicatorSelected)
        val layoutGravity = typedArray.getInt(styleable.VMIndicatorView_vm_indicator_gravity, mIndicatorLayoutGravity.ordinal)
        mIndicatorLayoutGravity = Gravity.values()[layoutGravity]
        val layoutMode = typedArray.getInt(styleable.VMIndicatorView_vm_indicator_mode, mIndicatorMode!!.ordinal)
        mIndicatorMode = Mode.values()[layoutMode]
        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val layer = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)
        for (holder in mHolders) {
            drawHolder(canvas, holder)
        }
        drawHolder(canvas, moveHolder)
        canvas.restoreToCount(layer)
    }

    /**
     * 注入ViewPager
     *
     * @param viewPager
     */
    fun setViewPager(viewPager: ViewPager?) {
        mViewPager = viewPager
        createTabItems()
        createMoveItems()
        initViewPagerListener()
    }

    /**
     * 注入 ViewPager2
     *
     * @param viewPager2
     */
    fun setViewPager(viewPager: ViewPager2?) {
        mViewPager2 = viewPager
        createTabItems()
        createMoveItems()
        initViewPagerListener()
    }

    /**
     * 监听 ViewPager 滑动
     */
    private fun initViewPagerListener() {
        mViewPager?.let {
            it.addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    if (mIndicatorMode != SOLO) {
                        trigger(position, positionOffset)
                    }
                }

                override fun onPageSelected(position: Int) {
                    if (mIndicatorMode == SOLO) {
                        trigger(position, 0f)
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {}
            })
        }
        mViewPager2?.let {
            it.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    if (mIndicatorMode != SOLO) {
                        trigger(position, positionOffset)
                    }
                }

                override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
                    if (mIndicatorMode == SOLO) {
                        trigger(position, 0f)
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                }
            })
        }
    }

    /**
     * 创建小圆点个数,依赖于 ViewPager
     */
    private fun createTabItems() {
        mViewPager?.let {
            for (i in 0 until (it.adapter?.count ?: 0)) {
                createItem()
            }
        }
        mViewPager2?.let {
            for (i in 0 until (it.adapter?.itemCount ?: 0)) {
                createItem()
            }
        }
    }

    private fun createItem() {
        val circle = OvalShape()
        val drawable = ShapeDrawable(circle)
        val holder = VMIndicatorHolder(drawable)
        val paint = drawable.paint
        paint.color = mIndicatorNormal
        paint.isAntiAlias = true
        holder.paint = paint
        mHolders.add(holder)
    }

    /**
     * 创建移动小圆点
     */
    private fun createMoveItems() {
        val circle = OvalShape()
        val drawable = ShapeDrawable(circle)
        moveHolder = VMIndicatorHolder(drawable)
        val paint = drawable.paint
        paint.color = mIndicatorSelected
        paint.isAntiAlias = true
        when (mIndicatorMode) {
            INSIDE -> paint.xfermode = PorterDuffXfermode(SRC_ATOP)
            OUTSIDE -> paint.xfermode = PorterDuffXfermode(SRC_OVER)
            SOLO -> paint.xfermode = PorterDuffXfermode(SRC)
        }
        moveHolder.paint = paint
    }

    private fun trigger(position: Int, positionOffset: Float) {
        mCurrentPosition = position
        mCurrentPositionOffset = positionOffset
        requestLayout()
        invalidate()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val width = width
        val height = height
        layoutItem(width, height)
        layoutMoveItem(mCurrentPosition, mCurrentPositionOffset)
    }

    /**
     * 计算每个小圆点位置
     */
    private fun layoutItem(width: Int, height: Int) {
        val heightY = height * 0.5f
        val startPosition = startDrawPosition(width)
        for (i in mHolders.indices) {
            val holder = mHolders[i]
            holder.resizeShape(2 * mIndicatorRadius.toFloat(), 2 * mIndicatorRadius.toFloat())
            holder.y = heightY - mIndicatorRadius
            val x = startPosition + (2 * mIndicatorRadius + mIndicatorMargin) * i
            holder.x = x.toFloat()
        }
    }

    /**
     * 设置移动小圆点位置
     *
     * @param curItemPosition
     * @param curItemPositionOffset
     */
    private fun layoutMoveItem(curItemPosition: Int, curItemPositionOffset: Float) {
        if (0 == mHolders.size) {
            return
        }
        val holder = mHolders[curItemPosition]
        moveHolder.resizeShape(holder.width, holder.height)
        val x = holder.x + (mIndicatorMargin + mIndicatorRadius * 2) * curItemPositionOffset
        moveHolder.x = x
        moveHolder.y = holder.y
    }

    /**
     * 设置小圆点起始位置
     *
     * @param width
     */
    private fun startDrawPosition(width: Int): Int {
        if (mIndicatorLayoutGravity == LEFT) {
            return 0
        }
        val tabItemLength = mHolders.size * (2 * mIndicatorRadius + mIndicatorMargin) - mIndicatorMargin
        if (width < tabItemLength) {
            return 0
        }
        return if (mIndicatorLayoutGravity == CENTER) {
            (width - tabItemLength) / 2
        } else width - tabItemLength
    }

    /**
     * 开始绘画圆点
     *
     * @param canvas
     * @param holder
     */
    private fun drawHolder(canvas: Canvas, holder: VMIndicatorHolder) {
        canvas.save()
        canvas.translate(holder.x, holder.y)
        holder.shape.draw(canvas)
        canvas.restore()
    }

    /**
     * 修改指示器切换模式
     */
    fun setIndicatorMode(indicatorMode: Mode) {
        mIndicatorMode = indicatorMode
    }

    /**
     * 修改指示器距离
     */
    fun setIndicatorMargin(indicatorMargin: Int) {
        mIndicatorMargin = indicatorMargin
    }

    /**
     * 修改指示器半径大小
     */
    fun setIndicatorRadius(indicatorRadius: Int) {
        mIndicatorRadius = indicatorRadius
    }

    /**
     * 修改指示器对齐方式
     */
    fun setIndicatorLayoutGravity(indicatorLayoutGravity: Gravity) {
        mIndicatorLayoutGravity = indicatorLayoutGravity
    }

    /**
     * 修改指示器选中色
     */
    fun setIndicatorSelected(indicatorSelected: Int) {
        mIndicatorSelected = indicatorSelected
    }

    /**
     * 对齐方式
     */
    enum class Gravity {
        LEFT, // 左对齐
        CENTER, // 居中
        RIGHT // 右对齐
    }

    /**
     * 切换模式
     */
    enum class Mode {
        INSIDE, // 内部覆盖
        OUTSIDE, // 外部覆盖
        SOLO
    }
}