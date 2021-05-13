package com.vmloft.develop.library.example.ui.main.explore

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.common.base.BaseFragment

/**
 * Create by lzan13 on 2020/05/02 11:54
 * 描述：发现
 */
class ExploreFragment : BaseFragment() {

    override fun layoutId() = R.layout.fragment_explore

    override fun initUI() {
        super.initUI()
        setTopTitle(R.string.nav_explore)

    }

    override fun initData() {

    }

}