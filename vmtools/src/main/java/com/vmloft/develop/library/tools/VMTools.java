package com.vmloft.develop.library.tools;

/**
 * Created by lzan13 on 2017/7/7.
 * 自己封装的工具入口类
 */
public class VMTools {

    private static VMTools instance;

    private VMTools() {
    }

    public static VMTools getInstance() {
        if (instance == null) {
            instance = new VMTools();
        }
        return instance;
    }

    /**
     * 加载原生库文件
     */
    private void loadLibrary() {
        System.loadLibrary("vmtools");
    }

    public void init() {
        loadLibrary();
    }
}
