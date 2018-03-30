package com.vmloft.develop.library.example.record;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.tools.VMActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzan13 on 2018/1/9.
 * 屏幕录制
 */
public class ScreenRecordActivity extends VMActivity {
    @BindView(R.id.img_view) ImageView imageView;

    private Bitmap captureBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_screen);
        ButterKnife.bind(activity);

        if (ScreenCaptureManager.getInstance().state == ScreenCaptureManager.State.RUNNING) {
            setScreenCaptureCallback();
        } else {
            ScreenCaptureManager.getInstance().init(activity);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == ScreenCaptureManager.RECORD_REQUEST_CODE) {
                ScreenCaptureManager.getInstance().initMediaProjection(resultCode, data);
            }
        }
    }

    /**
     * 设置屏幕捕获回调
     */
    private void setScreenCaptureCallback() {
        ScreenCaptureManager.getInstance().setScreenCaptureCallback(
                new ScreenCaptureManager.ScreenCaptureCallback() {
                    @Override
                    public void onBitmap(Bitmap bitmap) {
                        captureBitmap = bitmap;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(captureBitmap);
                            }
                        });
                    }
                });
    }

    /**
     * 捕获屏幕截图
     */
    private void startScreenshot() {
        setScreenCaptureCallback();
        ScreenCaptureManager.getInstance().start();
    }

    /**
     * 停止截图
     */
    private void stopScreenshot() {
        ScreenCaptureManager.getInstance().stop();
    }

    @Override
    public void onBackPressed() {
        ScreenCaptureManager.getInstance().setScreenCaptureCallback(null);
        finish();
    }

    /**
     * 按钮点击事件
     */
    @OnClick({R.id.btn_screenshot_start, R.id.btn_screenshot_stop, R.id.btn_record_screen_start,
                     R.id.btn_record_screen_stop})
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.btn_screenshot_start:
            startScreenshot();
            break;
        case R.id.btn_screenshot_stop:
            stopScreenshot();
            break;
        case R.id.btn_record_screen_start:
            break;
        case R.id.btn_record_screen_stop:
            break;
        }
    }

}
