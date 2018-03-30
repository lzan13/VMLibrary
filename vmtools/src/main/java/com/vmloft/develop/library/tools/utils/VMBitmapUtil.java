package com.vmloft.develop.library.tools.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by lzan13 on 2016/1/11.
 * 图片处理类，压缩，转换
 */
public class VMBitmapUtil {

    // 图片最大大小 默认500Kb
    private static int maxSize = 500;
    private static int maxDimension = 1920;

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
     * 将 base64 的字符串 转为 Bitmap
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
        return compressBitmapByMatrix(bitmap, dimension);
    }

    /**
     * 获取一个 View 的缓存视图
     *
     * @param view 需要保存图像的 View 控件
     * @return 返回保存的 Bitmap 图像
     */
    private Bitmap loadCacheBitmapFromView(View view) {
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
     * 通过文件加载图片，这里也可以加载大图，保证不会出现 OOM，
     *
     * @param path 要压缩的图片路径
     * @param dimension 定义压缩后的最大尺寸
     * @return 返回经过压缩处理的图片
     */
    public static Bitmap loadBitmapByFile(String path, int dimension) {
        return compressByDimension(path, dimension);
    }

    /**
     * 获取最佳缩放比例
     *
     * @param actualWidth Bitmap的实际宽度
     * @param actualHeight Bitmap的实际高度
     * @param dimension 定义压缩后最大尺寸
     * @return 返回最佳缩放比例
     */
    public static int getZoomScale(int actualWidth, int actualHeight, int dimension) {
        int scale = 1;
        if (actualWidth > actualHeight && actualWidth > dimension) {
            // 如果宽度大的话根据宽度固定大小缩放
            scale = (int) (actualWidth / dimension);
        } else if (actualWidth < actualHeight && actualHeight > dimension) {
            // 如果高度高的话根据高度固定大小缩放
            scale = (int) (actualHeight / dimension);
        }
        if (scale <= 0) {
            scale = 1;
        }
        return scale;
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
     * -------------------- 图片压缩处理 --------------------
     */

    /**
     * 通过矩阵压缩 Bitmap 到指定尺寸
     *
     * @param bitmap 需要压缩的 Bitmap
     * @param dimension 图片压缩后最大宽高
     * @return 返回压缩后的 Bitmap
     */
    public static Bitmap compressBitmapByMatrix(Bitmap bitmap, int dimension) {
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
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
                                   true);
    }

    /**
     * 通过矩阵按比例压缩 Bitmap
     *
     * @param bitmap 需要压缩的 Bitmap
     * @param scale 图片压缩比率(0-1)
     * @return 返回压缩后的 Bitmap
     */
    public static Bitmap compressBitmapByScale(Bitmap bitmap, int scale) {
        // 使用矩阵进行压缩图片
        Matrix matrix = new Matrix();
        float s = (float) 1 / scale;
        matrix.postScale(s, s);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
                                   true);
    }

    /**
     * 质量压缩
     *
     * @param bitmap 原图
     * @return 压缩后的图片
     */
    private static Bitmap compressByQuality(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 70;
        //质量压缩方法，100表示不压缩，把压缩后的数据存放到baos中
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        //循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > maxSize) {
            // 重置baos即清空baos
            baos.reset();
            // 这里压缩options%，把压缩后的数据存放到baos中
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            // 每次都减少20
            options -= 20;
        }
        // 把压缩后的数据存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());

        // 把ByteArrayInputStream数据生成图片
        return BitmapFactory.decodeStream(isBm, null, null);
    }

    /**
     * 质量压缩到指定大小
     *
     * @param bitmap 原图
     * @param size 指定大小
     * @return
     */
    private static Bitmap compressByQuality(Bitmap bitmap, int size) {
        maxSize = size;
        return compressByQuality(bitmap);
    }

    /**
     * 等比例压缩到指定尺寸
     *
     * @param path 图片路径
     * @return
     */
    private static Bitmap compressByDimension(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        options.inJustDecodeBounds = true;
        // 此时返回 bitmap 为空，并不会真正的加载图片
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        int w = options.outWidth;
        int h = options.outHeight;
        // 缩放比，由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = getZoomScale(w, h, maxDimension);
        // 设置缩放比例
        options.inSampleSize = be;
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(path, options);
        // 等比例压缩后再进行质量压缩
        return bitmap;
    }

    /**
     * 等比例压缩到指定尺寸大小
     *
     * @param path 图片路径
     * @param dimension 最大尺寸
     * @return
     */
    private static Bitmap compressByDimension(String path, int dimension) {
        maxDimension = dimension;
        return compressByDimension(path);
    }

    /**
     * 临时压缩图片
     *
     * @param path 图片路径
     * @return 压缩后的图片临时路径
     */
    public static String compressTempImage(String path) throws IOException {
        VMLog.d("compressTempImage start");
        Bitmap bitmap = compressByQuality(compressByDimension(path));
        VMLog.d("compressTempImage end");
        //得到文件名
        String tempName = generateTempName(path);
        // 临时存放路径
        String tempPath = VMFileUtil.getCacheFromSDCard() + "temp";
        VMFileUtil.createDirectory(tempPath);
        saveBitmapToSDCard(bitmap, tempPath + "/" + tempName);
        return tempPath + "/" + tempName;
    }

    /**
     * 临时压缩图片到指定尺寸
     *
     * @param path 原始路径
     * @param dimension 最大尺寸
     * @return
     * @throws IOException
     */
    public static String compressTempImageByDimension(String path,
            int dimension) throws IOException {
        maxDimension = dimension;
        return compressTempImage(path);
    }

    /**
     * 临时压缩图片到指定大小
     *
     * @param path 原始路径
     * @param size 最大大小
     * @return
     * @throws IOException
     */
    public static String compressTempImageBySize(String path, int size) throws IOException {
        maxSize = size;
        return compressTempImage(path);
    }

    /**
     * 临时压缩图片到指定尺寸和大小
     *
     * @param path 原始路径
     * @param dimension 最大尺寸
     * @param size 最大大小
     * @return
     * @throws IOException
     */
    public static String compressTempImage(String path, int dimension,
            int size) throws IOException {
        maxDimension = dimension;
        maxSize = size;
        return compressTempImage(path);
    }

    /**
     * 生成临时文件名
     *
     * @param path 文件原始路径
     * @return
     */
    private static String generateTempName(String path) {
        return System.currentTimeMillis() + getExtensionName(path);
    }

    /**
     * 获取文件扩展名
     *
     * @param path 文件原始路径
     */
    private static String getExtensionName(String path) {
        if ((path != null) && (path.length() > 0)) {
            int dot = path.lastIndexOf('.');
            if ((dot > -1) && (dot < (path.length() - 1))) {
                return path.substring(dot, path.length());
            }
        }
        return path;
    }

    /**
     * 保存Bitmap到SD卡
     *
     * @param bitmap 需要保存的图片数据
     * @param path 保存路径
     */
    public static void saveBitmapToSDCard(Bitmap bitmap, String path) throws IOException {
        VMLog.d("saveBitmapToSDCard start");
        OutputStream outputStream = null;
        outputStream = new FileOutputStream(path);
        if (outputStream != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
            outputStream.close();
        }
        VMLog.d("saveBitmapToSDCard end");
    }

}