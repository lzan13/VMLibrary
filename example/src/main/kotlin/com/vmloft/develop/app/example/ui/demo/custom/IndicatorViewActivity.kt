package com.vmloft.develop.app.example.ui.demo.custom

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.vmloft.develop.library.base.BActivity

import com.vmloft.develop.app.example.databinding.ActivityDemoViewIndicatorBinding
import com.vmloft.develop.app.example.router.AppRouter
import com.vmloft.develop.library.tools.adapter.VMViewPager2Adapter
import com.vmloft.develop.library.tools.adapter.VMViewPagerAdapter

import java.util.ArrayList

/**
 * Create by lzan13 on 2019/04/11
 *
 * 自定义指示器验证示例
 */
@Route(path = AppRouter.appIndicator)
class IndicatorViewActivity : BActivity<ActivityDemoViewIndicatorBinding>() {

    override fun initVB() = ActivityDemoViewIndicatorBinding.inflate(layoutInflater)

    override fun initUI() {
        super.initUI()
        setTopTitle("自定义指示器")

        initViewPager1()
        initViewPager2()
        initViewPager3()
        initViewPager4()
    }

    override fun initData() {}

    /**
     * 初始化 ViewPager
     */
    private fun initViewPager1() {

        var fragmentList1: MutableList<Fragment> = mutableListOf()

        fragmentList1.add(IndicatorViewFragment.newInstance("第 1 页"))
        fragmentList1.add(IndicatorViewFragment.newInstance("第 2 页"))
        fragmentList1.add(IndicatorViewFragment.newInstance("第 3 页"))

        var mAdapter1 = VMViewPagerAdapter(supportFragmentManager, fragmentList1)

        mBinding.viewPager1.offscreenPageLimit = 3
        mBinding.viewPager1.adapter = mAdapter1

        mBinding.indicatorView1.setViewPager(mBinding.viewPager1)
    }

    /**
     * 初始化 ViewPager2
     */
    private fun initViewPager2() {

        var fragmentList2: MutableList<Fragment> = mutableListOf()

        fragmentList2.add(IndicatorViewFragment.newInstance("第 4 页"))
        fragmentList2.add(IndicatorViewFragment.newInstance("第 5 页"))
        fragmentList2.add(IndicatorViewFragment.newInstance("第 6 页"))

        var mAdapter2 = VMViewPager2Adapter(fragmentList2, this)

        mBinding.viewPager2.offscreenPageLimit = 3
        mBinding.viewPager2.adapter = mAdapter2

        mBinding.indicatorView2.setViewPager(mBinding.viewPager2)
    }

    /**
     * 初始化 ViewPager2
     */
    private fun initViewPager3() {

        var fragmentList3: MutableList<Fragment> = mutableListOf()

        fragmentList3.add(IndicatorViewFragment.newInstance("第 7 页"))
        fragmentList3.add(IndicatorViewFragment.newInstance("第 8 页"))
        fragmentList3.add(IndicatorViewFragment.newInstance("第 9 页"))

        var mAdapter3 = VMViewPager2Adapter(fragmentList3, this)

        mBinding.viewPager3.offscreenPageLimit = 3
        mBinding.viewPager3.adapter = mAdapter3

        mBinding.indicatorView3.setViewPager(mBinding.viewPager3)
    }

    /**
     * 初始化自定义个数
     */
    private fun initViewPager4() {
        mBinding.previousTV.setOnClickListener { mBinding.indicatorView4.switchPrevious() }
        mBinding.positionTV.setOnClickListener { mBinding.indicatorView4.switchPosition(2) }
        mBinding.nextTV.setOnClickListener { mBinding.indicatorView4.switchNext() }

        mBinding.indicatorView4.setIndicatorCount(5)
    }
}