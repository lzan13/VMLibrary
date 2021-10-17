package com.vmloft.develop.library.common.ui.display

import android.view.LayoutInflater
import android.view.ViewGroup
import com.vmloft.develop.library.common.base.BItemDelegate
import com.vmloft.develop.library.common.databinding.DisplayItemDelegateBinding


/**
 * Create by lzan13 on 2020/02/15 17:56
 * 描述：展示美女
 */
class DisplayItemDelegate : BItemDelegate<String, DisplayItemDelegateBinding>() {

    override fun onBindView(holder: BItemHolder<DisplayItemDelegateBinding>, item: String) {
    }

    override fun initVB(inflater: LayoutInflater, parent: ViewGroup) = DisplayItemDelegateBinding.inflate(inflater, parent, false)
}