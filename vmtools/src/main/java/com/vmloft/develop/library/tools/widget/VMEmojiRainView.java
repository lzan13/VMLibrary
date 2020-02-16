package com.vmloft.develop.library.tools.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.vmloft.develop.library.tools.utils.VMDimen;
import com.vmloft.develop.library.tools.utils.VMUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by lzan13 on 2019/7/17 10:34
 *
 * 表情雨控件，在 helloworldyx 基础上修改
 * 感谢 https://github.com/helloworldyx/EmoticonRainView
 */
public class VMEmojiRainView extends View {
    // 是否自动回收 bitmap
    private boolean isAutoRecycle = true;
    // 是否正在下表情雨
    private boolean isRaining;

    private Matrix mMatrix;
    private Paint mPaint;

    // 表情雨开始的时间戳
    private long mStartTimestamp;
    // x轴起始左右padding
    private int mXStartPadding;

    private final List<Bitmap> mBaseBitmaps = new ArrayList<>();
    private final List<Emoticon> mEmoticonList = new ArrayList<>();
    // 表情图宽高
    private float mEmoticonWidth;
    private float mEmoticonHeight;
    // 表情雨显示最大个数
    private int mMaxCount = 36;
    // 两个表情最大延迟时间间隔
    private int mMaxDelay = 160;
    // 最大持续时间
    private int mMaxDuration = 2500;
    private int mMinDuration = 2200;
    // 表情缩放百分比
    private int mMaxScale = 100;
    private int mMinScale = 80;

    public VMEmojiRainView(Context context) {
        this(context, null);
    }

    public VMEmojiRainView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VMEmojiRainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setVisibility(GONE);
        setWillNotDraw(false);

        mXStartPadding = VMDimen.dp2px(15);
        mEmoticonWidth = VMDimen.dp2px(32);
        mEmoticonHeight = VMDimen.dp2px(32);

