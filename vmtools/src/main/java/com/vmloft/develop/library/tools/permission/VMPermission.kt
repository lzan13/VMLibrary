package com.vmloft.develop.library.tools.permission

import android.Manifest.permission
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Parcelable
import androidx.core.content.ContextCompat
import com.vmloft.develop.library.tools.VMTools
import com.vmloft.develop.library.tools.base.VMConstant
import java.util.ArrayList

/**
 * Create by lzan13 on 2019/04/25
 *
 * 处理权限请求操作类
 */
object VMPermission {
    private var dialogEnable = false // 是否显示对话框
    private var dialogTitle: String = ""  // 对话框标题
    private var dialogContent: String = "" // 对话框内容

    private var requestCallback: (Boolean) -> Unit = {}

    // 权限列表
    private val permissionList = mutableListOf<VMPermissionBean>()

    /**
     * 是否开启请求权限前的弹窗，默认为 false
     * @param enable 控制弹窗
     */
    fun setEnableDialog(enable: Boolean): VMPermission {
        dialogEnable = enable
        return this
    }

    /**
     * 设置授权弹窗标题
     * @param title 授权弹窗标题
     */
    fun setDialogTitle(title: String): VMPermission {
        dialogTitle = title
        return this
    }

    /**
     * 设置授权弹窗描述内容
     * @param message 授权弹窗描述内容
     */
    fun setDialogContent(content: String): VMPermission {
        dialogContent = content
        return this
    }

    /**
     * 设置授权弹窗权限列表
     * @param permission 权限
     */
    fun addPermission(permission: VMPermissionBean): VMPermission {
        permissionList.clear()
        permissionList.add(permission)
        return this
    }

    /**
     * 设置授权弹窗权限列表
     * @param list 权限集合
     */
    fun addPermissionList(list: List<VMPermissionBean>): VMPermission {
        permissionList.clear()
        permissionList.addAll(list)
        return this
    }

    /**
     * 检查权限方法，这里只是判断是否已授权，并不会请求授权
     *
     * @param permission 需要检查的权限
     * @return 返回是否授权
     */
    fun checkPermission(permission: String): Boolean {
        val checkPermission = ContextCompat.checkSelfPermission(VMTools.context, permission)
        return checkPermission == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 检查权限方法，这里只是判断是否已授权，并不会请求授权
     *
     * @param permissions 需要检查的权限集合
     * @return 返回是否授权
     */
    fun checkPermission(permissions: List<String>): Boolean {
        var result = true
        for (i in permissions.indices) {
            result = checkPermission(permissions[i])
            if (!result) {
                return false
            }
        }
        return result
    }

    /**
     * 请求权限
     * @param callback 结果回调 true 表示权限请求成功，false 表示失败
     */
    fun requestPermission(context: Context, callback: (Boolean) -> Unit = {}) {
        if (permissionList.isEmpty()) {
            callback.invoke(true)
            return
        }
        /**
         * 运行在 6.0 以下设备上，直接返回授权完成
         */
        if (VERSION.SDK_INT < VERSION_CODES.M) {
            callback.invoke(true)
            return
        }
        /**
         * 过滤已允许的权限
         */
        val iterator = permissionList.listIterator()
        while (iterator.hasNext()) {
            if (checkPermission(iterator.next().permission)) {
                iterator.remove()
            }
        }
        if (permissionList.size == 0) {
            callback.invoke(true)
            return
        }
        requestCallback = callback
        startActivity(context)
    }

    /**
     * 获取权限请求回调，用户权限请求界面通知回调
     */
    fun getRequestCallback(): (Boolean) -> Unit {
        return requestCallback
    }

    /**
     * 默认实现检查 访问相机 权限
     */
    fun checkCamera(): Boolean {
        return checkPermission(permission.CAMERA)
    }

    /**
     * 默认实现请求 访问相机 权限
     * @param callback 结果回调 true 表示权限请求成功，false 表示失败
     */
    fun requestCamera(context: Context, callback: (Boolean) -> Unit = {}) {
        val bean = VMPermissionBean(permission.CAMERA, "访问相机", "拍摄照片需要 “访问相机” 权限，请授权此权限")
        addPermission(bean)
        requestPermission(context, callback)
    }

    /**
     * 默认实现检查 读写手机存储 权限
     */
    fun checkStorage(): Boolean {
        return checkPermission(permission.WRITE_EXTERNAL_STORAGE)
    }

    /**
     * 默认实现请求 读写手机存储 权限
     * @param callback 结果回调 true 表示权限请求成功，false 表示失败
     */
    fun requestStorage(context: Context, callback: (Boolean) -> Unit = {}) {
        val bean = VMPermissionBean(
            permission.WRITE_EXTERNAL_STORAGE, "读写手机存储", "访问设备图片等文件需要 “访问手机存储” " + "权限，请授权此权限"
        )
        addPermission(bean)
        requestPermission(context, callback)
    }

    /**
     * 默认实现检查 录音 权限
     */
    fun checkRecord(): Boolean {
        return checkPermission(permission.RECORD_AUDIO)
    }

    /**
     * 默认实现请求 录音 权限
     * @param callback 结果回调 true 表示权限请求成功，false 表示失败
     */
    fun requestRecord(context: Context, callback: (Boolean) -> Unit = {}) {
        val bean = VMPermissionBean(permission.RECORD_AUDIO, "录音", "录制语音需要 “录音” 权限，请授权此权限")
        addPermission(bean)
        requestPermission(context, callback)
    }

    /**
     * 开启授权
     */
    private fun startActivity(context: Context) {
        val intent = Intent(context, VMPermissionActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        intent.putExtra(VMConstant.vmPermissionDialogEnableKey, dialogEnable)
        intent.putExtra(VMConstant.vmPermissionDialogTitleKey, dialogTitle)
        intent.putExtra(VMConstant.vmPermissionDialogContentKey, dialogContent)
        intent.putParcelableArrayListExtra(VMConstant.vmPermissionListKey, permissionList as ArrayList<out Parcelable>)

        context.startActivity(intent)
    }
}