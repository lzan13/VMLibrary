package com.vmloft.develop.library.example.base

import android.view.View

import com.vmloft.develop.library.example.R.drawable
import com.vmloft.develop.library.example.R.id
import com.vmloft.develop.library.tools.base.VMBActivity
import com.vmloft.develop.library.tools.utils.VMDimen
import com.vmloft.develop.library.tools.utils.VMTheme
import kotlinx.android.synthetic.main.widget_common_top_bar.*

abstract class AppActivity : VMBActivity() {
    /**
     * 通用的获取 TopBar 方法
     */

    override fun initUI() {
        // 设置黑色状态栏，这个需要和主题色配置使用，主题色是深色的话建议设置 false
        VMTheme.setDarkStatusBar(this, true)
        setupTobBar()
    }

    /**
     * 装载 TopBar
     */
    private fun setupTobBar() {
        // TODO 设置状态栏透明主题时，布局整体会上移，需要给头部 View 设置 StatusBar 的高度
        commonTopSpaceView?.layoutParams?.height = VMDimen.statusBarHeight
        commonTopBar?.setIcon(drawable.ic_arrow_back)
        commonTopBar?.setIconListener(View.OnClickListener { onBackPressed() })
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
    protected fun setTopTitle(title: String?) {
        commonTopBar?.setTitle(title)
    }

    /**
     * 设置子标题
     */
    protected fun setTopSubtitle(title: String?) {
        commonTopBar?.setSubtitle(title)
    }

}