package com.vmloft.develop.library.tools.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.vmloft.develop.library.tools.VMTools

/**
 * Created by lzan13 on 2018/4/26.
 * 颜色工具类
 */
object VMColor {
    /**
     * 通过资源 id 获取颜色值
     */
    fun byRes(resId: Int): Int {
        return byRes(VMTools.context, resId)
    }

    /**
     * 通过资源 id 获取颜色值
     */
    fun byRes(context: Context, resId: Int): Int {
        return ContextCompat.getColor(context, resId)
    }
}