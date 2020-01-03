package com.vmloft.develop.library.example.demo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.example.common.AppActivity;
import com.vmloft.develop.library.tools.utils.VMLog;

import butterknife.OnClick;

/**
 * Create by lzan13 on 2019-11-15 13:15
 */
public class ThreadActivity extends AppActivity {

    @Override
    protected int layoutId() {
        return R.layout.activity_thread;
    }

    @Override
    protected void init() {
        setTopTitle("验证线程操作");
    }

    @OnClick({R.id.thread_btn_1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.thread_btn_1:
                startTask();
                break;
        }
    }

    /**
     * 模拟启动异步任务
     */
    public void startTask() {
        mHandler.removeCallbacks(runnable);
        startRunnable();
    }


    private void startRunnable() {
        mHandler.postDelayed(runnable, 2000);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            VMLog.d("验证线程停止执行情况 - 0 - start -");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 中间标识执行过程
            VMLog.d("验证线程停止执行情况 - 1 - run -");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            VMLog.d("验证线程停止执行情况 - 2 - end -");

            //要做的事情
            startRunnable();
        }
    };

}
