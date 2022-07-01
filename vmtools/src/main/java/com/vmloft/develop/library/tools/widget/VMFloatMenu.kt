package com.vmloft.develop.library.tools.widget

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat

import com.vmloft.develop.library.tools.R
import com.vmloft.develop.library.tools.utils.VMDimen
import com.vmloft.develop.library.tools.utils.VMTheme
import com.vmloft.develop.library.tools.utils.VMView

/**
 * Create by lzan13 on 2019/05/28 19:33
 *
 * 自定义实现悬浮菜单，可以跟随按下位置他拿出，同时适配屏幕大小
 */
class VMFloatMenu(private val mContext: Context) : PopupWindow(mContext) {
    private var mItemContainer: LinearLayout
    private var showAtVertical = 0
    private var showAtOrientation = 0

    // 菜单数量
    private var mItemCount = 0
    private var mItemPadding = 0
    private var listener: IItemClickListener? = null

    /**
     * 初始化
     */
    init {
        contentView = LayoutInflater.from(mContext).inflate(R.layout.vm_widget_float_menu, null)

        mItemContainer = contentView.findViewById(R.id.vmFloatMenuContainer)

        mItemPadding = VMDimen.dp2px(12)
        width = LayoutParams.WRAP_CONTENT
        height = LayoutParams.WRAP_CONTENT
        isFocusable = true
        isOutsideTouchable = true
        inputMethodMode = INPUT_METHOD_NOT_NEEDED
        val drawable = ContextCompat.getDrawable(mContext, R.color.vm_transparent)
        setBackgroundDrawable(drawable)
        VMView.changeShadow(mItemContainer, 0.2f)
    }

    /**
     * 设置菜单背景资源
     */
    fun setMenuBackground(resId: Int) {
        if (resId != 0) {
            mItemContainer.setBackgroundResource(resId)
        }
    }

    /**
     * 设置自定义 View
     */
    fun setCustomView(resId: Int) {
        val view = LayoutInflater.from(mContext).inflate(resId, null)
        setCustomView(view)
    }

    /**
     * 设置自定义 View
     */
    fun setCustomView(view: View) {
        mItemContainer.removeAllViews()
        mItemContainer.addView(view)
    }

    /**
     * 显示悬浮菜单在指定位置，显示之前需要根据菜单的高度进行重新计算菜单位置
     */
    fun showAtLocation(view: View, positionX: Int, positionY: Int) {
//        if (mItemCount == 0) {
//            return
//        }
        val offset = VMDimen.dp2px(16)
        val screenW = VMDimen.screenWidth
        val screenH = VMDimen.screenHeight
        // 计算悬浮菜单显示区域
        contentView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        val windowsHeight = contentView.measuredHeight
        val windowsWidth = contentView.measuredWidth
        // 菜单弹出显示的最终坐标
        var x = positionX
        var y = positionY
        if (screenH - positionY < windowsHeight) {
            // 向上弹出
            y = positionY - windowsHeight + offset
            showAtVertical = showUp
        } else {
            //向下弹出
            y = positionY - offset
            showAtVertical = showDown
        }
        if (positionX > windowsWidth || positionX > screenW / 5 * 2) {
            // 左弹出
            x = positionX - windowsWidth + offset
            showAtOrientation = showLeft
        } else {
            // 右弹出
            x = positionX - offset
            showAtOrientation = showRight
        }
        setMenuAnim()
        showAtLocation(view, Gravity.NO_GRAVITY, x, y)
    }

    /**
     * 清空之前添加的 Item
     */
    fun clearAllItem() {
        mItemContainer.removeAllViews()
        mItemCount = 0
    }

    /**
     * 直接添加一个集合
     */
    fun addItemList(items: List<ItemBean>) {
        for (bean in items) {
            addItem(bean)
        }
    }

    /**
     * 添加菜单项
     */
    fun addItem(bean: ItemBean) {
        val view = LayoutInflater.from(mContext).inflate(R.layout.vm_widget_float_menu_item, null)
        val iconIV = view.findViewById<ImageView>(R.id.vmItemIconIV)
        val titleTV = view.findViewById<TextView>(R.id.vmItemTitleTV)

        view.id = bean.itemId
        view.setBackgroundResource(R.drawable.vm_selector_transparent_full)

        if (bean.itemIcon != 0) {
            iconIV.visibility = View.VISIBLE
            iconIV.setImageResource(bean.itemIcon)
        } else {
            iconIV.visibility = View.GONE
        }

        titleTV.text = bean.itemTitle
        titleTV.setTextColor(ContextCompat.getColor(mContext, bean.itemColor))

        mItemContainer.addView(view)
        mItemCount++
        setItemClick(view)
    }

    /**
     * 设置悬浮菜单在触摸位置弹出动画
     */
    private fun setMenuAnim() {
        if (showAtOrientation == showRight && showAtVertical == showUp) {
            animationStyle = R.style.VMFloatMenuLB
        }
        if (showAtOrientation == showRight && showAtVertical == showDown) {
            animationStyle = R.style.VMFloatMenuLT
        }
        if (showAtOrientation == showLeft && showAtVertical == showUp) {
            animationStyle = R.style.VMFloatMenuRB
        }
        if (showAtOrientation == showLeft && showAtVertical == showDown) {
            animationStyle = R.style.VMFloatMenuRT
        }
    }

    /**
     * 设置菜单项点击事件
     */
    private fun setItemClick(view: View) {
        view.setOnClickListener { v: View ->
            dismiss()
            if (listener != null) {
                listener!!.onItemClick(v.id)
            }
        }
    }

    /**
     * 菜单项的数据 Bean 类
     */
    class ItemBean(
        val itemId: Int,
        val itemTitle: String,
        val itemColor: Int = R.color.vm_menu,
        val itemIcon: Int = 0,
    ) {}

    /**
     * 定义悬浮菜单点击监听接口
     */
    abstract class IItemClickListener {
        abstract fun onItemClick(id: Int)
    }

    fun setItemClickListener(listener: IItemClickListener?) {
        this.listener = listener
    }

    companion object {
        private const val showLeft = 0X11
        private const val showRight = 0X12
        private const val showUp = 0X13
        private const val showDown = 0X14
    }

}