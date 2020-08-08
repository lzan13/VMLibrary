package com.vmloft.develop.library.example.ui.main.mine

import android.view.View

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.base.BaseFragment
import com.vmloft.develop.library.example.router.AppRouter
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.widget_common_top_bar.*

/**
 * Create by lzan13 on 2020/05/02 11:54
 * 描述：我的
 */
class MineFragment : BaseFragment() {


    override fun layoutId() = R.layout.fragment_mine

    override fun initUI() {
        super.initUI()
        setTopTitle(R.string.nav_mine)
        commonTopBar.visibility = View.GONE

        mineSettingsLV.setOnClickListener { AppRouter.goSettings() }
    }

    override fun initData() {

    }

}