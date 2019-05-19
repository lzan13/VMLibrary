package com.vmloft.develop.library.tools;

import android.content.Context;

import com.vmloft.develop.library.tools.base.VMApp;

/**
 * Created by lzan13 on 2018/4/16.
 *
 * 工具初始化入口
 */
public class VMTools {

    static Context context;

    /**
     * 初始化工具类库
     */
    public static void init(Context context) {
        VMTools.context = context;
    }

    /**
     * 获取工具类库当前保存的上下文对象，如果没有进行初始化就从自定义的 VMApp 获取，如果项目也没有继承自 VMApp 则为空
     * 这个主要是为了方便工具类库中的其他接口直接使用上下文对象，不需要在调用相关方法时都传递一个 context 或者 mActivity
     */
    public static Context getContext() {
        if (context == null) {
            return VMApp.getContext();
        }
        return context;
    }
}
