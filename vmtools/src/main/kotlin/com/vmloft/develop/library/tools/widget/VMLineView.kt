package com.vmloft.develop.library.tools.widget

import android.content.Context
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.vmloft.develop.library.tools.R
import com.vmloft.develop.library.tools.R.layout
import com.vmloft.develop.library.tools.R.styleable
import com.vmloft.develop.library.tools.utils.VMColor
import com.vmloft.develop.library.tools.utils.VMDimen
import com.vmloft.develop.library.tools.utils.logger.VMLog

/**
 * Create by lzan13 on 2019/5/12 22:25
 *
 * 自定义单行控件，主要用于设置选项
 */
class VMLineView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {

    private var vmLineRootView: View // 根布局
    private var mIconView: ImageView // 左侧图标
    private var mTitleView: TextView // 标题
    private var mRightContainer: LinearLayout // 右侧容器
    private var mCaptionIconView: ImageView // 说明图标
    private var mCaptionView: TextView // 说明文本
    private var mRightIconView: ImageView // 右侧图标
    private var mDescriptionView: TextView // 底部描述文本
    private var mBottomContainer: LinearLayout // 底部容器
    private var mDecorationView: View // 分割线

    // 分割线辅助定位
    private var mDecorationAidedStartView: View
    private var mDecorationAidedEndView: View

    private var mIconRes = 0 // 图标
    private var mIconSize = 24 // 图标大小

    private var mTitle: String? = null
    private var mTitleColor = 0
    private var mTitleStyle = 0
    private var mTitleSpace = 0 // 标题内容边距

    private var mCaptionIconRes = 0
    private var mCaption: String? = null
    private var mCaptionColor = 0
    private var mCaptionStyle = 0
    private var mCaptionSpace = 0 // 右侧描述内容边距

    private var mRightIconRes = 0 // 右侧图标
    private var mRightIconSize = 24 // 右侧图标大小

    private var mDescription: String? = null
    private var mDescriptionColor = 0
    private var mDescriptionStyle = 0 // 底部描述样式

    private var mDecoration = 0 // 是否展示分割线
    private var mDecorationHeight = 0 // 分割线高度
    private var mDecorationColor = 0 // 分割线颜色
    private var mDecorationAidedStart = 0 // 分割线开始空间
    private var mDecorationAidedEnd = 0 // 分割线结束空间

    /**
     * 初始化
     */
    init {
        LayoutInflater.from(context).inflate(layout.vm_widget_line_view, this)

        mTitleColor = VMColor.byRes(R.color.vm_title)
        mCaptionColor = VMColor.byRes(R.color.vm_caption)
        mDescriptionColor = VMColor.byRes(R.color.vm_description)

        mTitleSpace = VMDimen.dp2px(16)
        mCaptionSpace = VMDimen.dp2px(8)

        mIconSize = VMDimen.dp2px(24)
        mRightIconSize = VMDimen.dp2px(24)

        mDecorationAidedStart = VMDimen.dp2px(16)
        mDecorationAidedEnd = VMDimen.dp2px(16)

        mDecorationHeight = VMDimen.getDimenPixel(R.dimen.vm_dimen_0_1)
        mDecorationColor = VMColor.byRes(R.color.vm_decoration)

        vmLineRootView = findViewById(R.id.vmLineRootView)
        mIconView = findViewById(R.id.vmLineIconIV)
        mTitleView = findViewById(R.id.vmLineTitleTV)
        mRightContainer = findViewById(R.id.vmLineRightContainer)
        mCaptionIconView = findViewById(R.id.vmLineCaptionIconIV)
        mCaptionView = findViewById(R.id.vmLineCaptionTV)
        mRightIconView = findViewById(R.id.vmLineRightIconIV)
        mDescriptionView = findViewById(R.id.vmLineDescriptionTV)
        mBottomContainer = findViewById(R.id.vmLineBottomContainer)
        mDecorationView = findViewById(R.id.vmLineDecoration)
        mDecorationAidedStartView = findViewById(R.id.vmLineDecorationAidedStartView)
        mDecorationAidedEndView = findViewById(R.id.vmLineDecorationAidedEndView)

        // 获取控件的属性值
        handleAttrs(context, attrs)
        isClickable = true
        setupView()
    }

