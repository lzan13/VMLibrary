package com.vmloft.develop.library.tools.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by lzan13 on 2017/5/12.
 * 自定义显示详情控件，可以改变
 */
public class VMDetailsView extends LinearLayout {

    // 上下文对象
    private Context context;
    // 控件点击监听
    private OnClickListener listener;

    // 显示内容控件
    private TextView contentView;
    // 显示状态控件
    private TextView stateView;

    // 控件显示的行高
    private int showMaxLines = 3;
    // 内容
    private String contentText = "你好啊，你叫什么名字，我是自定义可伸缩控件！";
    // 控件状态，默认是折叠状态
    private boolean isFold = true;

    /**
     * 构造方法
     */
    public VMDetailsView(Context context) {
        this(context, null);
    }

    public VMDetailsView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VMDetailsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
        bindView();
        bindListener();
    }

    /**
     * 初始化方法
     */
    private void initView() {
        // 设置控件布局为竖直布局
        setOrientation(VERTICAL);
        // 设置控件布局方式
        setGravity(Gravity.CENTER_HORIZONTAL);

        // 初始化并添加内容控件
        contentView = new TextView(context);
        addView(contentView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        // 初始化并添加状态控件
        stateView = new TextView(context);
        stateView.setText("显示更多");
        stateView.setTextColor(0xff239efe);
        addView(stateView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    /**
     * 绑定控件，将内容和控件联系起来
     */
    private void bindView() {
        contentView.setText(contentText);
        contentView.setHeight(contentView.getLineHeight() * showMaxLines);
        post(new Runnable() {
            @Override public void run() {
                // 判断是否徐亚欧显示显示更多
                if (contentView.getLineCount() > showMaxLines) {
                    stateView.setVisibility(View.VISIBLE);
                    if (isFold) {
                        contentView.setHeight(contentView.getLineHeight() * showMaxLines);
                        stateView.setText("显示更多");
                    } else {
                        contentView.setHeight(contentView.getLineHeight() * contentView.getLineCount());
                        stateView.setText("收起");
                    }
                } else {
                    // 当内容补足语超过显示详情的最大内容时，设置控件高度为内容高度
                    contentView.setHeight(contentView.getLineHeight() * contentView.getLineCount());
                    stateView.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 绑定点击事件，这里会优先使用自己设置的点击监听，如果没有设置，则按照默认处理
     */
    private void bindListener() {
        if (listener != null) {
            return;
        }
        setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                // 如果内容本身不足以折叠，就不处理
                if (contentView.getLineCount() <= showMaxLines) {
                    return;
                }
                isFold = !isFold;
                if (isFold) {
                    contentView.setHeight(contentView.getLineHeight() * showMaxLines);
                    stateView.setText("显示更多");
                } else {
                    contentView.setHeight(contentView.getLineHeight() * contentView.getLineCount());
                    stateView.setText("收起");
                }
            }
        });
    }

    /**
     * 设置控件的点击监听
     *
     * @param l 外部实现的点击监听
     */
    @Override public void setOnClickListener(@Nullable OnClickListener l) {
        listener = l;
        super.setOnClickListener(listener);
    }

    /**
     * 设置要显示的内容
     *
     * @param str 需要显示的内容
     */
    public void setContentText(String str) {
        contentText = str;
        bindView();
    }

    /**
     * 获取控件内容
     */
    public String getContentText() {
        return contentText;
    }

    public void setFold(boolean fold) {
        isFold = fold;
    }
}
