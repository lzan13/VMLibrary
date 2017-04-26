package com.vmloft.develop.library.simple.camera;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.vmloft.develop.library.simple.R;
import com.vmloft.develop.library.tools.VMBaseActivity;
import com.vmloft.develop.library.tools.widget.VMCameraPreview;

/**
 * Created by lzan13 on 2017/4/24.
 * 实现相机预览、拍照、录像等功能
 */
public class VMCameraActivity extends VMBaseActivity {

    @BindView(R.id.layout_camera_preview) FrameLayout cameraLayout;
    @BindView(R.id.btn_camera_recording) Button recordingBtn;
    @BindView(R.id.btn_camera_picture) Button takePictureBtn;

    private VMCameraPreview cameraPreview;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        activity = this;

        ButterKnife.bind(activity);

        initCamera();
    }

    private void initCamera() {
        cameraPreview = new VMCameraPreview(activity);
        cameraLayout.addView(cameraPreview);
    }

    @OnClick({ R.id.btn_camera_picture, R.id.btn_camera_recording }) void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_camera_picture:
                // 拍照
                cameraPreview.takePicture();
                break;
            case R.id.btn_camera_recording:

                break;
        }
    }

    @Override protected void onResume() {
        super.onResume();
        initCamera();
    }

    @Override protected void onPause() {
        cameraLayout.removeAllViews();
        cameraPreview = null;
        super.onPause();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 屏幕方向改变回调方法
     */
    @Override public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
