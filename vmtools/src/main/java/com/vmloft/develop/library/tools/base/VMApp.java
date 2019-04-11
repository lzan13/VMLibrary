package com.vmloft.develop.library.tools.base;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzan13 on 2017/4/13.
 * 这里主要是定义一个全局的上下文对象，以后所有引用此库的项目直接使用
 */
public class VMApp extends Application {

    protected static Context context;
    private static List<VMActivity> activityList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }

    public static void putActivity(VMActivity activity) {
        if (!activityList.contains(activity)) {
            activityList.add(0, activity);
        }
    }

    public static void removeActivity(VMActivity activity) {
        activityList.remove(activity);
    }

    public static VMActivity getTopActivity() {
        if (activityList.size() > 0) {
            return activityList.get(0);
        }
        return null;
    }
}
