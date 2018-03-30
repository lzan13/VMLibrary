package com.vmloft.develop.library.tools.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.Button;


/**
 * Created by lzan13 on 2017/11/30.
 */
@SuppressLint("AppCompatCustomView")
public class VMAuthCodeBtn extends Button {

    private Handler handler;
    // 按钮本来的内容
    private String btnText;
    // 倒计时时间
    private int maxTime = 60;
    private int countTime = maxTime;


    public VMAuthCodeBtn(Context context) {
        this(context, null);
    }

    public VMAuthCodeBtn(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VMAuthCodeBtn(Context context, AttributeSet attrs, int defStyleAttr) {
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
        countTime--;
        if (countTime > 0) {
            setText(countTime + "s");
            handler.sendEmptyMessageDelayed(0, 1000);
        } else {
            setClickable(true);
            setText(btnText);
            countTime = maxTime;
        }
    }

    /**
     * 设置最大时间 单位 秒
     *
     * @param time 时间
     */
    public void setMaxTime(int time) {
        maxTime = time;
        countTime = maxTime;
    }

    public void startCountDown() {
        setClickable(false);
        countDown();
    }
}

