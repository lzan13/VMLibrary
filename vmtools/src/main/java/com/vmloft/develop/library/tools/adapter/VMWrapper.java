package com.vmloft.develop.library.tools.adapter;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by lzan13 on 2017/12/9.
 * 包装 Adapter
 */
public class VMWrapper<T> extends RecyclerView.Adapter<VMHolder> {

    public static final int ITEM_TYPE_EMPTY = 1000;
    public static final int ITEM_TYPE_HEADER = 2000;
    public static final int ITEM_TYPE_FOOTER = 3000;
    public static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 1;

    public VMAdapter mAdapter;

    // HeaderView 集合
    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    // FooterView 集合
    private SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();
    // 空数据视图
    public View mEmptyView;

    public VMWrapper(VMAdapter adapter) {
        mAdapter = adapter;
    }

    /**
     * 设置空布局
     */
    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }

    @Override
    public VMHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            return new VMHolder(mHeaderViews.get(viewType));
        }
        if (isEmpty()) {
            return new VMHolder(mEmptyView);
        }
        if (mFooterViews.get(viewType) != null) {
            return new VMHolder(mFooterViews.get(viewType));
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(VMHolder holder, int position) {
        if (isEmpty() || isHeaderView(position) || isFooterView(position)) {
            return;
        }
        mAdapter.onBindViewHolder(holder, position - getHeaderCount());
    }

    /**
     * 获取 Item 个数
     */
    @Override
    public int getItemCount() {
        if (isEmpty()) {
            return getHeaderCount() + 1 + getFooterCount();
        }
        return getHeaderCount() + getRealItemCount() + getFooterCount();
    }

    /**
     * 获取 Item 类型
     */
    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {
            return mHeaderViews.keyAt(position);
        }
        if (isFooterView(position)) {
            return mFooterViews.keyAt(position - getHeaderCount() - getRealItemCount());
        }
        if (isEmpty()) {
            return ITEM_TYPE_EMPTY;
        }
        return mAdapter.getItemViewType(position - getHeaderCount());
    }

    /**
     * 添加 Header View
     */
    public void addHeaderView(View view) {
        mHeaderViews.put(getHeaderCount() + ITEM_TYPE_HEADER, view);
    }

    /**
     * 移除 Header View
     */
    public void removeHeaderView(View view) {
        int index = mHeaderViews.indexOfValue(view);
        mHeaderViews.removeAt(index);
    }

    /**
     * 添加 Footer View
     */
    public void addFooterView(View view) {
        mFooterViews.put(getFooterCount() + ITEM_TYPE_FOOTER, view);
    }

    /**
     * 删除 Footer View
     */
    public void removeFooterView(View view) {
        int index = mFooterViews.indexOfValue(view);
        mFooterViews.removeAt(index);
    }

    /**
     * 刷新 Adapter
     */
    public void refresh() {
        notifyDataSetChanged();
    }

    /**
     * 刷新 Adapter
     */
    public void refresh(List<T> list) {
        mAdapter.refresh(list);
        notifyDataSetChanged();
    }

    /**
     * Header View 个数
     */
    public int getHeaderCount() {
        return mHeaderViews.size();
    }

    /**
     * Footer View 个数
     */
    public int getFooterCount() {
        return mFooterViews.size();
    }

    /**
     * 判断是否为空
     */
    private boolean isEmpty() {
        return mEmptyView != null && getRealItemCount() == 0;
    }

    /**
     * 判断是不是 headerView
     */
    private boolean isHeaderView(int position) {
        return position < getHeaderCount();
    }

    /**
     * 判断是不是 footerView
     */
    private boolean isFooterView(int position) {
        return position >= getHeaderCount() + getRealItemCount();
    }

    /**
     * 获取真实 Item 个数
     */
    private int getRealItemCount() {
        return mAdapter.getItemCount();
    }
}
