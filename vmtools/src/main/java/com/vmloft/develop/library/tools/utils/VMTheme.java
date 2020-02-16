package com.vmloft.develop.library.tools.utils;

import android.app.Activity;
import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatDelegate;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by lzan13 on 2018/4/17.
 * 主题工具类
 */
public class VMTheme {

    /**
     * 设置是否使用夜间模式，这里只是切换主题设置，具体的主题样式资源等还需要自己配置，
     * 参考项目的 values-night 配置
     *
     * @param night 是否设置为夜间模式
     */
    public static void setNightTheme(boolean night) {
        if (night) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    /**
     * 状态栏深色模式，设置状态栏黑色文字、图标
     *
     * @return 1-MIUI 2=-Flyme 3-Android 6.0 原生
     */
    public static int setDarkStatusBar(Activity activity, boolean dark) {
        int result = 0;
        if (statusBarDarkModeFromMIUI(activity, dark)) {
            result = 1;
        } else if (statusBarDarkModeFromFlyme(activity.getWindow(), dark)) {
            result = 2;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (dark) {
                activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
            result = 3;
        }
        return result;
    }

    /**
     * 针对魅族系统设置状态栏图标为深色和魅族特定的文字风格
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏文字及图标颜色设置为深色
     * @return 成功执行返回 true
     */
    public static boolean statusBarDarkModeFromFlyme(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field mzFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                mzFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = mzFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                mzFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 针对 MIUI 系统设置深色状态栏
     *
     * @param dark 是否把状态栏文字及图标颜色设置为深色
     * @return 成功执行返回 true
     */
    public static boolean statusBarDarkModeFromMIUI(Activity activity, boolean dark) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        activity.getWindow()
                            .getDecorView()
                            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 改变 View 背景阴影透明度
     */
    public static void changeShadow(View view) {
        changeShadow(view, 0.36f);
    }

    /**
     * 改变 View 背景阴影透明度
     */
    public static void changeShadow(View view, final float alpha) {
        view.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                if (view.getBackground() != null) {
                    view.getBackground().getOutline(outline);
                    outline.setAlpha(alpha);
                }
            }
        });
    }
}
