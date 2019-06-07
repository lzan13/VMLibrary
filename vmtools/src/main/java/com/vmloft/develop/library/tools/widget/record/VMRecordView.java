package com.vmloft.develop.library.tools.widget.record;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.permission.VMPermission;
import com.vmloft.develop.library.tools.utils.VMColor;
import com.vmloft.develop.library.tools.utils.VMDimen;
import com.vmloft.develop.library.tools.utils.VMStr;

/**
 * Created by lzan13 on 2019/06/06 14:30
 *
 * 自定义录音控件
 */
public class VMRecordView extends View {

    private int mWidth;
    private int mHeight;

    // 是否可用
    protected boolean isUsable;
    // 不可用描述
    private String mUnusableDesc = "录音不可用";

    // 画笔
    private Paint mPaint;
    // 控件背景颜色
    private int mBGColor = VMColor.byRes(R.color.vm_transparent);
    private int mBGCancelColor = VMColor.byRes(R.color.vm_gray_white);

    // 外圈的颜色、大小
    private int mOuterColor = VMColor.byRes(R.color.vm_green_38);
    private int mOuterSize = VMDimen.dp2px(128);
    // 内圈录音按钮的颜色、大小
    private int mInnerColor = VMColor.byRes(R.color.vm_green);
    private int mInnerSize = VMDimen.dp2px(96);

    // 触摸区域提示文本
    private String mTouchDesc;
    private String mTouchNormalDesc = "触摸录音";
    private String mTouchCancelDesc = "松开取消";
    private int mTouchDescColor = VMColor.byRes(R.color.vm_white);
    private int mTouchDescSize = VMDimen.dp2px(16);

    // 时间字体的大小、颜色
    private int mTimeColor;
    private int mTimeNormalColor = VMColor.byRes(R.color.vm_green);
    private int mTimeCancelColor = VMColor.byRes(R.color.vm_white);
    private int mTimeSize = VMDimen.dp2px(14);

    //是否开始录制
    private boolean isStartRecord = false;
    // 录制开始时间
    protected long mStartTime;
    // 录制持续时间
    protected long mRecordTime;

    public VMRecordView(Context context) {
        this(context, null);
    }

    public VMRecordView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VMRecordView(final Context context, final AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        checkPermission();

        init(attrs);
    }

    /**
     * 检查权限
     */
    private void checkPermission() {
        // 检查录音权限
        if (VMPermission.getInstance(getContext()).checkRecord()) {
            isUsable = true;
        } else {
            isUsable = false;
            VMPermission.getInstance(getContext()).requestRecord(new VMPermission.PCallback() {
                @Override
                public void onReject() {
                    isUsable = false;
                    postInvalidate();
                }

                @Override
                public void onComplete() {
                    isUsable = true;
                    postInvalidate();
                }
            });
        }
    }

