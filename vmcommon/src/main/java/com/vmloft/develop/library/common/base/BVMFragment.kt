package com.vmloft.develop.library.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

import com.vmloft.develop.library.common.R
import com.vmloft.develop.library.common.utils.errorBar
import com.vmloft.develop.library.common.utils.showBar
import com.vmloft.develop.library.common.widget.CommonDialog
import com.vmloft.develop.library.tools.utils.VMDimen

import kotlinx.android.synthetic.main.widget_common_top_bar.*

/**
 * Created by lzan13 on 2020/02/15 11:16
 * 描述：Fragment MVVM 框架基类
 */
abstract class BVMFragment<VM : BViewModel> : Fragment() {

    protected var mDialog: CommonDialog? = null

    protected lateinit var mBinding: ViewDataBinding
    protected lateinit var mViewModel: VM
    protected var isLoaded: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mViewModel = initVM()
        mBinding.lifecycleOwner = this
        initUI()
        startObserve()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (!isLoaded) {
            isLoaded = true
            initData()
        }
    }

    /**
     * 布局资源 id
     */
    abstract fun layoutId(): Int

    /**
     * 初始化 ViewModel
     */
    abstract fun initVM(): VM

    /**
     * 初始化 UI
     */
    open fun initUI() {
        setupTobBar()
    }

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 模型变化回调
     */
    abstract fun onModelRefresh(model: BViewModel.UIModel)

    open fun onModelError(model: BViewModel.UIModel) {
        model.error?.let { message -> errorBar(message) }
    }

    /**
     * 开始观察 View 生命周期
     */
    private fun startObserve() {
        mViewModel.uiState.observe(this, {
            if (it.isSuccess) {
                onModelRefresh(it)
            }
            it.toast?.let { message -> showBar(message) }

            onModelError(it)
        })
    }

    /**
     * 装载 TopBar
     */
    private fun setupTobBar() {
        // 设置状态栏透明主题时，布局整体会上移，所以给头部 View 设置 StatusBar 的高度
        commonTopSpace?.layoutParams?.height = VMDimen.statusBarHeight

        commonTopBar?.setCenter(true)
        commonTopBar?.setTitleStyle(R.style.AppText_Title)
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
     * 设置子标题
     */
    protected fun setTopEndView(view: View?) {
        commonTopBar?.addEndView(view)
    }
}
