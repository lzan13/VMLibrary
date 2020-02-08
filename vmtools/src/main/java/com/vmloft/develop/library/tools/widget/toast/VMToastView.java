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
import com.vmloft.develop.library.tools.animator.VMAnimator;
import com.vmloft.develop.library.tools.utils.VMColor;
import com.vmloft.develop.library.tools.utils.VMDimen;

/**
 * Create by lzan13 on 2019/5/11 12:34
 *
 * 自定义弹出 toast view
 */
public class VMToastView extends RelativeLayout {

    // 移除提醒
    private static final int TOAST_REMOVE = 1001;
    // 动画时间
    private static final int ANIM_DURATION = 225;

    // Toast 背景控件
    private View mBGView;
    // Toast 图标控件
    private ImageView mIconView;
    // Toast 消息控件
    private TextView mMsgView;
    // 记录 Toast 是否正在展示
    private boolean isShow;
    private float mHeight;

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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
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
     * @param duration Toast 显示持续时间
     */
    public void showToast(final int duration) {
        mHandler.removeMessages(TOAST_REMOVE);
        if (mHeight == 0) {
            mHeight = VMDimen.dp2px(72);
        }
        VMAnimator.Options options = VMAnimator.createOptions(this, VMAnimator.TRANSY, -mHeight, 0.0f);
        VMAnimator.createAnimator().play(options).addListener(new Animator.AnimatorListener() {
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
        }).start(ANIM_DURATION);
    }

    /**
     * 移除提醒
     */
    private void removeToast() {
        ViewGroup viewGroup = (ViewGroup) this.getParent();
        if (viewGroup != null) {
            VMAnimator.Options options = VMAnimator.createOptions(this, VMAnimator.TRANSY, 0.0f, -mHeight);
            VMAnimator.createAnimator().play(options).addListener(new Animator.AnimatorListener() {
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
            }).start(ANIM_DURATION);
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
