package com.vmloft.develop.library.example.demo.screen;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.example.common.AppActivity;
import com.vmloft.develop.library.tools.utils.VMDate;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class RecordScreenActivity extends AppActivity {

    @BindView(R.id.text_timer) TextView timerView;
    @BindView(R.id.img_screen_short) ImageView imageView;

    private SRService srService;
    private boolean isRecord = false;

    @Override
    protected int layoutId() {
        return R.layout.activity_record_screen;
    }

    @Override
    protected void initUI() {
        super.initUI();
        Intent intent = new Intent(mActivity, SRService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SRManager.RECORD_REQUEST_CODE) {
                SRManager.getInstance().initMediaProjection(resultCode, data);
                start();
            }
        }
    }

    /**
     * 按钮点击事件
     */
    @OnClick({ R.id.btn_start_screen_short, R.id.btn_start_screen_record, R.id.btn_stop })
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.btn_start_screen_short:
            prepare(false);
            break;
        case R.id.btn_start_screen_record:
            prepare(true);
            break;
        case R.id.btn_stop:
            stop();
            break;
        }
    }

    private void prepare(boolean record) {
        isRecord = record;
        SRManager.getInstance().init(mActivity);
    }

    /**
     * 捕获屏幕截图
     */
    private void start() {
        startTimer();
        if (isRecord) {
            srService.startScreenRecord();
        } else {
            setScreenShortCallback();
            srService.startScreenShort();
        }
    }

    /**
     * 停止截图
     */
    private void stop() {
        stopTimer();
        srService.stopRecord();
    }

    /**
     * 设置屏幕截图回调
     */
    private void setScreenShortCallback() {
        SRManager.getInstance().setScreenShortCallback(new SRManager.ScreenShortCallback() {
            @Override
            public void onBitmap(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        });
    }

    private long startTime;
    private Timer timer;

    /**
     * 定时器，主要是更新界面持续时间
     */
    private void startTimer() {
        startTime = VMDate.currentMilli();
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        long expend = VMDate.currentMilli() - startTime;
                        timerView.setText(String.valueOf(expend / 1000));
                    }
                });
            }
        };
        timer.schedule(task, 1000, 1000);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    @Override
    public void onBackPressed() {
        SRManager.getInstance().setScreenShortCallback(null);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
        unbindService(serviceConnection);
    }

    /**
     * 自定义实现服务连接
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            SRService.RecordBinder binder = (SRService.RecordBinder) service;
            srService = binder.getRecordService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
}
