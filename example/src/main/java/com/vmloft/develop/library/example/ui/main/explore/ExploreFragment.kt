package com.vmloft.develop.library.example.ui.main.explore

import android.view.LayoutInflater
import android.view.ViewGroup

import com.vmloft.develop.library.base.BFragment
import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.databinding.FragmentExploreBinding

/**
 * Create by lzan13 on 2020/05/02 11:54
 * 描述：发现
 */
class ExploreFragment : BFragment<FragmentExploreBinding>() {

    override fun initVB(inflater: LayoutInflater, parent: ViewGroup?) = FragmentExploreBinding.inflate(inflater, parent, false)

    override fun initUI() {
        super.initUI()
        setTopTitle(R.string.nav_explore)

    }

    override fun initData() {

    }

}