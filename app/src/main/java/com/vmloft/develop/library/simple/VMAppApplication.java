package com.vmloft.develop.library.simple;

import com.vmloft.develop.library.tools.VMApplication;

/**
 * Created by lzan13 on 2017/7/7.
 * 程序入口
 */
public class VMAppApplication extends VMApplication {
    @Override public void onCreate() {
        super.onCreate();

        loadLibrary();
    }

    /**
     * 加载原生库文件
     */
    private void loadLibrary() {
        System.loadLibrary("vmnativetools");
    }
}
