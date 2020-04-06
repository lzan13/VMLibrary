package com.vmloft.develop.library.tools.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Created by lzan13 on 2016/7/6.
 * Fragment的基类，进行简单的封装
 */
abstract class VMBFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId(), container, false)
    }

    //    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    //        super.onViewCreated(view, savedInstanceState)
    //        initUI()
    //        initData()
    //    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initUI()
        initData()
    }

    abstract fun layoutId(): Int

    abstract fun initUI()

    abstract fun initData()

}