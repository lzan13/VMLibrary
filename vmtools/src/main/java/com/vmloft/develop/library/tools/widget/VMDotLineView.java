package com.vmloft.develop.library.tools.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.utils.VMLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzan13 on 2017/3/22.
 * 点线相连控件，可以定义点个数，连线动画效果
 */
public class VMDotLineView extends View {

    protected int width;
    protected int height;

    // 坐标点集合
    private List<Point> points = new ArrayList<>();
    // 控件画笔
    protected Paint paint;

    // 点的颜色
    protected int dotColor = 0xddf82304;
    // 点的半径
    protected int dotRadius = 10;
    // 点圆弧线宽
    protected int dotWidth = 4;
    // 线的颜色
    protected int lineColor = 0xddf86734;
    // 线的宽度
    protected int lineWidth = 8;
    // 是否闭合
    protected boolean isClosure = false;

    // 当前绘制到的点索引
    protected int currentIndex = 0;

    // 当前绘制到的坐标点
    protected Point currentPoint;
    // 下一个绘制到的坐标点
    protected Point nextPoint;
    // 线绘制速度
    protected int speed = 10;
    protected boolean isChange = true;
    // 坐标点增量
    protected float incrementX = 0.0f;
    protected float incrementY = 0.0f;
    protected float stepX = 0.0f;
    protected float stepY = 0.0f;

    /**
     * 构造方法
     */
    public VMDotLineView(Context context) {
        super(context);
        init(context, null);
    }

    public VMDotLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public VMDotLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 控件初始化
     */
    protected void init(Context context, AttributeSet attrs) {

        // 获取控件的属性值
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VMDotLineView);
            // 获取自定义属性值，如果没有设置就是默认值
            dotColor = array.getColor(R.styleable.VMDotLineView_vm_dot_color, dotColor);
            dotRadius = array.getDimensionPixelOffset(R.styleable.VMDotLineView_vm_dot_radius,
                    dotRadius);
            dotWidth =
                    array.getDimensionPixelOffset(R.styleable.VMDotLineView_vm_dot_width, dotWidth);

            lineColor = array.getColor(R.styleable.VMDotLineView_vm_line_color, lineColor);
            lineWidth = array.getDimensionPixelOffset(R.styleable.VMDotLineView_vm_line_width,
                    lineWidth);

            isClosure = array.getBoolean(R.styleable.VMDotLineView_vm_is_closure, isClosure);
            speed = array.getInteger(R.styleable.VMDotLineView_vm_speed, speed);

