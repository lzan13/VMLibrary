package com.vmloft.develop.library.tools.widget

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.vmloft.develop.library.tools.R
import com.vmloft.develop.library.tools.R.layout
import com.vmloft.develop.library.tools.R.styleable
import com.vmloft.develop.library.tools.utils.VMColor
import com.vmloft.develop.library.tools.utils.VMDimen.dp2px
import com.vmloft.develop.library.tools.utils.VMStr
import kotlinx.android.synthetic.main.vm_widget_top_bar.view.vmTopBarEndBtn
import kotlinx.android.synthetic.main.vm_widget_top_bar.view.vmTopBarEndCcon
import kotlinx.android.synthetic.main.vm_widget_top_bar.view.vmTopBarEndContainer
import kotlinx.android.synthetic.main.vm_widget_top_bar.view.vmTopBarIconBtn
import kotlinx.android.synthetic.main.vm_widget_top_bar.view.vmTopBarSubtitleTV
import kotlinx.android.synthetic.main.vm_widget_top_bar.view.vmTopBarTitleTV

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
    private var mEndText: String? = null
    private var mEndIcon = 0

    // 居中
    private var isCenter = false

    /**
     * 初始化
     */
    init {
        LayoutInflater.from(context).inflate(layout.vm_widget_top_bar, this)

        mTitleColor = VMColor.byRes(R.color.vm_title)
        mSubtitleColor = VMColor.byRes(R.color.vm_subtitle)

        // 获取控件的属性值
        handleAttrs(context, attrs)
        if (mIcon == 0) {
            vmTopBarIconBtn.visibility = View.GONE
        } else {
            vmTopBarIconBtn.visibility = View.VISIBLE
            vmTopBarIconBtn.setImageResource(mIcon)
        }
        if (!VMStr.isEmpty(mTitle)) {
            vmTopBarTitleTV.text = mTitle
        }
        if (!VMStr.isEmpty(mSubtitle)) {
            vmTopBarSubtitleTV.visibility = View.VISIBLE
            vmTopBarSubtitleTV.text = mSubtitle
        } else {
            vmTopBarSubtitleTV.visibility = View.GONE
        }
        if (!VMStr.isEmpty(mEndText)) {
            vmTopBarEndBtn.visibility = View.VISIBLE
            vmTopBarEndBtn.text = mEndText
        } else {
            vmTopBarEndBtn.visibility = View.GONE
        }
        if (mEndIcon == 0) {
            vmTopBarEndCcon.visibility = View.GONE
        } else {
            vmTopBarEndCcon.visibility = View.VISIBLE
            vmTopBarEndCcon.setImageResource(mEndIcon)
        }
        setupText()
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
        mSubtitle = array.getString(styleable.VMTopBar_vm_subtitle)
        mEndText = array.getString(styleable.VMTopBar_vm_end_btn)
        mEndIcon = array.getResourceId(styleable.VMTopBar_vm_end_icon, mEndIcon)
        mTitleColor = array.getColor(styleable.VMTopBar_vm_title_color, mTitleColor)
        mSubtitleColor = array.getColor(styleable.VMTopBar_vm_subtitle_color, mSubtitleColor)
        isCenter = array.getBoolean(styleable.VMTopBar_vm_is_center, isCenter)

        // 回收资源
        array.recycle()
    }

    /**
     * 装载控件颜色
     */
    private fun setupText() {
        vmTopBarTitleTV.setTextColor(mTitleColor)
        vmTopBarSubtitleTV.setTextColor(mSubtitleColor)
        if (isCenter) {
            if (!vmTopBarEndContainer.isShown && !vmTopBarEndBtn.isShown) {
                vmTopBarEndContainer.visibility = View.VISIBLE
                vmTopBarEndContainer.layoutParams.width = dp2px(48)
            }
            vmTopBarTitleTV.gravity = Gravity.CENTER
            vmTopBarSubtitleTV.gravity = Gravity.CENTER
        } else {
            vmTopBarTitleTV.gravity = Gravity.START
            vmTopBarSubtitleTV.gravity = Gravity.START
        }
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
        vmTopBarEndCcon.imageTintList = ColorStateList.valueOf(color)
    }

    /**
     * 是否居中
     */
    fun setCenter(center: Boolean) {
        isCenter = center
        setupText()
    }

    /**
     * 设置标题
     */
    fun setTitle(resId: Int) {
        mTitle = VMStr.byRes(resId)
        vmTopBarTitleTV.text = mTitle
    }

    /**
     * 设置标题
     */
    fun setTitle(title: String?) {
        mTitle = title
        if (!VMStr.isEmpty(mTitle)) {
            vmTopBarTitleTV.text = mTitle
        }
    }

    /**
     * 设置标题颜色
     */
    fun setTitleColor(resId: Int) {
        if (resId != 0) {
            mTitleColor = VMColor.byRes(resId)
            setupText()
        }
    }

    /**
     * 设置标题颜色
     */
    fun setSubTitleColor(resId: Int) {
        if (resId != 0) {
            mSubtitleColor = VMColor.byRes(resId)
            setupText()
        }
    }

    /**
     * 设置标题样式
     */
    fun setTitleStyle(resId: Int) {
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            vmTopBarTitleTV.setTextAppearance(resId)
        } else {
            vmTopBarTitleTV.setTextAppearance(context, resId)
        }
    }

    /**
     * 设置子标题
     */
    fun setSubtitle(title: String?) {
        mSubtitle = title
        if (VMStr.isEmpty(mSubtitle)) {
            vmTopBarSubtitleTV.visibility = View.GONE
        } else {
            vmTopBarSubtitleTV.visibility = View.VISIBLE
            vmTopBarSubtitleTV.text = mSubtitle
        }
    }

    /**
     * 设置子标题样式
     */
    fun setSubTitleStyle(resId: Int) {
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            vmTopBarSubtitleTV.setTextAppearance(resId)
        } else {
            vmTopBarSubtitleTV.setTextAppearance(context, resId)
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
            vmTopBarEndCcon.visibility = View.GONE
        } else {
            vmTopBarEndCcon.visibility = View.VISIBLE
            vmTopBarEndCcon.setImageResource(mEndIcon)
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
        vmTopBarEndCcon.setOnClickListener(listener)
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
        mEndText = text
        if (VMStr.isEmpty(mEndText)) {
            vmTopBarEndBtn.visibility = View.GONE
        } else {
            vmTopBarEndBtn.visibility = View.VISIBLE
            vmTopBarEndBtn.text = mEndText
        }
    }

    /**
     * 设置按钮背景样式
     *
     * @param resId 背景资源 id
     */
    fun setEndBtnBackground(resId: Int) {
        vmTopBarEndBtn.setBackgroundResource(resId)
    }

    /**
     * 设置按钮文字颜色
     *
     * @param color 颜色值
     */
    fun setEndBtnTextColor(color: Int) {
        vmTopBarEndBtn.setTextColor(color)
    }

    /**
     * 设置右侧的按钮点击的监听
     *
     * @param listener 回调接口
     */
    fun setEndBtnListener(listener: OnClickListener?) {
        setEndBtnListener(mEndText, listener)
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