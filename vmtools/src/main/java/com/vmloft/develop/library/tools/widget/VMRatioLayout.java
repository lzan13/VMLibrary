package com.vmloft.develop.library.tools.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.vmloft.develop.library.tools.R;

/**
 * Create by lzan13 on 2019/9/20 18:50
 *
 * 自定义正方形布局s
 */
public class VMRatioLayout extends RelativeLayout {
    // 正方形布局大小为统一宽度
    private float ratio = 1.0f;
    // 跟随宽度计算
    private boolean isFollowWidth = true;

    public VMRatioLayout(Context context) {
        this(context, null);
    }

    public VMRatioLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VMRatioLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        handleAttrs(context, attrs);
    }

    /**
     * 获取资源属性
     */
    private void handleAttrs(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VMRatioLayout);
        ratio = array.getFloat(R.styleable.VMRatioLayout_vm_layout_ratio, ratio);
        isFollowWidth = array.getBoolean(R.styleable.VMRatioLayout_vm_follow_width, isFollowWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int childWidthSize = getMeasuredWidth();
        int childHeightSize = getMeasuredHeight();
        // 根据属性计算高度和宽度
        if (isFollowWidth) {
            childHeightSize = (int) (childWidthSize * ratio);
        } else {
            childWidthSize = (int) (childHeightSize * ratio);
        }
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY);

        //设定宽高比例
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置比例
     */
    public void setRatio(float ratio) {
        this.ratio = ratio;
        requestLayout();
    }

    /**
     * 设置跟随宽度
     */
    public void setFollowWidth(boolean unify) {
        isFollowWidth = unify;
        requestLayout();
    }
}
