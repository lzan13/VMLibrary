package com.vmloft.develop.library.tools.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lzan13 on 2017/12/9.
 * 为 Adapter 添加空数据包装
 */
public class VMEmptyWrapper extends RecyclerView.Adapter {

    private final int ITEM_TYPE_EMPTY = 3000;
    private RecyclerView.Adapter innerAdapter;
    private View emptyView;
    private int emptyLayoutId;

    public VMEmptyWrapper(RecyclerView.Adapter adapter) {
        innerAdapter = adapter;
    }

    public void updateAdapter(RecyclerView.Adapter adapter) {
        innerAdapter = adapter;
    }

    private boolean isEmpty() {
        return (emptyView != null || emptyLayoutId != 0) && getRealItemCount() == 0;
    }

    /**
     * 设置空布局
     */
    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    public void setEmptyView(int layoutId) {
        emptyLayoutId = layoutId;
    }

    /**
     * 获取真实 Item 个数
     */
    private int getRealItemCount() {
        return innerAdapter.getItemCount();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isEmpty()) {
            if (emptyView != null) {
                return new VMViewHolder(emptyView);
            }
            if (emptyLayoutId != 0) {
                View view = LayoutInflater.from(parent.getContext())
                                          .inflate(emptyLayoutId, parent, false);
                return new VMViewHolder(view);
            }
        }
        return innerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isEmpty()) {
            return;
        }
        innerAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        if (isEmpty()) {
            return ITEM_TYPE_EMPTY;
        }
        return innerAdapter.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (isEmpty()) {
            return 1;
        }
        return innerAdapter.getItemCount();
    }
}
