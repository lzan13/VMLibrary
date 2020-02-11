package com.vmloft.develop.library.tools.permission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Parcelable;
import androidx.core.content.ContextCompat;

import com.vmloft.develop.library.tools.base.VMConstant;
import com.vmloft.develop.library.tools.router.VMRouter;

import java.util.ArrayList;
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
    private PCallback mCallback;

    private boolean mEnableDialog;
    private String mTitle;
    private String mMessage;
    private List<VMPermissionBean> mPermissions = new ArrayList<>();

    /**
     * 私有构造方法
     */
    private VMPermission() {
    }

    /**
     * 内部类实现单例
     */
    private static class InnerHolder {
        public static final VMPermission INSTANCE = new VMPermission();
    }

    /**
     * 获取单例类的实例
     */
    public static final VMPermission getInstance(Context context) {
        mContext = context;
        return InnerHolder.INSTANCE;
    }

    /**
     * 获取回调
     */
    public PCallback getPermissionCallback() {
        return mCallback;
    }

    /**
     * 是否开启请求权限前的弹窗，默认为 false
     *
     * @param enable 控制弹窗
     */
    public VMPermission setEnableDialog(boolean enable) {
        mEnableDialog = enable;
        return this;
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
     * @param permission 权限
     */
    public VMPermission setPermission(VMPermissionBean permission) {
        mPermissions.clear();
        mPermissions.add(permission);
        return this;
    }

    /**
     * 设置授权弹窗权限列表
     *
     * @param permissions 权限集合
     */
    public VMPermission setPermissionList(List<VMPermissionBean> permissions) {
        mPermissions.clear();
        mPermissions.addAll(permissions);
        return this;
    }

    /**
     * 检查权限方法，这里只是判断是否已授权，并不会请求授权
     *
     * @param permission 需要检查的权限
     * @return 返回是否授权
     */
    public boolean checkPermission(String permission) {
        int checkPermission = ContextCompat.checkSelfPermission(mContext, permission);
        if (checkPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    /**
     * 检查权限方法，这里只是判断是否已授权，并不会请求授权
     *
     * @param permissions 需要检查的权限集合
     * @return 返回是否授权
     */
    public boolean checkPermission(List<String> permissions) {
        boolean result = true;
        for (int i = 0; i < permissions.size(); i++) {
            result = checkPermission(permissions.get(i));
            if (!result) {
                return false;
            }
        }
        return result;
    }

    /**
     * 检查权限
     *
     * @param callback 授权回调接口
     */
    public void requestPermission(PCallback callback) {
        if (mPermissions == null || mPermissions.size() == 0) {
            if (callback != null) {
                callback.onComplete();
            }
            return;
        }
        /**
         * 运行在 6.0 以下设备上，直接返回授权完成
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (callback != null) {
                callback.onComplete();
            }
            return;
        }

        /**
         * 过滤已允许的权限
         */
        ListIterator<VMPermissionBean> iterator = mPermissions.listIterator();
        while (iterator.hasNext()) {
            if (checkPermission(iterator.next().permission)) {
                iterator.remove();
            }
        }
        if (mPermissions.size() == 0) {
            if (callback != null) {
                callback.onComplete();
            }
            return;
        }
        mCallback = callback;
        startActivity();
    }

    /**
     * 默认实现检查 访问相机 权限
     */
    public boolean checkCamera() {
        return checkPermission(Manifest.permission.CAMERA);
    }

    /**
     * 默认实现请求 访问相机 权限
     *
     * @param callback 回调接口
     */
    public void requestCamera(PCallback callback) {
        VMPermissionBean bean = new VMPermissionBean(Manifest.permission.CAMERA, "访问相机", "拍摄照片需要 “访问相机” 权限，请授权此权限");
        setPermission(bean);
        requestPermission(callback);
    }

    /**
     * 默认实现检查 读写手机存储 权限
     */
    public boolean checkStorage() {
        return checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 默认实现请求 读写手机存储 权限
     *
     * @param callback 回调接口
     */
    public void requestStorage(PCallback callback) {
        VMPermissionBean bean = new VMPermissionBean(Manifest.permission.WRITE_EXTERNAL_STORAGE, "读写手机存储", "访问设备图片等文件需要 “访问手机存储” "
            + "权限，请授权此权限");
        setPermission(bean);
        requestPermission(callback);
    }

    /**
     * 默认实现检查 录音 权限
     */
    public boolean checkRecord() {
        return checkPermission(Manifest.permission.RECORD_AUDIO);
    }

    /**
     * 默认实现请求 录音 权限
     *
     * @param callback 回调接口
     */
    public void requestRecord(PCallback callback) {
        VMPermissionBean bean = new VMPermissionBean(Manifest.permission.RECORD_AUDIO, "录音", "录制语音需要 “录音” 权限，请授权此权限");
        setPermission(bean);
        requestPermission(callback);
    }

    /**
     * 开启授权
     */
    private void startActivity() {
        Intent intent = new Intent();
        intent.putExtra(VMConstant.VM_KEY_PERMISSION_ENABLE_DIALOG, mEnableDialog);
        intent.putExtra(VMConstant.VM_KEY_PERMISSION_TITLE, mTitle);
        intent.putExtra(VMConstant.VM_KEY_PERMISSION_MSG, mMessage);
        intent.putParcelableArrayListExtra(VMConstant.VM_KEY_PERMISSION_LIST, (ArrayList<? extends Parcelable>) mPermissions);
        VMRouter.goPermission(mContext, intent);
    }

    /**
     * Create by lzan13 on 2019/04/25
     *
     * 权限申请授权结果回调接口
     */
    public interface PCallback {

        /**
         * 权限申请被拒绝回调
         */
        void onReject();

        /**
         * 权限申请完成回调
         */
        void onComplete();
    }
}