        // 初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);
        mMatrix = new Matrix();
    }

    /**
     * 设置控件是否自动回收传入的 bitmaps
     */
    public void setAutoRecycle(boolean autoRecycle) {
        this.isAutoRecycle = autoRecycle;
    }

    /**
     * 开始动画
     */
    public void start(final Config config) {
        if (config == null || ((config.bitmapList == null || config.bitmapList.size() == 0) && (config.resList == null || config.resList.size() == 0))) {
            throw new RuntimeException("配置不能为空");
        }
        stop();
        setVisibility(VISIBLE);

        post(() -> {
            initAndResetData(config);
            isRaining = true;
            invalidate();
        });
    }

    /**
     * 初始化并重置数据
     */
    private void initAndResetData(Config config) {
        if (config == null) {
            return;
        }
        if (config.width != 0) {
            mEmoticonWidth = config.width;
        }
        if (config.height != 0) {
            mEmoticonHeight = config.height;
        }
        if (config.count != 0) {
            mMaxCount = config.count;
        }
        if (config.delay != 0) {
            mMaxDelay = config.delay;
        }
        if (config.maxDuration != 0) {
            mMaxDuration = config.maxDuration;
        }
        if (config.minDuration != 0) {
            mMinDuration = config.minDuration;
        }
        if (config.maxScale != 0) {
            mMaxScale = config.maxScale;
        }
        if (config.minScale != 0) {
            mMinScale = config.minScale;
        }

        mStartTimestamp = System.currentTimeMillis();

        mBaseBitmaps.clear();
        if (config.bitmapList != null && config.bitmapList.size() > 0) {
            mBaseBitmaps.addAll(config.bitmapList);
        } else if (config.resList != null && config.resList.size() > 0) {
            for (int resId : config.resList) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
                mBaseBitmaps.add(bitmap);
            }
        }
        mEmoticonList.clear();

        // 开始画表情的总时间
        int currentDuration = 0;
        int type = mBaseBitmaps.size();
        int count = 0;
        while (count < mMaxCount) {
            Emoticon.Builder builder = new Emoticon.Builder().bitmap(mBaseBitmaps.get(count % type));
            float scale = (VMUtils.random(mMaxScale - mMinScale + 1) + mMinScale) / 100f;
            builder.scale(scale).x(VMUtils.random(getWidth() - (int) (mEmoticonWidth * scale) - mXStartPadding * 2) + mXStartPadding);
            int y = (int) -Math.ceil(mEmoticonHeight * scale);
            builder.y(y);

            float height = getHeight() + (-y);
            float duration = (VMUtils.random(mMaxDuration - mMinDuration + 1) + mMinDuration);
            // 下落速度(pixel / 16ms)
            int velocityY = (int) (height * 16 / duration);
            builder.velocityY(velocityY == 0 ? 1 : velocityY);
            builder.velocityX(VMUtils.random(4) - 2);
            builder.appearTimestamp(currentDuration);
            mEmoticonList.add(builder.build());

            currentDuration += VMUtils.random(mMaxDelay);
            count++;
        }
    }

    /**
     * 停止并考虑回收
     */
    public void stop() {
        isRaining = false;
        setVisibility(GONE);
        if (mBaseBitmaps != null && isAutoRecycle) {
            for (Bitmap bitmap : mBaseBitmaps) {
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isRaining) {
            return;
        }
        long currentTimestamp = System.currentTimeMillis();
        boolean isDrawSuccess = false;
        long totalTimeInterval = currentTimestamp - mStartTimestamp;
        if (mEmoticonList.size() > 0) {
            for (int i = 0; i < mEmoticonList.size(); i++) {
                Emoticon emoticon = mEmoticonList.get(i);
                Bitmap bitmap = emoticon.getBitmap();
                if (bitmap.isRecycled() || totalTimeInterval < emoticon.getAppearTimestamp()) {
                    continue;
                }
                isDrawSuccess = true;

                mMatrix.reset();

                float heightScale = mEmoticonHeight / bitmap.getHeight();
                float widthScale = mEmoticonWidth / bitmap.getWidth();
                mMatrix.setScale(widthScale * emoticon.getScale(), heightScale * emoticon.getScale());

                emoticon.setX(emoticon.getX() + emoticon.getVelocityX());
                emoticon.setY(emoticon.getY() + emoticon.getVelocityY());

                mMatrix.postTranslate(emoticon.getX(), emoticon.getY());

                canvas.drawBitmap(bitmap, mMatrix, mPaint);
            }
        }
        if (!isDrawSuccess) {
            stop();
        } else {
            postInvalidate();
        }
    }

    /**
     * 某张图的位置是否超出下边界
     */
    private boolean isOutOfBottomBound(int position) {
        return mEmoticonList.get(position).getY() > getHeight();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 停止
        stop();
    }


    /**
     * 配置
     */
    public static class Config {
        // 表情bitmap，虽然这里可以指定宽高，但是仍然需要先手动压缩一下bitmap，避免OOM
        private List<Bitmap> bitmapList;
        private List<Integer> resList;
        private int width;
        private int height;
        private int count;
        private int delay;
        private int maxDuration;
        private int minDuration;
        private int maxScale;
        private int minScale;

        private Config(Builder builder) {
            bitmapList = builder.bitmapList;
            resList = builder.resList;
            width = builder.width;
            height = builder.height;
            count = builder.count;
            delay = builder.delay;
            maxDuration = builder.maxDuration;
            minDuration = builder.minDuration;
            maxScale = builder.maxScale;
            minScale = builder.minScale;
        }

        public static final class Builder {
            private List<Bitmap> bitmapList;
            private List<Integer> resList;
            private int height;
            private int width;
            private int count;
            private int delay;
            private int maxDuration;
            private int minDuration;
            private int maxScale;
            private int minScale;

            public Builder() {
            }

            public Builder setBitmapList(List<Bitmap> val) {
                bitmapList = val;
                return this;
            }

            public Builder setResList(List<Integer> val) {
                resList = val;
                return this;
            }

            public Builder setHeight(int val) {
                height = val;
                return this;
            }

            public Builder setWidth(int val) {
                width = val;
                return this;
            }

            public Builder setCount(int val) {
                count = val;
                return this;
            }

            public Builder setDelay(int val) {
                delay = val;
                return this;
            }

            public Builder setMaxDuration(int val) {
                maxDuration = val;
                return this;
            }

            public Builder setMinDuration(int val) {
                minDuration = val;
                return this;
            }

            public Builder setMaxScale(int val) {
                maxScale = val;
                return this;
            }

            public Builder setMinScale(int val) {
                minScale = val;
                return this;
            }

            public Config build() {
                return new Config(this);
            }
        }
    }

    /**
     * 表情实体类，记录表情下落的各种数据
     */
    private static class Emoticon {
        // 出现的时间时间戳
        private int appearTimestamp;
        // 对应的Bitmap
        private Bitmap bitmap;
        // 随机缩放比例
        private float scale;
        // 位置
        private int x, y;
        // 位移速度
        private int velocityX, velocityY;

        private Emoticon(Builder builder) {
            appearTimestamp = builder.appearTimestamp;
            bitmap = builder.bitmap;
            scale = builder.scale;
            setX(builder.x);
            setY(builder.y);
            velocityX = builder.velocityX;
            velocityY = builder.velocityY;
        }

        public int getAppearTimestamp() {
            return appearTimestamp;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public float getScale() {
            return scale;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getVelocityX() {
            return velocityX;
        }

        public int getVelocityY() {
            return velocityY;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public static final class Builder {
            private int appearTimestamp;
            private Bitmap bitmap;
            private float scale;
            private int x;
            private int y;
            private int velocityX;
            private int velocityY;

            public Builder() {
            }

            public Builder appearTimestamp(int val) {
                appearTimestamp = val;
                return this;
            }

            public Builder bitmap(Bitmap val) {
                bitmap = val;
                return this;
            }

            public Builder scale(float val) {
                scale = val;
                return this;
            }

            public Builder x(int val) {
                x = val;
                return this;
            }

            public Builder y(int val) {
                y = val;
                return this;
            }

            public Builder velocityX(int val) {
                velocityX = val;
                return this;
            }

            public Builder velocityY(int val) {
                velocityY = val;
                return this;
            }

            public Emoticon build() {
                return new Emoticon(this);
            }
        }
    }

}
