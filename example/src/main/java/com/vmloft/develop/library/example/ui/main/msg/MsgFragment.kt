package com.vmloft.develop.library.example.ui.main.msg

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.base.BaseFragment

/**
 * Create by lzan13 on 2020/05/02 11:54
 * 描述：发现
 */
class MsgFragment : BaseFragment() {

    override fun layoutId() = R.layout.fragment_msg

    override fun initUI() {
        super.initUI()
        setTopTitle(R.string.nav_msg)

    }

    override fun initData() {

    }

}