package com.vmloft.develop.library.tools.widget

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout

import com.vmloft.develop.library.tools.R
import com.vmloft.develop.library.tools.R.layout
import com.vmloft.develop.library.tools.R.styleable
import com.vmloft.develop.library.tools.utils.VMColor
import com.vmloft.develop.library.tools.utils.VMStr

import kotlinx.android.synthetic.main.vm_widget_top_bar.view.*

/**
 * Create by lzan13 on 2019/5/19 12:36
 *
 * 自定义 VMTopBar 控件
 */
class VMTopBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {
    // 设置数据
    private var mIcon = 0

    private var mTitle: String? = null
    private var mTitleColor = 0

    private var mSubtitle: String? = null
    private var mSubtitleColor = 0

    private var mEndBtnText: String? = null
    private var mEndBtnColor: Int = 0
    private var mEndBtnBG: Int = 0
    private var mEndIcon = 0

    // 居中
    private var isCenter = false

    /**
     * 初始化
     */
    init {
        LayoutInflater.from(context).inflate(layout.vm_widget_top_bar, this)

        mTitleColor = VMColor.byRes(R.color.vm_title)
        mSubtitleColor = VMColor.byRes(R.color.vm_subhead)
        mEndBtnColor = VMColor.byRes(R.color.vm_text_dark_color)
        mEndBtnBG = R.drawable.vm_selector_transparent_fillet

        // 获取控件的属性值
        handleAttrs(context, attrs)

        if (mIcon == 0) {
            vmTopBarIconBtn.visibility = View.GONE
        } else {
            vmTopBarIconBtn.visibility = View.VISIBLE
            vmTopBarIconBtn.setImageResource(mIcon)
        }

        if (!VMStr.isEmpty(mEndBtnText)) {
            vmTopBarEndBtn.visibility = View.VISIBLE
            vmTopBarEndBtn.text = mEndBtnText
        } else {
            vmTopBarEndBtn.visibility = View.GONE
        }
        if (mEndBtnColor != 0) {
            vmTopBarEndBtn.setTextColor(mEndBtnColor)
        }
        if (mEndBtnBG != 0) {
            vmTopBarEndBtn.setBackgroundResource(mEndBtnBG)
        }

        if (mEndIcon == 0) {
            vmTopBarEndIcon.visibility = View.GONE
        } else {
            vmTopBarEndIcon.visibility = View.VISIBLE
            vmTopBarEndIcon.setImageResource(mEndIcon)
        }

        bindTitle()
        bindSubtitle()
    }

    /**
     * 获取资源属性
     *
     * @param context
     * @param attrs
     */
    private fun handleAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        val array = context.obtainStyledAttributes(attrs, styleable.VMTopBar)
        // 获取自定义属性值，如果没有设置就是默认值
        mIcon = array.getResourceId(styleable.VMTopBar_vm_icon, mIcon)

        mTitle = array.getString(styleable.VMTopBar_vm_title)
        mTitleColor = array.getColor(styleable.VMTopBar_vm_title_color, mTitleColor)

        mSubtitle = array.getString(styleable.VMTopBar_vm_subtitle)
        mSubtitleColor = array.getColor(styleable.VMTopBar_vm_subtitle_color, mSubtitleColor)

        mEndBtnText = array.getString(styleable.VMTopBar_vm_end_btn)
        mEndBtnColor = array.getColor(styleable.VMTopBar_vm_end_btn_color, mEndBtnColor)
        mEndBtnBG = array.getResourceId(styleable.VMTopBar_vm_end_btn_bg, mEndBtnBG)

        mEndIcon = array.getResourceId(styleable.VMTopBar_vm_end_icon, mEndIcon)

        isCenter = array.getBoolean(styleable.VMTopBar_vm_is_center, isCenter)

