package com.vmloft.develop.library.tools.widget.record;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.permission.VMPermission;
import com.vmloft.develop.library.tools.utils.VMColor;
import com.vmloft.develop.library.tools.utils.VMDimen;
import com.vmloft.develop.library.tools.utils.VMLog;
import com.vmloft.develop.library.tools.utils.VMStr;
import com.vmloft.develop.library.tools.utils.VMSystem;

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
    private int mCancelColor = VMColor.byRes(R.color.vm_red_87);

    // 外圈的颜色、大小
    private int mOuterColor = VMColor.byRes(R.color.vm_green_38);
    private int mOuterSize = VMDimen.dp2px(128);
    // 内圈录音按钮的颜色、大小
    private int mInnerColor = VMColor.byRes(R.color.vm_green);
    private int mInnerSize = VMDimen.dp2px(96);

    // 触摸区域提示文本
    private String mDescNormal = "触摸录音";
    private String mDescCancel = "松开取消";
    private int mDescColor = VMColor.byRes(R.color.vm_white);
    private int mDescSize = VMDimen.dp2px(16);

    // 时间字体的大小、颜色
    private int mTimeColor = VMColor.byRes(R.color.vm_green);
    private int mTimeSize = VMDimen.dp2px(14);

    //是否开始录制
    private boolean isStartRecord = false;
    private boolean isCancelRecord = false;
    // 录制开始时间
    protected long mStartTime;
    // 录制持续时间
    protected long mRecordTime;
    // 分贝
    protected int mDecibel = 1;
    // 分贝取样时间 毫秒值
    protected long mSampleTime = 260;

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

        // 获得绘制文本的宽和高
        mPaint = new Paint();
        // 设置抗锯齿
        mPaint.setAntiAlias(true);
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
        mCancelColor = array.getColor(R.styleable.VMRecordView_vm_cancel_color, mCancelColor);

        mOuterColor = array.getColor(R.styleable.VMRecordView_vm_outer_color, mOuterColor);
        mInnerColor = array.getColor(R.styleable.VMRecordView_vm_inner_color, mInnerColor);
        mInnerSize = array.getDimensionPixelOffset(R.styleable.VMRecordView_vm_inner_size, mInnerSize);

        mDescNormal = array.getString(R.styleable.VMRecordView_vm_touch_normal_desc);
        mDescCancel = array.getString(R.styleable.VMRecordView_vm_touch_cancel_desc);
        if (VMStr.isEmpty(mDescNormal)) {
            mDescNormal = "触摸录音";
        }
        if (VMStr.isEmpty(mDescCancel)) {
            mDescCancel = "松开取消";
        }
        mDescColor = array.getColor(R.styleable.VMRecordView_vm_desc_color, mDescColor);
        mDescSize = array.getDimensionPixelOffset(R.styleable.VMRecordView_vm_desc_size, mDescSize);

        mTimeColor = array.getColor(R.styleable.VMRecordView_vm_time_color, mTimeColor);
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

        drawBackground(canvas);

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
     * 绘制背景
     */
    private void drawBackground(Canvas canvas) {
        if (isCancelRecord) {
            mPaint.setColor(mCancelColor);
        } else {
            mPaint.setColor(VMColor.byRes(R.color.vm_transparent));
        }
        // 绘制背景
        canvas.drawRect(0, 0, mWidth, mHeight, mPaint);
    }

    /**
     * 绘制不可用时的 UI
     */
    protected void drawUnusable(Canvas canvas) {
        // 绘制提示文本
        mPaint.setColor(mCancelColor);
        mPaint.setTextSize(mDescSize);
        float tWidth = VMDimen.getTextWidth(mPaint, mUnusableDesc);
        float tHeight = VMDimen.getTextHeight(mPaint);
        canvas.drawText(mUnusableDesc, mWidth / 2 - tWidth / 2, mHeight / 2 + tHeight / 3, mPaint);
    }

    /**
     * 绘制触摸区域
     */
    protected void drawTouch(Canvas canvas) {
        if (isStartRecord && !isCancelRecord) {
            // 绘制外圈
            mPaint.setColor(mOuterColor);
            canvas.drawCircle(mWidth / 2, mHeight / 2, mOuterSize / 2, mPaint);
        }

        int innerColor;
        int descColor;
        String desc;
        if (isCancelRecord) {
            innerColor = VMColor.byRes(R.color.vm_white);
            descColor = mCancelColor;
            desc = mDescCancel;
        } else {
            innerColor = mInnerColor;
            descColor = mDescColor;
            desc = mDescNormal;
        }
        // 绘制触摸区域
        mPaint.setColor(innerColor);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mInnerSize / 2, mPaint);

        // 绘制提示文本
        mPaint.setColor(descColor);
        mPaint.setTextSize(mDescSize);
        float tWidth = VMDimen.getTextWidth(mPaint, desc);
        float tHeight = VMDimen.getTextHeight(mPaint);
        canvas.drawText(desc, mWidth / 2 - tWidth / 2, mHeight / 2 + tHeight / 3, mPaint);
    }

    /**
     * 绘制录音时间
     */
    protected void drawTime(Canvas canvas) {
        int timeColor;
        if (isCancelRecord) {
            timeColor = VMColor.byRes(R.color.vm_white);
        } else {
            timeColor = mTimeColor;
        }
        mPaint.setColor(timeColor);
        mPaint.setStrokeWidth(1);
        mPaint.setTextSize(mTimeSize);

        int minute = (int) (mRecordTime / 1000 / 60);
        int seconds = (int) (mRecordTime / 1000 % 60);
        String time = VMStr.byArgs("%02d'%02d''", minute, seconds);
//        int millisecond = (int) (mRecordTime % 1000 / 100);
//        String time = VMStr.byArgs("%02d'%02d''%d'''", minute, seconds, millisecond);

        float tWidth = VMDimen.getTextWidth(mPaint, time);
        float tHeight = VMDimen.getTextHeight(mPaint);
        canvas.drawText(time, mWidth / 2 - tWidth / 2, mHeight / 6 - tHeight, mPaint);
    }

    /**
     * 外圈动画
     */
    private void startOuterAnim() {
        VMSystem.runInUIThread(() -> {
            ValueAnimator mAnimator = ValueAnimator.ofInt(mInnerSize, mInnerSize + mDecibel * mHeight / 10, mInnerSize);
            mAnimator.setDuration(mSampleTime);
            mAnimator.setRepeatCount(0);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.addUpdateListener(a -> {
                mOuterSize = (int) a.getAnimatedValue();
                invalidate();
            });
            mAnimator.start();
        });
    }

    /**
     * 启动录音时间记录
     */
    protected void setupRecordTime() {
        // 开始录音，记录开始录制时间
        mStartTime = System.currentTimeMillis();
        new Thread(() -> {
            while (isStartRecord) {
                mRecordTime = System.currentTimeMillis() - mStartTime;
                mDecibel = VMRecorder.getInstance().getDecibel();
                startOuterAnim();
                postInvalidate();
                // 每间隔 100 毫秒更新一次时间
                try {
                    Thread.sleep(mSampleTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

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
            // TODO 正在录制中，不做处理
        } else if (code == VMRecorder.ERROR_SYSTEM) {
            isStartRecord = false;
            recordError(code, "录音系统错误");
            reset();
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
                        isCancelRecord = true;
                    } else {
                        isCancelRecord = false;
                    }
                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isUsable && isStartRecord) {
                    if (isCancelRecord) {
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
        isCancelRecord = false;
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
        public void onStart() {
        }

        /**
         * 录音取消，默认空实现，有需要可重写
         */
        public void onCancel() {
        }

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