    /**
     * 获取资源属性
     */
    private fun handleAttrs(context: Context, attrs: AttributeSet?) {
        // 获取控件的属性值
        if (attrs == null) {
            return
        }
        val array = context.obtainStyledAttributes(attrs, styleable.VMLineView)

        mIconRes = array.getResourceId(styleable.VMLineView_vm_line_icon, mIconRes)
        mIconSize = array.getDimensionPixelOffset(styleable.VMLineView_vm_line_icon_size, mIconSize)
        mTitle = array.getString(styleable.VMLineView_vm_line_title)
        mTitleColor = array.getColor(styleable.VMLineView_vm_line_title_color, mTitleColor)
        mTitleStyle = array.getResourceId(styleable.VMLineView_vm_line_title_style, mTitleStyle)
        mTitleSpace = array.getDimensionPixelOffset(styleable.VMLineView_vm_line_title_space, mTitleSpace)

        mCaptionIconRes = array.getResourceId(styleable.VMLineView_vm_line_caption_icon, mCaptionIconRes)
        mCaption = array.getString(styleable.VMLineView_vm_line_caption)
        mCaptionColor = array.getColor(styleable.VMLineView_vm_line_caption_color, mCaptionColor)
        mCaptionStyle = array.getResourceId(styleable.VMLineView_vm_line_caption_style, mCaptionStyle)
        mCaptionSpace = array.getDimensionPixelOffset(styleable.VMLineView_vm_line_caption_space, mCaptionSpace)

        mRightIconRes = array.getResourceId(styleable.VMLineView_vm_line_right_icon, mRightIconRes)
        mRightIconSize = array.getDimensionPixelOffset(styleable.VMLineView_vm_line_right_icon_size, mRightIconSize)

        mDescription = array.getString(styleable.VMLineView_vm_line_description)
        mDescriptionColor = array.getColor(styleable.VMLineView_vm_line_description_color, mDescriptionColor)
        mDescriptionStyle = array.getResourceId(styleable.VMLineView_vm_line_description_style, mDescriptionStyle)

        mDecoration = array.getInt(styleable.VMLineView_vm_line_decoration, mDecoration)
        mDecorationColor = array.getColor(styleable.VMLineView_vm_line_decoration_color, mDecorationColor)
        mDecorationAidedStart = array.getDimensionPixelOffset(styleable.VMLineView_vm_line_decoration_start, mDecorationAidedStart)
        mDecorationAidedEnd = array.getDimensionPixelOffset(styleable.VMLineView_vm_line_decoration_end, mDecorationAidedEnd)
        mDecorationHeight = array.getDimensionPixelOffset(styleable.VMLineView_vm_line_decoration_height, mDecorationHeight)
        array.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = measuredHeight
        val rootHeight = vmLineRootView.measuredHeight
        // 根据测量结果，将内部布局高度设置为与外层布局一样
        if (height > rootHeight) {
            vmLineRootView.layoutParams.height = height
        }
    }

    /**
     * 装载控件内容
     */
    private fun setupView() {
        setIconRes(mIconRes)
        setIconSize(mIconSize)

        setTitle(mTitle)
        setTitleColor(mTitleColor)
        setTitleStyle(mTitleStyle)

        setCaptionIcon(mCaptionIconRes)
        setCaption(mCaption)
        setCaptionColor(mCaptionColor)
        setCaptionStyle(mCaptionStyle)

        setRightIcon(mRightIconRes)
        setRightIconSize(mRightIconSize)

        setDescription(mDescription)
        setDescriptionColor(mDescriptionColor)
        setDescriptionStyle(mDescriptionStyle)

        if (mDecoration == 0) {
            mDecorationView.visibility = View.GONE
        } else {
            mDecorationView.visibility = View.VISIBLE
            mDecorationView.layoutParams.height = mDecorationHeight
            mDecorationView.setBackgroundColor(mDecorationColor)

            mDecorationAidedStartView.visibility = if (mDecoration == 1) View.GONE else View.VISIBLE
            mDecorationAidedEndView.visibility = if (mDecoration == 2) View.VISIBLE else View.GONE

            mDecorationAidedStartView.layoutParams.width = mDecorationAidedStart
            mDecorationAidedEndView.layoutParams.width = mDecorationAidedEnd
        }
    }

    /**
     * ----------------------------------- 内容设置 -----------------------------------
     *
     * 设置图标
     */
    fun setIconRes(resId: Int) {
        mIconRes = resId
        if (mIconRes == 0) {
            mIconView.visibility = View.GONE
        } else {
            mIconView.visibility = View.VISIBLE
            mIconView.setImageResource(mIconRes)
        }
    }

    /**
     * 设置图标大小
     */
    fun setIconSize(size: Int) {
        mIconSize = size
        mIconView.layoutParams.width = mIconSize
        mIconView.layoutParams.height = mIconSize
    }

