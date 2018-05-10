package com.vmloft.develop.library.tools.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lzan13 on 2017/12/8.
 * 为 Adapter 包装加载更多
 */
public class VMMoreWrapper extends RecyclerView.Adapter {

    private final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 1;
    private RecyclerView.Adapter innerAdapter;
    private View loadMoreView;
    private OnLoadMoreListener loadMoreListener;
    private boolean isMore;
    private boolean isEmpty;

    public VMMoreWrapper(RecyclerView.Adapter adapter) {
        innerAdapter = adapter;
    }

    public void updateAdapter(RecyclerView.Adapter adapter) {
        innerAdapter = adapter;
    }

    /**
     * 是否有 loadMoreView
     */
    private boolean hasLoadMore() {
        if (loadMoreView != null) {
            return true;
        }
        return false;
    }

    /**
     * 是否显示 loadMoreView
     */
    private boolean isShowLoadMore(int position) {
        if (hasLoadMore() && position >= getRealItemCount() && getRealItemCount() != 0 && !isEmpty) {
            return true;
        }
        return false;
    }

    /**
     * adapter item 真实个数
     */
    private int getRealItemCount() {
        return innerAdapter.getItemCount();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_LOAD_MORE) {
            return new VMHolder(loadMoreView);
        }
        return innerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isShowLoadMore(position)) {
            if (loadMoreListener != null && isMore) {
                loadMoreListener.onLoadMore();
            }
            return;
        }
        innerAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        if (!isEmpty) {
            return getRealItemCount() + (hasLoadMore() ? 1 : 0);
        } else {
            return getRealItemCount();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowLoadMore(position)) {
            return ITEM_TYPE_LOAD_MORE;
        }
        return innerAdapter.getItemViewType(position);
    }

    /**
     * 刷新 adapter 以及是否有更多数据
     */
    public void refresh(boolean more, boolean empty) {
        isMore = more;
        isEmpty = empty;
        notifyDataSetChanged();
    }

    /**
     * 设置 loadMoreView
     */
    public void setLoadMoreView(View view) {
        loadMoreView = view;
    }

    /**
     * 设置加载更多接口
     */
    public void setLoadMoreListener(OnLoadMoreListener listener) {
        loadMoreListener = listener;
    }

    /**
     * 加载更多接口
     */
    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
