package com.vmloft.develop.library.tools.camera;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.utils.VMLog;
import java.io.IOException;
import java.util.List;

public class VMCameraView extends FrameLayout implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private Context context;
    private DataListener listener;
    // 相机实例
    private Camera camera;
    private Camera.CameraInfo cameraInfo;
    // 相机 id 默认为后置摄像头
    private int cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    // 相机旋转角度
    private int rotateAngle;
    private float aspectRatio = -1.0f;
    // 是否在聚焦中
    private boolean isFocusing = false;
    // 是否在预览中
    private boolean isPreview = false;
    // 是否打开了闪光灯
    private boolean isFlashLight = false;
    // 是否启用手势，默认启用
    private boolean isGesture = true;
    private boolean isSingle = true;
    private boolean isDouble = false;
    private float focusX = 0.0f;
    private float focusY = 0.0f;
    private float oldZoom;
    // 定义预览摄像头画面默认大小为 1080p，如果当前设备不支持后边会进行适配
    private int cameraWidth = 1920;
    private int cameraHeight = 1080;

    private SurfaceView surfaceView;
    private VMFocusView focusView;

    public VMCameraView(@NonNull Context context) {
        this(context, null);
    }

    public VMCameraView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VMCameraView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        inflate(context, R.layout.vm_widget_camera_view, this);
        surfaceView = findViewById(R.id.vm_surface_view);
        focusView = findViewById(R.id.vm_focus_view);

        surfaceView.setZOrderMediaOverlay(true);
        surfaceView.getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {}

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (listener != null) {
            listener.onData(data);
        }
    }

    /**
     * 获取相机实例
     */
    private void getCameraInstance() {
        if (camera == null) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            int numCameras = Camera.getNumberOfCameras();
            for (int i = 0; i < numCameras; i++) {
                Camera.getCameraInfo(i, info);
                if (info.facing == cameraId) {
                    cameraId = i;
                    camera = Camera.open(cameraId);
                    break;
                }
            }
        }
    }

    /**
     * 打开相机
     */
    public void launchCamera() {
        // 打开摄像头
        if (camera == null) {
            CameraHandlerThread thread = new CameraHandlerThread("CameraThread");
            synchronized (thread) {
                thread.open();
            }
        }

        if (camera == null) {
            VMLog.e("相机打开失败");
            return;
        }

        // 设置相机旋转角度
        rotateAngle = setCameraDisplayOrientation();

        Camera.Parameters parameters = camera.getParameters();
        // 选择预览大小
        chooseCameraPreviewSize(parameters);

        //这边采用自动对焦的模式
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }

        // 设置捕获图片数据格式，可以不设置
        parameters.setPictureFormat(ImageFormat.NV21);
        // 设置捕获照片大小
        //parameters.setPictureSize(cameraWidth, cameraHeight);
        // 设置预览最大最小帧 这里设置每秒20~30之间
        //parameters.setPreviewFpsRange(20, 30);
        // 将配置好的参数设置给相机
        camera.setParameters(parameters);

        fitView();
    }

    private void fitView() {
        surfaceView.post(new Runnable() {
            @Override
            public void run() {
                startPreview();
                //这里可以获取真正的预览的分辨率，在这里要进行屏幕的适配，主要适配非16:9的屏幕
                aspectRatio = ((float) cameraHeight) / cameraWidth;
                VMCameraView.this.measure(-1, -1);
            }
        });
    }

    /**
     * 开启预览
     */
    public void startPreview() {
        if (camera == null) {
            VMLog.e("相机没有打开");
            return;
        }
        camera.setPreviewCallback(this);
        try {
            // 设置摄像头预览控件
            camera.setPreviewDisplay(surfaceView.getHolder());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 开启预览
        camera.startPreview();
        // 设置相机预览回调
        camera.setPreviewCallback(this);
        isPreview = true;
        isFocusing = false;
        //进行一次自动对焦
        autoFocus();
    }

    /**
     * 切换相机
     */
    public int switchCamera() {
        // 释放相机
        releaseCamera();
        if (cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        } else {
            cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }
        // 重新打开相机
        launchCamera();
        return cameraId;
    }

    /**
     * 选择相机参数
     */
    private void chooseCameraPreviewSize(Camera.Parameters parameters) {
        //先判断是否支持该分辨率
        Camera.Size maxSize = null;
        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            //满足16:9的才进行使用并且小于等于cameraWidth的才进行使用
            if (size.width * cameraHeight == size.height * cameraWidth && size.width <= cameraWidth) {
                if (maxSize == null) {
                    maxSize = size;
                } else if (maxSize.width < size.width) {
                    maxSize = size;
                }
            }
            if (size.width == cameraWidth && size.height == cameraHeight) {
                parameters.setPreviewSize(cameraHeight, cameraHeight);
                break;
            }
        }
        if (maxSize != null) {
            // 如果存在maxSize的话就采用maxSize
            parameters.setPreviewSize(maxSize.width, maxSize.height);
        } else {
            // 如果没有16:9的话就采用默认的
            Camera.Size ppsfv = parameters.getPreferredPreviewSizeForVideo();
            parameters.setPreviewSize(maxSize.width, ppsfv.height);
        }

        // 请注意这个地方, camera 返回的图像并不一定是设置的大小（因为可能并不支持）
        Camera.Size size = parameters.getPreviewSize();
        cameraWidth = size.width;
        cameraHeight = size.height;
    }

    /**
     * 获取屏幕旋转角度，来计算相机需要旋转的角度
     */
    private int setCameraDisplayOrientation() {
        if (camera == null) {
            VMLog.e("相机没有打开");
            return 0;
        }
        // 获取相机配置信息
        cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, cameraInfo);
        // 相机本身旋转角度
        int orientation = cameraInfo.orientation;
        //VMLog.d("camera orientation: %d", orientation);

        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        // 屏幕旋转的角度
        int rotation = display.getRotation();
        //VMLog.d("screen rotation: %d", rotation);

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
        int result;
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            //前置摄像头需要镜像,转化后进行设置
            result = (orientation + degrees) % 360;
            camera.setDisplayOrientation((360 - result) % 360);
        } else {
            //后置摄像头直接进行显示
            result = (orientation - degrees + 360) % 360;
            camera.setDisplayOrientation(result);
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (aspectRatio > 0) {
            int initialWidth = MeasureSpec.getSize(widthMeasureSpec);
            int initialHeight = MeasureSpec.getSize(heightMeasureSpec);

            int horizPadding = getPaddingLeft() + getPaddingRight();
            int vertPadding = getPaddingTop() + getPaddingBottom();
            initialWidth -= horizPadding;
            initialHeight -= vertPadding;

            double viewAspectRatio = (double) initialWidth / initialHeight;
            double aspectDiff = aspectRatio / viewAspectRatio - 1;

            if (Math.abs(aspectDiff) < 0.01) {
            } else {
                if (aspectDiff > 0) {
                    initialHeight = (int) (initialWidth / aspectRatio);
                } else {
                    initialWidth = (int) (initialHeight * aspectRatio);
                }
                initialWidth += horizPadding;
                initialHeight += vertPadding;
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(initialWidth, MeasureSpec.EXACTLY);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(initialHeight, MeasureSpec.EXACTLY);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 拦截触摸事件实现聚焦
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isGesture) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
            isSingle = true;
            break;
        case MotionEvent.ACTION_POINTER_DOWN:
            isSingle = false;
            isDouble = true;
            oldZoom = getZoom(event);
            break;
        case MotionEvent.ACTION_MOVE:
            if (isSingle || !isDouble) {
                break;
            }
            float newZoom = getZoom(event);
            int zoomScale = (int) Math.abs((newZoom - oldZoom) / 3);
            if (newZoom > oldZoom) {
                handleZoom(true, zoomScale);
            } else {
                handleZoom(false, zoomScale);
            }
            oldZoom = newZoom;
            break;
        case MotionEvent.ACTION_POINTER_UP:
            isDouble = false;
            break;
        case MotionEvent.ACTION_UP:
            if (isSingle) {
                focusX = event.getX();
                focusY = event.getY();
                handleFocus(focusX, focusY);
            }
            break;
        }
        return true;
    }

    /**
     * 是否开启手势
     */
    public void setGestureEnable(boolean gesture) {
        isGesture = gesture;
    }

    /**
     * 处理聚焦，主要是为了显示聚焦的动画
     *
     * @param x 聚焦 x 坐标
     * @param y 聚焦 y 坐标
     */
    private void handleFocus(float x, float y) {
        //后置摄像头才有对焦功能
        if (camera != null && cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            return;
        }
        if (x != -1 && y != -1) { //这里有一个对焦的动画
            // 手动聚焦动画
            focusView.manualFocus(x, y);
        }
        autoFocus();
    }

    /**
     * 相机的自动聚焦
     */
    public void autoFocus() {
        try {
            // camera 不为空，并且 isFocusing = false 的时候才去对焦
            if (camera != null && !isFocusing && isPreview) {
                camera.cancelAutoFocus();
                isFocusing = true;
                camera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        isFocusing = false;
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取缩放因数
     */
    public float getZoom(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 处理缩放
     *
     * @param isZoomIn 是否放大
     * @param zoomScale 放大因数
     */
    public void handleZoom(boolean isZoomIn, int zoomScale) {
        if (camera == null) {
            VMLog.e("相机未打开");
            return;
        }
        Camera.Parameters parameters = camera.getParameters();
        if (parameters.isZoomSupported()) {
            int maxZoom = parameters.getMaxZoom();
            int zoom = parameters.getZoom();
            if (isZoomIn) {
                zoom = zoom + zoomScale;
            } else {
                zoom = zoom - zoomScale;
            }
            if (zoom > maxZoom) {
                zoom = maxZoom;
            } else if (zoom < 0) {
                zoom = 0;
            }
            parameters.setZoom(zoom);
            camera.setParameters(parameters);
        } else {
            VMLog.e("不支持缩放");
        }
    }

    /**
     * 闪光等
     */
    public void handleFlashLight() {
        if (camera == null) {
            isFlashLight = false;
            VMLog.e("相机未打开");
            return;
        }
        Camera.Parameters parameters = camera.getParameters();
        if (isFlashLight) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            isFlashLight = false;
        } else {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            isFlashLight = true;
        }
        camera.setParameters(parameters);
    }

    /**
     * 相机线程
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

        void open() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    getCameraInstance();
                    notifyCameraOpened();
                }
            });
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 释放相机
     */
    public void releaseCamera() {
        surfaceView.getHolder().removeCallback(this);
        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
            isFocusing = false;
            isPreview = false;
            isFlashLight = false;
        }
    }

    /**
     * 设置摄像头数据回调接口
     */
    public void setDataListener(DataListener listener) {
        this.listener = listener;
    }

    /**
     * 摄像头数据回调接口
     */
    public interface DataListener {

        void onData(byte[] data);
    }

    public Camera getCamera() {
        return camera;
    }

    /**
     * 在启动之前设置相机 id 可以修改默认打开的摄像头
     */
    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public int getCameraId() {
        return cameraId;
    }

    public int getRotateAngle() {
        return rotateAngle;
    }

    /**
     * 设置预览相机的宽
     */
    public void setCameraWidth(int cameraWidth) {
        this.cameraWidth = cameraWidth;
    }

    public int getCameraWidth() {
        return cameraWidth;
    }

    /**
     * 设置预览相机的高
     */
    public void setCameraHeight(int cameraHeight) {
        this.cameraHeight = cameraHeight;
    }

    public int getCameraHeight() {
        return cameraHeight;
    }
}
