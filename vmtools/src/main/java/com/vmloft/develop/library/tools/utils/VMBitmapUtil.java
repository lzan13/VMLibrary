package com.vmloft.develop.library.tools.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Base64;
import android.view.View;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by lzan13 on 2016/1/11.
 * 图片处理类，压缩，转换
 */
public class VMBitmapUtil {

    /**
     * 将Bitmap 转为 base64 字符串
     *
     * @param bitmap 需要转为Base64 字符串的Bitmap对象
     * @return 返回转换后的字符串
     */
    public static String bitmp2String(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        result = Base64.encodeToString(b, Base64.DEFAULT);
        return result;
    }

    /**
     * 将 base64 的字符串 转为Bimmap
     *
     * @param imageData 需要转为Bitmap的 Base64 字符串
     * @return 转为Bitmap的对象
     */
    public static Bitmap string2Bitmap(String imageData) {
        Bitmap bitmap = null;
        byte[] decode = Base64.decode(imageData, Base64.DEFAULT);
        bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        return bitmap;
    }

    /**
     * 获取图片文件的缩略图
     *
     * @param path 图片文件路径
     * @param dimension 设置缩略图最大尺寸
     * @return 返回压缩后的缩略图
     */
    public static Bitmap loadBitmapThumbnail(String path, int dimension) {
        Bitmap bitmap = loadBitmapByFile(path, dimension);
        // 调用矩阵方法压缩图片
        return compressBitmapByMatrixToDimension(bitmap, dimension);
    }

