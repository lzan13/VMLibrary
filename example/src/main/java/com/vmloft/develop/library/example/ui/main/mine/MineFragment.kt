package com.vmloft.develop.library.example.ui.main.mine


import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.base.BaseFragment
import com.vmloft.develop.library.example.router.AppRouter
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * Create by lzan13 on 2020/05/02 11:54
 * 描述：我的
 */
class MineFragment : BaseFragment() {


    override fun layoutId() = R.layout.fragment_mine

    override fun initUI() {
        super.initUI()

        mineSettingsLV.setOnClickListener { AppRouter.goSettings() }
    }

    override fun initData() {

    }

}