        // 回收资源
        array.recycle()
    }

    /**
     * 设置图标
     */
    fun setIcon(resId: Int) {
        mIcon = resId
        if (mIcon == 0) {
            vmTopBarIconBtn.visibility = View.GONE
        } else {
            vmTopBarIconBtn.visibility = View.VISIBLE
            vmTopBarIconBtn.setImageResource(mIcon)
        }
    }

    /**
     * 设置图标颜色
     */
    fun setIconColor(color: Int) {
        // 对图标着色
        vmTopBarIconBtn.imageTintList = ColorStateList.valueOf(color)
        vmTopBarEndIcon.imageTintList = ColorStateList.valueOf(color)
    }

    /**
     * 是否居中
     */
    fun setCenter(center: Boolean) {
        isCenter = center
        bindTitle()
        bindSubtitle()
    }

    /**
     * 设置标题
     */
    fun setTitle(resId: Int) {
        mTitle = VMStr.byRes(resId)
        bindTitle()
    }

    /**
     * 设置标题
     */
    fun setTitle(title: String?) {
        mTitle = title
        bindTitle()
    }

    /**
     * 设置标题颜色
     */
    fun setTitleColor(resId: Int) {
        if (resId != 0) {
            mTitleColor = VMColor.byRes(resId)
            bindTitle()
        }
    }

    /**
     * 设置标题样式
     */
    fun setTitleStyle(resId: Int) {
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            vmTopBarTitleTV.setTextAppearance(resId)
            vmTopBarCenterTitleTV.setTextAppearance(resId)
        } else {
            vmTopBarTitleTV.setTextAppearance(context, resId)
            vmTopBarCenterTitleTV.setTextAppearance(context, resId)
        }
    }

    /**
     * 设置子标题
     */
    fun setSubtitle(title: String?) {
        mSubtitle = title
        bindSubtitle()
    }

    /**
     * 设置标题颜色
     */
    fun setSubTitleColor(resId: Int) {
        if (resId != 0) {
            mSubtitleColor = VMColor.byRes(resId)
            bindSubtitle()
        }
    }

    /**
     * 设置子标题样式
     */
    fun setSubTitleStyle(resId: Int) {
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            vmTopBarSubtitleTV.setTextAppearance(resId)
            vmTopBarCenterSubtitleTV.setTextAppearance(resId)
        } else {
            vmTopBarSubtitleTV.setTextAppearance(context, resId)
            vmTopBarCenterSubtitleTV.setTextAppearance(context, resId)
        }
    }

    /**
     * 绑定标题数据
     */
    private fun bindTitle() {
        if (isCenter) {
            vmTopBarCenterTitleTV.setTextColor(mTitleColor)
        } else {
            vmTopBarTitleTV.setTextColor(mTitleColor)
        }

        if (!VMStr.isEmpty(mTitle)) {
            if (isCenter) {
                vmTopBarTitleTV.visibility = View.GONE

                vmTopBarCenterTitleTV.visibility = View.VISIBLE
                vmTopBarCenterTitleTV.text = mTitle
            } else {
                vmTopBarTitleTV.text = mTitle
                vmTopBarTitleTV.visibility = View.VISIBLE

                vmTopBarCenterTitleTV.visibility = View.GONE
            }
        } else {
            vmTopBarTitleTV.visibility = View.GONE
            vmTopBarCenterTitleTV.visibility = View.GONE
        }
    }

    /**
     * 绑定子标题数据
     */
    private fun bindSubtitle() {
        if (isCenter) {
            vmTopBarCenterSubtitleTV.setTextColor(mSubtitleColor)
        } else {
            vmTopBarSubtitleTV.setTextColor(mSubtitleColor)
        }

        if (!VMStr.isEmpty(mSubtitle)) {
            if (isCenter) {
                vmTopBarSubtitleTV.visibility = View.GONE

                vmTopBarCenterSubtitleTV.visibility = View.VISIBLE
                vmTopBarCenterSubtitleTV.text = mSubtitle
            } else {
                vmTopBarSubtitleTV.text = mSubtitle
                vmTopBarSubtitleTV.visibility = View.VISIBLE

                vmTopBarCenterSubtitleTV.visibility = View.GONE
            }
        } else {
            vmTopBarSubtitleTV.visibility = View.GONE
            vmTopBarCenterSubtitleTV.visibility = View.GONE
        }
    }

    /**
     * 设置添加尾部控件
     */
    fun addEndView(view: View?) {
        if (view != null) {
            vmTopBarEndContainer.removeAllViews()
            vmTopBarEndContainer.addView(view)
            vmTopBarEndContainer.visibility = View.VISIBLE
        } else {
            vmTopBarEndContainer.removeAllViews()
            vmTopBarEndContainer.visibility = View.GONE
        }
    }

    /**
     * 设置图标
     */
    fun setEndIcon(resId: Int) {
        mEndIcon = resId
        if (mEndIcon == 0) {
            vmTopBarEndIcon.visibility = View.GONE
        } else {
            vmTopBarEndIcon.visibility = View.VISIBLE
            vmTopBarEndIcon.setImageResource(mEndIcon)
        }
    }

    /**
     * 设置图标点击监听
     */
    fun setIconListener(listener: OnClickListener?) {
        vmTopBarIconBtn.setOnClickListener(listener)
    }

    /**
     * 设置右侧图标点击监听
     */
    fun setEndIconListener(listener: OnClickListener?) {
        vmTopBarEndIcon.setOnClickListener(listener)
    }

    /**
     * 设置右侧按钮状态
     */
    fun setEndBtnEnable(enable: Boolean) {
        vmTopBarEndBtn.isEnabled = enable
    }

    /**
     * 设置右侧的按钮
     */
    fun setEndBtn(text: String?) {
        mEndBtnText = text
        if (VMStr.isEmpty(mEndBtnText)) {
            vmTopBarEndBtn.visibility = View.GONE
        } else {
            vmTopBarEndBtn.visibility = View.VISIBLE
            vmTopBarEndBtn.text = mEndBtnText
        }
    }

    /**
     * 设置按钮背景样式
     *
     * @param resId 背景资源 id
     */
    fun setEndBtnBackground(resId: Int) {
        mEndBtnBG = resId
        if (mEndBtnBG != 0) {
            vmTopBarEndBtn.setBackgroundResource(mEndBtnBG)
        }
    }

    /**
     * 设置按钮文字颜色
     *
     * @param resId 颜色资源Id
     */
    fun setEndBtnTextColor(resId: Int) {
        mEndBtnColor = VMColor.byRes(resId)
        vmTopBarEndBtn.setTextColor(mEndBtnColor)
    }

    /**
     * 设置按钮文字样式
     *
     * @param resId 样式 Id
     */
    fun setEndBtnTextStyle(resId: Int) {
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            vmTopBarEndBtn.setTextAppearance(resId)
        } else {
            vmTopBarEndBtn.setTextAppearance(context, resId)
        }
    }

    /**
     * 设置右侧的按钮点击的监听
     *
     * @param listener 回调接口
     */
    fun setEndBtnListener(listener: OnClickListener?) {
        setEndBtnListener(mEndBtnText, listener)
    }

    /**
     * 设置消极的按钮点击监听
     *
     * @param str      文本
     * @param listener 回调接口
     */
    fun setEndBtnListener(str: String?, listener: OnClickListener?) {
        setEndBtn(str)
        vmTopBarEndBtn.setOnClickListener(listener)
    }
}