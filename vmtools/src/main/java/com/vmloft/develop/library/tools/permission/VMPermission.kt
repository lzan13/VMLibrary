package com.vmloft.develop.library.tools.permission

import android.Manifest.permission
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Parcelable
import androidx.core.content.ContextCompat
import com.vmloft.develop.library.tools.base.VMConstant
import java.util.ArrayList

/**
 * Create by lzan13 on 2019/04/25
 *
 * 处理权限请求操作类
 */
class VMPermission
/**
 * 私有构造方法
 */
private constructor() {
    /**
     * 获取回调
     */
    // 授权处理回调
    var permissionCallback: PCallback? = null
    private var mEnableDialog = false
    private var mTitle: String? = null
    private var mMessage: String? = null
    private val mPermissions: MutableList<VMPermissionBean>? = ArrayList()

    /**
     * 内部类实现单例
     */
    private object InnerHolder {
        val INSTANCE = VMPermission()
    }

    /**
     * 是否开启请求权限前的弹窗，默认为 false
     *
     * @param enable 控制弹窗
     */
    fun setEnableDialog(enable: Boolean): VMPermission {
        mEnableDialog = enable
        return this
    }

    /**
     * 设置授权弹窗标题
     *
     * @param title 授权弹窗标题
     */
    fun setTitle(title: String?): VMPermission {
        mTitle = title
        return this
    }

    /**
     * 设置授权弹窗描述内容
     *
     * @param message 授权弹窗描述内容
     */
    fun setMessage(message: String?): VMPermission {
        mMessage = message
        return this
    }

    /**
     * 设置授权弹窗权限列表
     *
     * @param permission 权限
     */
    fun setPermission(permission: VMPermissionBean): VMPermission {
        mPermissions!!.clear()
        mPermissions.add(permission)
        return this
    }

    /**
     * 设置授权弹窗权限列表
     *
     * @param permissions 权限集合
     */
    fun setPermissionList(permissions: List<VMPermissionBean>?): VMPermission {
        mPermissions!!.clear()
        mPermissions.addAll(permissions!!)
        return this
    }

    /**
     * 检查权限方法，这里只是判断是否已授权，并不会请求授权
     *
     * @param permission 需要检查的权限
     * @return 返回是否授权
     */
    fun checkPermission(permission: String?): Boolean {
        val checkPermission = ContextCompat.checkSelfPermission(mContext!!, permission!!)
        return if (checkPermission == PackageManager.PERMISSION_GRANTED) {
            true
        } else false
    }

    /**
     * 检查权限方法，这里只是判断是否已授权，并不会请求授权
     *
     * @param permissions 需要检查的权限集合
     * @return 返回是否授权
     */
    fun checkPermission(permissions: List<String?>): Boolean {
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
     * 检查权限
     *
     * @param callback 授权回调接口
     */
    fun requestPermission(callback: PCallback?) {
        if (mPermissions == null || mPermissions.size == 0) {
            callback?.onComplete()
            return
        }
        /**
         * 运行在 6.0 以下设备上，直接返回授权完成
         */
        if (VERSION.SDK_INT < VERSION_CODES.M) {
            callback?.onComplete()
            return
        }
        /**
         * 过滤已允许的权限
         */
        val iterator = mPermissions.listIterator()
        while (iterator.hasNext()) {
            if (checkPermission(iterator.next().permission)) {
                iterator.remove()
            }
        }
        if (mPermissions.size == 0) {
            callback?.onComplete()
            return
        }
        permissionCallback = callback
        startActivity()
    }

    /**
     * 默认实现检查 访问相机 权限
     */
    fun checkCamera(): Boolean {
        return checkPermission(permission.CAMERA)
    }

    /**
     * 默认实现请求 访问相机 权限
     *
     * @param callback 回调接口
     */
    fun requestCamera(callback: PCallback?) {
        val bean = VMPermissionBean(permission.CAMERA, "访问相机", "拍摄照片需要 “访问相机” 权限，请授权此权限")
        setPermission(bean)
        requestPermission(callback)
    }

    /**
     * 默认实现检查 读写手机存储 权限
     */
    fun checkStorage(): Boolean {
        return checkPermission(permission.WRITE_EXTERNAL_STORAGE)
    }

    /**
     * 默认实现请求 读写手机存储 权限
     *
     * @param callback 回调接口
     */
    fun requestStorage(callback: PCallback?) {
        val bean = VMPermissionBean(
            permission.WRITE_EXTERNAL_STORAGE, "读写手机存储", "访问设备图片等文件需要 “访问手机存储” " + "权限，请授权此权限"
        )
        setPermission(bean)
        requestPermission(callback)
    }

    /**
     * 默认实现检查 录音 权限
     */
    fun checkRecord(): Boolean {
        return checkPermission(permission.RECORD_AUDIO)
    }

    /**
     * 默认实现请求 录音 权限
     *
     * @param callback 回调接口
     */
    fun requestRecord(callback: PCallback?) {
        val bean = VMPermissionBean(permission.RECORD_AUDIO, "录音", "录制语音需要 “录音” 权限，请授权此权限")
        setPermission(bean)
        requestPermission(callback)
    }

    /**
     * 开启授权
     */
    private fun startActivity() {
        val intent = Intent(mContext, VMPermissionActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(VMConstant.VM_KEY_PERMISSION_ENABLE_DIALOG, mEnableDialog)
        intent.putExtra(VMConstant.VM_KEY_PERMISSION_TITLE, mTitle)
        intent.putExtra(VMConstant.VM_KEY_PERMISSION_MSG, mMessage)
        intent.putParcelableArrayListExtra(VMConstant.VM_KEY_PERMISSION_LIST, mPermissions as ArrayList<out Parcelable>?)
        mContext!!.startActivity(intent)
    }

    /**
     * Create by lzan13 on 2019/04/25
     *
     * 权限申请授权结果回调接口
     */
    interface PCallback {
        /**
         * 权限申请被拒绝回调
         */
        fun onReject()

        /**
         * 权限申请完成回调
         */
        fun onComplete()
    }

    companion object {
        // 上下文对象
        private var mContext: Context? = null

        /**
         * 获取单例类的实例
         */
        @JvmStatic
        fun getInstance(context: Context?): VMPermission {
            mContext = context
            return InnerHolder.INSTANCE
        }
    }
}