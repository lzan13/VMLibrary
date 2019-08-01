package com.vmloft.develop.library.tools.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.utils.VMStr;

/**
 * Create by lzan13 on 2019/5/12 22:25
 *
 * 自定义单行控件，主要用于设置选项
 */
public class VMLineView extends RelativeLayout {

    // 左侧图标
    private ImageView mIconView;
    // 标题
    private TextView mTitleView;
    // 说明图标
    private ImageView mCaptionIconView;
    // 说明文本
    private TextView mCaptionView;
    // 右侧图标
    private ImageView mRightIconView;
    // 底部描述文本
    private TextView mDescriptionView;
    // 分割线
    private View mDecorationView;

    private int mIconRes;
    private String mTitle;
    private int mTitleColor;
    private int mCaptionIconRes;
    private String mCaption;
    private int mRightIconRes;
    private String mDescription;
    private boolean mDecoration;

    public VMLineView(Context context) {
        this(context, null);
    }

    public VMLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VMLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     */
    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.vm_widget_line_view, this);

        mIconView = findViewById(R.id.vm_line_icon_iv);
        mTitleView = findViewById(R.id.vm_line_title_tv);
        mCaptionIconView = findViewById(R.id.vm_line_caption_icon_iv);
        mCaptionView = findViewById(R.id.vm_line_caption_tv);
        mRightIconView = findViewById(R.id.vm_line_right_icon_iv);
        mDescriptionView = findViewById(R.id.vm_line_description_tv);
        mDecorationView = findViewById(R.id.vm_line_decoration);

        // 获取控件的属性值
        handleAttrs(context, attrs);

        setClickable(true);

        setupView();
    }

    /**
     * 获取资源属性
     */
    private void handleAttrs(Context context, AttributeSet attrs) {
        // 获取控件的属性值
        if (attrs == null) {
            return;
        }

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VMLineView);
        mIconRes = array.getResourceId(R.styleable.VMLineView_vm_line_icon, mIconRes);
        mTitle = array.getString(R.styleable.VMLineView_vm_line_title);
        mTitleColor = array.getColor(R.styleable.VMLineView_vm_line_title_color, mTitleColor);
        mCaptionIconRes = array.getResourceId(R.styleable.VMLineView_vm_line_caption_icon, mCaptionIconRes);
        mCaption = array.getString(R.styleable.VMLineView_vm_line_caption);
        mRightIconRes = array.getResourceId(R.styleable.VMLineView_vm_line_right_icon, mRightIconRes);
        mDescription = array.getString(R.styleable.VMLineView_vm_line_description);
        mDecoration = array.getBoolean(R.styleable.VMLineView_vm_line_decoration, mDecoration);
        array.recycle();
    }

    /**
     * 装载控件内容
     */
    private void setupView() {
        if (mIconRes == 0) {
            mIconView.setVisibility(GONE);
        } else {
            mIconView.setVisibility(VISIBLE);
            mIconView.setImageResource(mIconRes);
        }
        if (!VMStr.isEmpty(mTitle)) {
            mTitleView.setText(mTitle);
        }
        if (mTitleColor != 0) {
            mTitleView.setTextColor(mTitleColor);
        }

        if (mCaptionIconRes == 0) {
            mCaptionIconView.setVisibility(GONE);
        } else {
            mCaptionIconView.setVisibility(VISIBLE);
            mCaptionIconView.setImageResource(mCaptionIconRes);
        }
        if (VMStr.isEmpty(mCaption)) {
            mCaptionView.setVisibility(GONE);
        } else {
            mCaptionView.setVisibility(VISIBLE);
            mCaptionView.setText(mCaption);
        }
        if (mRightIconRes == 0) {
            mRightIconView.setVisibility(GONE);
        } else {
            mRightIconView.setVisibility(VISIBLE);
            mRightIconView.setImageResource(mRightIconRes);
        }
        if (VMStr.isEmpty(mDescription)) {
            mDescriptionView.setVisibility(GONE);
        } else {
            mDescriptionView.setVisibility(VISIBLE);
            mDescriptionView.setText(mDescription);
        }
        if (mDecoration) {
            mDecorationView.setVisibility(VISIBLE);
        } else {
            mDecorationView.setVisibility(GONE);
        }
    }

    /**
     * ----------------------------------- 内容设置 -----------------------------------
     *
     * 设置图标
     */
    public void setIconRes(@DrawableRes int resId) {
        mIconRes = resId;
        if (mIconRes == 0) {
            mIconView.setVisibility(GONE);
            return;
        }
        mIconView.setVisibility(VISIBLE);
        mIconView.setImageResource(mIconRes);
    }

    /**
     * 设置标题
     */
    public void setTitle(String title) {
        mTitle = title;
        mTitleView.setText(mTitle);
    }

    /**
     * 设置标题颜色
     */
    public void setTitleColor(int color) {
        mTitleColor = color;
        mTitleView.setTextColor(mTitleColor);
    }

    /**
     * 设置标题样式
     */
    public void setTitleStyle(int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTitleView.setTextAppearance(resId);
        } else {
            mTitleView.setTextAppearance(getContext(), resId);
        }
    }

    /**
     * 设置右侧描述图标
     */
    public void setCaptionIcon(@DrawableRes int resId) {
        mCaptionIconRes = resId;
        if (mCaptionIconRes == 0) {
            mCaptionIconView.setVisibility(GONE);
            return;
        }
        mCaptionIconView.setVisibility(VISIBLE);
        mCaptionIconView.setImageResource(mCaptionIconRes);
    }

    /**
     * 设置右侧描述内容
     */
    public void setCaption(String caption) {
        mCaption = caption;
        if (VMStr.isEmpty(mCaption)) {
            mCaptionView.setVisibility(GONE);
            return;
        }
        mCaptionView.setVisibility(VISIBLE);
        mCaptionView.setText(mCaption);
    }

    /**
     * 设置右侧描述样式
     */
    public void setCaptionStyle(int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mCaptionView.setTextAppearance(resId);
        } else {
            mCaptionView.setTextAppearance(getContext(), resId);
        }
    }

    /**
     * 设置右侧图标
     */
    public void setRightIcon(@DrawableRes int resId) {
        mRightIconRes = resId;
        if (mRightIconRes == 0) {
            mRightIconView.setVisibility(GONE);
            return;
        }
        mRightIconView.setVisibility(VISIBLE);
        mRightIconView.setImageResource(mRightIconRes);
    }

    /**
     * 设置底部描述
     */
    public void setDescription(String description) {
        mDescription = description;
        if (VMStr.isEmpty(mDescription)) {
            mDescriptionView.setVisibility(GONE);
            return;
        }
        mDescriptionView.setVisibility(VISIBLE);
        mDescriptionView.setText(mDescription);
    }

    /**
     * 设置激活状态
     */
    @Override
    public void setActivated(boolean activated) {
        super.setActivated(activated);
        mRightIconView.setActivated(activated);
    }
}
