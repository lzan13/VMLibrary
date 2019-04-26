package com.vmloft.develop.library.tools.permission;

/**
 * Create by lzan13 on 2019/04/25
 *
 * 授权回调
 */
public interface VMPermissionCallback {
    /**
     * 授权关闭
     */
    void onClose();

    /**
     * 授权完成
     */
    void onFinish();

    /**
     * 拒绝权限回调
     *
     * @param permission 权限
     */
    void onDenied(String permission);

    /**
     * 同意权限回调
     *
     * @param permission 权限
     */
    void onGranted(String permission);
}
