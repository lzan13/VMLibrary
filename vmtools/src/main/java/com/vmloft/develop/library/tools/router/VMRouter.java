package com.vmloft.develop.library.tools.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.vmloft.develop.library.tools.permission.VMPermissionActivity;

/**
 * Created by lzan13 on 2018/4/24.
 *
 * 项目跳转导航路由器
 */
public class VMRouter {

    /**
     * 回到手机桌面
     */
    public static void goLauncher(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(intent);
    }

    /**
     * 唤起权限申请界面
     *
     * @param context 上下文对象
     * @param params  可序列化参数
     */
    public static void goPermission(Context context, VMParams params) {
        overlay(context, VMPermissionActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK, params);
    }

    /**
     * ------------------- 正常跳转，直接跳到下一个界面，当前界面处于 stop 状态 -------------------
     *
     * 最普通的跳转
     *
     * @param context 开始界面上下文
     * @param target  目标界面
     */
    protected static void overlay(Context context, Class<? extends Activity> target) {
        Intent intent = new Intent(context, target);
        context.startActivity(intent);
        context = null;
    }

    /**
     * 带有可序列化参数跳转
     *
     * @param context 开始界面上下文
     * @param target  目标界面
     */
    protected static void overlay(Context context, Class<? extends Activity> target, Parcelable parcelable) {
        Intent intent = new Intent(context, target);
        putParams(intent, parcelable);
        context.startActivity(intent);
        context = null;
    }

    /**
     * 带有 flags
     *
     * @param context 开始界面上下文
     * @param target  目标界面
     * @param flags   条件
     */
    protected static void overlay(Context context, Class<? extends Activity> target, int flags) {
        Intent intent = new Intent(context, target);
        intent.setFlags(flags);
        context.startActivity(intent);
        context = null;
    }

    /**
     * 带有可序列化参数，以及 flags
     *
     * @param context 开始界面上下文
     * @param target  目标界面
     */
    protected static void overlay(Context context, Class<? extends Activity> target, int flags, Parcelable parcelable) {
        Intent intent = new Intent(context, target);
        putParams(intent, parcelable);
        context.startActivity(intent);
        context = null;
    }

    /**
     * ---------------------------- 向前跳转，跳转结束会 finish 当前界面 ----------------------------
     *
     * 最普通的跳转
     *
     * @param context     上下文对象
     * @param targetClass 目标
     */
    protected static void forward(Context context, Class<? extends Activity> targetClass) {
        Intent intent = new Intent(context, targetClass);
        context.startActivity(intent);
        if (isActivity(context)) {
            ((Activity) context).finish();
            context = null;
        }
    }

    /**
     * 带有序列化参数的跳转
     *
     * @param context 上下文对象
     * @param target  目标
     */
    protected static void forward(Context context, Class<? extends Activity> target, Parcelable parcelable) {
        Intent intent = new Intent(context, target);
        putParams(intent, parcelable);
        context.startActivity(intent);
        if (isActivity(context)) {
            ((Activity) context).finish();
            context = null;
        }
    }

    /**
     * 带有 flag 的跳转
     *
     * @param context 上下文对象
     * @param target  目标
     * @param flags   条件
     */
    protected static void forward(Context context, Class<? extends Activity> target, int flags) {
        Intent intent = new Intent(context, target);
        setFlags(intent, flags);
        context.startActivity(intent);
        if (isActivity(context)) {
            ((Activity) context).finish();
            context = null;
        }
    }

    /**
     * 带有 flag 和序列化参数的跳转
     *
     * @param context 上下文对象
     * @param target  目标
     * @param flags   条件
     */
    protected static void forward(Context context, Class<? extends Activity> target, int flags, Parcelable parcelable) {
        Intent intent = new Intent(context, target);
        setFlags(intent, flags);
        putParams(intent, parcelable);
        context.startActivity(intent);
        if (isActivity(context)) {
            ((Activity) context).finish();
            context = null;
        }
    }

    /**
     * 获取序列化的参数
     */
    public static VMParams getParams(Activity activity) {
        Parcelable parcelable = activity.getIntent().getParcelableExtra(VMParams.ROUTER_EXT);
        return (VMParams) parcelable;
    }

    /**
     * 添加可序列化的参数对象
     */
    private static void putParams(Intent intent, Parcelable parcelable) {
        if (intent == null) {
            return;
        }
        intent.putExtra(VMParams.ROUTER_EXT, parcelable);
    }

    /**
     * 设置标记
     */
    private static void setFlags(Intent intent, int flags) {
        if (flags < 0) {
            return;
        }
        intent.setFlags(flags);
    }

    /**
     * 判断当前上下文是不是 activity
     *
     * @param context 上下文
     */
    private static boolean isActivity(Context context) {
        if (context instanceof Activity) {
            return true;
        }
        return false;
    }
}