            // 回收资源
            array.recycle();
        }

        // 实例化画笔
        paint = new Paint();
        // 设置画笔颜色
        paint.setColor(0xdd2384fe);
        // 设置抗锯齿
        paint.setAntiAlias(true);
        // 效果同上
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        // 设置画笔宽度
        paint.setStrokeWidth(lineWidth);
        // 设置画笔模式
        paint.setStyle(Paint.Style.STROKE);
        // 设置画笔末尾样式
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    /**
     * 重写 onDraw 方法
     *
     * @param canvas 当前 View 画布
     */
    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制线
        drawLine(canvas);
        // 绘制闭合部分
        drawClosureLine(canvas);
        // 绘制点
        drawDot(canvas);
    }

    /**
     * 绘制线
     *
     * @param canvas 当前控件画布
     */
    protected void drawLine(Canvas canvas) {
        // 设置绘制线时的颜色
        paint.setColor(lineColor);
        paint.setStrokeWidth(lineWidth);
        checkCurrentPoint(currentIndex + 1);
        // 绘制已经绘制过的线段
        int count = 0;
        if (currentIndex < points.size() - 1) {
            canvas.drawLine(currentPoint.x, currentPoint.y, currentPoint.x + incrementX,
                    currentPoint.y + incrementY, paint);
            while (count < currentIndex) {
                Point point1 = points.get(count);
                Point point2 = points.get(count + 1);
                canvas.drawLine(point1.x, point1.y, point2.x, point2.y, paint);
                count++;
            }
            // 没有绘制到最后一个点，就一直循环
            if (currentIndex < points.size() - 1) {
                postInvalidateDelayed(speed);
            }
        } else if (currentIndex == points.size() - 1) {
            while (count < points.size() - 1) {
                Point point1 = points.get(count);
                Point point2 = points.get(count + 1);
                canvas.drawLine(point1.x, point1.y, point2.x, point2.y, paint);
                count++;
            }
        } else if (currentIndex == points.size()) {
            while (count < points.size() - 1) {
                Point point1 = points.get(count);
                Point point2 = points.get(count + 1);
                canvas.drawLine(point1.x, point1.y, point2.x, point2.y, paint);
                count++;
            }
            Point pointEnd = points.get(points.size() - 1);
            Point pointStart = points.get(0);
            canvas.drawLine(pointEnd.x, pointEnd.y, pointStart.x, pointStart.y, paint);
        }
    }

    /**
     * 绘制闭合部分线
     *
     * @param canvas 当前控件画布
     */
    protected void drawClosureLine(Canvas canvas) {
        if (isClosure && currentIndex == points.size() - 1) {

            checkCurrentPoint(0);

            canvas.drawLine(currentPoint.x, currentPoint.y, currentPoint.x + incrementX,
                    currentPoint.y + incrementY, paint);

            // 没有绘制到最后一个点，就一直循环
            if (currentIndex < points.size()) {
                postInvalidateDelayed(speed);
            }
        }
    }

    /**
     * 绘制点
     *
     * @param canvas 当前控件画布
     */
    protected void drawDot(Canvas canvas) {
        paint.setColor(dotColor);
        paint.setStrokeWidth(dotWidth);
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            //VMLog.d("drawDot x: %d, y: %d", point.x, point.y);
            // 绘制圆弧，当绘制圆弧为360度的时候，和下边的绘制圆环效果一样
            //RectF rectF = new RectF(point.x - dotRadius, point.y - dotRadius, point.x + dotRadius,
            //        point.y + dotRadius);
            //canvas.drawArc(rectF, 0, 360, false, paint);
            // 绘制圆环
            //canvas.drawMic(point.x, point.y, dotRadius, paint);

            // 这里进行判断下，是否已经连接到此点，如果是则绘制实心点，否则绘制空心点，后期可以给这个点加上动画效果
            if (i <= currentIndex) {
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(point.x, point.y, dotRadius, paint);
            } else {
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(point.x, point.y, dotRadius, paint);
            }
        }
    }

    /**
     * 检查是否绘制到下一个线段
     */
    protected void checkCurrentPoint(int nextIndex) {
        if (isChange) {
            if (currentIndex >= points.size()) {
                VMLog.e("IndexOutOf");
                return;
            }
            currentPoint = points.get(currentIndex);
            nextPoint = points.get(nextIndex);
            int distanceX = nextPoint.x - currentPoint.x;
            int distanceY = nextPoint.y - currentPoint.y;
            double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
            double time = distance / speed;
            stepX = (float) (distanceX / time);
            stepY = (float) (distanceY / time);
            isChange = false;
        }
        // 让当前线段的增量加上变化值
        incrementX += stepX;
        incrementY += stepY;

        // 判断是否到达下一个点
        if (incrementX > 0) {
            if (currentPoint.x + incrementX >= nextPoint.x) {
                isChange = true;
                currentIndex++;
                incrementX = 0.0f;
                incrementY = 0.0f;
            }
        } else if (incrementX < 0) {
            if (currentPoint.x + incrementX <= nextPoint.x) {
                isChange = true;
                currentIndex++;
                incrementX = 0.0f;
                incrementY = 0.0f;
            }
        } else {
            if (incrementY > 0) {
                if (currentPoint.y + incrementY >= nextPoint.y) {
                    isChange = true;
                    currentIndex++;
                    incrementX = 0.0f;
                    incrementY = 0.0f;
                }
            } else {
                if (currentPoint.y + incrementY <= nextPoint.y) {
                    isChange = true;
                    currentIndex++;
                    incrementX = 0.0f;
                    incrementY = 0.0f;
                }
            }
        }
    }

    /**
     * 添加一个坐标点
     *
     * @param point 添加的点坐标
     * @return 返回添加的坐标点在集合中的位置
     */
    public int addPoint(Point point) {
        if (points != null) {
            points.add(point);
            invalidate();
            return points.size();
        }
        return -1;
    }

    /**
     * 移除坐标集合中的一个点
     *
     * @param position 移除点位置
     * @return 返回移除的坐标点
     */
    public Point removePoint(int position) {
        if (points != null && position < points.size()) {
            return points.remove(position);
        }
        return null;
    }

    /**
     * 清空坐标点集合
     */
    public void clearPoints() {
        if (points != null) {
            points.clear();
        }
    }

    public void setDotColor(int dotColor) {
        this.dotColor = dotColor;
    }

    public void setDotRadius(int dotRadius) {
        this.dotRadius = dotRadius;
    }

    public void setDotWidth(int dotWidth) {
        this.dotWidth = dotWidth;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    /**
     * 设置控件是否闭合
     *
     * @param closure 是否闭合
     */
    public void setClosure(boolean closure) {
        isClosure = closure;
    }

    /**
     * 刷新控件，就是进行重新绘制
     */
    public void refresh() {
        incrementX = 0.0f;
        incrementY = 0.0f;
        isChange = true;
        currentIndex = 0;
        currentPoint = null;
        nextPoint = null;
        postInvalidate();
    }
}
