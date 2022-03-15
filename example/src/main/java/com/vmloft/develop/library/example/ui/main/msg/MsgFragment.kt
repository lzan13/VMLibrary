package com.vmloft.develop.library.example.ui.main.msg

import android.view.LayoutInflater
import android.view.ViewGroup

import com.vmloft.develop.library.base.BFragment
import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.databinding.FragmentMsgBinding

/**
 * Create by lzan13 on 2020/05/02 11:54
 * 描述：发现
 */
class MsgFragment : BFragment<FragmentMsgBinding>() {

    override fun initVB(inflater: LayoutInflater, parent: ViewGroup?) = FragmentMsgBinding.inflate(inflater, parent, false)

    override fun initUI() {
        super.initUI()
        setTopTitle(R.string.nav_msg)

    }

    override fun initData() {

    }

}