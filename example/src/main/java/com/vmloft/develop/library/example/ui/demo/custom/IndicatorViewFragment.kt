package com.vmloft.develop.library.example.ui.demo.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.vmloft.develop.library.common.base.BFragment

import com.vmloft.develop.library.example.R.layout
import com.vmloft.develop.library.example.databinding.FragmentDemoDefaultBinding


/**
 * Create by lzan13 on 2019/04/09
 *
 * 通用界面 Fragment 实现容器
 */
class IndicatorViewFragment : BFragment<FragmentDemoDefaultBinding>() {
    private var mContent: String? = null
    override fun initVB(inflater: LayoutInflater, parent: ViewGroup?) = FragmentDemoDefaultBinding.inflate(inflater, parent, false)

    override fun initUI() {}

    override fun initData() {
        mContent = requireArguments().getString(ARG_CONTENT_ID)
        mBinding.titleTV.text = mContent
    }

    companion object {
        private const val ARG_CONTENT_ID = "arg_content_id"

        /**
         * Fragment 的工厂方法，方便创建并设置参数
         */
        fun newInstance(content: String?): IndicatorViewFragment {
            val fragment = IndicatorViewFragment()
            val args = Bundle()
            args.putString(ARG_CONTENT_ID, content)
            fragment.arguments = args
            return fragment
        }
    }
}