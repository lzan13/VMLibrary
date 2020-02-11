package com.vmloft.develop.library.tools.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.appcompat.widget.AppCompatImageView;

import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.utils.VMColor;
import com.vmloft.develop.library.tools.utils.VMDimen;
import com.vmloft.develop.library.tools.utils.bitmap.VMBlur;

/**
 * Created by lzan13 on 2017/3/30.
 * 自定义 ImageView 控件，实现了圆角和边框，以及按下变色
 */
public class VMImageView extends AppCompatImageView {
    // 图片按下的画笔
    private Paint pressPaint;
    // 图片的宽高
    private int width;
    private int height;

    // 定义 Bitmap 的默认配置
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 1;

    // 模糊半径，为 0 即不进行模糊
    protected int blurRadius;
    // 模糊前的缩放比例，原图缩放的越小，模糊耗费资源越少
    protected int blurScale;
    // 边框颜色
    protected int borderColor;
    // 边框宽度
    protected int borderWidth;
    // 按下的透明度
    protected int pressAlpha;
    // 按下的颜色
    protected int pressColor;
    // 圆角半径
    protected int radius;
    // 图片类型（0正常, 1圆形, 2圆角）
    protected int shapeType;
    // 当前控件是否继续分发触摸事件，默认 true 即继续向下分发，不拦截触摸事件
    protected boolean isDispatchTouchEvent = true;

    public VMImageView(Context context) {
        super(context);
        init(context, null);
    }

    public VMImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public VMImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //初始化默认值
        blurRadius = 0;
        blurScale = 2;
        borderWidth = VMDimen.dp2px(2);
        borderColor = VMColor.byRes(R.color.vm_white_87);
        pressAlpha = 0x42;
        pressColor = VMColor.byRes(R.color.vm_black_38);
        radius = VMDimen.dp2px(8);
        shapeType = 2;

        // 获取控件的属性值
        handleAttrs(context, attrs);

