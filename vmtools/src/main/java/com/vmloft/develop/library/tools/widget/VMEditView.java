package com.vmloft.develop.library.tools.widget;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.vmloft.develop.library.tools.R;

public class VMEditView extends RelativeLayout {

    // 输入框
    private TextInputEditText mEditText;
    // 清空按钮
    private ImageButton mClearBtn;
    // 显示隐藏按钮
    private ImageButton mHideBtn;

    private int mTextSize;

    public VMEditView(Context context) {
        this(context, null);
    }

    public VMEditView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VMEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化方法，获取属性等操作
     */
    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.vm_widget_edit, this);
        mEditText = findViewById(R.id.vm_edit_text);
        mClearBtn = findViewById(R.id.vm_clear_btn);
        mHideBtn = findViewById(R.id.vm_hide_btn);
        mEditText.setTextSize(mTextSize);
    }
}
