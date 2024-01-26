package com.vmloft.develop.library.tools.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Create by lzan13 on 2024/01/13
 * 描述：自定义实现 简单通用 Fragment + ViewPager2 适配器，主打一个方便快速使用
 */
class VMViewPager2Adapter : FragmentStateAdapter {
    val fragments = mutableListOf<Fragment>()

    constructor(list: List<Fragment>, fragmentActivity: FragmentActivity) : super(fragmentActivity) {
        initAdapter(list)
    }

    constructor(list: List<Fragment>, fragment: Fragment) : super(fragment) {
        initAdapter(list)
    }

    constructor(list: List<Fragment>, fragmentManager: FragmentManager, lifecycle: Lifecycle) : super(fragmentManager, lifecycle) {
        initAdapter(list)
    }

    private fun initAdapter(list: List<Fragment>) {
        fragments.addAll(list)
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size
    }
}