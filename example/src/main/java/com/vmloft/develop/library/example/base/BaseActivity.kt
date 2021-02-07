package com.vmloft.develop.library.example.base

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.tools.utils.VMDimen
import com.vmloft.develop.library.tools.utils.VMTheme

import kotlinx.android.synthetic.main.widget_common_top_bar.*

abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var mActivity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivity = this

        setContentView(layoutId())

        initUI()

        initData()
    }

    /**
     * 布局资源 id
     */
    abstract fun layoutId(): Int

    /**
     * 初始化 UI
     */
    open fun initUI() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        VMTheme.setDarkStatusBar(mActivity, true)
        setupTobBar()
    }

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 装载 TopBar
     */
    private fun setupTobBar() {
        // 设置状态栏透明主题时，布局整体会上移，所以给头部 View 设置 StatusBar 的高度
        commonTopSpaceView?.layoutParams?.height = VMDimen.statusBarHeight

        commonTopBar?.setCenter(true)
        commonTopBar?.setTitleStyle(R.style.AppText_Title)
        commonTopBar?.setIcon(R.drawable.ic_arrow_back)
        commonTopBar?.setIconListener { onBackPressed() }
        commonTopBar?.setEndBtnTextStyle(R.style.AppText_TopBarEndBtn)
    }

    /**
     * 设置顶部标题背景色
     */
    protected fun setTopBGColor(color: Int) {
        commonTopLL?.setBackgroundColor(color)
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
     * 设置 TopBar 右侧按钮
     */
    protected fun setTopEndBtnEnable(enable: Boolean) {
        commonTopBar?.setEndBtnEnable(enable)
    }

    /**
     * 设置 TopBar 右侧按钮
     */
    protected fun setTopEndBtn(str: String?) {
        commonTopBar?.setEndBtn(str)
    }

    /**
     * 设置 TopBar 右侧按钮监听
     */
    protected fun setTopEndBtnListener(str: String? = null, listener: View.OnClickListener) {
        commonTopBar?.setEndBtnListener(str, listener)
    }

}
