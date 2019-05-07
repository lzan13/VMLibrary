package com.vmloft.develop.library.example.demo.image;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;

/**
 * Create by lzan13 on 2019/05/05
 *
 * 图片比对
 */
public class ImageDsicern {
    /**
     * 比较两个图片相似度
     *
     * @param oneBitmap 第一个 bitmap
     * @param twoBitmap 第二个 bitmap
     * @return
     */
    public static Bitmap compareBitmap(Bitmap oneBitmap, Bitmap twoBitmap, Point startPoint, Point targetPoint) {
        if (oneBitmap == null || twoBitmap == null) {
            return null;
        }
        int status = 0;
        int color = 0xff0000;
        int width = oneBitmap.getWidth();
        int height = oneBitmap.getHeight();

        Bitmap copyBitmap = oneBitmap.copy(Bitmap.Config.ARGB_8888, true);
        checkExit:
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height / 2 + 50; j++) {
                boolean isAlike = checkPixel(i, j, oneBitmap, twoBitmap);
                // 检测到不同，开始检查指定大小方框内是否相同
                if (!isAlike) {
//                    changeBitmapPixel(i, j, copyBitmap, color);
                    isAlike = checkPixelRect(i, j, oneBitmap, twoBitmap, copyBitmap);
                    if (!isAlike) {
                        if (status == 0) {
                            startPoint.x = i + 20;
                            startPoint.y = j + 50;
                            status = 1;
                            break;
                        } else if (status == 2) {
                            targetPoint.x = i;
                            targetPoint.y = j + 50;
                            break checkExit;
                        }
                    }
                }
            }
            // 这里是当找到左边的滑块部分后，跳过滑块，去查找缺失部分
            if (status == 1) {
                i += 170;
                status = 2;
            }
        }
        return copyBitmap;

    }

    /**
     * 检查像素点
     *
     * @param x         像素点x坐标
     * @param y         像素点y坐标
     * @param oneBitmap 第一个 bitmap 图片数据
     * @param twoBitmap 第二个 bitmap 图片数据
     * @return
     */
    private static boolean checkPixel(int x, int y, Bitmap oneBitmap, Bitmap twoBitmap) {
        int onePixel = oneBitmap.getPixel(x, y);
        int twoPixel = twoBitmap.getPixel(x, y);
        if (onePixel == twoPixel) {
            return true;
        }
        return false;
    }

    /**
     * 检查 10 * 10 的空间是否都不相同
     *
     * @param x         像素点x坐标
     * @param y         像素点y坐标
     * @param oneBitmap 第一个 bitmap 图片数据
     * @param twoBitmap 第二个 bitmap 图片数据
     * @return
     */
    private static boolean checkPixelRect(int x, int y, Bitmap oneBitmap, Bitmap twoBitmap, Bitmap copyBitmap) {
        boolean isAlike = false;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                isAlike = checkPixel(x + i, y + 50 + j, oneBitmap, twoBitmap);
                if (!isAlike) {
                    int color = 0x0000ff;
                    changeBitmapPixel(x + 20 + i, y + 50 + j, copyBitmap, color);
                } else {
                    return isAlike;
                }
            }
        }
        return isAlike;
    }

    /**
     * 修改 Bitmap 像素点颜色
     *
     * @param x      像素点x坐标
     * @param y      像素点y坐标
     * @param bitmap bitmap 图片数据
     * @param color  修改的颜色
     */
    private static void changeBitmapPixel(int x, int y, Bitmap bitmap, int color) {
        int pixel = bitmap.getPixel(x, y);
        final int alpha = (pixel >> 24) & 0xff;
        if (alpha > 0 && alpha != 0xff) {
            color = (alpha << 24) | (color & 0xffffff);
        }
        bitmap.setPixel(x, y, (alpha << 24) | color);
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

    /**
     * 修改 Bitmap 颜色
     */
    public static Bitmap changeBitmapColor(Bitmap bitmap) {
        int bitmap_h;
        int bitmap_w;
        int mArrayColorLengh;
        int[] mArrayColor;
        int count = 0;
        mArrayColorLengh = bitmap.getWidth() * bitmap.getHeight();
        mArrayColor = new int[mArrayColorLengh];
        bitmap_w = bitmap.getWidth();
        bitmap_h = bitmap.getHeight();
        int newcolor = -1;
        for (int i = 0; i < bitmap.getHeight(); i++) {
            for (int j = 0; j < bitmap.getWidth(); j++) {
                //获得Bitmap 图片中每一个点的color颜色值
                int pixel = bitmap.getPixel(j, i);
                //将颜色值存在一个数组中 方便后面修改
                // mArrayColor[count] = color;
                int r = Color.red(pixel);
                int g = Color.green(pixel);
                int b = Color.blue(pixel);
                int a = Color.alpha(pixel);
                if ((90 < r && r <= 200) && (90 < g && g <= 200) && (90 < b && b <= 200)) {//大概得把非道路（路旁变透明）
                    a = 0;
                } else if (r == 255 && g == 255 && b == 33) {//把黄色的箭头白色 因为黄色箭头rgb大部分是255 255 33(值可以用画图工具取值) 组合
                    // 但是还有小部分有别的值组成（箭头所不能变成全白有黄色斑点）
                    r = 255;
                    g = 255;
                    b = 255;
                }
                pixel = Color.argb(a, r, g, b);
                mArrayColor[count] = pixel;
                count++;
            }
        }
        Bitmap result = Bitmap.createBitmap(mArrayColor, bitmap_w, bitmap_h, Bitmap.Config.ARGB_4444);
        return result;
    }
}