        // 按下的画笔设置
        pressPaint = new Paint();
        pressPaint.setAntiAlias(true);
        pressPaint.setStyle(Paint.Style.FILL);
        pressPaint.setColor(pressColor);
        pressPaint.setAlpha(0);
        pressPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        setClickable(true);
        setDrawingCacheEnabled(true);
        setWillNotDraw(false);
    }


    private void handleAttrs(Context context, AttributeSet attrs) {
        // 获取控件的属性值
        if (attrs == null) {
            return;
        }

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VMImageView);
        blurRadius = array.getInteger(R.styleable.VMImageView_vm_blur_radius, blurRadius);
        blurScale = array.getInteger(R.styleable.VMImageView_vm_blur_scale, blurScale);
        borderColor = array.getColor(R.styleable.VMImageView_vm_border_color, borderColor);
        borderWidth = array.getDimensionPixelOffset(R.styleable.VMImageView_vm_border_width, borderWidth);
        pressAlpha = array.getInteger(R.styleable.VMImageView_vm_press_alpha, pressAlpha);
        pressColor = array.getColor(R.styleable.VMImageView_vm_press_color, pressColor);
        radius = array.getDimensionPixelOffset(R.styleable.VMImageView_vm_radius, radius);
        shapeType = array.getInteger(R.styleable.VMImageView_vm_shape_type, shapeType);
        isDispatchTouchEvent = array.getBoolean(R.styleable.VMImageView_vm_dispatch_touch_event, isDispatchTouchEvent);
        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (shapeType == 0) {
            super.onDraw(canvas);
            return;
        }
        // 获取当前控件的 drawable
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        // 这里 get 回来的宽度和高度是当前控件相对应的宽度和高度（在 xml 设置）
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        // 获取 bitmap，即传入 imageview 的 bitmap
        // Bitmap bitmap = ((BitmapDrawable) ((SquaringDrawable)
        // drawable).getCurrent()).getBitmap();
        // 这里参考赵鹏的获取 bitmap 方式，因为上边的获取会导致 Glide 加载的drawable 强转为 BitmapDrawable 出错
        Bitmap bitmap = getBitmapFromDrawable(drawable);
        if (bitmap == null) {
            return;
        }
        // 根据模糊半径是否为 0 来判断是否模糊图片
        if (blurRadius > 0) {
            bitmap = VMBlur.stackBlurBitmap(bitmap, blurScale, blurRadius, false);
        }
        // 绘制图片到画板
        drawDrawable(canvas, bitmap);

        drawPress(canvas);
        drawBorder(canvas);
    }

    /**
     * 实现圆角的绘制
     */
    @SuppressLint("WrongConstant")
    private void drawDrawable(Canvas canvas, Bitmap bitmap) {
        // 画笔
        Paint paint = new Paint();
        // 颜色设置
        paint.setColor(0xffffffff);
        // 抗锯齿
        paint.setAntiAlias(true);
        //Paint 的 Xfermode，PorterDuff.Mode.SRC_IN 取两层图像的交集部门, 只显示上层图像。
        PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        // 标志
        int saveFlags = Canvas.ALL_SAVE_FLAG;
        canvas.saveLayer(0, 0, width, height, null, saveFlags);

        if (shapeType == 1) {
            // 画遮罩，画出来就是一个和空间大小相匹配的圆（这里在半径上 -1 是为了不让图片超出边框）
            canvas.drawCircle(width / 2, height / 2, width / 2 - 1, paint);
        } else if (shapeType == 2) {
            // 当ShapeType == 2 时 图片为圆角矩形 （这里在宽高上 -1 是为了不让图片超出边框）
            RectF rectf = new RectF(1, 1, getWidth() - 1, getHeight() - 1);
            canvas.drawRoundRect(rectf, radius + 1, radius + 1, paint);
        }

        paint.setXfermode(xfermode);

        // 空间的大小 / bitmap 的大小 = bitmap 缩放的倍数
        float scaleWidth = ((float) getWidth()) / bitmap.getWidth();
        float scaleHeight = ((float) getHeight()) / bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        //bitmap 缩放
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
                true);

        //draw 上去
        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.restore();
    }

    /**
     * 绘制控件的按下效果
     */
    private void drawPress(Canvas canvas) {
        // 这里根据类型判断绘制的效果是圆形还是矩形
        if (shapeType == 1) {
            // 当ShapeType == 1 时 图片为圆形 （这里在半径上 -1 是为了不让图片超出边框）
            canvas.drawCircle(width / 2, height / 2, width / 2 - 1, pressPaint);
        } else if (shapeType == 2) {
            // 当ShapeType == 2 时 图片为圆角矩形 （这里在宽高上 -1 是为了不让图片超出边框）
            RectF rectF = new RectF(1, 1, width - 1, height - 1);
            canvas.drawRoundRect(rectF, radius + 1, radius + 1, pressPaint);
        }
    }

    /**
     * 绘制自定义控件边框
     */
    private void drawBorder(Canvas canvas) {
        if (borderWidth > 0) {
            Paint paint = new Paint();
            paint.setStrokeWidth(borderWidth);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(borderColor);
            paint.setAntiAlias(true);
            // 根据控件类型的属性去绘制圆形或者矩形
            if (shapeType == 1) {
                canvas.drawCircle(width / 2, height / 2, (width - borderWidth) / 2, paint);
            } else if (shapeType == 2) {
                // 当ShapeType = 1 时 图片为圆角矩形
                RectF rectf =
                        new RectF(borderWidth / 2, borderWidth / 2, getWidth() - borderWidth / 2,
                                getHeight() - borderWidth / 2);
                canvas.drawRoundRect(rectf, radius, radius, paint);
            }
        }
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
        width = w;
        height = h;
    }

    /**
     * View 的事件分发
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isDispatchTouchEvent) {
            return false;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 重写 onTouchEvent 监听方法，用来响应控件触摸
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pressPaint.setAlpha(pressAlpha);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                pressPaint.setAlpha(0);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            default:
                pressPaint.setAlpha(0);
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 这里是参考其他开发者获取Bitmap内容的方法， 之前是因为没有考虑到 Glide 加载的图片
     * 导致drawable 类型是属于 SquaringDrawable 类型，导致强转失败
     * 这里是通过drawable不同的类型来进行获取Bitmap
     */
    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        try {
            Bitmap bitmap;
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            } else if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION,
                        BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 设置边框颜色
     */
    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        invalidate();
    }

    /**
     * 设置边框宽度
     */
    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    /**
     * 设置图片按下颜色透明度
     */
    public void setPressAlpha(int pressAlpha) {
        this.pressAlpha = pressAlpha;
    }

    /**
     * 设置图片按下的颜色
     */
    public void setPressColor(int pressColor) {
        this.pressColor = pressColor;
    }

    /**
     * 设置倒角半径
     */
    public void setRadius(int radius) {
        this.radius = radius;
        invalidate();
    }

    /**
     * 设置形状类型
     */
    public void setShapeType(int shapeType) {
        this.shapeType = shapeType;
        invalidate();
    }
}
