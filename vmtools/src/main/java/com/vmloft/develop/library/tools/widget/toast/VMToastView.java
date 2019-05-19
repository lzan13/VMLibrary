package com.vmloft.develop.library.tools.widget.toast;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.utils.VMColor;

/**
 * Create by lzan13 on 2019/5/11 12:34
 *
 * 自定义弹出 toast view
 */
public class VMToastView extends RelativeLayout {

    // 移除提醒
    private static final int TOAST_REMOVE = 1001;

    // Toast 背景控件
    private View mBGView;
    // Toast 图标控件
    private ImageView mIconView;
    // Toast 消息控件
    private TextView mMsgView;
    // 记录 Toast 是否正在展示
    private boolean isShow;

    public VMToastView(Context context) {
        this(context, null);
    }

    public VMToastView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VMToastView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.vm_widget_toast, this);

        mBGView = findViewById(R.id.vm_toast_layout);
        mIconView = findViewById(R.id.vm_toast_icon_iv);
        mMsgView = findViewById(R.id.vm_toast_msg_tv);
    }

    public void setIconRes(int resId) {
        if (resId == 0) {
            mIconView.setVisibility(GONE);
        } else {
            mIconView.setVisibility(VISIBLE);
            mIconView.setImageResource(resId);
        }
    }

    public void setMsg(String msg) {
        if (mMsgView != null) {
            mMsgView.setText(msg);
        }
    }

    public void setMsgColor(int resId) {
        if (resId == 0) {
            return;
        }
        mIconView.setColorFilter(VMColor.byRes(resId));
        mMsgView.setTextColor(VMColor.byRes(resId));
    }

    public void setBGColor(int resId) {
        if (resId == 0) {
            return;
        }
        mBGView.setBackgroundResource(resId);
    }

    public boolean isShow() {
        return isShow;
    }

    /**
     * 显示提醒
     *
     * @param duration 持续时间
     */
    public void showToast(final int duration) {
        // 载入XML动画
        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.vm_toast_top_in);
        // 设置动画对象
        animator.setTarget(this);
        // 启动动画
        animator.start();
        mHandler.removeMessages(TOAST_REMOVE);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isShow = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Message msg = mHandler.obtainMessage(TOAST_REMOVE);
                mHandler.sendMessageDelayed(msg, duration);
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
    }

    /**
     * 移除提醒
     */
    private void removeToast() {
        final ViewGroup viewGroup = (ViewGroup) VMToastView.this.getParent();
        if (viewGroup != null) {
            // 载入XML动画
            Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.vm_toast_top_out);
            // 设置动画对象
            animator.setTarget(this);
            // 启动动画
            animator.start();
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}

                @Override
                public void onAnimationEnd(Animator animation) {
                    isShow = false;
                    viewGroup.removeView(VMToastView.this);
                }

                @Override
                public void onAnimationCancel(Animator animation) {}

                @Override
                public void onAnimationRepeat(Animator animation) {}
            });
        }
    }

    /**
     * 自定义 Handler 实现定时移除提醒
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case TOAST_REMOVE:
                removeToast();
                break;
            }
        }
    };
}
