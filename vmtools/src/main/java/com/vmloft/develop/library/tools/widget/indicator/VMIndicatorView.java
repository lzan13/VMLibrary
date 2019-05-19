package com.vmloft.develop.library.tools.widget.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.utils.VMColor;
import com.vmloft.develop.library.tools.utils.VMDimen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzan13 on 2019/04/10
 *
 * 自定义指示器控件
 */
public class VMIndicatorView extends View {

    // 指示器半径
    private int mIndicatorRadius;
    // 指示器之间的距离
    private int mIndicatorMargin;
    // 指示器背景色
    private int mIndicatorBackground;
    // 指示器选中颜色
    private int mIndicatorSelected;
    // 指示器当前位置
    private int mCurrentPosition;
    // 指示器当前位置偏移量
    private float mCurrentPositionOffset;
    // 指示器对齐方式
    private Gravity mIndicatorLayoutGravity;
    // 指示器模式
    private Mode mIndicatorMode;
    // 可以动的指示器对象
    private VMIndicatorHolder moveHolder;
    private List<VMIndicatorHolder> mHolders;
    private ViewPager mViewPager;

    public VMIndicatorView(Context context) {
        this(context, null);
    }

    public VMIndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VMIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        // 初始化默认值
        mIndicatorRadius = VMDimen.dp2px(4);
        mIndicatorMargin = VMDimen.dp2px(8);
        mIndicatorBackground = VMColor.byRes(R.color.vm_gray_54);
        mIndicatorSelected = VMColor.byRes(R.color.vm_green_87);
        mCurrentPosition = 0;
        mCurrentPositionOffset = 0;
        mIndicatorLayoutGravity = Gravity.CENTER;
        mIndicatorMode = Mode.SOLO;
        mHolders = new ArrayList<>();

