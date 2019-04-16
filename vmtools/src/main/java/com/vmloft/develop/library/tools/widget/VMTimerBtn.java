package com.vmloft.develop.library.tools.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.Button;

import com.vmloft.develop.library.tools.R;

/**
 * Created by lzan13 on 2017/11/30.
 *
 * 定时按钮
 */
@SuppressLint("AppCompatCustomView")
public class VMTimerBtn extends Button {

    private Handler mHandler;
    // 按钮默认文本
    private String mBtnText;
    // 倒计时文本 需为可格式化样式 例: 剩余(%d)
    private String mTimerText;
    // 倒计时时间， 默认5秒
    private int mMaxTime;
    private int mTimerTime;
    private TimerListener listener;

    public VMTimerBtn(Context context) {
        this(context, null);
    }

    public VMTimerBtn(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VMTimerBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mBtnText = this.getText().toString();

        handleAttrs(context, attrs);

        mTimerTime = mMaxTime;

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                countDown();
            }
        };
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
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VMTimerBtn);
        mTimerText = array.getString(R.styleable.VMTimerBtn_vm_timer_text);
        mMaxTime = array.getInt(R.styleable.VMTimerBtn_vm_timer_time, mMaxTime);
    }

    /**
     * 倒计时循环调用
     */
    private void countDown() {
        if (mTimerTime > 0) {
            setText(String.format(mTimerText, mTimerTime));
            mHandler.sendEmptyMessageDelayed(0, 1000);
            mTimerTime--;
        } else {
            setEnabled(true);
            setText(mBtnText);
            mTimerTime = mMaxTime;
            if (listener != null) {
                listener.onTimeOut();
            }
        }
    }

    /**
     * 设置最大时间 单位 秒
     *
     * @param time 时间
     */
    public void setTimerTime(int time) {
        mMaxTime = time;
        mTimerTime = mMaxTime;
    }

    /**
     * 开始倒计时
     */
    public void startTimer() {
        setEnabled(false);
        countDown();
    }

    /**
     * 设置定时接口
     */
    public void setTimerListener(TimerListener listener) {
        this.listener = listener;
    }

    /**
     * 定时接口
     */
    public interface TimerListener {
        void onTimeOut();
    }
}