    /**
     * 获取图片文件的缩略图
     *
     * @param path 图片文件路径
     * @return 返回加载的图片Bitmap
     */
    public static Bitmap loadBitmapByFile(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 设置图片文件仅仅加载边界为false
        options.inJustDecodeBounds = false;
        // 加载图片到 Bitmap 对象并返回
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 通过文件加载图片，这里也可以加载大图，保证不会出现 OOM，
     *
     * @param path 要压缩的图片路径
     * @param dimension 定义压缩后的最大尺寸
     * @return 返回经过压缩处理的图片
     */
    public static Bitmap loadBitmapByFile(String path, int dimension) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设为true
        // 这个参数的意义是仅仅解析边缘区域，从而可以得到图片的一些信息，比如大小，而不会整个解析图片，防止OOM
        options.inJustDecodeBounds = true;

        // 此时bitmap还是为空的
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        // 得到图片文件的实际宽高
        int actualWidth = options.outWidth;
        int actualHeight = options.outHeight;

        // 根据宽高计算缩放比例
        float scale = getZoomScale(actualWidth, actualHeight, dimension);
        if (scale % 2 > 0.4) {
            scale += 1;
        }
        // 设置压缩比，必须是整形数，这样加载出的bitmap不会占用过大内存，又能显示清晰
        options.inSampleSize = (int) scale;
        // 设置图片文件仅仅加载边界为false
        options.inJustDecodeBounds = false;
        // 加载图片到 Bitmap 对象并返回
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 按比例压缩 Bitmap
     *
     * @param bitmap 需要压缩的 Bitmap
     * @param dimension 图片压缩后最大宽高
     * @return 返回压缩后的 Bitmap
     */
    public static Bitmap compressBitmapByMatrixToDimension(Bitmap bitmap, int dimension) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        float scale = 0.5f;
        if (w > h) {
            scale = (float) dimension / w;
        } else {
            scale = (float) dimension / h;
        }
        // 使用矩阵进行压缩图片
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap result =
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
                        true);
        return result;
    }

    /**
     * 按比例压缩 Bitmap
     *
     * @param bitmap 需要压缩的 Bitmap
     * @param scale 图片压缩比率(0-1)
     * @return 返回压缩后的 Bitmap
     */
    public static Bitmap compressBitmapByMatrixToScale(Bitmap bitmap, int scale) {
        // 使用矩阵进行压缩图片
        Matrix matrix = new Matrix();
        float s = (float) 1 / scale;
        matrix.postScale(s, s);
        Bitmap result =
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
                        true);
        return result;
    }

    /**
     * 获取一个 View 的缓存视图
     *
     * @param view 需要保存图像的 View 控件
     * @return 返回保存的 Bitmap 图像
     */
    private Bitmap getCacheBitmapFromView(View view) {
        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        return bitmap;
    }

    /**
     * 获取最佳缩放比例
     *
     * @param actualWidth Bitmap的实际宽度
     * @param actualHeight Bitmap的实际高度
     * @param dimension 定义压缩后最大尺寸
     * @return 返回最佳缩放比例
     */
    public static float getZoomScale(int actualWidth, int actualHeight, int dimension) {
        float scale = 1.0f;
        if (actualWidth > actualHeight) {
            scale = (float) actualWidth / dimension;
        } else {
            scale = (float) actualHeight / dimension;
        }
        return scale;
    }

    /**
     * 保存Bitmap到SD卡
     *
     * @param bitmap 需要保存的图片数据
     * @param path 保存路径
     */
    public static void saveBitmapToSDCard(Bitmap bitmap, String path) {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(path);
            if (outputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                outputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据传入的路径，获取图片的宽高
     *
     * @param filepath 图片文件的路径
     * @return 返回图片的宽高是拼接的字符串
     */
    public static String getImageSize(String filepath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设为true了
        // 这个参数的意义是仅仅解析边缘区域，从而可以得到图片的一些信息，比如大小，而不会整个解析图片，防止OOM
        options.inJustDecodeBounds = true;

        // 此时bitmap还是为空的
        Bitmap bitmap = BitmapFactory.decodeFile(filepath, options);

        int actualWidth = options.outWidth;
        int actualHeight = options.outHeight;
        return actualWidth + "." + actualHeight;
    }

    /**
     * 使用 RenderScript 模糊图片
     * PS:此方法只能在 SDK API 17 以上使用
     *
     * @param context 上下文对象
     * @param bitmap 需要模糊的bitmap
     * @param scale 模糊的比例因数
     * @param radius 模糊半径
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1) public static Bitmap rsBlurBitmp(Context context,
            Bitmap bitmap, int scale, float radius) {
        // 创建一个新的压缩过的 Bitmap
        Bitmap overlay = compressBitmapByMatrixToScale(bitmap, scale);

        RenderScript rs = null;
        try {
            rs = RenderScript.create(context);
            rs.setMessageHandler(new RenderScript.RSMessageHandler());
            Allocation input =
                    Allocation.createFromBitmap(rs, overlay, Allocation.MipmapControl.MIPMAP_NONE,
                            Allocation.USAGE_SCRIPT);
            Allocation output = Allocation.createTyped(rs, input.getType());

            ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

            blur.setInput(input);
            blur.setRadius(radius);
            blur.forEach(output);
            output.copyTo(overlay);
        } finally {
            if (rs != null) {
                rs.destroy();
            }
        }
        return overlay;
    }

    /**
     * Stack Blur v1.0 from
     * http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
     * <p>
     * Java Author: Mario Klingemann <mario at quasimondo.com>
     * http://incubator.quasimondo.com
     * created Feburary 29, 2004
     * Android port : Yahel Bouaziz <yahel at kayenko.com>
     * http://www.kayenko.com
     * ported april 5th, 2012
     * <p>
     * This is a compromise between Gaussian Blur and Box blur
     * It creates much better looking blurs than Box Blur, but is
     * 7x faster than my Gaussian Blur implementation.
     * <p>
     * I called it Stack Blur because this describes best how this
     * filter works internally: it creates a kind of moving stack
     * of colors whilst scanning through the image. Thereby it
     * just has to add one new block of color to the right side
     * of the stack and remove the leftmost color. The remaining
     * colors on the topmost layer of the stack are either added on
     * or reduced by one, depending on if they are on the right or
     * on the left side of the stack.
     * <p>
     * If you are using this algorithm in your code please add
     * the following line:
     * <p>
     * Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>
     * ------------------------------------------------------------------
     * java层进行模糊，使用堆栈计算模糊法
     *
     * @param sentBitmap 需要模糊的 Bitmap
     * @param scale 模糊前缩放比例，缩放越大模糊耗费资源越少，相对的模糊质量较低
     * @param radius 模糊半径
     * @param reuse 是否重用 Bitmap
     */
    public static Bitmap stackBlurBitmap(Bitmap sentBitmap, int scale, int radius, boolean reuse) {

        // 创建一个新的压缩过的 Bitmap
        Bitmap overlay = compressBitmapByMatrixToScale(sentBitmap, scale);

        Bitmap bitmap;
        if (reuse) {
            bitmap = overlay;
        } else {
            bitmap = overlay.copy(overlay.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }
}