    /**
     * 设置标题
     */
    fun setTitle(title: String?) {
        mTitle = title
        mTitleView.text = if (mTitle.isNullOrEmpty()) "" else mTitle
        mTitleView.setPadding(mTitleSpace, 0, mTitleView.paddingEnd, 0)
    }

    /**
     * 设置标题颜色
     */
    fun setTitleColor(color: Int) {
        if (color == 0) return
        mTitleColor = color
        mTitleView.setTextColor(mTitleColor)
    }

    /**
     * 设置标题样式
     */
    fun setTitleStyle(resId: Int) {
        if (mTitleStyle == 0) return
        mTitleStyle = resId
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            mTitleView.setTextAppearance(mTitleStyle)
        } else {
            mTitleView.setTextAppearance(context, mTitleStyle)
        }
    }

    /**
     * 添加控件到右侧容器
     */
    fun setRightView(view: View?) {
        if (view != null) {
            mRightContainer.removeAllViews()
            mRightContainer.addView(view)
        } else {
            mRightContainer.removeAllViews()
        }
    }

    /**
     * 设置右侧描述图标
     */
    fun setCaptionIcon(resId: Int) {
        mCaptionIconRes = resId
        if (mCaptionIconRes == 0) {
            mCaptionIconView.visibility = View.GONE
        } else {
            mCaptionIconView.visibility = View.VISIBLE
            mCaptionIconView.setImageResource(mCaptionIconRes)
        }
    }

    /**
     * 设置右侧说明文本内容
     */
    fun setCaption(caption: String?) {
        mCaption = caption
        if (mCaption.isNullOrEmpty()) {
            mCaptionView.visibility = View.GONE
        } else {
            mCaptionView.visibility = View.VISIBLE
            mCaptionView.text = mCaption
            mCaptionView.setPadding(0, 0, mCaptionSpace, 0)
        }
    }

    /**
     * 设置右侧说明文本颜色
     */
    fun setCaptionColor(color: Int) {
        if (color == 0) return
        mCaptionColor = color
        mCaptionView.setTextColor(mCaptionColor)
    }

    /**
     * 设置右侧说明文本样式
     */
    fun setCaptionStyle(resId: Int) {
        if (mCaptionStyle == 0) return
        mCaptionStyle = resId
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            mCaptionView.setTextAppearance(mCaptionStyle)
        } else {
            mCaptionView.setTextAppearance(context, mCaptionStyle)
        }
    }

    /**
     * 设置右侧图标
     */
    fun setRightIcon(resId: Int) {
        mRightIconRes = resId
        if (mRightIconRes == 0) {
            mRightIconView.visibility = View.GONE
        } else {
            mRightIconView.visibility = View.VISIBLE
            mRightIconView.setImageResource(mRightIconRes)
        }
    }

    /**
     * 设置右侧图标大小
     */
    fun setRightIconSize(size: Int) {
        mRightIconSize = size
        mRightIconView.layoutParams.width = mRightIconSize
        mRightIconView.layoutParams.height = mRightIconSize
    }

    /**
     * 设置底部描述
     */
    fun setDescription(description: String?) {
        mDescription = description
        if (mDescription.isNullOrEmpty()) {
            mDescriptionView.visibility = View.GONE
        } else {
            mDescriptionView.visibility = View.VISIBLE
            mDescriptionView.text = mDescription
            mDescriptionView.setPadding(mTitleSpace, 0, mDescriptionView.paddingEnd, 0)
        }
    }

    /**
     * 设置右侧说明文本颜色
     */
    fun setDescriptionColor(color: Int) {
        if (color == 0) return
        mDescriptionColor = color
        mDescriptionView.setTextColor(mDescriptionColor)
    }

    /**
     * 设置右侧说明文本样式
     */
    fun setDescriptionStyle(resId: Int) {
        if (mDescriptionStyle == 0) return
        mDescriptionStyle = resId
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            mDescriptionView.setTextAppearance(mDescriptionStyle)
        } else {
            mDescriptionView.setTextAppearance(context, mDescriptionStyle)
        }
    }

    /**
     * 添加控件到底部容器
     */
    fun setBottomView(view: View?) {
        if (view != null) {
            mBottomContainer.removeAllViews()
            mBottomContainer.addView(view)
        } else {
            mBottomContainer.removeAllViews()
        }
    }

    /**
     * 设置激活状态
     */
    override fun setActivated(activated: Boolean) {
        super.setActivated(activated)
        mRightIconView.isActivated = activated
    }

}