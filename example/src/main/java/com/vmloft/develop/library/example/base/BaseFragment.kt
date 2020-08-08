package com.vmloft.develop.library.example.base

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.tools.base.VMBFragment
import com.vmloft.develop.library.tools.utils.VMDimen

import kotlinx.android.synthetic.main.widget_common_top_bar.*

/**
 * Created by lzan13 on 2020/02/15 11:16
 * 描述：Fragment 基类
 */
abstract class BaseFragment : VMBFragment() {

    override fun initUI() {
        setupTobBar()
    }

    /**
     * 装载 TopBar
     */
    private fun setupTobBar() {
        // 设置状态栏透明主题时，布局整体会上移，所以给头部 View 设置 StatusBar 的高度
        commonTopSpaceView?.layoutParams?.height = VMDimen.statusBarHeight

        commonTopBar?.setCenter(true)
        commonTopBar?.setTitleStyle(R.style.VMText_Title)
    }

    /**
     * 设置图标
     */
    protected fun setTopIcon(resId: Int) {
        commonTopBar?.setIcon(resId)
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
     * 设置子标题
     */
    protected fun setTopSubtitle(title: String?) {
        commonTopBar?.setSubtitle(title)
    }
}