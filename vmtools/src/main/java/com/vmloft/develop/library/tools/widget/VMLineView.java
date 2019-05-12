package com.vmloft.develop.library.tools.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.vmloft.develop.library.tools.R;

/**
 * Create by lzan13 on 2019/5/12 22:25
 *
 * 自定义单行控件，主要用于设置选项
 */
public class VMLineView extends RelativeLayout {

    private ImageView mIconView;

    public VMLineView(Context context) {
        super(context);
    }

    public VMLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VMLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.vm_widget_line_view, this);

    }
}
