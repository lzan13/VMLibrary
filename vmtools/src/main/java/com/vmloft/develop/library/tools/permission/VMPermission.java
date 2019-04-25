package com.vmloft.develop.library.tools.permission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import com.vmloft.develop.library.tools.base.VMConstant;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by lzan13 on 2019/04/25
 *
 * 处理权限请求操作类
 */
public class VMPermission {
    // 上下文对象
    private Context mContext;
    // 授权处理回调
    private VMPermissionCallback mCallback;
    // 授权类型 0-单个权限 1-多个权限
    private int mPermissionType;

    private String mTitle;
    private String mMsg;
    private int mStyleResId = -1;
    private int mFilterColor = 0;
    private int mAnimStyleId = -1;

    private List<String> mPermissions;

    private String[] mNormalPermissionNames;
    private String[] mNormalPermissions = {
        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
    };
    //private int[] mNormalPermissionIconRes = {
    //    R.drawable.permission_ic_storage, R.drawable.permission_ic_location,
    //    R.drawable.permission_ic_camera
    //};

    /**
     * 私有构造方法
     */
    private VMPermission() {}

    /**
     * 内部类实现单例
     */
    private static class VMPermissionHolder {
        public static final VMPermission INSTANCE = new VMPermission();
    }

    /**
     * 获取单例类的实例
     */
    public static final VMPermission getInstance() {
        return VMPermissionHolder.INSTANCE;
    }

    /**
     * 获取回调
     */
    public VMPermissionCallback getPermissionCallback() {
        return mCallback;
    }
    //
    //public HiPermission title(String title) {
    //    mTitle = title;
    //    return this;
    //}
    //
    //public HiPermission msg(String msg) {
    //    mMsg = msg;
    //    return this;
    //}
    //
    //public HiPermission permissions(List<PermissionItem> permissionItems) {
    //    mPermissions = permissionItems;
    //    return this;
    //}
    //
    //public HiPermission filterColor(int color) {
    //    mFilterColor = color;
    //    return this;
    //}
    //
    //public HiPermission animStyle(int styleId) {
    //    mAnimStyleId = styleId;
    //    return this;
    //}
    //
    //public HiPermission style(int styleResIdsId) {
    //    mStyleResId = styleResIdsId;
    //    return this;
    //}

    private List<PermissionItem> getNormalPermissions() {
        List<PermissionItem> permissionItems = new ArrayList<>();
        for (int i = 0; i < mNormalPermissionNames.length; i++) {
            permissionItems.add(new PermissionItem(mNormalPermissions[i], mNormalPermissionNames[i], mNormalPermissionIconRes[i]));
        }
        return permissionItems;
    }

    /**
     * 检查权限
     *
     * @param context
     * @param permission
     * @return
     */
    private boolean checkPermission(Context context, String permission) {
        int checkPermission = ContextCompat.checkSelfPermission(context, permission);
        if (checkPermission == PackageManager.PERMISSION_DENIED) {
            return true;
        }
        return false;
    }

    /**
     * 检查多个权限
     *
     * @param callback
     */
    public void checkMutiPermission(PermissionCallback callback) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (callback != null) {
                callback.onFinish();
            }
            return;
        }

        if (mPermissions == null) {
            mPermissions = new ArrayList<>();
            mPermissions.addAll(getNormalPermissions());
        }

        //检查权限，过滤已允许的权限
        Iterator<PermissionItem> iterator = mPermissions.listIterator();
        while (iterator.hasNext()) {
            if (checkPermission(mContext, iterator.next().Permission)) {
                iterator.remove();
            }
        }
        mCallback = callback;
        if (mPermissions.size() > 0) {
            startActivity();
        } else {
            if (callback != null) {
                callback.onFinish();
            }
        }
    }

    /**
     * 检查单个权限
     *
     * @param permission
     * @param callback
     */
    public void checkSinglePermission(String permission, PermissionCallback callback) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkPermission(mContext, permission)) {
            if (callback != null) {
                callback.onGuarantee(permission, 0);
            }
            return;
        }
        mCallback = callback;
        mPermissionType = PermissionActivity.PERMISSION_TYPE_SINGLE;
        mPermissions = new ArrayList<>();
        mPermissions.add(new PermissionItem(permission));
        startActivity();
    }

    private void startActivity() {
        Intent intent = new Intent(mContext, VMPermissionActivity.class);
        intent.putExtra(VMConstant.KEY_TITLE, mTitle);
        intent.putExtra(VMConstant.KEY_PERMISSION_TYPE, mPermissionType);
        intent.putExtra(VMConstant.KEY_MSG, mMsg);
        intent.putExtra(VMConstant.KEY_FILTER_COLOR, mFilterColor);
        intent.putExtra(VMConstant.KEY_STYLE_ID, mStyleResId);
        intent.putExtra(VMConstant.KEY_ANIM_STYLE, mAnimStyleId);
        intent.putExtra(VMConstant.KEY_PERMISSIONS, (Serializable) mPermissions);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
