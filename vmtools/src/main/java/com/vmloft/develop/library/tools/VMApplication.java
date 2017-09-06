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
        // 初始化 LeakCanary
        setupLeakCanary();
        context = this;
    }

    public static Context getContext() {
        return context;
    }

    /**
     * 初始化内训泄露
     *
     * @return 返回内存泄露观察者对象
     */
    protected RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }
}
