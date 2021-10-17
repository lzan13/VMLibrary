package com.vmloft.develop.library.example.ui.demo.custom

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.vmloft.develop.library.common.base.BActivity

import com.vmloft.develop.library.example.databinding.ActivityDemoViewIndicatorBinding
import com.vmloft.develop.library.example.router.AppRouter
import com.vmloft.develop.library.tools.adapter.VMFragmentPagerAdapter

import java.util.ArrayList

/**
 * Create by lzan13 on 2019/04/11
 *
 * 自定义指示器验证示例
 */
@Route(path = AppRouter.appIndicator)
class IndicatorViewActivity : BActivity<ActivityDemoViewIndicatorBinding>() {
    private var fragmentList: MutableList<Fragment> = mutableListOf()
    private var mAdapter: VMFragmentPagerAdapter? = null

    override fun initVB() = ActivityDemoViewIndicatorBinding.inflate(layoutInflater)

    override fun initUI() {
        super.initUI()
        setTopTitle("自定义指示器")

        fragmentList = ArrayList()
        fragmentList.add(IndicatorViewFragment.newInstance("第 1 页"))
        fragmentList.add(IndicatorViewFragment.newInstance("第 2 页"))
        fragmentList.add(IndicatorViewFragment.newInstance("第 3 页"))

        mAdapter = VMFragmentPagerAdapter(supportFragmentManager, fragmentList)

        mBinding.viewPager.offscreenPageLimit = 3
        mBinding.viewPager.adapter = mAdapter

        mBinding.indicatorView.setViewPager(mBinding.viewPager)
    }

    override fun initData() {}
}