package com.vmloft.develop.library.tools.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.vmloft.develop.library.tools.VMTools;

import com.vmloft.develop.library.tools.utils.logger.VMLog;
import java.lang.reflect.Method;

/**
 * Created by lzan13 on 2015/4/15.
 * 尺寸转化工具类
 */
public class VMDimen {

    private static final String RES_STATUS_BAR_HEIGHT = "status_bar_height";
    private static final String RES_NAV_BAR_HEIGHT = "navigation_bar_height";

    private VMDimen() {
        throw new AssertionError();
    }

    /**
     * 获取屏幕宽高
     */
    public static int getScreenWidth() {
        return getScreenSize().x;
    }

    public static int getScreenHeight() {
        return getScreenSize().y;
    }

    /**
     * 获取屏幕大小
     */
    public static Point getScreenSize() {
        return getScreenSize(VMTools.getContext());
    }

    /**
     * 获取屏幕大小
     */
    public static Point getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point outSize = new Point();
        display.getSize(outSize);
        return outSize;
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight() {
        int height = 0;
        Resources res = VMTools.getContext().getResources();
        int resId = res.getIdentifier(RES_STATUS_BAR_HEIGHT, "dimen", "android");
        if (resId > 0) {
            height = res.getDimensionPixelSize(resId);
        }
        return height;
    }

    /**
     * 获取导航栏的高度（在 NavigationBar 存在的情况下）
     */
    public static int getNavigationBarHeight() {
        int height = 0;
        Resources res = VMTools.getContext().getResources();
        if (hasNavigationBar()) {
            String key = RES_NAV_BAR_HEIGHT;
            height = getInternalDimensionSize(res, key);
        }
        return height;
    }

    /**
     * 将控件尺寸的资源转换为像素尺寸
     *
     * @param resId 尺寸资源id
     */
    public static int getDimenPixel(int resId) {
        Resources res = VMTools.getContext().getResources();
        int result = res.getDimensionPixelSize(resId);
        return result;
    }

    /**
     * 将控件尺寸大小转为当前设备下的像素大小
     *
     * @param dp 控件尺寸大小
     */
    public static int dp2px(int dp) {
        Resources res = VMTools.getContext().getResources();
        float density = res.getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    /**
     * 将字体尺寸大小转为当前设备下的像素尺寸大小
     *
     * @param sp 字体的尺寸大小
     */
    public static float sp2px(int sp) {
        Resources res = VMTools.getContext().getResources();
        float density = res.getDisplayMetrics().scaledDensity;
        return (int) (sp * density + 0.5f);
    }

    /**
     * 获取文字的宽度
     *
     * @param paint 绘制文字的画笔
     * @param str   需要计算宽度的字符串
     * @return 返回字符串宽度
     */
    public static float getTextWidth(Paint paint, String str) {
        float textWidth = 0;
        if (str != null && str.length() > 0) {
            // 记录字符串中每个字符宽度的数组
            float[] widths = new float[str.length()];
            // 获取字符串中每个字符的宽度到数组
            paint.getTextWidths(str, widths);
            for (int i = 0; i < str.length(); i++) {
                textWidth += (float) Math.ceil(widths[i]);
            }
        }
        return textWidth;
    }

    /**
     * 计算文字的高度
     *
     * @param paint 绘制文字的画笔
     * @return 返回字符串高度
     */
    public static float getTextHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (float) Math.ceil(fm.descent - fm.ascent);
    }

    /**
     * 获取系统内部尺寸
     *
     * @param res 资源管理
     * @param key 内部资源的 key
     * @return
     */
    private static int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 判断是否有虚拟导航栏NavigationBar，
     */
    public static boolean hasNavigationBar() {
        boolean hasNavigationBar = false;
        Resources rs = VMTools.getContext().getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            VMLog.e(e.getMessage());
        }
        return hasNavigationBar;
    }
}
