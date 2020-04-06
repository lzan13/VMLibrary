package com.vmloft.develop.library.tools.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Create by lzan13 on 2019/04/09
 *
 * 简单通用 Fragment + ViewPager 适配器
 */
class VMFragmentPagerAdapter(fm: FragmentManager?, private val fragments: List<Fragment>) : FragmentPagerAdapter(fm!!) {
    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

}