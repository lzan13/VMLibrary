package com.vmloft.develop.library.tools;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by lzan13 on 2017/4/13.
 * 这里继承自MultiDex的Application，解决项目方法数超过65536问题
 */
public class VMApplication extends MultiDexApplication {

    // 全局的上下文对象
    protected static Context context;

    // 内存溢出检测观察者
    protected static RefWatcher watcher;

    @Override public void onCreate() {
        super.onCreate();

        context = this;
        // 初始化当前工具类
        VMTools.getInstance().init();
        // 初始化 LeakCanary
        watcher = LeakCanary.install(this);
    }

    public static Context getContext() {
        return context;
    }

    /**
     * 获取内存泄露观察者
     *
     * @return 返回内存泄露观察者对象
     */
    public static RefWatcher getRefWatcher() {
        return watcher;
    }
}
