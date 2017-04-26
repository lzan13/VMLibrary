package com.vmloft.develop.library.simple.camera;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

/**
 * Created by lzan13 on 2017/4/24.
 * 自定义实现摄像头画面预览控件
 */
public class VMCameraGLPreview extends GLSurfaceView{

    public VMCameraGLPreview(Context context) {
        super(context);
    }

    public VMCameraGLPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override public void surfaceCreated(SurfaceHolder holder) {
        super.surfaceCreated(holder);
    }

    @Override public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
    }

    @Override public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        super.surfaceChanged(holder, format, w, h);
    }
}
