package com.vmloft.develop.library.tools;

import android.content.Context;

/**
 * Created by lzan13 on 2018/4/16.
 * 工具初始化入口
 */
public class VMTools {
    static Context context;

    public static void init(Context context) {
        VMTools.context = context;
    }

    public static Context getContext() {
        return context;
    }
}
