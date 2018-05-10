package com.vmloft.develop.library.tools.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

/**
 * Created by lzan13 on 2018/4/27.
 * Adapter 基类封装，主要是实现 Item 点击事件传递操作
 */
public abstract class VMAdapter<T, VH extends VMHolder> extends RecyclerView.Adapter<VH> {

    private ICListener listener;

    protected LayoutInflater inflater;
    protected List<T> dataList;

    public VMAdapter(Context context, List<T> list) {
        dataList = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, final int position) {
        final T data = getItemData(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemAction(position, data);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) {
                    listener.onItemLongAction(position, data);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 获取当前位置数据
     */
    public T getItemData(int position) {
        return dataList.get(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    /**
     * 刷新方法
     */
    public void refresh() {
        notifyDataSetChanged();
    }


    /**
     * 设置 Item 点击监听
     */
    public void setItemClickListener(ICListener listener) {
        this.listener = listener;
    }

    /**
     * Item 点击监听接口
     */
    public static interface ICListener {
        void onItemAction(int action, Object object);

        void onItemLongAction(int action, Object object);
    }
}

