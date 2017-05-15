package com.vmloft.develop.library.tools.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.utils.VMDimenUtil;
import com.vmloft.develop.library.tools.utils.VMFileUtil;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by lzan13 on 2017/3/18.
 * 自定义录音控件
 */
public class VMRecordView extends View {

    public static final int REASON_NONE = 0;     // 没有错误
    public static final int REASON_CANCEL = 1;   // 录音取消
    public static final int REASON_FAILED = 2;   // 录制失败
    public static final int REASON_SHORT = 3;    // 录音时间过短
    public static final int REASON_SYSTEM = 4;   // 系统错误

    // 上下文对象
    protected Context context;

    // 媒体录影机，可以录制音频和视频
    private MediaRecorder mediaRecorder;
    // 计算分贝基准值
    protected int decibelBase = 200;
    // 录制文件保存路径
    protected String recordFilePath;
    // 录音最大持续时间 10 分钟
    protected int maxDuration = 10 * 60 * 1000;
    // 音频采样率 单位 Hz
    protected int samplingRate = 8000;
    // 音频编码比特率
    protected int encodingBitRate = 64;
    // 记录是否录制中
    protected boolean isRecording = false;
    protected boolean isCancel = false;

    // 录音控件回调接口
    protected VMRecordCallback recordCallback;

    // 波形信息集合
    protected LinkedList<Integer> waveformList;

    // 控件画笔
    protected Paint paint;

    // 控件宽高
    protected float viewWidth;
    protected float viewHeight;

    // 录制开始时间
    protected long startTime = 0L;
    // 录制持续时间
    protected int recordTime = 0;

    // 指示器颜色
    protected int indicatorColor = 0xddd22a14;
    // 指示器大小
    protected int indicatorSize;

    // 文字颜色
    protected int textColor = 0x89212121;
    // 文字大小
    protected int textSize;

    // 触摸区域颜色
    protected int touchColor = 0xdd2384fe;
    // 触摸区域大小，此处根据控件高度计算，不单独设置
    protected float touchSize;
    // 触摸提示文字
    protected String touchText = "";
    // 触摸区域中心点位置
    protected float touchCenterX;
    protected float touchCenterY;

    // 波形刻度颜色
    protected int waveformColor = 0xddff5722;
    // 波形刻度间隔
    protected int waveformInterval;
    // 波形刻度宽度
    protected int waveformWidth;

    /**
     * 单参数构造方法
     *
     * @param context 上下文对象
     */
    public VMRecordView(Context context) {
        this(context, null);
    }

    public VMRecordView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VMRecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VMRecordView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * 控件初始化方法
     */
    protected void init(Context context, AttributeSet attrs) {
        this.context = context;
        //  关闭控件级别的硬件加速
        //this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        // 波形数据集合
        waveformList = new LinkedList<Integer>();

        // 默认高度 54dp
        viewHeight = VMDimenUtil.getDimenPixel(context, R.dimen.vm_dimen_56);

        // 波形刻度相关参数
        waveformInterval = VMDimenUtil.getDimenPixel(context, R.dimen.vm_dimen_1);
        waveformWidth = VMDimenUtil.getDimenPixel(context, R.dimen.vm_dimen_2);

        // 默认指示器相关参数
        indicatorSize = VMDimenUtil.getDimenPixel(context, R.dimen.vm_dimen_16);

        // 文字相关参数
        textSize = VMDimenUtil.getDimenPixel(context, R.dimen.vm_size_subhead);

        // 获取控件的属性值
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VMRecordView);
            // 获取自定义属性值，如果没有设置就是默认值
            touchColor = array.getColor(R.styleable.VMRecordView_vm_touch_color, touchColor);
            touchText = array.getString(R.styleable.VMRecordView_vm_touch_text);

            waveformColor = array.getColor(R.styleable.VMRecordView_vm_waveform_color, waveformColor);
            waveformInterval = array.getDimensionPixelOffset(R.styleable.VMRecordView_vm_waveform_interval, waveformInterval);
            waveformWidth = array.getDimensionPixelOffset(R.styleable.VMRecordView_vm_waveform_width, waveformWidth);

            indicatorColor = array.getColor(R.styleable.VMRecordView_vm_indicator_color, indicatorColor);
            indicatorSize = array.getDimensionPixelOffset(R.styleable.VMRecordView_vm_indicator_size, indicatorSize);

