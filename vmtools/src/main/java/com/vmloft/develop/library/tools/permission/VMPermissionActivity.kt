package com.vmloft.develop.library.tools.permission

import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AlertDialog.Builder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.vmloft.develop.library.tools.R.id
import com.vmloft.develop.library.tools.R.layout
import com.vmloft.develop.library.tools.R.string
import com.vmloft.develop.library.tools.base.VMConstant
import com.vmloft.develop.library.tools.permission.VMPermission.PCallback
import com.vmloft.develop.library.tools.utils.VMStr
import com.vmloft.develop.library.tools.utils.VMSystem
import com.vmloft.develop.library.tools.widget.VMViewGroup

/**
 * Create by lzan13 on 2019/04/25
 *
 * 处理权限请求界面
 */
class VMPermissionActivity : AppCompatActivity() {
    /**
     * 重新申请权限数组的索引
     */
    private var mAgainIndex = 0

    // 是否显示申请权限弹窗
    private var mEnableDialog: Boolean? = null

    // 申请权限弹窗标题
    private var mTitle: String? = null

    // 申请权限弹窗描述
    private var mMessage: String? = null

    // 权限列表
    private var mPermissions: MutableList<VMPermissionBean> = mutableListOf()

    // 权限列表拷贝
    private var mPermissionsCopy: MutableList<VMPermissionBean> = mutableListOf()

    // 授权提示框
    private var mDialog: AlertDialog? = null
    private var mAppName: String? = null
    private var mCallback: PCallback? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    /**
     * 权限请求初始化操作
     */
    private fun init() {
        // 初始化获取数据
        mAppName = VMSystem.getAppName(this)
        mCallback = VMPermission.getInstance(this).permissionCallback
        mEnableDialog = intent.getBooleanExtra(VMConstant.VM_KEY_PERMISSION_ENABLE_DIALOG, false)
        mTitle = intent.getStringExtra(VMConstant.VM_KEY_PERMISSION_TITLE)
        mMessage = intent.getStringExtra(VMConstant.VM_KEY_PERMISSION_MSG)
        mPermissions = intent.getParcelableArrayListExtra(VMConstant.VM_KEY_PERMISSION_LIST)
        mPermissionsCopy = mPermissions
        if (mPermissions == null || mPermissions.size == 0) {
            finish()
            return
        }
        // 根据需要弹出说明对话框
        if (mEnableDialog!!) {
            showPermissionDialog()
        } else {
            requestPermission(permissionArray, REQUEST_PERMISSION)
        }
    }

    /**
     * 获取申请权限集合
     */
    private val permissionArray: Array<String?>
        get() {
            val permissionArray = arrayOfNulls<String>(mPermissions.size)
            for (i in mPermissions.indices) {
                val item = mPermissions[i]
                permissionArray[i] = item.permission
            }
            return permissionArray
        }

    /**
     * 根据权限名获取申请权限的实体类
     */
    private fun getPermissionItem(permission: String): VMPermissionBean? {
        for (i in mPermissions.indices) {
            val item = mPermissions[i]
            if (item.permission == permission) {
                return item
            }
        }
        return null
    }

    /**
     * 显示提醒对话框
     *
     * @param title 标题
     * @param message 内容
     * @param cancelStr 取消按钮文本
     * @param okStr 确认按钮文本
     * @param listener 确认事件回调
     */
    private fun showAlertDialog(title: String, message: String, cancelStr: String, okStr: String, listener: OnClickListener) {
        val alertDialog = Builder(this).setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setNegativeButton(cancelStr) { dialog: DialogInterface, which: Int ->
                dialog.dismiss()
                onPermissionReject()
                finish()
            }
            .setPositiveButton(okStr, listener)
            .create()
        alertDialog.show()
    }

    /**
     * 弹出授权窗口
     */
    private fun showPermissionDialog() {
        val view = LayoutInflater.from(this).inflate(layout.vm_widget_permission_dialog, null)
        val builder = Builder(this)
        if (VMStr.isEmpty(mTitle)) {
            mTitle = VMStr.byRes(string.vm_permission_title)
        }
        builder.setTitle(mTitle)
        builder.setView(view)
        // 设置提醒信息
        if (VMStr.isEmpty(mMessage)) {
            mMessage = VMStr.byRes(string.vm_permission_reason)
        }
        val contentView = view.findViewById<TextView>(id.vm_permission_dialog_content_tv)
        contentView.text = mMessage
        val viewGroup: VMViewGroup = view.findViewById(id.vm_permission_dialog_custom_vg)
        for (bean in mPermissions) {
            val pView = VMPermissionView(this)
            pView.setPermissionIcon(bean.resId)
            pView.setPermissionName(bean.name)
            viewGroup.addView(pView)
        }
        view.findViewById<View>(id.vm_i_know_btn).setOnClickListener {
            if (mDialog != null && mDialog!!.isShowing) {
                mDialog!!.dismiss()
            }
            // 开始申请权限
            requestPermission(permissionArray, REQUEST_PERMISSION)
        }
        mDialog = builder.create()
        mDialog!!.setCancelable(false)
        mDialog!!.show()
    }

