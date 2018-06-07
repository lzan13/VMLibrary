package com.vmloft.develop.library.tools.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lzan13 on 2017/12/5.
 * 点击聚焦提示框
 */
public class VMFocusView extends View {

    private Context context;
    private Paint paint;
    private int outCircleWidth;
    private int outCircleRadius;
    private int outCircleRadiusMin;
    private int innerCircleRadius;
    private int innerCircleRadiusMax;
    private int outCircleAlpha = 128;
    private int innerCircleAlpha = 64;

    private float centerX = -1;
    private float centerY = -1;

    public VMFocusView(Context context) {
        this(context, null);
    }

    public VMFocusView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VMFocusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        outCircleWidth = 4;
        outCircleRadius = 56;
        outCircleRadiusMin = outCircleRadius;
        innerCircleRadius = 40;
        innerCircleRadiusMax = innerCircleRadius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawFocus(canvas);
    }

    private void drawFocus(Canvas canvas) {
        if (centerX == -1 || centerY == -1) {
            return;
        }
        paint.setStyle(Paint.Style.FILL);
        paint.setARGB(innerCircleAlpha, 255, 255, 255);
        canvas.drawCircle(centerX, centerY, innerCircleRadius, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(outCircleWidth);
        paint.setARGB(outCircleAlpha, 255, 255, 255);
        canvas.drawCircle(centerX, centerY, outCircleRadius, paint);
    }

    private void animFocus() {
        outCircleRadius -= 2;
        innerCircleRadius += 1;
        invalidate();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (outCircleRadius > outCircleRadiusMin) {
                    animFocus();
                } else {
                    setVisibility(GONE);
                }
            }
        }, 16);
    }

    Handler handler;

    /**
     * 开始聚焦
     */
    public void manualFocus(float x, float y) {
        setVisibility(VISIBLE);
        centerX = x;
        centerY = y;
        outCircleRadius = outCircleRadiusMin * 2;
        innerCircleRadius = innerCircleRadiusMax / 2;
        handler = new Handler(Looper.getMainLooper());
        animFocus();
    }
}
