package com.vmloft.develop.library.tools.widget;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.WindowManager;
import com.vmloft.develop.library.tools.utils.VMDateUtil;
import com.vmloft.develop.library.tools.utils.VMFileUtil;
import com.vmloft.develop.library.tools.utils.VMLog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by lzan13 on 2017/4/24.
 * 使用 TextureView 自定义实现摄像头画面预览控件
 */
public class VMCamera2Preview extends TextureView implements TextureView.SurfaceTextureListener {

    private Context context;

    // 相机设备管理者
    private CameraManager cameraManager;
    // 通过相机预览进行捕获图像的 session
    private CameraCaptureSession captureSession;

    // 已打开的相机设备
    private CameraDevice cameraDevice;
    //当前摄像头 ID {@link CameraDevice}.
    private String cameraId;
    private int cameraState;

    // 相机状态：显示相机预览
    private static final int STATE_PREVIEW = 0;
    // 相机状态：等待焦点被锁定
    private static final int STATE_WAITING_LOCK = 1;
    // 相机状态：等待曝光，预拍摄状态
    private static final int STATE_WAITING_PRECAPTURE = 2;
    // 相机状态：等待曝光状态，预拍摄以外的其他状态
    private static final int STATE_WAITING_NON_PRECAPTURE = 3;
    // 相机状态：拍摄照片
    private static final int STATE_PICTURE_TAKEN = 4;

    // 定义的最大预览宽度
    private static final int MAX_PREVIEW_WIDTH = 1920;
    // 定义的最大预览高度
    private static final int MAX_PREVIEW_HEIGHT = 1080;

    // 相机预览的大小
    private Size previewSize;

    // 因为 Camera2 全程异步，所以新建一个线程，用来处理非 UI 的操作
    private HandlerThread cameraThread;

    // 处理在后台运行的任务
    private Handler backgroundHandler;

    // 用于实现静态图像的捕捉
    private ImageReader imageReader;

    // 保存文件
    private File cameraFile;

    // 用于相机预览
    private CaptureRequest.Builder previewRequestBuilder;
    // 通过上边的 builder 生成
    private CaptureRequest previewRequest;

    // 传感器方向
    private int sensorOrientation;

    // 相机信号锁 防止应用程序在关闭相机之前退出
    private Semaphore cameraOpenCloseLock = new Semaphore(1);

    // 当前设备是否支持闪光灯
    private boolean isFlashSupported;

    /**
     * 构造函数
     *
     * @param context 上下文对象
     */
    public VMCamera2Preview(Context context) {
        this(context, null);
    }

