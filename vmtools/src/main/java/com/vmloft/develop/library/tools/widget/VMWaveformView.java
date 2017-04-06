package com.vmloft.develop.library.tools.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.utils.VMDimenUtil;

/**
 * Created by lzan13 on 2017/3/18.
 * 波形测试绘制类
 */
public class VMWaveformView extends View {

    // 上下文对象
    protected Context context;

    protected WaveformCallback waveformCallback;

    // 是否在触摸区域
    protected boolean isTouch = false;
    // 是否正在播放
    protected boolean isPlay = false;

    // 点坐标集合，四个一组
    protected float[] waveformPoints;
    // 采集到的声音信息
    protected byte[] waveformBytes;
    // 当前进度位置
    protected int position;

    // 控件布局边界
    protected RectF viewBounds;
    // 控件宽高
    protected float viewWidth;
    protected float viewHeight;

    // 波形部分画笔
    protected Paint waveformPaint;
    // 文字画笔
    protected Paint textPaint;

    // 持续时间
    protected String timeText;
    // 文字颜色
    protected int textColor;
    // 文字大小
    protected int textSize;

    // 波形刻度颜色
    protected int waveformColor;
    // 波形间隔
    protected int waveformInterval;
    // 波形宽度
    protected int waveformWidth;

    /**
     * 构造方法
     */
    public VMWaveformView(Context context) {
        this(context, null);
    }

