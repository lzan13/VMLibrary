package com.vmloft.develop.library.tools.tv.widget;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import com.vmloft.develop.library.tools.tv.utils.VMLog;
import java.io.IOException;

/**
 * Created by lzan13 on 2017/4/24.
 * 自定义实现摄像头画面预览控件
 */
public class VMCameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private Camera camera;

    private Camera.CameraInfo cameraInfo;

    public VMCameraPreview(Context context) {
        super(context);
        init();
    }

    public VMCameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        holder = getHolder();
        holder.addCallback(this);
    }

    /**
     * 初始化相机实例
     */
    private Camera getCameraInstance() {
        if (camera == null) {
            int cameraCount = Camera.getNumberOfCameras();
            if (cameraCount >= 0) {
                camera = Camera.open();
            } else {
                VMLog.d("没有找到摄像头，无法使用预览功能");
                return null;
            }
        }
        return camera;
    }

    /**
     * 预览界面创建时回调
     *
     * @param holder 图片预览句柄
     */
    @Override public void surfaceCreated(SurfaceHolder holder) {
        VMLog.d("surfaceCreated");
        // 初始化相机实例
        getCameraInstance();
        // 开启相机预览
        try {
            startCameraPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 设置摄像头旋转角度
        //setCameraOrientation();
    }

    /**
     * SurfaceView 变化回调，默认第一次加载会在{@link #surfaceChanged(SurfaceHolder, int, int, int)}后执行一次
     */
    @Override public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        VMLog.d("surfaceChanged format: %d, width: %d, height: %d", format, width, height);
        // 设置摄像头旋转角度
        setCameraOrientation();
    }

    /**
     * 界面销毁时调用
     *
     * @param holder 图片预览句柄
     */
    @Override public void surfaceDestroyed(SurfaceHolder holder) {
        VMLog.d("surfaceDestroyed release camera!");
        this.holder.removeCallback(this);
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    /**
     * 开启相机预览
     */
    private void startCameraPreview() throws IOException {
        // 设置摄像头预览控件
        camera.setPreviewDisplay(holder);
        // 开启预览
        camera.startPreview();
    }

    /**
     * 切换摄像头
     */
    public void changeCamera() {

    }

    /**
     * 设置相机角度
     */
    public void setCameraOrientation() {
        int orientation = getDisplayOrientation();
        // 设置摄像头方向
        camera.setDisplayOrientation(orientation);
    }

    /**
     * 获取屏幕旋转角度，来计算相机需要旋转的角度
     */
    public int getDisplayOrientation() {
        // 获取相机配置信息
        cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, cameraInfo);
        // 相机本身旋转角度
        int orientation = cameraInfo.orientation;
        VMLog.d("camera orientation: %d", orientation);

        Display display = ((WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE)).getDefaultDisplay();

        // 屏幕旋转的角度
        int rotation = display.getRotation();
        VMLog.d("screen rotation: %d", rotation);

        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result = (orientation - degrees + 360) % 360;
        return result;
    }
}
