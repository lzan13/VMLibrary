package com.vmloft.develop.library.tools.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
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
public abstract class VMAdapter<T, VH extends VMHolder> extends RecyclerView.Adapter<VMHolder> {

    protected static final int ITEM_TYPE_DEFAULT = 100;
    protected static final int ITEM_TYPE_EMPTY = 1000;
    protected static final int ITEM_TYPE_HEADER = 2000;
    protected static final int ITEM_TYPE_FOOTER = 3000;
    protected static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 1;

    protected Activity mActivity;
    protected Context mContext;
    protected LayoutInflater mInflater;

    // HeaderView 集合
    protected SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    // FooterView 集合
    protected SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();
    // 空数据视图
    protected View mEmptyView;

    // Item 点击回调
    protected IClickListener mClickListener;
    protected ILongClickListener mLongClickListener;
    // 适配器数据源
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
    public VMHolder onCreateViewHolder(@NonNull ViewGroup root, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            return new VMHolder(mHeaderViews.get(viewType));
        }
        if (isEmpty()) {
            return new VMHolder(mEmptyView);
        }
        if (mFooterViews.get(viewType) != null) {
            return new VMHolder(mFooterViews.get(viewType));
        }
        return createHolder(root, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull VMHolder holder, final int position) {
        if (isEmpty() || isHeaderView(position) || isFooterView(position)) {
            return;
        }
        final T data = getItemData(position - getHeaderCount());
        holder.itemView.setOnClickListener(v -> {
            if (mClickListener != null) {
                mClickListener.onItemClick(position, data);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (mLongClickListener != null) {
                return mLongClickListener.onItemLongClick(position, data);
            }
            return false;
        });
        bindHolder((VH) holder, position - getHeaderCount());
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
        return getItemType(position - getHeaderCount());
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
        if (isEmpty()) {
            return getHeaderCount() + 1 + getFooterCount();
        }
        return getHeaderCount() + getRealItemCount() + getFooterCount();
    }

    /**
     * 刷新
     */
    public void refresh() {
        notifyDataSetChanged();
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
     * 设置空布局
     */
    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
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
        return mDataList.size();
    }

    /**
     * 创建 VMHolder
     */
    public abstract VH createHolder(@NonNull ViewGroup root, int viewType);

    /**
     * 绑定 VMHolder
     */
    public abstract void bindHolder(@NonNull VH holder, int position);

    /**
     * 获取 Item 类型
     */
    public int getItemType(int position) {
        return ITEM_TYPE_DEFAULT;
    }

    /**
     * ------------------------------------------------------
     * 设置 Item 点击监听
     */
    public void setClickListener(IClickListener listener) {
        this.mClickListener = listener;
    }

    public static interface IClickListener<T> {
        void onItemClick(int action, T t);
    }

    /**
     * 设置 Item 长按监听
     */
    public void setLongClickListener(ILongClickListener listener) {
        this.mLongClickListener = listener;
    }

    public interface ILongClickListener<T> {
        boolean onItemLongClick(int action, T t);
    }
}

