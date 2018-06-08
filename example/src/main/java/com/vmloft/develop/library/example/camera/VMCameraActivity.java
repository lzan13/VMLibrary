package com.vmloft.develop.library.example.camera;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.tools.VMActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.vmloft.develop.library.tools.camera.VMCameraView;

/**
 * Created by lzan13 on 2017/4/24.
 * 实现相机预览、拍照、录像等功能
 */
public class VMCameraActivity extends VMActivity {

    @BindView(R.id.widget_camera_view) VMCameraView cameraView;

    private int width = 640;
    private int height = 640;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        activity = this;

        ButterKnife.bind(activity);

        init();
    }

    private void init() {

    }

    @OnClick({ R.id.btn_take_picture, R.id.btn_flash_light, R.id.btn_switch_camera })
    void onClick(View view) {
        switch (view.getId()) {
        case R.id.btn_take_picture:
            break;
        case R.id.btn_flash_light:
            cameraView.handleFlashLight();
            break;
        case R.id.btn_switch_camera:
            cameraView.switchCamera();
            break;
        }
    }

    private void openCamera() {
        cameraView.setCameraWidth(width);
        cameraView.setCameraHeight(height);
        cameraView.launchCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        openCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cameraView.releaseCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 屏幕方向改变回调方法
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
