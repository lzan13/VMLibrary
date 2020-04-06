package com.vmloft.develop.library.tools

import android.content.Context
import com.vmloft.develop.library.tools.base.VMApp

/**
 * Created by lzan13 on 2018/4/16.
 *
 * 工具初始化入口
 */
object VMTools {
    private lateinit var mContext: Context

    /**
     * 初始化工具类库
     */
    @JvmStatic
    fun init(context: Context) {
        mContext = context
    }

    /**
     * 获取工具类库当前保存的上下文对象:
     * 如果没有进行初始化就从自定义的 VMApp 获取，如果项目也没有继承自 VMApp 则为空
     * 这个主要是为了方便工具类库中的其他接口直接使用上下文对象，不需要在调用相关方法时都传递上下文对象
     */
    @JvmStatic val context: Context
        get() {
            if (mContext == null) {
                mContext = VMApp.appContext
            }
            if (mContext == null) {
                throw NullPointerException("使用 VMTools 自定义库，需先调用 VMTools.init() 初始化或继承自 VMApp 类")
            }
            return mContext
        }
}