    /**
     * 初始化控件
     */
    private void init(AttributeSet attrs) {

        // 获取控件属性
        handleAttrs(attrs);

        mTouchDesc = mTouchNormalDesc;
        mTimeColor = mTimeNormalColor;

        // 获得绘制文本的宽和高
        mPaint = new Paint();
        // 设置抗锯齿
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTouchDescSize);
    }

    /**
     * 获取控件属性
     */
    private void handleAttrs(AttributeSet attrs) {
        // 获取控件的属性值
        if (attrs == null) {
            return;
        }
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.VMRecordView);
        mBGCancelColor = array.getColor(R.styleable.VMRecordView_vm_cancel_color, mBGCancelColor);

        mOuterColor = array.getColor(R.styleable.VMRecordView_vm_outer_color, mOuterColor);
        mOuterSize = array.getDimensionPixelOffset(R.styleable.VMRecordView_vm_outer_size, mOuterSize);
        mInnerColor = array.getColor(R.styleable.VMRecordView_vm_inner_color, mInnerColor);
        mInnerSize = array.getDimensionPixelOffset(R.styleable.VMRecordView_vm_inner_size, mInnerSize);

        mTouchNormalDesc = array.getString(R.styleable.VMRecordView_vm_touch_normal_desc);
        mTouchCancelDesc = array.getString(R.styleable.VMRecordView_vm_touch_cancel_desc);
        if (VMStr.isEmpty(mTouchNormalDesc)) {
            mTouchNormalDesc = "触摸录音";
        }
        if (VMStr.isEmpty(mTouchCancelDesc)) {
            mTouchCancelDesc = "松开取消";
        }
        mTouchDescColor = array.getColor(R.styleable.VMRecordView_vm_touch_desc_color, mTouchDescColor);
        mTouchDescSize = array.getDimensionPixelOffset(R.styleable.VMRecordView_vm_touch_desc_size, mTouchDescSize);

        mTimeNormalColor = array.getColor(R.styleable.VMRecordView_vm_time_normal_color, mTimeNormalColor);
        mTimeCancelColor = array.getColor(R.styleable.VMRecordView_vm_time_cancel_color, mTimeCancelColor);
        mTimeSize = array.getDimensionPixelOffset(R.styleable.VMRecordView_vm_time_size, mTimeSize);

        array.recycle();
    }

    /**
     * 重写父类的 onSizeChanged 方法，检测控件宽高的变化
     *
     * @param w    控件当前宽
     * @param h    控件当前高
     * @param oldw 控件原来的宽
     * @param oldh 控件原来的高
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制背景
        mPaint.setColor(mBGColor);
        // 绘制背景
        canvas.drawRect(0, 0, mWidth, mHeight, mPaint);

        if (!isUsable) {
            drawUnusable(canvas);
            return;
        }

        // 绘制触摸区域
        drawTouch(canvas);

        // 绘制时间
        drawTime(canvas);
    }

    /**
     * 绘制不可用时的 UI
     */
    protected void drawUnusable(Canvas canvas) {
        // 绘制提示文本
        mPaint.setColor(mTimeColor);
        float tWidth = VMDimen.getTextWidth(mPaint, mUnusableDesc);
        float tHeight = VMDimen.getTextHeight(mPaint);
        canvas.drawText(mUnusableDesc, mWidth / 2 - tWidth / 2, mHeight / 2 + tHeight / 3, mPaint);
    }

    /**
     * 绘制触摸区域
     */
    protected void drawTouch(Canvas canvas) {
        if (isStartRecord) {
            // 绘制外圆
            mPaint.setColor(mOuterColor);
            canvas.drawCircle(mWidth / 2, mHeight / 2, mOuterSize / 2, mPaint);
        }

        // 绘制触摸区域
        mPaint.setColor(mInnerColor);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mInnerSize / 2, mPaint);

        // 绘制提示文本
        mPaint.setColor(mTouchDescColor);
        float tWidth = VMDimen.getTextWidth(mPaint, mTouchDesc);
        float tHeight = VMDimen.getTextHeight(mPaint);
        canvas.drawText(mTouchDesc, mWidth / 2 - tWidth / 2, mHeight / 2 + tHeight / 3, mPaint);
    }

    /**
     * 绘制录音时间
     */
    protected void drawTime(Canvas canvas) {
        mPaint.setColor(mTimeColor);
        mPaint.setStrokeWidth(1);
        mPaint.setTextSize(mTimeSize);

        int minute = (int) (mRecordTime / 1000 / 60);
        int seconds = (int) (mRecordTime / 1000 % 60);
        int millisecond = (int) (mRecordTime % 1000 / 100);
        String time = VMStr.byArgs("%02d'%02d''%d'''", minute, seconds, millisecond);

        float tWidth = VMDimen.getTextWidth(mPaint, time);
        float tHeight = VMDimen.getTextHeight(mPaint);
        canvas.drawText(time, mWidth / 2 - tWidth / 2, mHeight / 6 - tHeight, mPaint);
    }

    /**
     * 启动录音时间记录
     */
    protected void setupRecordTime() {
        // 开始录音，记录开始录制时间
        mStartTime = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (VMRecorder.getInstance().isRecording()) {
                    // 每间隔 100 毫秒更新一次时间
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mRecordTime = getRecordTime();
                    postInvalidate();
                }
            }
        }).start();
    }

    /**
     * 获取录音持续时间
     */
    protected long getRecordTime() {return System.currentTimeMillis() - mStartTime;}

    /**
     * 开始录音
     */
    protected void startRecord() {
        isStartRecord = true;
        // 调用录音机开始录制音频
        int code = VMRecorder.getInstance().startRecord(null);
        if (code == VMRecorder.ERROR_NONE) {
            setupRecordTime();
            recordStart();
        } else if (code == VMRecorder.ERROR_RECORDING) {
            // TODO 正在录制
        } else if (code == VMRecorder.ERROR_SYSTEM) {
            isStartRecord = false;
            recordError(code, "录音系统错误");
        }
        postInvalidate();
    }

    /**
     * 停止录音
     *
     * @param cancel 是否为取消
     */
    protected void stopRecord(boolean cancel) {
        if (cancel) {
            VMRecorder.getInstance().cancelRecord();
            recordCancel();
        } else {
            int code = VMRecorder.getInstance().stopRecord();
            if (code == VMRecorder.ERROR_FAILED) {
                recordError(code, "录音失败");
            } else if (code == VMRecorder.ERROR_SYSTEM || mRecordTime < 1000) {
                if (mRecordTime < 1000) {
                    // 录制时间太短
                    recordError(VMRecorder.ERROR_SHORT, "录音时间过短");
                } else {
                    recordError(VMRecorder.ERROR_SHORT, "录音系统出现错误");
                }
            } else {
                recordComplete();
            }
        }

        reset();

        mBGColor = VMColor.byRes(R.color.vm_transparent);
        mTouchDesc = mTouchNormalDesc;
        mTimeColor = mTimeNormalColor;
        postInvalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action) {
        case MotionEvent.ACTION_DOWN:
            if (isUsable && x > mWidth / 2 - mInnerSize / 2 && x < mWidth / 2 + mInnerSize / 2 && y > mHeight / 2 - mInnerSize / 2 && y < mHeight / 2 + mInnerSize / 2) {
                startRecord();
            }
            break;
        case MotionEvent.ACTION_MOVE:
            if (isUsable && isStartRecord) {
                if (x < mWidth / 2 - mInnerSize / 2 || x > mWidth / 2 + mInnerSize / 2 || y < mHeight / 2 - mInnerSize / 2 || y > mHeight / 2 + mInnerSize / 2) {
                    mBGColor = mBGCancelColor;
                    mTouchDesc = mTouchCancelDesc;
                    mTimeColor = mTimeCancelColor;
                } else {
                    mBGColor = VMColor.byRes(R.color.vm_transparent);
                    mTouchDesc = mTouchNormalDesc;
                    mTimeColor = mTimeNormalColor;
                }
                postInvalidate();
            }
            break;
        case MotionEvent.ACTION_UP:
            if (isUsable && isStartRecord) {
                if (x < mWidth / 2 - mInnerSize / 2 || x > mWidth / 2 + mInnerSize / 2 || y < mHeight / 2 - mInnerSize / 2 || y > mHeight / 2 + mInnerSize / 2) {
                    stopRecord(true);
                } else {
                    stopRecord(false);
                }
            } else {
                checkPermission();
            }
            break;
        }
        return true;
    }

    /**
     * 录音开始
     */
    private void recordStart() {
        if (mRecordListener != null) {
            mRecordListener.onStart();
        }
    }

    /**
     * 录音取消
     */
    private void recordCancel() {
        if (mRecordListener != null) {
            mRecordListener.onCancel();
        }
    }

    /**
     * 录音出现错误
     *
     * @param code
     * @param desc
     */
    private void recordError(int code, String desc) {
        if (mRecordListener != null) {
            mRecordListener.onError(code, desc);
        }
    }

    /**
     * 录音完成
     */
    private void recordComplete() {
        if (mRecordListener != null) {
            mRecordListener.onComplete(VMRecorder.getInstance().getRecordFile(), mRecordTime);
        }
    }

    /**
     * 重置控件
     */
    protected void reset() {
        isStartRecord = false;
        mStartTime = 0l;
        mRecordTime = 0l;
        VMRecorder.getInstance().reset();
    }

    /**
     * ---------------------------------- 定义录音回调 ----------------------------------
     */
    // 录音控件回调接口
    protected RecordListener mRecordListener;

    /**
     * 设置录音回调
     */
    public void setRecordListener(RecordListener listener) {
        mRecordListener = listener;
    }

    /**
     * 定义录音控件的回调接口，用于回调给调用者录音结果
     */
    public abstract static class RecordListener {

        /**
         * 录音开始，默认空实现，有需要可重写
         */
        public void onStart() {}

        /**
         * 录音取消，默认空实现，有需要可重写
         */
        public void onCancel() {}

        /**
         * 录音错误
         *
         * @param code 错误码
         * @param desc 错误描述
         */
        public abstract void onError(int code, String desc);

        /**
         * 录音成功
         *
         * @param path 录音文件的路径
         * @param time 录音时长
         */
        public abstract void onComplete(String path, long time);
    }
}
