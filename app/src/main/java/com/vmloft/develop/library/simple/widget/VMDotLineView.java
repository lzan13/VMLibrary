package com.vmloft.develop.library.simple.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
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
    // 画笔颜色
    protected int dotColor = 0xddf82304;
    protected int lineColor = 0xddf86734;
    // 画笔宽度
    protected float paintWidth = 8.0f;
    // 点坐标半径
    protected float dotRadius = 10.0f;
    // 当前绘制到的点索引
    protected int currentIndex = 0;

    // 当前绘制到的坐标点
    protected Point currentPoint;
    // 下一个绘制到的坐标点
    protected Point nextPoint;
    // 线绘制速度
    protected float speed = 3.0f;
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
        init(null);
    }

    public VMDotLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public VMDotLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
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
        // 绘制点
        drawDot(canvas);
    }

    /**
     * 绘制点
     *
     * @param canvas 当前控件画布
     */
    protected void drawDot(Canvas canvas) {
        paint.setColor(dotColor);
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            //VMLog.d("drawDot x: %d, y: %d", point.x, point.y);
            // 绘制圆弧，当绘制圆弧为360度的时候，和下边的绘制圆环效果一样
            //RectF rectF = new RectF(point.x - dotRadius, point.y - dotRadius, point.x + dotRadius,
            //        point.y + dotRadius);
            //canvas.drawArc(rectF, 0, 360, false, paint);
            // 绘制圆环
            //canvas.drawCircle(point.x, point.y, dotRadius, paint);

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
     * 绘制线
     */
    protected void drawLine(Canvas canvas) {
        // 设置绘制线时的颜色
        paint.setColor(lineColor);

        checkCurrentPoint();

        canvas.drawLine(currentPoint.x, currentPoint.y, currentPoint.x + incrementX,
                currentPoint.y + incrementY, paint);

        // 绘制已经绘制过的线段
        int count = 0;
        while (count < currentIndex) {
            Point point1 = points.get(count);
            Point point2 = points.get(count + 1);
            canvas.drawLine(point1.x, point1.y, point2.x, point2.y, paint);
            count++;
        }
        // 没有绘制到最后一个点，就一直循环
        if (currentIndex < points.size() - 1) {
            postInvalidateDelayed(10);
        }
    }

    /**
     * 检查是否绘制到下一个线段
     */
    protected void checkCurrentPoint() {
        if (isChange) {
            currentPoint = points.get(currentIndex);
            nextPoint = points.get(currentIndex + 1);
            int distanceX = nextPoint.x - currentPoint.x;
            int distanceY = nextPoint.y - currentPoint.y;
            VMLog.d("distance x: " + distanceX + "; y: " + distanceY);
            double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
            double time = distance / speed;
            stepX = (float) (distanceX / time);
            stepY = (float) (distanceY / time);
            VMLog.d("step x: " + stepX + "; y: " + stepY);
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
     * 控件初始化
     */

    protected void init(AttributeSet attrs) {
        // 实例化画笔
        paint = new Paint();
        // 设置画笔颜色
        paint.setColor(0xdd2384fe);
        // 设置抗锯齿
        paint.setAntiAlias(true);
        // 效果同上
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        // 设置画笔宽度
        paint.setStrokeWidth(paintWidth);
        // 设置画笔模式
        paint.setStyle(Paint.Style.STROKE);
        // 设置画笔末尾样式
        paint.setStrokeCap(Paint.Cap.ROUND);
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
}
