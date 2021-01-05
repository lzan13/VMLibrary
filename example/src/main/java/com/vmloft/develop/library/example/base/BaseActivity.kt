package com.vmloft.develop.library.example.base

import android.view.View

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.tools.base.VMBActivity
import com.vmloft.develop.library.tools.utils.VMColor
import com.vmloft.develop.library.tools.utils.VMDimen
import com.vmloft.develop.library.tools.utils.VMTheme

import kotlinx.android.synthetic.main.widget_common_top_bar.*

abstract class BaseActivity : VMBActivity() {

    override fun initUI() {
        VMTheme.setDarkStatusBar(this, true)
        setupTobBar()
    }

    /**
     * 装载 TopBar
     */
    private fun setupTobBar() {
        // 设置状态栏透明主题时，布局整体会上移，所以给头部 View 设置 StatusBar 的高度
        commonTopSpaceView?.layoutParams?.height = VMDimen.statusBarHeight

        commonTopBar?.setCenter(false)
        commonTopBar?.setTitleStyle(R.style.VMText_Title)
        commonTopBar?.setIcon(R.drawable.ic_arrow_back)
        commonTopBar?.setIconColor(VMColor.byRes(R.color.app_title))
        commonTopBar?.setIconListener { onBackPressed() }
    }

    /**
     * 设置图标
     */
    protected fun setTopIcon(resId: Int) {
        commonTopBar?.setIcon(resId)
    }

    /**
     * 设置图标颜色
     */
    protected fun setTopIconColor(resId: Int) {
        commonTopBar?.setIconColor(resId)
    }

    /**
     * 设置标题
     */
    protected fun setTopTitle(resId: Int) {
        commonTopBar?.setTitle(resId)
    }

    /**
     * 设置标题
     */
    protected fun setTopTitle(title: String) {
        commonTopBar?.setTitle(title)
    }

    /**
     * 设置标题颜色
     */
    protected fun setTopTitleColor(resId: Int) {
        commonTopBar?.setTitleColor(resId)
    }

    /**
     * 设置子标题
     */
    protected fun setTopSubtitle(title: String?) {
        commonTopBar?.setSubtitle(title)
    }

    /**
     * 设置背景色
     */
    protected fun setTopBG(resId: Int) {
        commonTopLL.setBackgroundResource(resId)
    }

}