            textColor = array.getColor(R.styleable.VMRecordView_vm_text_color, textColor);
            textSize = array.getDimensionPixelOffset(R.styleable.VMRecordView_vm_text_size, textSize);
            // 回收资源
            array.recycle();
        }
        if (touchText == null) {
            touchText = "Slide cancel";
        }

        // 实例化画笔
        paint = new Paint();
        // 设置抗锯齿
        paint.setAntiAlias(true);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        // 画笔模式
        paint.setStyle(Paint.Style.FILL);
        // 设置画笔宽度
        paint.setStrokeWidth(waveformWidth);
        // 设置画笔末尾样式
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    /**
     * 初始化录制音频
     */
    public void initVoiceRecorder() {
        // 录音文件默认保存在 /sdcard/android/data/packagename/files/下，可设置
        recordFilePath = VMFileUtil.getFilesFromSDCard(context) + System.currentTimeMillis() + ".amr";
        // 实例化媒体录影机
        mediaRecorder = new MediaRecorder();
        // 设置音频源为麦克风
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        /**
         * 设置音频文件编码格式，这里设置默认
         * https://developer.android.com/reference/android/media/MediaRecorder.AudioEncoder.html
         */
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        /**
         * 设置音频文件输出格式
         * https://developer.android.com/reference/android/media/MediaRecorder.OutputFormat.html
         */
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        // 设置录音最大持续时间
        mediaRecorder.setMaxDuration(maxDuration);
        // 设置音频采样率
        mediaRecorder.setAudioSamplingRate(samplingRate);
        // 设置音频编码比特率
        mediaRecorder.setAudioEncodingBitRate(encodingBitRate);
    }

    /**
     * 重载 onDraw 方法
     *
     * @param canvas 当前控件画布
     */
    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 使此控件位于最上层，避免被遮挡
        bringToFront();

        // 绘制背景
        drawBackground(canvas);
        // 绘制指示器
        drawIndicator(canvas);
        // 绘制波形刻度部分
        drawWaveform(canvas);
        // 绘制文字
        drawTimeText(canvas);
        // 绘制触摸区域
        drawMic(canvas);
    }

    /**
     * 绘制控件背景，这里要注意不能绘制全部，只能回执有效区域
     */
    protected void drawBackground(Canvas canvas) {
        // 只有处于录制状态才开始绘制背景
        if (isRecording) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(0xffffffff);
            Rect rect = new Rect(0, 0, (int) viewWidth, (int) viewHeight);
            canvas.drawRect(rect, paint);
        }
    }

    /**
     * 绘制指示器
     */
    protected void drawIndicator(Canvas canvas) {
        if (isRecording) {
            paint.setStyle(Paint.Style.FILL);
            // 设置画笔颜色
            paint.setColor(indicatorColor);
            // 绘制指示器的圆
            canvas.drawCircle(viewHeight / 2, viewHeight / 2, indicatorSize / 2, paint);
        }
    }

    /**
     * 绘制波形
     */
    protected void drawWaveform(Canvas canvas) {
        if (isRecording) {
            paint.setStyle(Paint.Style.FILL);
            // 设置画笔模式
            paint.setStyle(Paint.Style.STROKE);
            // 设置画笔宽度
            paint.setStrokeWidth(waveformWidth);
            // 设置画笔末尾样式
            paint.setStrokeCap(Paint.Cap.ROUND);
            // 设置画笔颜色
            paint.setColor(waveformColor);
            int count = (int) (viewWidth / (waveformWidth + waveformInterval));
            for (int i = 0; i < count; i++) {
                float startX = i * (waveformInterval + waveformWidth);
                float waveformHeight = 2;
                if (i < waveformList.size()) {
                    waveformHeight = waveformList.get(i) * viewHeight / 12;
                }
                if (waveformHeight == 0) {
                    // 防止计算得到的波形高度为 0 导致显示空白
                    waveformHeight = 2;
                }
                // 绘制波形线
                canvas.drawLine(startX, viewHeight - waveformHeight, startX, viewHeight, paint);
            }
        }
    }

    /**
     * 画时间文字
     */
    protected void drawTimeText(Canvas canvas) {
        if (isRecording) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(textColor);
            paint.setStrokeWidth(1);
            paint.setTextSize(textSize);
            String timeText = "";
            int minute = recordTime / 1000 / 60;
            if (minute < 10) {
                timeText = "0" + minute;
            } else {
                timeText = "" + minute;
            }
            int seconds = recordTime / 1000 % 60;
            if (seconds < 10) {
                timeText = timeText + ":0" + seconds;
            } else {
                timeText = timeText + ":" + seconds;
            }
            int millisecond = recordTime % 1000 / 100;
            timeText = timeText + "." + millisecond;
            float textWidth = paint.measureText(timeText);
            canvas.drawText(timeText, viewHeight / 2 + textWidth / 2, viewHeight / 2 + textSize / 3, paint);
        }
    }

    /**
     * 绘制麦克风部分
     */
    protected void drawMic(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5);
        paint.setColor(touchColor);
        if (isRecording) {
            if (isCancel) {
                paint.setColor(touchColor);
            } else {
                paint.setColor(textColor);
            }
            // 绘制触摸提示文字
            paint.setTextSize(textSize);
            float textWidth = VMDimenUtil.getTextWidth(paint, touchText);
            float textHeight = VMDimenUtil.getTextHeight(paint);
            canvas.drawText(touchText, touchCenterX - textWidth - touchSize / 3 * 2, touchCenterY + textHeight / 3, paint);

            // 绘制滑动取消箭头
            canvas.drawLine(touchCenterX - textWidth * 2, touchCenterY, touchCenterX - textWidth * 2 + 15, touchCenterY - 15,
                    paint);
            canvas.drawLine(touchCenterX - textWidth * 2, touchCenterY, touchCenterX - textWidth * 2 + 15, touchCenterY + 15,
                    paint);

            paint.setColor(touchColor);
            // 绘制触摸圆形区域
            canvas.drawCircle(touchCenterX, touchCenterY, touchSize / 2, paint);
            // 绘制按下状态麦克风
            paint.setColor(0xfff8f8f8);
        }
        // 绘制麦克风
        RectF roundRectF = new RectF(touchCenterX - 10, touchCenterY - 35, touchCenterX + 10, touchCenterY + 5);
        canvas.drawRoundRect(roundRectF, 15, 15, paint);

        paint.setStyle(Paint.Style.STROKE);
        RectF arcRectF = new RectF(touchCenterX - 20, touchCenterY - 35, touchCenterX + 20, touchCenterY + 15);
        canvas.drawArc(arcRectF, 20, 140, false, paint);

        canvas.drawLine(touchCenterX, touchCenterY + 15, touchCenterX, touchCenterY + 25, paint);
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        RectF rectF = new RectF(getLeft(), getTop(), getRight(), getBottom());

        viewWidth = rectF.right - rectF.left;
        viewHeight = rectF.bottom - rectF.top;
        // 触摸区域中心位置
        touchCenterX = viewWidth - viewHeight / 2;
        touchCenterY = viewHeight / 2;

        touchSize = viewHeight / 2 * 3;
    }

    /**
     * 重写 onTouchEvent 监听方法，用来响应控件触摸
     */
    @Override public boolean onTouchEvent(MotionEvent event) {
        if (!this.isShown()) {
            return false;
        }
        // 触摸点横坐标
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 判断按下的位置是不是在触摸区域
                if (x < viewWidth - viewHeight) {
                    return false;
                }
                // 按下后更改触摸区域半径
                touchSize = VMDimenUtil.getDimenPixel(context, R.dimen.vm_dimen_72);
                // 触摸开始录音
                startRecord();
                break;
            case MotionEvent.ACTION_UP:
                if (isRecording) {
                    touchCenterX = viewWidth - viewHeight / 2;
                    // 抬起后更改触摸区域半径
                    // 根据向左滑动的距离判断是正常停止录制，还是取消录制
                    if (x > viewWidth / 2) {
                        // 抬起停止录制
                        stopRecord();
                    } else {
                        // 取消录制
                        cancelRecord();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isRecording) {
                    if (x < viewWidth - viewHeight / 2) {
                        touchCenterX = x;
                    } else {
                        touchCenterX = viewWidth - viewHeight / 2;
                    }
                    if (x > viewWidth / 5 * 3) {
                        // 抬起停止录制
                        isCancel = false;
                    } else {
                        // 取消录制
                        isCancel = true;
                    }
                    if (x < viewWidth / 3) {
                        touchCenterX = viewWidth - viewHeight / 2;
                        cancelRecord();
                        return false;
                    }
                }
                break;
        }
        // 通知控件重新绘制
        postInvalidate();
        // 这里不调用系统的onTouchEvent方法，防止抬起时画面无法重绘
        // return super.onTouchEvent(event);
        return true;
    }

    /**
     * 开始录制
     */
    public void startRecord() {
        // 判断录制系统是否空闲
        if (isRecording) {
            //recordCallback.onStop(REASON_RECORDING);
            return;
        }
        // 设置为录制音频中
        isRecording = true;
        // 判断媒体录影机是否释放，没有则释放
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }

        // 释放之后重新初始化
        initVoiceRecorder();

        // 设置输出文件路径
        mediaRecorder.setOutputFile(recordFilePath);
        try {
            // 准备录制
            mediaRecorder.prepare();
            // 开始录制
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
            if (recordCallback != null) {
                recordCallback.onStop(REASON_SYSTEM);
            }
        }
        // 初始化开始录制时间
        startTime = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override public void run() {
                while (isRecording) {
                    // 睡眠 100 毫秒，
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    recordTime = getRecordTime();
                    int decibel = getVoiceWaveform();
                    waveformList.addFirst(decibel);
                    postInvalidate();
                }
            }
        }).start();
    }

    /**
     * 停止录制
     */
    public void stopRecord() {
        isRecording = false;
        isCancel = false;

        // 释放媒体录影机
        if (mediaRecorder != null) {
            // 防止录音机 start 后马上调用 stop 出现异常，好像没什么作用
            //mediaRecorder.setOnErrorListener(null);
            try {
                // 停止录制
                mediaRecorder.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
                if (recordCallback != null) {
                    // 系统错误，一般是刚开始录制马上停止
                    recordCallback.onStop(REASON_SYSTEM);
                }
            }
            // 重置媒体录影机
            mediaRecorder.reset();
            // 释放媒体录影机
            mediaRecorder.release();
            mediaRecorder = null;
        }
        // 根据录制结果判断录音是否成功
        if (!VMFileUtil.isFileExists(recordFilePath)) {
            if (recordCallback != null) {
                // 录制失败
                recordCallback.onStop(REASON_FAILED);
            }
        }
        // 停止录制，清除集合
        waveformList.clear();
        // 计算录制时间
        recordTime = getRecordTime();
        if (recordCallback != null) {
            if (recordTime < 1000) {
                // 录制时间太短
                recordCallback.onStop(REASON_SHORT);
            }
            // 录音成功
            recordCallback.onSuccess(recordFilePath, recordTime);
        }
        recordTime = 0;
    }

    /**
     * 取消录制
     */
    public void cancelRecord() {
        isRecording = false;
        isCancel = false;

        // 释放媒体录影机
        if (mediaRecorder != null) {
            try {
                // 停止录制
                mediaRecorder.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
                if (recordCallback != null) {
                    recordCallback.onStop(REASON_CANCEL);
                }
            }
            // 重置媒体录影机
            mediaRecorder.reset();
            // 释放媒体录影机
            mediaRecorder.release();
            mediaRecorder = null;
        }
        // 根据录制结果判断录音是否成功
        if (VMFileUtil.isFileExists(recordFilePath)) {
            VMFileUtil.deleteFile(recordFilePath);
        }
        if (recordCallback != null) {
            recordCallback.onStop(REASON_CANCEL);
        }
        // 停止录制，清除集合
        waveformList.clear();
        recordTime = 0;
    }

    /**
     * 获取录音持续时间
     */
    private int getRecordTime() {
        return (int) (System.currentTimeMillis() - startTime);
    }

    /**
     * 获取声音分贝信息，根据分贝数据转化为可视的刻度信息
     */
    public int getVoiceWaveform() {
        int waveform = 1;
        if (mediaRecorder != null) {
            int ratio = 0;
            try {
                ratio = mediaRecorder.getMaxAmplitude() / decibelBase;
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            if (ratio > 0) {
                // 根据麦克风采集到的声音振幅计算声音分贝大小
                waveform = (int) (20 * Math.log10(ratio)) / 10;
            }
        }
        return waveform;
    }

    /**
     * 设置录音回调
     */
    public void setRecordCallback(VMRecordCallback callback) {
        recordCallback = callback;
    }

    /**
     * 录音控件的回调接口，用于回调给调用者录音结果
     */
    public interface VMRecordCallback {

        /**
         * 录音停止
         *
         * @param reason 停止原因
         */
        public void onStop(int reason);

        /**
         * 录音开始
         */
        public void onStart();

        /**
         * 录音成功
         *
         * @param path 录音文件的路径
         * @param time 录音时长
         */
        public void onSuccess(String path, int time);
    }
}
