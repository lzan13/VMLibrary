package com.vmloft.develop.library.tools.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
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
import java.util.Iterator;
import java.util.List;

/**
 * Created by lzan13 on 2017/4/24.
 * 自定义实现摄像头画面预览控件
 */
public class VMCameraPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private CameraDataCallback callback;

    private int width = 640;
    private int height = 480;

    private SurfaceHolder holder;
    private Camera camera;

    private Camera.CameraInfo cameraInfo;

    /**
     * 工厂方法
     *
     * @param context 上下文对象
     * @param width 初始化预览宽
     * @param height 初始化预览高
     * @return 返回预览控件对象
     */
    public static VMCameraPreview newInstance(Context context, int width, int height) {
        VMCameraPreview preview = new VMCameraPreview(context);
        preview.setCameraSize(width, height);
        return preview;
    }

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
     * 设置相机预览大小
     *
     * @param width 相机宽
     * @param height 相机高
     */
    public void setCameraSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * 获取相机实例
     */
    public Camera getCameraInstance() {
        if (camera == null) {
            CameraHandlerThread thread = new CameraHandlerThread("camera thread");
            synchronized (thread) {
                thread.openCamera();
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
        startCameraPreview();
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
        close();
    }

    /**
     * 开启相机预览
     */
    public void startCameraPreview() {
        // 初始化相机实例
        getCameraInstance();

        try {
            // 设置摄像头预览控件
            camera.setPreviewDisplay(holder);
            // 开启预览
            camera.startPreview();
            // 设置相机预览回调
            camera.setPreviewCallback(this);
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
        // 设置预览画面方向
        camera.setDisplayOrientation(orientation);
        // 设置捕获图片画面方向
        Camera.Parameters parameters = camera.getParameters();
        parameters.setRotation(orientation);
        camera.setParameters(parameters);
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

        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

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

    /**
     * 保存图片
     */
    public void takePicture() {
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override public void onPictureTaken(byte[] data, Camera camera) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                String filePath = VMFileUtil.getDCIM() + "IMG" + VMDateUtil.getDateTimeNoSpacing() + ".jpg";
                VMBitmapUtil.saveBitmapToSDCard(bitmap, filePath);
                VMLog.d("Take picture success: %s", filePath);
                // 继续开启相机预览
                camera.startPreview();
            }
        });
    }

    /**
     * 摄像头预览数据回调接口
     *
     * @param data 预览到的摄像头数据
     * @param camera 摄像头对象
     */
    @Override public void onPreviewFrame(byte[] data, Camera camera) {
        if (callback != null) {
            int rotation = getDisplayOrientation();
            callback.onCameraDataCallback(data, width, height, rotation);
        }
    }

    /**
     * 关闭摄像头
     */
    public void close() {
        this.holder.removeCallback(this);
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    /**
     * 打开摄像头
     */
    private void openCameraOriginal() {
        try {
            camera = Camera.open();

            Camera.Parameters parameters = camera.getParameters();
            List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
            if (sizeList.size() > 0) {
                Iterator<Camera.Size> itor = sizeList.iterator();
                while (itor.hasNext()) {
                    Camera.Size size = itor.next();
                    VMLog.d("camera supported size: w: %d, h: %d", size.width, size.height);
                }
            }
            // 设置预览大小，预览就按照默认最大就行
            parameters.setPreviewSize(width, height);
            // 设置捕获照片大小
            //parameters.setPictureSize(width, height);
            // 设置捕获图片数据格式，可以不设置
            //parameters.setPictureFormat(ImageFormat.YUV_422_888);
            camera.setParameters(parameters);
        } catch (Exception e) {
            VMLog.d("camera is not available");
        }
    }

    /**
     * 为相机创建单独的进程
     */
    private class CameraHandlerThread extends HandlerThread {
        Handler handler;

        public CameraHandlerThread(String name) {
            super(name);
            start();
            handler = new Handler(getLooper());
        }

        synchronized void notifyCameraOpened() {
            notify();
        }

        void openCamera() {
            handler.post(new Runnable() {
                @Override public void run() {
                    openCameraOriginal();
                    notifyCameraOpened();
                }
            });
            try {
                wait();
            } catch (InterruptedException e) {
                VMLog.e("wait was interrupted");
            }
        }
    }

    /**
     * 设置摄像头数据回调接口
     *
     * @param callback 外部实现的摄像头数据回调接口
     */
    public void setCameraDataCallback(CameraDataCallback callback) {
        this.callback = callback;
    }

    /**
     * 摄像头预览界面回调接口，用来回调预览数据
     */
    public interface CameraDataCallback {
        /**
         * 摄像头预览数据回调方法
         *
         * @param data 摄像头采集到的数据
         * @param width 数据宽
         * @param height 数据高
         * @param rotation 数据旋转方向
         */
        public void onCameraDataCallback(byte[] data, int width, int height, int rotation);
    }
}
