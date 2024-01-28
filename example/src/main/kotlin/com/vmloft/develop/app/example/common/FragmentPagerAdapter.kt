package com.vmloft.develop.app.example.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import java.util.*

/**
 * Created by liyongan on 19/3/5.
 */
class FragmentPagerAdapter(fm: FragmentManager, fragments: ArrayList<Fragment>) : FragmentPagerAdapter(fm) {
    private val fragments: ArrayList<Fragment> = fragments

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

}