package com.vmloft.develop.library.tools.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzan13 on 2018/4/27.
 * Adapter 基类封装，主要是实现 Item 点击事件传递操作
 */
public abstract class VMAdapter<T, VH extends VMHolder> extends RecyclerView.Adapter<VH> {

    protected Activity mActivity;
    protected Context mContext;
    protected LayoutInflater mInflater;

    /**
     * Item 点击回调
     */
    protected IClickListener mListener;
    /**
     * 适配器数据源
     */
    protected List<T> mDataList;


    public VMAdapter(Context context) {
        mActivity = (Activity) context;
        mContext = context;
        mInflater = LayoutInflater.from(context);

        mDataList = new ArrayList<>();
    }

    public VMAdapter(Context context, List<T> list) {
        mActivity = (Activity) context;
        mContext = context;
        mInflater = LayoutInflater.from(context);

        if (list == null || list.size() == 0) {
            mDataList = new ArrayList<>();
        } else {
            mDataList = list;
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, final int position) {
        final T data = getItemData(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemAction(position, data);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mListener != null) {
                    return mListener.onItemLongAction(position, data);
                }
                return false;
            }
        });
    }

    /**
     * 获取当前位置数据
     */
    public T getItemData(int position) {
        if (position < getItemCount()) {
            return mDataList.get(position);
        }
        return null;
    }

    /**
     * 获取当前数据源数量
     */
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 刷新数据方法
     */
    public void refresh(List<T> list) {
        if (list == null || list.size() == 0) {
            mDataList.clear();
        } else {
            mDataList.clear();
            mDataList.addAll(list);
        }
        notifyDataSetChanged();
    }


    /**
     * 设置 Item 点击监听
     */
    public void setClickListener(IClickListener listener) {
        this.mListener = listener;
    }

    /**
     * Item 点击监听接口
     */
    public static interface IClickListener<T> {

        void onItemAction(int action, T t);

        boolean onItemLongAction(int action, T t);
    }
}

