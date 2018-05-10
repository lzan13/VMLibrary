package com.vmloft.develop.library.tools.utils;

import android.support.v7.app.AppCompatDelegate;

/**
 * Created by lzan13 on 2018/4/17.
 * 主题工具类
 */
public class VMTheme {


    /**
     * 设置是否使用夜间模式
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
}
