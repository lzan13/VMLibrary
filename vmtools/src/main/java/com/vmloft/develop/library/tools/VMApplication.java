package com.vmloft.develop.library.tools;

import android.app.Application;
import android.content.Context;

/**
 * Created by lzan13 on 2017/4/13.
 * 这里主要是定义一个全局的上下文对象，以后所有引用此库的项目直接使用
 */
public class VMApplication extends Application {

    protected static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
