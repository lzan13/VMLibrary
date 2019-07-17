package com.vmloft.develop.library.tools.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.utils.VMUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by lzan13 on 2019/7/17 10:34
 *
 * 表情雨控件
 */
public class VMEmojiRainView extends View {
    // 判断是否开始下落，默认没有
    private boolean isStart;

    private Paint mPaint;
    //图片处理
    private Matrix mMatrix;

    // 表情数量，默认 60
    private int mEmojiCount = 60;
    // 表情持续距离，默认一个半屏幕高度
    private int mDistance;
    //表情包集合
    private List<Integer> mResList;
    private List<EmojiItem> mEmojiList;


    public VMEmojiRainView(Context context) {
        this(context, null);
    }

    public VMEmojiRainView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VMEmojiRainView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);

        mMatrix = new Matrix();

        mResList = new ArrayList<>();
        mEmojiList = new ArrayList<>();

        handleAttrs(context, attrs);
    }


    /**
     * 获取资源属性
     */
    private void handleAttrs(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VMEmojiRainView);
        // 获取自定义属性值，如果没有设置就是默认值
        mEmojiCount = array.getInt(R.styleable.VMEmojiRainView_vm_emoji_count, mEmojiCount);
        int emojiId = array.getResourceId(R.styleable.VMEmojiRainView_vm_emoji_id, 0);
        if (emojiId != 0) {
            mResList.add(emojiId);
        }
        // 回收资源
        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isStart) {
            //用于判断表情下落结束，结束即不再进行重绘
            boolean isInScreen = false;
            for (int i = 0; i < mEmojiList.size(); i++) {
                mMatrix.reset();
                // 缩放
                mMatrix.setScale(mEmojiList.get(i).scale, mEmojiList.get(i).scale);
                // 下落过程坐标
                mEmojiList.get(i).x = mEmojiList.get(i).x + mEmojiList.get(i).offsetX;
                mEmojiList.get(i).y = mEmojiList.get(i).y + mEmojiList.get(i).offsetY;
                if (mEmojiList.get(i).y <= getHeight()) {
                    // 当表情仍在视图内，则继续重绘
                    isInScreen = true;
                }
                // 位移
                mMatrix.postTranslate(mEmojiList.get(i).x, mEmojiList.get(i).y);
                canvas.drawBitmap(mEmojiList.get(i).bitmap, mMatrix, mPaint);
            }
            if (isInScreen) {
                postInvalidate();
            } else {
                release();
            }
        }
    }

    /**
     * 添加表情资源
     */
    public void addEmoji(int resId) {
        mResList.add(resId);
    }

    /**
     * 设置表情数量
     */
    public void setCount(int count) {
        mEmojiCount = count;
    }

    /**
     * 设置表情总位移距离
     */
    public void setDistance(int distance) {
        mDistance = distance;
    }

    /**
     * 开始表情雨降落
     */
    public void start() {
        if (mResList.size() == 0) {
            return;
        }
        isStart = true;
        initData();
        postInvalidate();
    }

    /**
     * 停止
     */
    public void stop() {
        release();
    }

    /**
     * 初始化表情数据
     */
    private void initData() {
        if (mDistance == 0) {
            mDistance = getHeight() + getHeight() / 2;
        }
        for (int i = 0; i < mEmojiCount; i++) {
            EmojiItem item = new EmojiItem();
            int resId = mResList.get(VMUtils.random(mResList.size()));
            item.bitmap = BitmapFactory.decodeResource(getResources(), resId);
            // 起始横坐标在[100, getWidth()-100) 之间
            item.x = VMUtils.random(getWidth() - 300) + 150;
            // 起始纵坐标在(-getHeight(),0] 之间，即一开始位于屏幕上方以外
            item.y = -VMUtils.random(mDistance);
            // 横向偏移[-2,2) ，即左右摇摆区间
            item.offsetX = VMUtils.random(4) - 2;
            // 纵向固定下落距离
            item.offsetY = VMUtils.random(5, 9);
            // 缩放比例[0.8, 1.2) 之间
            item.scale = (float) (VMUtils.random(40) + 80) / 100f;
            mEmojiList.add(item);
        }
    }

    /**
     * 释放资源
     */
    private void release() {
        isStart = false;
        if (mResList != null && mResList.size() > 0) {
            mResList.clear();
        }
        if (mEmojiList != null && mEmojiList.size() > 0) {
            for (EmojiItem item : mEmojiList) {
                if (!item.bitmap.isRecycled()) {
                    item.bitmap.recycle();
                }
            }
            mEmojiList.clear();
        }
    }


    /**
     * 表情实体类，记录表情下落的各种数据
     */
    private class EmojiItem {
        // 坐标
        public int x;
        public int y;
        // 横向偏移
        public int offsetX;
        // 纵向偏移
        public int offsetY;
        // 缩放
        public float scale;
        // 图片资源
        public Bitmap bitmap;
    }

}
