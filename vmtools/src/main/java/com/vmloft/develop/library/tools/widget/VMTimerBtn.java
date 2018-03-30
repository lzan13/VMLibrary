package com.vmloft.develop.library.tools.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.Button;


/**
 * Created by lzan13 on 2017/11/30.
 * 定时按钮
 */
@SuppressLint("AppCompatCustomView")
public class VMTimerBtn extends Button {

    private Handler handler;
    // 按钮本来的内容
    private String btnText;
    // 倒计时时间
    private int defaultTime = 5;
    private int timerTime = defaultTime;
    private TimerListener listener;


    public VMTimerBtn(Context context) {
        this(context, null);
    }

    public VMTimerBtn(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VMTimerBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        btnText = this.getText().toString();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                countDown();
            }
        };
    }

    /**
     * 倒计时
     */
    private void countDown() {
        timerTime--;
        if (timerTime >= 0) {
            setText(timerTime + " | " + btnText);
            handler.sendEmptyMessageDelayed(0, 1000);
        } else {
            setClickable(false);
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
        defaultTime = time;
        timerTime = defaultTime;
    }

    public void startTimer() {
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

