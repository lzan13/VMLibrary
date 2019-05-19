package com.vmloft.develop.library.tools.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.utils.VMColor;
import com.vmloft.develop.library.tools.utils.VMStr;

/**
 * Create by lzan13 on 2019/5/19 12:36
 *
 * 自定义 VMTopBar 控件
 */
public class VMTopBar extends RelativeLayout {

    // 控件
    private ImageButton mIconBtn;
    private TextView mTitleView;
    private TextView mSubtitleView;
    private LinearLayout mCustomContainer;
    private Button mEndBtn;
    private ImageButton mEndIconBtn;

    // 设置数据
    private int mIcon;
    private String mTitle;
    private String mSubtitle;
    private String mEndText;
    private int mEndIcon;
    private int mTitleColor;
    private int mBGColor;

    public VMTopBar(Context context) {
        this(context, null);
    }

    public VMTopBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VMTopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.vm_widget_top_bar, this);

        mIconBtn = findViewById(R.id.vm_top_bar_icon);
        mTitleView = findViewById(R.id.vm_top_bar_title_tv);
        mSubtitleView = findViewById(R.id.vm_top_bar_subtitle_tv);
        mCustomContainer = findViewById(R.id.vm_top_bar_custom_container);
        mEndBtn = findViewById(R.id.vm_top_bar_end_btn);
        mEndIconBtn = findViewById(R.id.vm_top_bar_end_icon);

        mTitleColor = VMColor.byRes(R.color.vm_btn_text_dark);
        mBGColor = VMColor.byRes(R.color.vm_theme_background);

        // 获取控件的属性值
        handleAttrs(context, attrs);

        if (mIcon == 0) {
            mIconBtn.setVisibility(GONE);
        } else {
            mIconBtn.setVisibility(VISIBLE);
            mIconBtn.setImageResource(mIcon);
        }

        if (!VMStr.isEmpty(mTitle)) {
            mTitleView.setText(mTitle);
        }

        if (!VMStr.isEmpty(mSubtitle)) {
            mSubtitleView.setVisibility(VISIBLE);
            mSubtitleView.setText(mSubtitle);
        } else {
            mSubtitleView.setVisibility(GONE);
        }

        if (!VMStr.isEmpty(mEndText)) {
            mEndBtn.setVisibility(VISIBLE);
            mEndBtn.setText(mEndText);
        } else {
            mEndBtn.setVisibility(GONE);
        }

        if (mEndIcon == 0) {
            mEndIconBtn.setVisibility(GONE);
        } else {
            mEndIconBtn.setVisibility(VISIBLE);
            mEndIconBtn.setImageResource(mEndIcon);
        }

        setupColor();
    }

    /**
     * 装载控件颜色
     */
    private void setupColor() {
        setBackgroundColor(mBGColor);
        mTitleView.setTextColor(mTitleColor);
        mSubtitleView.setTextColor(mTitleColor);
        // 对图标着色
        mIconBtn.setImageTintList(ColorStateList.valueOf(mTitleColor));
        mEndIconBtn.setImageTintList(ColorStateList.valueOf(mTitleColor));
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
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VMTopBar);
        // 获取自定义属性值，如果没有设置就是默认值
        mIcon = array.getResourceId(R.styleable.VMTopBar_vm_top_bar_icon, mIcon);
        mTitle = array.getString(R.styleable.VMTopBar_vm_top_bar_title);
        mSubtitle = array.getString(R.styleable.VMTopBar_vm_top_bar_subtitle);
        mEndText = array.getString(R.styleable.VMTopBar_vm_top_bar_end_btn);
        mEndIcon = array.getResourceId(R.styleable.VMTopBar_vm_top_bar_end_icon, mEndIcon);
        mTitleColor = array.getColor(R.styleable.VMTopBar_vm_top_bar_title_color, mTitleColor);
        mBGColor = array.getColor(R.styleable.VMTopBar_vm_top_bar_bg_color, mBGColor);
        // 回收资源
        array.recycle();
    }

    /**
     * 设置图标
     */
    public void setIcon(int resId) {
        mIcon = resId;
        if (mIcon == 0) {
            mIconBtn.setVisibility(GONE);
        } else {
            mIconBtn.setVisibility(VISIBLE);
            mIconBtn.setImageResource(mIcon);
        }
    }

    /**
     * 设置标题
     */
    public void setTitle(int resId) {
        mTitle = VMStr.byRes(resId);
        mTitleView.setText(mTitle);
    }

    /**
     * 设置标题
     */
    public void setTitle(String title) {
        mTitle = title;
        if (!VMStr.isEmpty(mTitle)) {
            mTitleView.setText(mTitle);
        }
    }

    /**
     * 设置子标题
     */
    public void setSubtitle(String title) {
        mSubtitle = title;
        if (VMStr.isEmpty(mSubtitle)) {
            mTitleView.setVisibility(GONE);
        } else {
            mTitleView.setVisibility(VISIBLE);
            mTitleView.setText(mSubtitle);
        }
    }

    /**
     * 设置自定义控件
     */
    public void setCustomView(View view) {
        if (view != null) {
            mCustomContainer.removeAllViews();
            mCustomContainer.addView(view);
        }
    }

    /**
     * 设置图标
     */
    public void setEndIcon(int resId) {
        mEndIcon = resId;
        if (mEndIcon == 0) {
            mEndIconBtn.setVisibility(GONE);
        } else {
            mEndIconBtn.setVisibility(VISIBLE);
            mEndIconBtn.setImageResource(mEndIcon);
        }
    }

    /**
     * 设置标题颜色
     */
    public void setTitleCOlor(int resId) {
        if (resId != 0) {
            mTitleColor = VMColor.byRes(resId);
            setupColor();
        }
    }

    /**
     * 设置背景色
     */
    public void setBGColor(int resId) {
        if (resId != 0) {
            mBGColor = VMColor.byRes(resId);
            setupColor();
        }
    }

    /**
     * 设置图标点击监听
     */
    public void setIconListener(OnClickListener listener) {
        mIconBtn.setOnClickListener(listener);
    }

    /**
     * 设置右侧图标点击监听
     */
    public void setEndIconListener(OnClickListener listener) {
        mEndBtn.setOnClickListener(listener);
    }

    /**
     * 设置右侧按钮状态
     */
    public void setEndBtnEnable(boolean enable) {
        mEndBtn.setEnabled(enable);
    }

    /**
     * 设置右侧的按钮
     */
    public void setEndBtn(String text) {
        mEndText = text;
        if (VMStr.isEmpty(mEndText)) {
            mEndBtn.setVisibility(GONE);
        } else {
            mEndBtn.setVisibility(VISIBLE);
            mEndBtn.setText(mEndText);
        }
    }

    /**
     * 设置右侧的按钮点击的监听
     *
     * @param listener 回调接口
     */
    public void setEndBtnListener(OnClickListener listener) {
        setEndBtnListener(mEndText, listener);
    }

    /**
     * 设置消极的按钮点击监听
     *
     * @param str      文本
     * @param listener 回调接口
     */
    public void setEndBtnListener(String str, OnClickListener listener) {
        mEndText = str;
        mEndBtn.setText(mEndText);
        mEndBtn.setOnClickListener(listener);
    }
}