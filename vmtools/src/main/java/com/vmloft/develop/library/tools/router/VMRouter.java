package com.vmloft.develop.library.tools.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.Settings;

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
     * @param intent  权限申请 Intent 包含参数
     */
    public static void goPermission(Context context, Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, VMPermissionActivity.class);
        overlay(context, intent);
    }

    /**
     * 打开 App 的详细设置页面
     */
    public static void goSettingDetail(Context context, int requestCode) {
        Uri packageURI = Uri.parse("package:" + context.getPackageName());
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        overlayResult(context, intent, requestCode);
    }

    /**
     * 打开选择器
     */
    public static void goPicker(Context context, Intent intent, int requestCode) {
        overlayResult(context, intent, requestCode);
    }

    /**
     * ------------------- 传递 Intent 参数进行跳转-------------------
     *
     * @param context 开始界面上下文
     * @param intent  目标 Intent
     */
    protected static void overlay(Context context, Intent intent) {
        context.startActivity(intent);
    }

    /**
     * ------------------- 传递 Intent 参数进行跳转-------------------
     *
     * @param context 开始界面上下文
     * @param intent
     */
    protected static void overlayResult(Context context, Intent intent) {
        overlayResult(context, intent, 0);
    }

    protected static void overlayResult(Context context, Intent intent, int requestCode) {
        if (isActivity(context)) {
            ((Activity) context).startActivityForResult(intent, requestCode);
        }
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
     * 普通的 finish 跳转
     *
     * @param context 上下文对象
     * @param target  目标
     */
    protected static void forward(Context context, Class<? extends Activity> target) {
        Intent intent = new Intent(context, target);
        context.startActivity(intent);
        if (isActivity(context)) {
            ((Activity) context).finish();
            context = null;
        }
    }

    /**
     * 带有序列化参数的 finish 跳转
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
     * 带有 flag 的 finish 跳转
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
     * 带有 flag 和序列化参数的 finish 跳转
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
     * ---------------------------- 界面跳转参数传递 ----------------------------
     * 获取序列化的参数
     */
    public static VMParams getParams(Activity activity) {
        Parcelable parcelable = activity.getIntent().getParcelableExtra(VMParams.VM_ROUTER_PARAMS);
        return (VMParams) parcelable;
    }

    /**
     * 添加可序列化的参数对象
     */
    private static void putParams(Intent intent, Parcelable parcelable) {
        if (intent == null) {
            return;
        }
        intent.putExtra(VMParams.VM_ROUTER_PARAMS, parcelable);
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
     * 判断当前上下文是不是 mActivity
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
