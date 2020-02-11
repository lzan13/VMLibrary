package com.vmloft.develop.library.tools.adapter;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Create by lzan13 on 2018/9/20 下午7:07
 *
 * 自定义列表布局间隔
 */
public class VMSpaceListDecoration extends RecyclerView.ItemDecoration {

    // 间隔
    private int mSpace;

    public VMSpaceListDecoration(int space) {
        mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int position = parent.getChildAdapterPosition(view);
        final int totalCount = parent.getAdapter().getItemCount();

        int left = mSpace;
        int top = mSpace;
        int bottom;
        int right = mSpace;
        if (position + 1 == totalCount) {
            bottom = mSpace;
        } else {
            top = mSpace;
            bottom = 0;
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
     * 判断是不是最后一行
     *
     * @param parent
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
        if (isVertical(parent)) {
            if ((pos - pos % spanCount) + spanCount >= childCount)
                return true;
        } else {
            if ((pos + 1) % spanCount == 0) {
                return true;
            }
        }
        return false;
    }


    /**
     * 判断是不是最后一列
     *
     * @param parent
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private boolean isLastColumn(RecyclerView parent, int pos, int spanCount, int childCount) {
        if (isVertical(parent)) {
            if ((pos + 1) % spanCount == 0) {
                return true;
            }
        } else {
            childCount = childCount - childCount % spanCount;
            if (pos >= childCount)
                return true;
        }
        return false;
    }
}
