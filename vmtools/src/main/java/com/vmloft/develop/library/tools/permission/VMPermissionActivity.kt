package com.vmloft.develop.library.tools.permission

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.LinearLayout

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.vmloft.develop.library.tools.R
import com.vmloft.develop.library.tools.base.VMConstant
import com.vmloft.develop.library.tools.permission.VMPermission.PCallback
import com.vmloft.develop.library.tools.utils.VMStr
import com.vmloft.develop.library.tools.utils.VMSystem
import com.vmloft.develop.library.tools.widget.VMDefaultDialog
import com.vmloft.develop.library.tools.widget.VMLineView

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
    private var mEnableDialog = false

    // 申请权限弹窗标题
    private var mTitle: String? = null

    // 申请权限弹窗描述
    private var mMessage: String? = null

    // 权限列表
    private var mPermissions: MutableList<VMPermissionBean> = mutableListOf()

    // 权限列表拷贝
    private var mPermissionsCopy: MutableList<VMPermissionBean> = mutableListOf()

    // 授权提示框
    private var mDialog: VMDefaultDialog? = null
    private var mAppName: String? = null
    private var mCallback: PCallback? = null


    companion object {
        // 权限申请
        private const val REQUEST_PERMISSION = 100

        // 被拒绝后再次申请
        private const val REQUEST_PERMISSION_AGAIN = 101

        // 调起设置界面设置权限
        private const val REQUEST_SETTING = 200
    }

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
        mEnableDialog = intent.getBooleanExtra(VMConstant.vmPermissionEnableDialogKey, false)
        mTitle = intent.getStringExtra(VMConstant.vmPermissionTitleKey)
        mMessage = intent.getStringExtra(VMConstant.vmPermissionMsgKey)
        mPermissions.clear()
        mPermissions.addAll(intent.getParcelableArrayListExtra(VMConstant.vmPermissionListKey))
        mPermissionsCopy = mPermissions
        if (mPermissions.isEmpty()) {
            finish()
            return
        }
        // 根据需要弹出说明对话框
        if (mEnableDialog) {
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
     * @param negative 消极的按钮文本
     * @param positive 积极的按钮文本
     * @param listener 积极事件回调
     */
    private fun showAlertDialog(title: String, message: String, negative: String, positive: String, listener: View.OnClickListener) {
        mDialog = VMDefaultDialog(this)
        mDialog?.let { dialog ->
            dialog.setTitle(title)
            dialog.setContent(message)
            dialog.setNegative(negative) {
                onPermissionReject()
                finish()
            }
            dialog.setPositive(positive, listener)
            dialog.show()
        }
    }

    /**
     * 弹出授权窗口
     */
    private fun showPermissionDialog() {
        if (VMStr.isEmpty(mTitle)) {
            mTitle = VMStr.byRes(R.string.vm_permission_title)
        }
        if (VMStr.isEmpty(mMessage)) {
            mMessage = VMStr.byResArgs(R.string.vm_permission_reason, mAppName)
        }
        mDialog = VMDefaultDialog(this)
        mDialog?.let { dialog ->
            dialog.touchDismissSwitch = false
            dialog.backDismissSwitch = false
            dialog.setTitle(mTitle)
            dialog.setContent(mMessage!!)
            val viewGroup = LinearLayout(this)
            viewGroup.orientation = LinearLayout.VERTICAL

            for (bean in mPermissions) {
                val itemView = VMLineView(this)
                itemView.setIconRes(bean.resId)
                itemView.setTitle(bean.name)
                viewGroup.addView(itemView)
            }
            dialog.setView(viewGroup)
            dialog.setNegative("")
            dialog.setPositive(VMStr.byRes(R.string.vm_i_know)) {
                // 开始申请权限
                requestPermission(permissionArray, REQUEST_PERMISSION)
            }
            dialog.show()
        }
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
    private fun requestPermissionAgain(item: VMPermissionBean) {
        val alertTitle = VMStr.byResArgs(R.string.vm_permission_again_title, item.name)
        val msg = VMStr.byResArgs(R.string.vm_permission_again_reason, item.name, item.reason)
        showAlertDialog(alertTitle, msg, VMStr.byResArgs(R.string.vm_btn_cancel), VMStr.byResArgs(R.string.vm_btn_confirm)) {
            requestPermission(arrayOf(item.permission), REQUEST_PERMISSION_AGAIN)
        }
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
                    val title = VMStr.byResArgs(R.string.vm_permission_again_title, item.name)
                    val msg = VMStr.byResArgs(R.string.vm_permission_denied_setting, mAppName, item.name)
                    showAlertDialog(title, msg, VMStr.byRes(R.string.vm_btn_reject), VMStr.byRes(R.string.vm_btn_go_to_setting)) {
                        val packageURI = Uri.parse("package:$packageName")
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
                        startActivityForResult(intent, requestCode)
                    }
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
            mDialog?.dismiss()
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
        val iterator: MutableIterator<VMPermissionBean> = mPermissions.listIterator()
        while (iterator.hasNext()) {
            val checkPermission = ContextCompat.checkSelfPermission(this, iterator.next().permission!!)
            if (checkPermission == PackageManager.PERMISSION_GRANTED) {
                iterator.remove()
            }
        }
    }

    /**
     * 回调权限申请被拒绝
     */
    private fun onPermissionReject() {
        mCallback?.onReject()
    }

    /**
     * 回调权限申请通过
     */
    private fun onPermissionComplete() {
        mCallback?.onComplete()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCallback = null

        mDialog?.let { dialog ->
            dialog.dismiss()
            mDialog = null
        }
    }

}