        handleAttrs(context, attrs);
    }

    /**
     * 获取资源属性
     *
     * @param context
     * @param attrs
     */
    private void handleAttrs(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VMIndicatorView);
        mIndicatorRadius = typedArray.getDimensionPixelSize(R.styleable.VMIndicatorView_vm_indicator_radius, mIndicatorRadius);
        mIndicatorMargin = typedArray.getDimensionPixelSize(R.styleable.VMIndicatorView_vm_indicator_margin, mIndicatorMargin);
        mIndicatorBackground = typedArray.getColor(R.styleable.VMIndicatorView_vm_indicator_background, mIndicatorBackground);
        mIndicatorSelected = typedArray.getColor(R.styleable.VMIndicatorView_vm_indicator_selected, mIndicatorSelected);
        int layoutGravity = typedArray.getInt(R.styleable.VMIndicatorView_vm_indicator_gravity, mIndicatorLayoutGravity.ordinal());
        mIndicatorLayoutGravity = Gravity.values()[layoutGravity];
        int layoutMode = typedArray.getInt(R.styleable.VMIndicatorView_vm_indicator_mode, mIndicatorMode.ordinal());
        mIndicatorMode = Mode.values()[layoutMode];
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int layer = canvas.saveLayer(0, 0, getWidth(), getHeight(), null);

        for (VMIndicatorHolder holder : mHolders) {
            drawHolder(canvas, holder);
        }

        if (null != moveHolder) {
            drawHolder(canvas, moveHolder);
        }

        canvas.restoreToCount(layer);
    }

    /**
     * 注入ViewPager
     *
     * @param viewPager
     */
    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        createTabItems();
        createMoveItems();
        initViewPagerListener();
    }

    /**
     * 监听 ViewPager 滑动
     */
    private void initViewPagerListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mIndicatorMode != Mode.SOLO) {
                    trigger(position, positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (mIndicatorMode == Mode.SOLO) {
                    trigger(position, 0);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 创建小圆点个数,依赖于ViewPager
     */
    private void createTabItems() {
        for (int i = 0; i < mViewPager.getAdapter().getCount(); i++) {
            OvalShape circle = new OvalShape();
            ShapeDrawable drawable = new ShapeDrawable(circle);
            VMIndicatorHolder holder = new VMIndicatorHolder(drawable);

            Paint paint = drawable.getPaint();
            paint.setColor(mIndicatorBackground);
            paint.setAntiAlias(true);

            holder.setPaint(paint);
            mHolders.add(holder);
        }
    }

    /**
     * 创建移动小圆点
     */
    private void createMoveItems() {
        OvalShape circle = new OvalShape();
        ShapeDrawable drawable = new ShapeDrawable(circle);
        moveHolder = new VMIndicatorHolder(drawable);

        Paint paint = drawable.getPaint();
        paint.setColor(mIndicatorSelected);
        paint.setAntiAlias(true);

        switch (mIndicatorMode) {
        case INSIDE:
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
            break;
        case OUTSIDE:
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
            break;
        case SOLO:
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            break;
        default:
            break;
        }

        moveHolder.setPaint(paint);
    }

    private void trigger(int position, float positionOffset) {
        this.mCurrentPosition = position;
        this.mCurrentPositionOffset = positionOffset;
        requestLayout();
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int width = getWidth();
        int height = getHeight();

        layoutItem(width, height);
        layoutMoveItem(mCurrentPosition, mCurrentPositionOffset);
    }

    /**
     * 计算每个小圆点位置
     */
    private void layoutItem(int width, int height) {
        if (null == mHolders) {
            throw new IllegalArgumentException("forget to create items?");
        }

        float heightY = height * 0.5f;
        int startPosition = startDrawPosition(width);

        for (int i = 0; i < mHolders.size(); i++) {
            VMIndicatorHolder holder = mHolders.get(i);
            holder.resizeShape(2 * mIndicatorRadius, 2 * mIndicatorRadius);
            holder.setY(heightY - mIndicatorRadius);

            int x = startPosition + (2 * mIndicatorRadius + mIndicatorMargin) * i;
            holder.setX(x);
        }
    }

    /**
     * 设置移动小圆点位置
     *
     * @param curItemPosition
     * @param curItemPositionOffset
     */
    private void layoutMoveItem(int curItemPosition, float curItemPositionOffset) {
        if (null == moveHolder) {
            throw new IllegalArgumentException("forget to create moveitem?");
        }

        if (0 == mHolders.size()) {
            return;
        }

        VMIndicatorHolder holder = mHolders.get(curItemPosition);
        moveHolder.resizeShape(holder.getWidth(), holder.getHeight());

        float x = holder.getX() + (mIndicatorMargin + mIndicatorRadius * 2) * curItemPositionOffset;
        moveHolder.setX(x);
        moveHolder.setY(holder.getY());
    }

    /**
     * 设置小圆点起始位置
     *
     * @param width
     */
    private int startDrawPosition(int width) {
        if (mIndicatorLayoutGravity == Gravity.LEFT) {
            return 0;
        }

        int tabItemLength = mHolders.size() * (2 * mIndicatorRadius + mIndicatorMargin) - mIndicatorMargin;
        if (width < tabItemLength) {
            return 0;
        }

        if (mIndicatorLayoutGravity == Gravity.CENTER) {
            return (width - tabItemLength) / 2;
        }

        return width - tabItemLength;
    }

    /**
     * 开始绘画圆点
     *
     * @param canvas
     * @param holder
     */
    private void drawHolder(Canvas canvas, VMIndicatorHolder holder) {
        canvas.save();
        canvas.translate(holder.getX(), holder.getY());
        holder.getShape().draw(canvas);
        canvas.restore();
    }

    /**
     * 修改指示器切换模式
     */
    public void setIndicatorMode(Mode indicatorMode) {
        this.mIndicatorMode = indicatorMode;
    }

    /**
     * 修改指示器距离
     */
    public void setIndicatorMargin(int indicatorMargin) {
        this.mIndicatorMargin = indicatorMargin;
    }

    /**
     * 修改指示器半径大小
     */
    public void setIndicatorRadius(int indicatorRadius) {
        this.mIndicatorRadius = indicatorRadius;
    }

    /**
     * 修改指示器对齐方式
     */
    public void setIndicatorLayoutGravity(Gravity indicatorLayoutGravity) {
        this.mIndicatorLayoutGravity = indicatorLayoutGravity;
    }

    /**
     * 修改指示器背景色
     */
    public void setIndicatorBackground(int indicatorBackground) {
        this.mIndicatorBackground = indicatorBackground;
    }

    /**
     * 修改指示器选中色
     */
    public void setIndicatorSelected(int indicatorSelected) {
        this.mIndicatorSelected = indicatorSelected;
    }


    /**
     * 对齐方式
     */
    public enum Gravity {
        LEFT, CENTER, RIGHT;
    }

    /**
     * 切换模式
     */
    public enum Mode {
        INSIDE, OUTSIDE, SOLO;
    }
}