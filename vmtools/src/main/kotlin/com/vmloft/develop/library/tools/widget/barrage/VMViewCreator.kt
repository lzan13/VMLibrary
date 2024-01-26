package com.vmloft.develop.library.tools.widget.barrage

import android.view.View

/**
 * Create by lzan13 on 2020/6/3 11:50
 * 描述：弹幕 Item 创建接口
 */
interface VMViewCreator<T> {
    fun layoutId(): Int

    fun onBind(itemView: View, data: T)
}