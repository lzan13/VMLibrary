package com.vmloft.develop.library.tools.base

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Create by lzan13 on 2019/05/19 17:57
 * 自定义一个通用的基类
 */
abstract class VMBActivity : AppCompatActivity() {
    open var mActivity: Activity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivity = this

        setContentView(layoutId())

        initUI()

        initData()
    }

    /**
     * 加载布局 id
     *
     * @return 返回布局 id
     */
    abstract fun layoutId(): Int

    /**
     * 初始化 UI
     */
    abstract fun initUI()

    /**
     * 初始化数据
     */
    abstract fun initData()
}