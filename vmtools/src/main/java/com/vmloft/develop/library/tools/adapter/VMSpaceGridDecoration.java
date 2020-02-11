package com.vmloft.develop.library.tools.adapter;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Create by lzan13 on 2018/9/20 下午7:07
 *
 * 自定义网格布局间隔
 */
public class VMSpaceGridDecoration extends RecyclerView.ItemDecoration {

    // 列
    private int mColumn;
    // 列表项总数
    private int mTotalCount;
    // 间隔
    private int mSpace;

    public VMSpaceGridDecoration(int column, int space) {
        mColumn = column;
        mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int position = parent.getChildAdapterPosition(view);
        mTotalCount = parent.getAdapter().getItemCount();

        int left, top, bottom, right;
        // 左右
        if (isFirstColumn(parent, position) && isLastColumn(parent, position)) {
            left = 0;
            right = 0;
        } else {
            left = position % mColumn * mSpace / mColumn;
            right = (mColumn - 1 - position % mColumn) * mSpace / mColumn;
        }
        // 上下
        if (isFirstRow(parent, position) && isLastRow(parent, position)) {
            top = 0;
            bottom = 0;
        } else {
            top = position / mColumn * mSpace / mColumn;
            bottom = (mColumn - 1 - position / mColumn) * mSpace / mColumn;
        }
        outRect.set(left, top, right, bottom);
    }

    /**
     * 判断列表方向
     *
     * @param parent
     * @return
     */
    private boolean isVertical(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int orientation = ((GridLayoutManager) layoutManager).getOrientation();
            return orientation == StaggeredGridLayoutManager.VERTICAL;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            return orientation == StaggeredGridLayoutManager.VERTICAL;
        }
        return false;
    }

    /**
     * 判断是不是第一行
     */
    private boolean isFirstRow(RecyclerView parent, int position) {
        if (isVertical(parent)) {
            if (position < mColumn) {
                return true;
            }
        } else {
            if (position % mColumn == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是不是第一列
     */
    private boolean isFirstColumn(RecyclerView parent, int position) {
        if (isVertical(parent)) {
            if (position % mColumn == 0) {
                return true;
            }
        } else {
            if (position < mColumn) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是不是最后一行
     */
    private boolean isLastRow(RecyclerView parent, int position) {
        if (isVertical(parent)) {
            if ((position - position % mColumn) + mColumn >= mTotalCount)
                return true;
        } else {
            if ((position + 1) % mColumn == 0) {
                return true;
            }
        }
        return false;
    }


    /**
     * 判断是不是最后一列
     */
    private boolean isLastColumn(RecyclerView parent, int position) {
        if (isVertical(parent)) {
            if ((position + 1) % mColumn == 0) {
                return true;
            }
        } else {
            mTotalCount = mTotalCount - mTotalCount % mColumn;
            if (position >= mTotalCount)
                return true;
        }
        return false;
    }
}
