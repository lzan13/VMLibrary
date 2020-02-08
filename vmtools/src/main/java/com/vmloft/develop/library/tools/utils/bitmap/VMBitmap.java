package com.vmloft.develop.library.tools.utils.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Base64;
import android.view.View;

import com.vmloft.develop.library.tools.utils.VMFile;
import com.vmloft.develop.library.tools.utils.VMLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by lzan13 on 2016/1/11.
 * 图片处理类，压缩，转换
 */
public class VMBitmap {

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
     * @param path      图片文件路径
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
    public Bitmap loadCacheBitmapFromView(View view) {
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
     * @param path      要压缩的图片路径
     * @param dimension 定义压缩后的最大尺寸
     * @return 返回经过压缩处理的图片
     */
    public static Bitmap loadBitmapByFile(String path, int dimension) {
        return compressByDimension(path, dimension);
    }

    /**
     * 获取最佳缩放比例
     *
     * @param actualWidth  Bitmap的实际宽度
     * @param actualHeight Bitmap的实际高度
     * @param dimension    定义压缩后最大尺寸
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
     * 获取图片的旋转角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照指定的角度进行旋转
     *
     * @param bitmap 需要旋转的图片
     * @param degree 指定的旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degree) {
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return newBitmap;
    }
    /**
     * -------------------- 图片压缩处理 --------------------
     */

    /**
     * 通过矩阵压缩 Bitmap 到指定尺寸
     *
     * @param bitmap    需要压缩的 Bitmap
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
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 通过矩阵按比例压缩 Bitmap
     *
     * @param bitmap 需要压缩的 Bitmap
     * @param scale  图片压缩比率(0-1)
     * @return 返回压缩后的 Bitmap
     */
    public static Bitmap compressBitmapByScale(Bitmap bitmap, int scale) {
        // 使用矩阵进行压缩图片
        Matrix matrix = new Matrix();
        float s = (float) 1 / scale;
        matrix.postScale(s, s);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 质量压缩到指定大小
     *
     * @param bitmap 原图
     * @param size   指定大小
     */
    public static Bitmap compressByQuality(Bitmap bitmap, int size) {
        maxSize = size;
        return compressByQuality(bitmap);
    }

    /**
     * 质量压缩，默认压缩到 500K 以下
     *
     * @param bitmap 原图
     * @return 压缩后的图片
     */
    public static Bitmap compressByQuality(Bitmap bitmap) {
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
     * 临时压缩图片到指定尺寸
     *
     * @param path      原始路径
     * @param dimension 最大尺寸
     */
    public static String compressTempImageByDimension(String path, int dimension) {
        maxDimension = dimension;
        return compressTempImage(path);
    }

    /**
     * 临时压缩图片
     *
     * @param path 图片路径
     * @return 压缩后的图片临时路径
     */
    public static String compressTempImage(String path) {
        VMLog.d("compressTempImage start");
        Bitmap bitmap = compressByQuality(compressByDimension(path));
        VMLog.d("compressTempImage end");
        //得到文件名
        String tempName = generateTempName(path);
        // 临时存放路径
        String tempPath = VMFile.getCacheFromSDCard() + "temp";
        VMFile.createDirectory(tempPath);
        saveBitmapToSDCard(bitmap, tempPath + "/" + tempName);
        return tempPath + "/" + tempName;
    }

    /**
     * 等比例压缩到指定尺寸大小
     *
     * @param path      图片路径
     * @param dimension 最大尺寸
     */
    public static Bitmap compressByDimension(String path, int dimension) {
        maxDimension = dimension;
        return compressByDimension(path);
    }

    /**
     * 等比例压缩到指定尺寸 默认到 1920 以下
     *
     * @param path 图片路径
     */
    public static Bitmap compressByDimension(String path) {
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
     * 临时压缩图片到指定大小
     *
     * @param path 原始路径
     * @param size 最大大小
     */
    public static String compressTempImageBySize(String path, int size) {
        maxSize = size;
        return compressTempImage(path);
    }

    /**
     * 临时压缩图片到指定尺寸和大小
     *
     * @param path      原始路径
     * @param dimension 最大尺寸
     * @param size      最大大小
     */
    public static String compressTempImage(String path, int dimension, int size) {
        maxDimension = dimension;
        maxSize = size;
        return compressTempImage(path);
    }

    /**
     * 生成临时文件名
     *
     * @param path 文件原始路径
     */
    private static String generateTempName(String path) {
        return System.currentTimeMillis() + getExtensionName(path);
    }

    /**
     * 获取文件扩展名
     *
     * @param path 文件原始路径
     */
    public static String getExtensionName(String path) {
        if ((path != null) && (path.length() > 0)) {
            int dot = path.lastIndexOf('.');
            if ((dot > -1) && (dot < (path.length() - 1))) {
                return path.substring(dot);
            }
        }
        return path;
    }

    /**
     * 保存 Bitmap 到 SDCard
     *
     * @param bitmap 需要保存的图片数据
     * @param path   保存路径
     */
    public static boolean saveBitmapToSDCard(Bitmap bitmap, String path) {
        //VMLog.d("saveBitmapToSDCard start");
        //OutputStream outputStream = new FileOutputStream(path);
        //if (outputStream != null) {
        //    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
        //    outputStream.close();
        //}
        //VMLog.d("saveBitmapToSDCard end");
        return saveBitmapToSDCard(bitmap, Bitmap.CompressFormat.JPEG, path);
    }

    /**
     * 保存 Bitmap 到 SDCard
     *
     * @param bitmap 需要保存的图片数据
     * @param format 格式类型
     * @param path   保存路径
     */
    public static boolean saveBitmapToSDCard(Bitmap bitmap, Bitmap.CompressFormat format, String path) {
        VMLog.d("saveBitmapToSDCard -start-");
        boolean result;
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(path);
            if (outputStream != null) {
                bitmap.compress(format, 90, outputStream);
            }
            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result = false;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        VMLog.d("saveBitmapToSDCard -end-");
        return result;
    }
}