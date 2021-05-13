package com.vmloft.develop.library.common.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

import com.drakeet.multitype.ItemViewDelegate

/**
 * Create by lzan13 on 2020/02/15 17:20
 * 描述：MultiType 适配器 Item 的代理基类类
 */
abstract class BItemDelegate<T, VDB : ViewDataBinding> : ItemViewDelegate<T, BItemDelegate.BItemHolder<VDB>> {
    lateinit var mContext: Context

    // 将事件回调给 View 层监听接口
    var mItemListener: BItemListener<T>? = null
    var mItemPListener: BItemPListener<T>? = null

    constructor() : super()

    constructor(listener: BItemListener<T>) : super() {
        mItemListener = listener
    }

    constructor(listener: BItemPListener<T>) : super() {
        mItemPListener = listener
    }

    override fun onBindViewHolder(holder: BItemHolder<VDB>, item: T) {
        holder.itemView.setOnClickListener {
            mItemListener?.onClick(item)
            mItemPListener?.onClick(item, getPosition(holder))
        }
        onBindView(holder, item)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): BItemHolder<VDB> {
        mContext = context
        var binding: VDB = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            layoutId(),
            parent,
            false
        );
        return BItemHolder(binding)
    }

    /**
     * 获取 Item 布局 uid
     */
    protected abstract fun layoutId(): Int

    /**
     * 绑定定数
     */
    protected abstract fun onBindView(holder: BItemHolder<VDB>, item: T)

    /**
     * ----------------------------------
     * 定义通用的 ViewHolder
     */
    class BItemHolder<B : ViewDataBinding>(val binding: B) : RecyclerView.ViewHolder(binding.root)

    /**
     * 回调给 View 层监听接口
     */
    interface BItemListener<T> {
        fun onClick(data: T)
    }

    interface BItemPListener<T> {
        fun onClick(data: T, position: Int)
    }
}
