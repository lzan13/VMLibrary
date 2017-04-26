package com.vmloft.develop.library.tools.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import com.vmloft.develop.library.tools.utils.VMBitmapUtil;
import com.vmloft.develop.library.tools.utils.VMDateUtil;
import com.vmloft.develop.library.tools.utils.VMFileUtil;
import com.vmloft.develop.library.tools.utils.VMLog;
import java.io.IOException;

/**
 * Created by lzan13 on 2017/4/24.
 * 自定义实现摄像头画面预览控件
 */
public class VMCameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private String imagePath = VMFileUtil.getPictures();
    private String videoPath = VMFileUtil.getMovies();

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
     * 预览界面创建时回调
     *
     * @param holder 图片预览句柄
     */
    @Override public void surfaceCreated(SurfaceHolder holder) {
        // 初始化相机实例
        initCameraInstance();
        // 开启相机预览
        startCameraPreview();
        // 设置摄像头旋转角度
        setCameraOrientation();
    }

    /**
     * SurfaceView 变化回调，默认第一次加载会在{@link #surfaceChanged(SurfaceHolder, int, int, int)}后执行一次
     */
    @Override public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // 设置摄像头旋转角度
        //setCameraOrientation();
    }

    /**
     * 界面销毁时调用
     *
     * @param holder 图片预览句柄
     */
    @Override public void surfaceDestroyed(SurfaceHolder holder) {
        this.holder.removeCallback(this);
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
        VMLog.d("release camera!");
    }

    /**
     * 开启相机预览
     */
    private void startCameraPreview() {
        try {
            // 设置摄像头预览控件
            camera.setPreviewDisplay(holder);
            // 开启预览
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        camera.setDisplayOrientation(90);
        VMLog.d("camera orientation: %d", orientation);
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

        Display display = ((WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE)).getDefaultDisplay();
        // 屏幕旋转的角度
        int rotation = display.getRotation();
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

    /**
     * 保存图片
     */
    public void takePicture() {
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override public void onPictureTaken(byte[] data, Camera camera) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                String filePath = imagePath + "IMG" + VMDateUtil.getDateTimeNoSpacing() + ".jpg";
                VMBitmapUtil.saveBitmapToSDCard(bitmap, filePath);
                VMLog.d("Take picture success: %s", filePath);
                // 继续开启相机预览
                camera.startPreview();
            }
        });
    }

    /**
     * 初始化相机实例
     */
    private Camera initCameraInstance() {
        if (camera == null) {
            // 启动相机进程
            CameraHandlerThread handlerThread = new CameraHandlerThread("CameraHandlerThread");
            synchronized (handlerThread) {
                handlerThread.openCamera();
            }
        }
        return camera;
    }

    private class CameraHandlerThread extends HandlerThread {
        // 当前线程的 handler
        private Handler handler;

        /**
         * 构造方法
         *
         * @param name 线程名字
         */
        CameraHandlerThread(String name) {
            super(name);
            // 创建线程后马上启动
            start();
            handler = new Handler(getLooper());
        }

        /**
         * 通知 Camera.open() 执行完毕
         */
        synchronized void notifyCameraOpened() {
            notify();
        }

        /**
         * 在子线程中打开相机
         */
        void openCamera() {
            handler.post(new Runnable() {
                @Override public void run() {
                    if (camera == null) {
                        camera = Camera.open();
                    }
                    notifyCameraOpened();
                }
            });
            try {
                // 为了防止 openCamera 之后马上使用 camera 对象，这里加上 wait-notify 安全机制
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
