package com.vmloft.develop.library.tools;

import android.content.Context;

import com.vmloft.develop.library.tools.base.VMApp;
import com.vmloft.develop.library.tools.picker.IPictureLoader;
import com.vmloft.develop.library.tools.picker.VMPicker;
import com.vmloft.develop.library.tools.utils.VMLog;

/**
 * Created by lzan13 on 2018/4/16.
 *
 * 工具初始化入口
 */
public class VMTools {

    static Context mContext;

    /**
     * 初始化工具类库
     */
    public static void init(Context context) {
        mContext = context;
    }

    /**
     * 初始化工具类库
     *
     * @param context  上下文对象
     * @param logLevel 日志级别
     */
    public static void init(Context context, int logLevel) {
        mContext = context;
        VMLog.setDebug(logLevel);
    }

    /**
     * 获取工具类库当前保存的上下文对象:
     * 如果没有进行初始化就从自定义的 VMApp 获取
     * 如果项目也没有继承自 VMApp 则为空
     * 这个主要是为了方便工具类库中的其他接口直接使用上下文对象，不需要在调用相关方法时都传递一个 context 或者 mActivity
     */
    public static Context getContext() {
        if (mContext == null) {
            mContext = VMApp.getContext();
        }
        if (mContext == null) {
            throw new NullPointerException("请初始化 VMTools 或者继承自 VMApp 类");
        }
        return mContext;
    }
}
