package com.vmloft.develop.library.tools.permission;

/**
 * Create by lzan13 on 2019/04/25
 *
 * 权限申请授权结果回调接口
 */
public interface VMPermissionCallback {

    /**
     * 权限申请被拒绝回调
     */
    void onReject();

    /**
     * 权限申请完成回调
     */
    void onComplete();
}
