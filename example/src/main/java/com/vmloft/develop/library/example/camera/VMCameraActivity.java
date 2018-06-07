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
    @BindView(R.id.btn_camera_recording) Button recordingBtn;
    @BindView(R.id.btn_camera_picture) Button takePictureBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        activity = this;

        ButterKnife.bind(activity);

        init();
    }

    private void init() {
        cameraView.setCameraWidth(1920);
        cameraView.setCameraHeight(1080);
        cameraView.openCamera();
    }

    @OnClick({ R.id.btn_camera_picture, R.id.btn_camera_recording })
    void onClick(View view) {
        switch (view.getId()) {
        case R.id.btn_camera_picture:
            // 拍照
            //cameraPreview.takePicture();
            //cameraView.takePicture();
            break;
        case R.id.btn_camera_recording:

            break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