    /**
     * 申请权限，内部方法
     *
     * @param permissions 权限列表
     * @param requestCode 请求码
     */
    private fun requestPermission(permissions: Array<String?>, requestCode: Int) {
        ActivityCompat.requestPermissions(this, permissions, requestCode)
    }

    /**
     * 再次申请权限
     *
     * @param item 要申请的权限
     */
    private fun requestPermissionAgain(item: VMPermissionBean?) {
        val alertTitle = String.format(getString(string.vm_permission_again_title), item!!.name)
        val msg = String.format(getString(string.vm_permission_again_reason), item.name, item.reason)
        showAlertDialog(alertTitle, msg, getString(string.vm_btn_cancel), getString(string.vm_btn_ok), OnClickListener { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
            requestPermission(arrayOf(item.permission), REQUEST_PERMISSION_AGAIN)
        })
    }

    /**
     * 申请权限回调
     *
     * @param requestCode 权限申请请求码
     * @param permissions 申请的权限数组
     * @param grantResults 授权结果集合
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION -> {
                var i = 0
                while (i < grantResults.size) {

                    // 权限允许后，删除需要检查的权限
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        mPermissions.remove(getPermissionItem(permissions[i]))
                    }
                    i++
                }
                if (mPermissions.size > 0) {
                    //用户拒绝了某个或多个权限，重新申请
                    requestPermissionAgain(mPermissions[mAgainIndex])
                } else {
                    onPermissionComplete()
                    finish()
                }
            }
            REQUEST_PERMISSION_AGAIN -> {
                if (permissions.isEmpty() || grantResults.isEmpty()) {
                    onPermissionReject()
                    finish()
                    return
                }
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    // 重新申请后再次拒绝，弹框警告
                    // permissions 可能返回空数组，所以try-catch
                    val item = mPermissions[0]
                    val title = String.format(getString(string.vm_permission_again_title), item.name)
                    val msg = String.format(getString(string.vm_permission_denied_setting), mAppName, item.name, mAppName)
                    showAlertDialog(title, msg, getString(string.vm_btn_reject), getString(string.vm_btn_go_to_setting), OnClickListener { _: DialogInterface?, _: Int ->
                        val packageURI = Uri.parse("package:$packageName")
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
                        startActivityForResult(intent, requestCode)
                    })
                } else {
                    if (mAgainIndex < mPermissions.size - 1) {
                        // 继续申请下一个被拒绝的权限
                        requestPermissionAgain(mPermissions[++mAgainIndex])
                    } else {
                        // 全部允许了
                        onPermissionComplete()
                        finish()
                    }
                }
            }
        }
    }

    /**
     * 回调用户手动设置授权结果，同时再次对权限进行检查，防止用户关闭了某些权限
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SETTING) {
            if (mDialog != null && mDialog!!.isShowing) {
                mDialog!!.dismiss()
            }
            checkPermission()
            if (mPermissions.size > 0) {
                mAgainIndex = 0
                requestPermissionAgain(mPermissions[mAgainIndex])
            } else {
                onPermissionComplete()
                finish()
            }
        }
    }

    /**
     * 循环检查权限，移除已授权权限
     * 这里的检查是一个完整的检查，为的是防止用户打开设置后取消了某些已授权的权限
     */
    private fun checkPermission() {
        mPermissions = mPermissionsCopy
        val iterator: MutableIterator<VMPermissionBean?> = mPermissions.listIterator()
        while (iterator.hasNext()) {
            val checkPermission = ContextCompat.checkSelfPermission(this, iterator.next()!!.permission!!)
            if (checkPermission == PackageManager.PERMISSION_GRANTED) {
                iterator.remove()
            }
        }
    }

    /**
     * 回调权限申请被拒绝
     */
    private fun onPermissionReject() {
        if (mCallback != null) {
            mCallback!!.onReject()
        }
    }

    /**
     * 回调权限申请通过
     */
    private fun onPermissionComplete() {
        if (mCallback != null) {
            mCallback!!.onComplete()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mCallback = null
        if (mDialog != null && mDialog!!.isShowing) {
            mDialog!!.dismiss()
        }
    }

    companion object {
        // 权限申请
        private const val REQUEST_PERMISSION = 100

        // 被拒绝后再次申请
        private const val REQUEST_PERMISSION_AGAIN = 101

        // 调起设置界面设置权限
        private const val REQUEST_SETTING = 200
    }
}