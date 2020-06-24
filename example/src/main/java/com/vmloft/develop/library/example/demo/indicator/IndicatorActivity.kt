package com.vmloft.develop.library.example.demo.indicator

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.vmloft.develop.library.example.R.layout
import com.vmloft.develop.library.example.base.AppActivity
import com.vmloft.develop.library.tools.adapter.VMFragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_indicator.indicatorView
import kotlinx.android.synthetic.main.activity_indicator.viewPager
import java.util.ArrayList

/**
 * Create by lzan13 on 2019/04/11
 *
 * 自定义指示器验证示例
 */
@Route(path = "/VMLoft/Indicator")
class IndicatorActivity : AppActivity() {
    private var fragmentList: MutableList<Fragment> = mutableListOf()
    private var mAdapter: VMFragmentPagerAdapter? = null
    override fun layoutId(): Int = layout.activity_indicator

    override fun initUI() {
        super.initUI()
        setTopTitle("自定义指示器")

        fragmentList = ArrayList()
        fragmentList.add(IndicatorFragment.newInstance("第 1 页"))
        fragmentList.add(IndicatorFragment.newInstance("第 2 页"))
        fragmentList.add(IndicatorFragment.newInstance("第 3 页"))
        fragmentList.add(IndicatorFragment.newInstance("第 4 页"))
        fragmentList.add(IndicatorFragment.newInstance("第 5 页"))

        mAdapter = VMFragmentPagerAdapter(supportFragmentManager, fragmentList)

        viewPager.offscreenPageLimit = 3
        viewPager.adapter = mAdapter

        indicatorView.setViewPager(viewPager)
    }

    override fun initData() {}
}