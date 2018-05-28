package com.vmloft.develop.library.tools.adapter;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lzan13 on 2017/12/8.
 * 为 Adapter 添加 headerView 包装
 */
public class VMHeaderWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_HEADER = 1000;
    private static final int ITEM_TYPE_FOOTER = 2000;

    private SparseArrayCompat<View> headerViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> footerViews = new SparseArrayCompat<>();

    private RecyclerView.Adapter innerAdapter;

    public VMHeaderWrapper(RecyclerView.Adapter adapter) {
        innerAdapter = adapter;
    }

    public void updateAdapter(RecyclerView.Adapter adapter) {
        innerAdapter = adapter;
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
     * headerView 个数
     */
    public int getHeaderCount() {
        return headerViews.size();
    }

    /**
     * footerView 个数
     */
    public int getFooterCount() {
        return footerViews.size();
    }

    /**
     * 列表真实数据个数(去除 header 和 footer)
     */
    private int getRealItemCount() {
        return innerAdapter.getItemCount();
    }

    /**
     * 添加 headerView
     */
    public void addHeaderView(View view) {
        headerViews.put(getHeaderCount() + ITEM_TYPE_HEADER, view);
    }

    /**
     * 移除
     *
     * @param view
     */
    public void removeHeaderView(View view) {
        int index = headerViews.indexOfValue(view);
        headerViews.removeAt(index);
    }

    /**
     * 添加 footerView
     */
    public void addFooterView(View view) {
        headerViews.put(getFooterCount() + ITEM_TYPE_FOOTER, view);
    }

    /**
     * 重写方法，实现 headerView 和 footerView 的包装
     */

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (headerViews.get(viewType) != null) {
            return new VMHolder(headerViews.get(viewType));
        } else if (footerViews.get(viewType) != null) {
            return new VMHolder(footerViews.get(viewType));
        }
        return innerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderView(position)) {
            return;
        }
        if (isFooterView(position)) {
            return;
        }
        innerAdapter.onBindViewHolder(holder, position - getHeaderCount());
    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + getRealItemCount() + getFooterCount();
    }

    /**
     * 重写 Adapter 获取 item 类型的方法，这里 headerView 和 footerView 直接返回的就是添加对应 view 的 key
     */
    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {
            return headerViews.keyAt(position);
        } else if (isFooterView(position)) {
            footerViews.keyAt(position - getHeaderCount() - getRealItemCount());
        }
        return innerAdapter.getItemViewType(position);
    }
}
