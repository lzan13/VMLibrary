package com.vmloft.develop.library.tools.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Create by lzan13 on 2019/04/09
 * 描述：自定义实现 简单通用 Fragment + ViewPager 适配器，主打一个方便快速使用
 */
class VMViewPagerAdapter(fm: FragmentManager, private val fragments: List<Fragment>) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

}