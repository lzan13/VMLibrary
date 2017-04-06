package com.vmloft.develop.library.tools.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lzan13 on 2017/3/25
 *
 * 自定义ViewGroup类，会根据子控件的宽度自动换行，
 */
public class VMViewGroup extends ViewGroup {

    private final int HORIZONTAL_SPACE = 2;
    private final int VERTICAL_SPACE = 2;

    public VMViewGroup(Context context) {
        super(context);
    }

    public VMViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int row = 0;

        // 获取子控件的个数
        int count = getChildCount();

        // 获取控件起始的宽高
        int horizontalWidth = left;
        int verticalHeight = top;

        // 循环设置所有子控件的大小
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            int w = view.getMeasuredWidth();
            int h = view.getMeasuredHeight();
            horizontalWidth += w + HORIZONTAL_SPACE;
            verticalHeight = top + row * (h + VERTICAL_SPACE) + h + VERTICAL_SPACE;

            // 判断当前宽度是否超过了 MLViewGroup的宽度
            if (horizontalWidth > right) {
                row++;
                horizontalWidth = left + w + HORIZONTAL_SPACE;
                verticalHeight = top + row * (h + VERTICAL_SPACE) + h + VERTICAL_SPACE;
            }

            // 设置控件的的大小
            view.layout(horizontalWidth - w, verticalHeight - h, horizontalWidth, verticalHeight);
        }
    }

    /**
     * 重写onMeasure方法，这里循环设置当前自定义控件的子控件的大小
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 循环设置子控件的大小
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        }
    }

    /**
     * 设置 MLViewGroup的子项的点击监听
     */
    public void setItemOnClickListener() {

    }

    public interface OnMLViewGroupListener {
        void onItemClick(View view, int position, long id);
    }
}
