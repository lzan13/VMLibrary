package com.vmloft.develop.library.example.demo.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.vmloft.develop.library.tools.utils.VMStr;

/**
 * Create by lzan13 on 2019/05/05
 *
 * 图片比对
 */
public class ImageDsicern {
    /**
     * 比较两个图片相似度
     *
     * @param onePath 第一个图片路径
     * @param twoPath 第二个图片路径
     * @return
     */
    public static String similarityImage(String onePath, String twoPath) {
        if (VMStr.isEmpty(onePath) || VMStr.isEmpty(twoPath)) {
            return "";
        }
        Bitmap oneBitmap = BitmapFactory.decodeFile(onePath);
        Bitmap twoBitmap = BitmapFactory.decodeFile(twoPath);
        return similarityImage(oneBitmap, twoBitmap);
    }

    /**
     * 比较两个图片相似度
     *
     * @param oneBitmap 第一个 bitmap
     * @param twoBitmap 第二个 bitmap
     * @return
     */
    public static String similarityImage(Bitmap oneBitmap, Bitmap twoBitmap) {
        if (oneBitmap == null || twoBitmap == null) {
            return "";
        }
        int[] result = {0, 0};
        // 保存图片所有像素个数的数组，图片宽×高
        int[] srcPixels = new int[oneBitmap.getWidth() * oneBitmap.getHeight()];
        int[] destPixels = new int[twoBitmap.getWidth() * twoBitmap.getHeight()];
        // 获取每个像素的RGB值
        oneBitmap.getPixels(srcPixels, 0, oneBitmap.getWidth(), 0, 0, oneBitmap.getWidth(), oneBitmap.getHeight());
        twoBitmap.getPixels(destPixels, 0, twoBitmap.getWidth(), 0, 0, twoBitmap.getWidth(), twoBitmap.getHeight());
        // 取像素少的图片作为循环条件。避免报错
        if (srcPixels.length >= destPixels.length) {
            // 对每一个像素的RGB值进行比较
            for (int i = 0; i < destPixels.length; i++) {
                comparePixels(result, i, srcPixels, destPixels);
            }
        } else {
            for (int i = 0; i < srcPixels.length; i++) {
                comparePixels(result, i, srcPixels, destPixels);
            }
        }
        return getPercent(result[0], result[1]);
    }

    /**
     * 比较两个像素点
     */
    private static void comparePixels(int[] relsult, int index, int[] onePixels, int[] twoPixels) {
        int pixels1 = onePixels[index];
        int pixels2 = twoPixels[index];
        if (pixels1 == pixels2) {
            relsult[0] += 1;
        } else {
            relsult[1] += 1;
        }
    }

    /**
     * 百分比的计算
     *
     * @param y(母子)
     * @param z（分子）
     * @return 百分比（保留小数点后两位）
     * @author xupp
     */
    public static String getPercent(int y, int z) {
        double baiy = y * 1.0;
        double baiz = z * 1.0;
        double fen = baiy / baiz;
        return String.format("%.2f", fen);
    }
}
