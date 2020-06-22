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
import com.vmloft.develop.library.tools.utils.VMStr

/**
 * Create by lzan13 on 2019/5/12 22:25
 *
 * 自定义单行控件，主要用于设置选项
 */
class VMLineView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {
    // 左侧图标
    private var mIconView: ImageView

    // 标题
    private var mTitleView: TextView

    // 右侧容器
    private var mRightContainer: LinearLayout

    // 说明图标
    private var mCaptionIconView: ImageView

    // 说明文本
    private var mCaptionView: TextView

    // 右侧图标
    private var mRightIconView: ImageView

    // 底部描述文本
    private var mDescriptionView: TextView

    // 底部容器
    private var mBottomContainer: LinearLayout

    // 分割线
    private var mDecorationView: View

    private var mIconRes = 0
    private var mTitle: String? = null
    private var mTitleColor = 0
    private var mCaptionIconRes = 0
    private var mCaption: String? = null
    private var mCaptionColor = 0
    private var mRightIconRes = 0
    private var mDescription: String? = null
    private var mDescriptionColor = 0

    // 是否展示分割线
    private var mDecoration = false

    /**
     * 初始化
     */
    init {
        LayoutInflater.from(context).inflate(layout.vm_widget_line_view, this)

        mTitleColor = VMColor.byRes(R.color.vm_title)
        mCaptionColor = VMColor.byRes(R.color.vm_caption)
        mDescriptionColor = VMColor.byRes(R.color.vm_description)

        mIconView = findViewById(R.id.vmLineIconIV)
        mTitleView = findViewById(R.id.vmLineTitleTV)
        mRightContainer = findViewById(R.id.vmLineRightContainer)
        mCaptionIconView = findViewById(R.id.vmLineCaptionIconIV)
        mCaptionView = findViewById(R.id.vmLineCaptionTV)
        mRightIconView = findViewById(R.id.vmLineRightIconIV)
        mDescriptionView = findViewById(R.id.vmLineDescriptionTV)
        mBottomContainer = findViewById(R.id.vmLineBottomContainer)
        mDecorationView = findViewById(R.id.vmLineDecoration)

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
        mTitle = array.getString(styleable.VMLineView_vm_line_title)
        mTitleColor = array.getColor(styleable.VMLineView_vm_line_title_color, mTitleColor)
        mCaptionIconRes = array.getResourceId(styleable.VMLineView_vm_line_caption_icon, mCaptionIconRes)
        mCaption = array.getString(styleable.VMLineView_vm_line_caption)
        mCaptionColor = array.getColor(styleable.VMLineView_vm_line_caption_color, mCaptionColor)
        mRightIconRes = array.getResourceId(styleable.VMLineView_vm_line_right_icon, mRightIconRes)
        mDescription = array.getString(styleable.VMLineView_vm_line_description)
        mDescriptionColor = array.getColor(styleable.VMLineView_vm_line_description_color, mDescriptionColor)
        mDecoration = array.getBoolean(styleable.VMLineView_vm_line_decoration, mDecoration)
        array.recycle()
    }

    /**
     * 装载控件内容
     */
    private fun setupView() {
        if (mIconRes == 0) {
            mIconView.visibility = View.GONE
        } else {
            mIconView.visibility = View.VISIBLE
            mIconView.setImageResource(mIconRes)
        }
        if (!VMStr.isEmpty(mTitle)) {
            mTitleView.text = mTitle
        }
        if (mTitleColor != 0) {
            mTitleView.setTextColor(mTitleColor)
        }
        if (mCaptionIconRes == 0) {
            mCaptionIconView.visibility = View.GONE
        } else {
            mCaptionIconView.visibility = View.VISIBLE
            mCaptionIconView.setImageResource(mCaptionIconRes)
        }
        if (VMStr.isEmpty(mCaption)) {
            mCaptionView.visibility = View.GONE
        } else {
            mCaptionView.visibility = View.VISIBLE
            mCaptionView.text = mCaption
        }
        if (mCaptionColor != 0) {
            mCaptionView.setTextColor(mCaptionColor)
        }
        if (mDescriptionColor != 0) {
            mDescriptionView.setTextColor(mDescriptionColor)
        }
        if (mRightIconRes == 0) {
            mRightIconView.visibility = View.GONE
        } else {
            mRightIconView.visibility = View.VISIBLE
            mRightIconView.setImageResource(mRightIconRes)
        }
        if (VMStr.isEmpty(mDescription)) {
            mDescriptionView.visibility = View.GONE
        } else {
            mDescriptionView.visibility = View.VISIBLE
            mDescriptionView.text = mDescription
        }
        if (mDecoration) {
            mDecorationView.visibility = View.VISIBLE
        } else {
            mDecorationView.visibility = View.GONE
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
            return
        }
        mIconView.visibility = View.VISIBLE
        mIconView.setImageResource(mIconRes)
    }

    /**
     * 设置标题
     */
    fun setTitle(title: String?) {
        mTitle = title
        mTitleView.text = mTitle
    }

    /**
     * 设置标题颜色
     */
    fun setTitleColor(color: Int) {
        mTitleColor = color
        mTitleView.setTextColor(mTitleColor)
    }

    /**
     * 设置标题样式
     */
    fun setTitleStyle(resId: Int) {
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            mTitleView.setTextAppearance(resId)
        } else {
            mTitleView.setTextAppearance(context, resId)
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
            return
        }
        mCaptionIconView.visibility = View.VISIBLE
        mCaptionIconView.setImageResource(mCaptionIconRes)
    }

    /**
     * 设置右侧说明文本内容
     */
    fun setCaption(caption: String?) {
        mCaption = caption
        if (VMStr.isEmpty(mCaption)) {
            mCaptionView.visibility = View.GONE
            return
        }
        mCaptionView.visibility = View.VISIBLE
        mCaptionView.text = mCaption
    }

    /**
     * 设置右侧说明文本颜色
     */
    fun setCaptionColor(color: Int) {
        mCaptionColor = color
        mCaptionView.setTextColor(mCaptionColor)
    }

    /**
     * 设置右侧说明文本样式
     */
    fun setCaptionStyle(resId: Int) {
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            mCaptionView.setTextAppearance(resId)
        } else {
            mCaptionView.setTextAppearance(context, resId)
        }
    }

    /**
     * 设置右侧图标
     */
    fun setRightIcon(resId: Int) {
        mRightIconRes = resId
        if (mRightIconRes == 0) {
            mRightIconView.visibility = View.GONE
            return
        }
        mRightIconView.visibility = View.VISIBLE
        mRightIconView.setImageResource(mRightIconRes)
    }

    /**
     * 设置底部描述
     */
    fun setDescription(description: String?) {
        mDescription = description
        if (VMStr.isEmpty(mDescription)) {
            mDescriptionView.visibility = View.GONE
            return
        }
        mDescriptionView.visibility = View.VISIBLE
        mDescriptionView.text = mDescription
    }

    /**
     * 设置右侧说明文本颜色
     */
    fun setDescriptionColor(color: Int) {
        mDescriptionColor = color
        mDescriptionView.setTextColor(mDescriptionColor)
    }

    /**
     * 设置右侧说明文本样式
     */
    fun setDescriptionStyle(resId: Int) {
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            mDescriptionView.setTextAppearance(resId)
        } else {
            mDescriptionView.setTextAppearance(context, resId)
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