    public VMCamera2Preview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VMCamera2Preview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        startBackgroundThread();
        cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        setSurfaceTextureListener(this);
    }

    /**
     * 通过制定摄像头 ID 打开相机
     */
    private void openCamera(int width, int height) {

        setUpCameraOutputs();
        configureTransform(width, height);
        try {
            if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            cameraManager.openCamera(cameraId, stateCallback, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
    }

    /**
     * 关闭当前的相机设备
     */
    private void closeCamera() {
        try {
            cameraOpenCloseLock.acquire();
            if (null != captureSession) {
                captureSession.close();
                captureSession = null;
            }
            if (null != cameraDevice) {
                cameraDevice.close();
                cameraDevice = null;
            }
            if (null != imageReader) {
                imageReader.close();
                imageReader = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            cameraOpenCloseLock.release();
        }
    }

    /**
     * Starts a background thread and its {@link Handler}.
     */
    private void startBackgroundThread() {
        cameraThread = new HandlerThread("CameraBackground");
        cameraThread.start();
        backgroundHandler = new Handler(cameraThread.getLooper());
    }

    /**
     * Stops the background thread and its {@link Handler}.
     */
    private void stopBackgroundThread() {
        cameraThread.quitSafely();
        try {
            cameraThread.join();
            cameraThread = null;
            backgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置与相机相关的一些变量
     */
    private void setUpCameraOutputs() {
        try {
            for (String tempId : cameraManager.getCameraIdList()) {
                // 获取摄像机配置信息
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(tempId);

                // 这里使用后置摄像头
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }

                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) {
                    continue;
                }

                // 对于拍照，可以使用最大的可用尺寸
                Size largest = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new Comparator<Size>() {
                    @Override public int compare(Size lhs, Size rhs) {
                        // 这里使用 long 保证不会溢出
                        return Long.signum((long) lhs.getWidth() * lhs.getHeight() - (long) rhs.getWidth() * rhs.getHeight());
                    }
                });
                imageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(), ImageFormat.JPEG, 2);
                imageReader.setOnImageAvailableListener(imageAvailableListener, backgroundHandler);

                // 传感器方向
                sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);

                // 如果直接使用最大的预览可能会导致 oom 错误，这里选择一个最合适的预览大小
                previewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class));

                // 检查是否支持闪光灯功能
                Boolean available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                isFlashSupported = available == null ? false : available;

                cameraId = tempId;
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            // 当不支持的设备运行 Camera2 的 API 时会触发此异常
            e.printStackTrace();
        }
    }

    /**
     * 筛选合适的预览大小，为了实现全屏预览，这里采用 16:9 宽高比
     *
     * @param choices 相机所支持的预览大小合集
     * @return 返回最合适的大小
     */
    private Size chooseOptimalSize(Size[] choices) {
        for (Size size : choices) {
            if (size.getWidth() == size.getHeight() * 16 / 9 && size.getWidth() <= MAX_PREVIEW_WIDTH) {
                return size;
            }
        }
        VMLog.e("找不到合适的预览大小");
        return choices[choices.length - 1];
    }

    /**
     * 为相机预览创建一个新的{@link CameraCaptureSession}
     */
    private void createCameraPreviewSession() {
        try {
            SurfaceTexture texture = getSurfaceTexture();
            assert texture != null;

            // We configure the size of default buffer to be the size of camera preview we want.
            texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());

            // This is the output Surface we need to start preview.
            Surface surface = new Surface(texture);

            // We set up a CaptureRequest.Builder with the output Surface.
            previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            previewRequestBuilder.addTarget(surface);

            // Here, we create a CameraCaptureSession for camera preview.
            cameraDevice.createCaptureSession(Arrays.asList(surface, imageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {

                        @Override public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            // The camera is already closed
                            if (null == cameraDevice) {
                                return;
                            }

                            // When the session is ready, we start displaying the preview.
                            captureSession = cameraCaptureSession;
                            try {
                                // Auto focus should be continuous for camera preview.
                                previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                // Flash is automatically enabled when necessary.
                                setAutoFlash(previewRequestBuilder);

                                // Finally, we start displaying the camera preview.
                                previewRequest = previewRequestBuilder.build();
                                captureSession.setRepeatingRequest(previewRequest, captureCallback, backgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                            VMLog.e("Failed");
                        }
                    }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将矩阵转换配置为当前纹理视图，
     * 在使用{@link #setUpCameraOutputs()} 确定相机预览大小后，
     * 还需要调用此方法调整控件大小
     *
     * @param viewWidth 控件宽度
     * @param viewHeight 控件高度
     */
    private void configureTransform(int viewWidth, int viewHeight) {
        if (null == previewSize || null == context) {
            return;
        }
        int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, previewSize.getHeight(), previewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max((float) viewHeight / previewSize.getHeight(), (float) viewWidth / previewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        setTransform(matrix);
    }

    /**
     * 设置自动闪光灯
     */
    private void setAutoFlash(CaptureRequest.Builder requestBuilder) {
        if (isFlashSupported) {
            requestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
        }
    }

    /**
     * 根据屏幕旋转方向确认图片的方向，
     * 大多数设备的摄像头传感器旋转角度为90，某些特殊设备为270，比如我的 nexus 5x
     * 所以这里需要一些额外的处理来进行正确的计算图片的方向
     *
     * @param rotation 屏幕旋转方向
     * @return 图片的方向(0, 90, 270, 360)
     */
    private int getOrientation(int rotation) {
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 90;
                break;
            case Surface.ROTATION_90:
                degrees = 0;
                break;
            case Surface.ROTATION_180:
                degrees = 270;
                break;
            case Surface.ROTATION_270:
                degrees = 180;
                break;
        }
        return (degrees + sensorOrientation + 270) % 360;
    }

    /**
     * 拍摄照片
     */
    public void takePicture() {
        lockFocus();
    }

    /**
     * 锁定焦点，这是拍照的第一步
     */
    private void lockFocus() {
        try {
            // 这里告诉相机如何锁定焦点
            previewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
            // 通知捕获回调等待锁定
            cameraState = STATE_WAITING_LOCK;
            captureSession.capture(previewRequestBuilder.build(), captureCallback, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解锁焦点，当拍照完成时调用，让相机重回预览状态
     */
    private void unlockFocus() {
        try {
            // 重置自动对焦
            previewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
            setAutoFlash(previewRequestBuilder);
            captureSession.capture(previewRequestBuilder.build(), captureCallback, backgroundHandler);

            // 之后相机重新恢复预览状态
            cameraState = STATE_PREVIEW;
            captureSession.setRepeatingRequest(previewRequest, captureCallback, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 运行拍照的与拍照序列，
     */
    private void runPrecaptureSequence() {
        try {
            // This is how to tell the camera to trigger.
            previewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                    CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the precapture sequence to be set.
            cameraState = STATE_WAITING_PRECAPTURE;
            captureSession.capture(previewRequestBuilder.build(), captureCallback, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 捕获静态照片
     */
    private void captureStillPicture() {
        try {
            if (null == context || null == cameraDevice) {
                return;
            }
            // 这里使用 CaptureRequest.Builder 进行拍照
            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(imageReader.getSurface());

            // 使用相同的曝光(AE)和自动对焦(AF)进行预览
            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            setAutoFlash(captureBuilder);

            // 方向
            int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));

            CameraCaptureSession.CaptureCallback CaptureCallback = new CameraCaptureSession.CaptureCallback() {
                @Override public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request,
                        @NonNull TotalCaptureResult result) {
                    unlockFocus();
                }
            };

            captureSession.stopRepeating();
            captureSession.capture(captureBuilder.build(), CaptureCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 捕捉画面相关回调接口，主要处理拍照的一些处理
     */
    private CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {

        private void process(CaptureResult result) {
            switch (cameraState) {
                case STATE_PREVIEW: {
                    // 相机预览正常工作，不需要做什么
                    break;
                }
                case STATE_WAITING_LOCK: {
                    Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                    if (afState == null) {
                        captureStillPicture();
                    } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState
                            || CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState) {
                        // 某些设备上的 CONTROL_AE_STATE 可以为空
                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                            cameraState = STATE_PICTURE_TAKEN;
                            captureStillPicture();
                        } else {
                            runPrecaptureSequence();
                        }
                    }
                    break;
                }
                case STATE_WAITING_PRECAPTURE: {
                    // 某些设备上的 CONTROL_AE_STATE 可以为空
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null
                            || aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE
                            || aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
                        cameraState = STATE_WAITING_NON_PRECAPTURE;
                    }
                    break;
                }
                case STATE_WAITING_NON_PRECAPTURE: {
                    // 某些设备上的 CONTROL_AE_STATE 可以为空
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        cameraState = STATE_PICTURE_TAKEN;
                        captureStillPicture();
                    }
                    break;
                }
            }
        }

        @Override public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request,
                @NonNull CaptureResult partialResult) {
            process(partialResult);
        }

        @Override public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request,
                @NonNull TotalCaptureResult result) {
            process(result);
        }
    };

    /**
     * 相机设备状态更改回调接口
     */
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {

        @Override public void onOpened(@NonNull CameraDevice device) {
            // This method is called when the camera is opened.  We start camera preview here.
            cameraOpenCloseLock.release();
            cameraDevice = device;
            createCameraPreviewSession();
        }

        @Override public void onDisconnected(@NonNull CameraDevice device) {
            cameraOpenCloseLock.release();
            cameraDevice.close();
            cameraDevice = null;
        }

        @Override public void onError(@NonNull CameraDevice device, int error) {
            cameraOpenCloseLock.release();
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    /**
     * TextureView 生命周期中的几个回调方法
     */
    @Override public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        VMLog.d("onSurfaceTextureAvailable width: %d, height: %d", width, height);
        openCamera(width, height);
    }

    @Override public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        VMLog.d("onSurfaceTextureSizeChanged width: %d, height: %d", width, height);
        configureTransform(width, height);
    }

    @Override public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    /**
     * 这是 {@link ImageReader}的回调接口，当需要捕捉画面时，将回调 onImageAvailable() 方法
     */
    private final ImageReader.OnImageAvailableListener imageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override public void onImageAvailable(ImageReader reader) {
            backgroundHandler.post(new ImageSaver(reader.acquireNextImage()));
        }
    };

    /**
     * 保存图片到指定的文件中
     */
    private static class ImageSaver implements Runnable {

        /**
         * JPEG 图像
         */
        private Image image;
        /**
         * 将图片保存到这个文件
         */
        private File file;

        public ImageSaver(Image image) {
            this.image = image;
            // 先创建文件夹
            file = new File(VMFileUtil.getPictures() + "VMCamera/IMG_" + VMDateUtil.getDateTimeNoSpacing() + ".jpg");
            if (!file.getParentFile().isDirectory()) {
                file.mkdirs();
            }
        }

        @Override public void run() {
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            FileOutputStream output = null;
            try {
                output = new FileOutputStream(file);
                output.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                image.close();
                if (null != output) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
