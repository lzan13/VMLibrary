package com.vmloft.develop.library.tools.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.RecyclerView;
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
    protected static final int ITEM_TYPE_MORE = Integer.MAX_VALUE - 1;

    protected Activity mActivity;
    protected Context mContext;
    protected LayoutInflater mInflater;

    // HeaderView 集合
    protected SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    // FooterView 集合
    protected SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();
    // 空数据视图
    protected View mEmptyView;
    // 更多视图
    private View mMoreView;
    private OnMoreListener mMoreListener;

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
        if (isEmpty() && viewType == ITEM_TYPE_EMPTY) {
            mEmptyView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
            return new VMHolder(mEmptyView);
        }
        if (mFooterViews.get(viewType) != null) {
            return new VMHolder(mFooterViews.get(viewType));
        }
        if (viewType == ITEM_TYPE_MORE) {
            return new VMHolder(mMoreView);
        }
        return createHolder(root, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull VMHolder holder, final int position) {
        if (isHeaderView(position) || isEmpty() || isFooterView(position)) {
            return;
        }
        if (isMoreView(position)) {
            if (mMoreListener != null) {
                mMoreListener.onLoadMore();
            }
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
        if (isEmpty() && position == getHeaderCount()) {
            return ITEM_TYPE_EMPTY;
        }
        if (isFooterView(position)) {
            return mFooterViews.keyAt(position - getHeaderCount() - getRealItemCount());
        }
        if (isMoreView(position)) {
            return ITEM_TYPE_MORE;
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
            return getHeaderCount() + 1;
        }
        return getHeaderCount() + getRealItemCount() + getFooterCount() + (hasMore() ? 1 : 0);
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
     * 设置 MoreView
     */
    public void setMoreView(View view) {
        mMoreView = view;
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
        return position >= getHeaderCount() + getRealItemCount() && position < getHeaderCount() + getRealItemCount() + getFooterCount();
    }

    /**
     * 是否有 MoreView
     */
    private boolean hasMore() {
        if (mMoreView != null) {
            return true;
        }
        return false;
    }

    /**
     * 是否显示 mMoreView
     */
    private boolean isMoreView(int position) {
        if (!isEmpty() && hasMore() && position >= getHeaderCount() + getRealItemCount() + getFooterCount()) {
            return true;
        }
        return false;
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

    /**
     * 设置加载更多监听
     */
    public void setMoreListener(OnMoreListener listener) {
        mMoreListener = listener;
    }

    public interface OnMoreListener {
        void onLoadMore();
    }
}

