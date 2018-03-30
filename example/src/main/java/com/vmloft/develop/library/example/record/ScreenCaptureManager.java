package com.vmloft.develop.library.example.record;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;

import com.vmloft.develop.library.tools.utils.VMLog;

import java.nio.ByteBuffer;

/**
 * Created by lzan13 on 2018/1/9.
 * 屏幕捕获管理类
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScreenCaptureManager {
    public static final int RECORD_REQUEST_CODE = 1000;
    private static ScreenCaptureManager instance;
    private Activity activity;

    private ScreenCaptureCallback screenCaptureCallback;

    public State state = State.IDLE;

    private MediaProjectionManager projectionManager;
    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;
    private ImageReader imageReader;

    private Display screenDisplay;

    private ScreenCaptureManager() {}

    public static ScreenCaptureManager getInstance() {
        if (instance == null) {
            instance = new ScreenCaptureManager();
        }
        return instance;
    }

    /**
     * 初始化
     */
    public void init(Activity activity) {
        this.activity = activity;
        projectionManager = (MediaProjectionManager) activity.getSystemService(
                Context.MEDIA_PROJECTION_SERVICE);
    }

    /**
     * 开始捕获屏幕
     */
    public void start() {
        if (projectionManager != null && state == State.IDLE) {
            activity.startActivityForResult(projectionManager.createScreenCaptureIntent(),
                    ScreenCaptureManager.RECORD_REQUEST_CODE);
            state = State.RUNNING;
        }
    }

    /**
     * 停止捕获
     */
    public void stop() {
        if (mediaProjection != null) {
            mediaProjection.stop();
            state = State.IDLE;
        }
    }

    /**
     * 初始化
     */
    public void initMediaProjection(int resultCode, Intent intent) {
        mediaProjection = projectionManager.getMediaProjection(resultCode, intent);
        if (mediaProjection != null) {
            // create virtual display depending on device width / height
            initVirtualDisplay();
            mediaProjection.registerCallback(new MediaProjectionStopCallback(), null);
        }
    }


    /**
     * 初始化虚拟显示，抓图和录屏主要都是根据这里
     */
    private void initVirtualDisplay() {
        // display metrics
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        int density = metrics.densityDpi;
        screenDisplay = activity.getWindowManager().getDefaultDisplay();
        // get width and height
        Point size = new Point();
        screenDisplay.getSize(size);
        int width = size.x;
        int height = size.y;

        // start capture reader
        imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 1);
        imageReader.setOnImageAvailableListener(new ImageAvailableListener(), null);

        String virtualDisplayName = "Screenshot";
        int flags = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY
                | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;
        virtualDisplay = mediaProjection.createVirtualDisplay(virtualDisplayName, width, height,
                density, flags, imageReader.getSurface(), null, null);
    }


    private long oldTime = 0;

    /**
     * 图片变化监听
     */
    private class ImageAvailableListener implements ImageReader.OnImageAvailableListener {
        @Override
        public void onImageAvailable(ImageReader reader) {
            Image image = reader.acquireLatestImage();
            long currTime = System.currentTimeMillis();
            VMLog.d("onImageAvailable %d", currTime - oldTime);
            if (currTime - oldTime > 100) {
                oldTime = currTime;
                VMLog.d("onImageAvailable");
                Bitmap bitmap = null;
                if (image != null) {
                    Image.Plane[] planes = image.getPlanes();
                    ByteBuffer buffer = planes[0].getBuffer();
                    int width = image.getWidth();
                    int height = image.getHeight();
                    int pixelStride = planes[0].getPixelStride();
                    int rowStride = planes[0].getRowStride();
                    int rowPadding = rowStride - pixelStride * width;
                    // create bitmap
                    bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height,
                            Bitmap.Config.ARGB_8888);
                    bitmap.copyPixelsFromBuffer(buffer);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
                    if (screenCaptureCallback != null) {
                        screenCaptureCallback.onBitmap(bitmap);
                    }
                }
            }
            if (image != null) {
                image.close();
            }
        }
    }

    /**
     * 屏幕捕获停止回调
     */
    private class MediaProjectionStopCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {
            if (virtualDisplay != null) {
                virtualDisplay.release();
            }
            if (imageReader != null) {
                imageReader.setOnImageAvailableListener(null, null);
            }
            mediaProjection.unregisterCallback(this);
            state = State.IDLE;
        }
    }


    /**
     * 设置屏幕捕获回调接口
     */
    public void setScreenCaptureCallback(ScreenCaptureCallback callback) {
        screenCaptureCallback = callback;
    }

    /**
     * 屏幕捕获回调接口
     */
    public interface ScreenCaptureCallback {
        void onBitmap(Bitmap bitmap);
    }

    enum State {
        IDLE,
        RUNNING
    }

}
