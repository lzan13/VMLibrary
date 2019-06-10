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
 * 自定义正方形布局
 */
public class VMSquareLayout extends RelativeLayout {

    // 正方形布局大小为统一宽度
    private boolean isUnifyWidth;

    public VMSquareLayout(Context context) {
        this(context, null);
    }

    public VMSquareLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VMSquareLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        handleAttrs(context, attrs);
    }

    /**
     * 获取资源属性
     *
     * @param context
     * @param attrs
     */
    private void handleAttrs(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VMSquareLayout);
        isUnifyWidth = array.getBoolean(R.styleable.VMSquareLayout_vm_unify_width, true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int childWidthSize = getMeasuredWidth();
        int childHeightSize = getMeasuredHeight();
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY);
        // 高度和宽度一样，否则反过来
        if (isUnifyWidth) {
            heightMeasureSpec = widthMeasureSpec;
        } else {
            widthMeasureSpec = heightMeasureSpec;
        }
        //设定宽高比例
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置是否统一宽度
     */
    public void setUnifyWidth(boolean unify) {
        isUnifyWidth = unify;
        requestLayout();
    }
}