    public VMWaveformView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VMWaveformView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VMWaveformView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init(attrs);
    }

    /**
     * 初始化操作
     */
    private void init(AttributeSet attrs) {

        waveformBytes = null;

        // 波形部分默认参数
        waveformColor = 0xddff5722;
        waveformInterval = VMDimenUtil.getDimenPixel(context, R.dimen.vm_dimen_1);
        waveformWidth = VMDimenUtil.getDimenPixel(context, R.dimen.vm_dimen_2);

        // 时间信息默认值
        timeText = "Time";
        textColor = 0xddffffff;
        textSize = VMDimenUtil.getDimenPixel(context, R.dimen.vm_size_12);

        // 获取控件的属性值
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VMWaveformView);
            // 获取自定义属性值，如果没有设置就是默认值
            textColor =
                    array.getColor(R.styleable.VMWaveformView_vm_waveform_text_color, textColor);
            textSize =
                    array.getDimensionPixelOffset(R.styleable.VMWaveformView_vm_waveform_text_size,
                            textSize);

            waveformColor = array.getColor(R.styleable.VMWaveformView_vm_waveform_waveform_color,
                    waveformColor);
            waveformInterval = array.getDimensionPixelOffset(
                    R.styleable.VMWaveformView_vm_waveform_waveform_interval, waveformInterval);
            waveformWidth = array.getDimensionPixelOffset(
                    R.styleable.VMWaveformView_vm_waveform_waveform_width, waveformWidth);

            // 回收资源
            array.recycle();
        }

        // 初始化画笔
        waveformPaint = new Paint();
        // 设置画笔抗锯齿
        waveformPaint.setAntiAlias(true);

        // 从前边的画笔创建新的画笔
        textPaint = new Paint(waveformPaint);
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制文字
        drawText(canvas);

        // 绘制波形
        drawFFT(canvas);
    }

    /**
     * 绘制文字
     *
     * @param canvas 当前控件画布
     */
    protected void drawText(Canvas canvas) {
        // 设置字体大小
        textPaint.setTextSize(textSize);
        // 设置字体颜色
        textPaint.setColor(textColor);
        textPaint.setStrokeWidth(4);
        float textWidth = VMDimenUtil.getTextWidth(textPaint, timeText);
        float textHeight = VMDimenUtil.getTextHeight(textPaint);
        // 绘制字体
        canvas.drawText(timeText, viewWidth - textWidth, textHeight, textPaint);
    }

    /**
     * 绘制频谱波形图
     */
    private void drawFFT(Canvas canvas) {
        // 设置画笔颜色
        waveformPaint.setColor(waveformColor);
        // 设置画笔末尾样式
        waveformPaint.setStrokeCap(Paint.Cap.ROUND);
        // 设置画笔宽度
        waveformPaint.setStrokeWidth(waveformWidth / 2);

        // 波形数据如果为 null 直接 return
        if (waveformBytes == null) {
            canvas.drawLine(0, viewHeight / 2, viewWidth, viewHeight / 2, waveformPaint);
            return;
        }

        if (waveformPoints == null || waveformPoints.length < waveformBytes.length * 4) {
            waveformPoints = new float[waveformBytes.length * 4];
        }
        float baseX = 0;
        float baseY = viewHeight / 2;
        // 绘制时域型波形图
        float interval = viewWidth / (waveformBytes.length - 1);
        for (int i = 0; i < waveformBytes.length - 1; i++) {
            // 计算第i个点的x坐标
            waveformPoints[i * 4] = baseX + i * interval;
            // 根据bytes[i]的值（波形点的值）计算第i个点的y坐标
            waveformPoints[i * 4 + 1] =
                    baseY + ((byte) (waveformBytes[i] + 128)) * (viewHeight / 2) / 128;
            // 计算第i+1个点的x坐标
            waveformPoints[i * 4 + 2] = baseX + interval * (i + 1);
            // 根据bytes[i+1]的值（波形点的值）计算第i+1个点的y坐标
            waveformPoints[i * 4 + 3] =
                    baseY + ((byte) (waveformBytes[i + 1] + 128)) * (viewHeight / 2) / 128;
        }
        canvas.drawLines(waveformPoints, waveformPaint);
    }

    /**
     * 绘制树状波形
     * TODO 由于采集数据频率以及数据大小，导致展示树状波形不是很理想，暂时不用
     *
     * @param canvas 当前控件的画布
     */
    private void drawWaveform(Canvas canvas) {
        // 设置画笔颜色
        waveformPaint.setColor(waveformColor);
        // 设置画笔宽度
        waveformPaint.setStrokeWidth(waveformWidth);
        // 设置画笔末尾样式
        waveformPaint.setStrokeCap(Paint.Cap.ROUND);

        int count = (int) (viewWidth / (waveformInterval + waveformWidth));
        canvas.drawLine(0, viewHeight, viewWidth, viewHeight, waveformPaint);

        // 波形数据如果为 null 直接 return
        if (waveformBytes == null) {
            return;
        }

        if (waveformPoints == null || waveformPoints.length < waveformBytes.length * 4) {
            waveformPoints = new float[waveformBytes.length * 4];
        }

        float baseX = 0;
        float baseY = viewHeight;

        // 回执频域型波形图
        for (int i = 0; i < count; i++) {
            float waveform = ((byte) (waveformBytes[i] + 128)) * (viewHeight / 2) / 128;
            waveformPoints[4 * i] = baseX + i * (waveformWidth + waveformInterval);
            waveformPoints[4 * i + 1] = baseY;
            waveformPoints[4 * i + 2] = baseX + i * (waveformWidth + waveformInterval);
            if (waveform < 0) {
                waveformPoints[4 * i + 3] = baseY + waveform;
            } else {
                waveformPoints[4 * i + 3] = baseY - waveform;
            }
        }
        canvas.drawLines(waveformPoints, waveformPaint);
    }

    /**
     * 设置控件持续时间
     *
     * @param time 持续时间
     */
    public void setTimeText(int time) {
        timeText =
                String.format("%d'%d\"%d", time / 1000 / 60, time / 1000 % 60, time % 1000 / 100);
        // 通知画布更新
        postInvalidate();
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewBounds = new RectF(getLeft(), getTop(), getRight(), getBottom());

        viewWidth = viewBounds.right - viewBounds.left;
        viewHeight = viewBounds.bottom - viewBounds.top;
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        // 触摸点横坐标
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                if (isTouch) {
                    waveformCallback.onDrag((int) x);
                }
                postInvalidate();
                break;
            default:
                break;
        }
        // 这里不调用系统的onTouchEvent方法，防止抬起时画面无法重绘
        return super.onTouchEvent(event);
    }

    /**
     * 更新 FFT 频域数据
     *
     * @param bytes 采集到的数据信息
     * @param position 音源当前播放位置
     */
    public void updateFFTData(byte[] bytes, int position) {
        waveformBytes = bytes;
        invalidate();
    }

    /**
     * 更新波形数据
     *
     * @param bytes 采集到的数据信息
     * @param position 音源当前播放位置
     */
    public void updateWaveformData(byte[] bytes, int position) {
        waveformBytes = bytes;
        invalidate();
    }

    /**
     * 设置回调接口
     */
    public void setWaveformCallback(WaveformCallback callback) {
        waveformCallback = callback;
    }

    /**
     * 控件回调接口
     */
    public interface WaveformCallback {

        /**
         * 拖动
         */
        public void onDrag(int position);
    }
}
