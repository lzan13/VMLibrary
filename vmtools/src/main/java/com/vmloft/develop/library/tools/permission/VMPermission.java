package com.vmloft.develop.library.tools.permission;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;

import com.vmloft.develop.library.tools.base.VMConstant;
import com.vmloft.develop.library.tools.router.VMParams;
import com.vmloft.develop.library.tools.router.VMRouter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Create by lzan13 on 2019/04/25
 *
 * 处理权限请求操作类
 */
public class VMPermission {
    // 上下文对象
    private static Context mContext;
    // 授权处理回调
    private VMPermissionCallback mCallback;

    private String mTitle;
    private String mMessage;
    private List<VMPermissionItem> mPermissions;

    /**
     * 私有构造方法
     */
    private VMPermission() {
    }

    /**
     * 内部类实现单例
     */
    private static class VMPermissionHolder {
        public static final VMPermission INSTANCE = new VMPermission();
    }

    /**
     * 获取单例类的实例
     */
    public static final VMPermission getInstance(Context context) {
        mContext = context;
        return VMPermissionHolder.INSTANCE;
    }

    /**
     * 获取回调
     */
    public VMPermissionCallback getPermissionCallback() {
        return mCallback;
    }

    /**
     * 设置授权弹窗标题
     *
     * @param title 授权弹窗标题
     */
    public VMPermission setTitle(String title) {
        mTitle = title;
        return this;
    }

    /**
     * 设置授权弹窗描述内容
     *
     * @param message 授权弹窗描述内容
     */
    public VMPermission setMessage(String message) {
        mMessage = message;
        return this;
    }

    /**
     * 设置授权弹窗权限列表
     *
     * @param permissions 权限集合
     */
    public VMPermission setPermissions(List<VMPermissionItem> permissions) {
        mPermissions = permissions;
        return this;
    }

    /**
     * 内部调用检查权限方法
     *
     * @param permission 需要检查的权限
     * @return 返回是否授权
     */
    private boolean checkPermission(String permission) {
        int checkPermission = ContextCompat.checkSelfPermission(mContext, permission);
        if (checkPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    /**
     * 检查权限
     *
     * @param callback 授权回调接口
     */
    public void checkPermission(VMPermissionCallback callback) {
        if (mPermissions == null || mPermissions.size() == 0) {
            if (callback != null) {
                callback.onFinish();
            }
            return;
        }
        /**
         * 运行在 6.0 以下设备上，直接返回授权完成
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (callback != null) {
                callback.onFinish();
            }
            return;
        }

        /**
         * 过滤已允许的权限
         */
        ListIterator<VMPermissionItem> iterator = mPermissions.listIterator();
        while (iterator.hasNext()) {
            if (checkPermission(iterator.next().permission)) {
                iterator.remove();
            }
        }
        mCallback = callback;
        startActivity();
    }

    /**
     * 开启授权
     */
    private void startActivity() {
        Intent intent = new Intent();
        intent.putExtra(VMConstant.KEY_, mTitle);
        intent.putExtra(VMConstant.KEY_, mMessage);
        intent.putExtra(VMConstant.KEY_, (Parcelable) mPermissions);
        VMRouter.goPermission(mContext, intent);
    }
}
