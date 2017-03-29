package com.vmloft.develop.library.tools.tv.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import com.vmloft.develop.library.tools.tv.R;

/**
 * Created by lzan13 on 2017/3/29.
 *
 * 实现获取焦点变化的控件，可以缩放，加边框等
 */
public class VMFocusLayout extends RelativeLayout {

    private Context context;

    private Animation scaleBig;
    private Animation scaleSmall;

    public VMFocusLayout(Context context) {
        this(context, null);
    }

    public VMFocusLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VMFocusLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
    }

    /**
     * 重写焦点变化回调方法
     *
     * @param gainFocus 焦点状态
     * @param direction 焦点描述
     * @param previouslyFocusedRect 之前的焦点
     */
    @Override protected void onFocusChanged(boolean gainFocus, int direction,
            @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus) {
            // 设置当前控件位于界面最上层
            bringToFront();
            getRootView().requestLayout();
            getRootView().invalidate();

            gainFocus();
        } else {
            loseFocus();
        }
    }

    /**
     * 处理控件得到焦点
     */
    private void gainFocus() {
        if (scaleBig == null) {
            scaleBig = AnimationUtils.loadAnimation(context, R.anim.vm_anim_scale_big);
        }
        startAnimation(scaleBig);
    }

    /**
     * 处理控件失去焦点
     */
    private void loseFocus() {
        if (scaleSmall == null) {
            scaleSmall = AnimationUtils.loadAnimation(context, R.anim.vm_anim_scale_small);
        }
        startAnimation(scaleBig);
    }
}
