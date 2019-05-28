package com.vmloft.develop.library.tools.utils;

import android.app.Activity;
import android.graphics.Outline;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.ViewOutlineProvider;

/**
 * Created by lzan13 on 2018/4/17.
 * 主题工具类
 */
public class VMTheme {

    /**
     * 设置是否使用夜间模式，这里只是切换主题设置，具体的主题样式资源等还需要自己配置，
     * 参考项目的 values-night 配置
     *
     * @param isNight 是否设置为夜间模式
     */
    public static void setNightTheme(boolean isNight) {
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    /**
     * 设置状态栏为黑色图标和文字
     *
     * @param activity mActivity 对象
     * @param isDark   是否黑色
     */
    public static void setDarkStatusBar(Activity activity, boolean isDark) {
        if (isDark) {
            // 1、设置状态栏文字深色，同时保留之前的 flag
            int originFlag = activity.getWindow().getDecorView().getSystemUiVisibility();
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(originFlag | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            //2、清除状态栏文字深色，同时保留之前的flag
            int originFlag = activity.getWindow().getDecorView().getSystemUiVisibility();
            //使用异或清除SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(originFlag ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
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
