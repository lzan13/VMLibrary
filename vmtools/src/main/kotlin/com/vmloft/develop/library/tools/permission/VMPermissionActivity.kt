package com.vmloft.develop.library.tools.permission

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

import com.vmloft.develop.library.tools.R
import com.vmloft.develop.library.tools.utils.VMStr
import com.vmloft.develop.library.tools.utils.VMSystem
import com.vmloft.develop.library.tools.widget.dialog.VMDefaultDialog
import com.vmloft.develop.library.tools.widget.VMLineView

/**
 * Create by lzan13 on 2019/04/25
 *
 * 处理权限请求界面
 */
class VMPermissionActivity : FragmentActivity() {

    private val requestPermission = 100 // 权限申请
    private val requestPermissionAgain = 101 // 被拒绝后再次申请
    private val requestSetting = 200 // 调起设置界面设置权限

    private var mAgainIndex = 0 // 重新申请权限数组的索引

    private var dialogEnable = false // 是否显示申请权限弹窗
    private var dialogTitle: String = "" // 申请权限弹窗标题
    private var dialogContent: String = "" // 申请权限弹窗描述
    private var permissionList = mutableListOf<VMPermissionBean>() // 权限列表
    private var permissionListCopy = mutableListOf<VMPermissionBean>() // 权限列表拷贝

    private var requestCallback: (Boolean) -> Unit = {}

    // 授权提示框
    private var mDialog: VMDefaultDialog? = null
    private var appName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    /**
     * 权限请求初始化操作
     */
    private fun init() {
        // 初始化获取数据
        appName = VMSystem.getAppName(this) ?: ""

        requestCallback = VMPermission.getRequestCallback()

        dialogEnable = intent.getBooleanExtra(VMPermission.vmPermissionDialogEnableKey, false)
        dialogTitle = intent.getStringExtra(VMPermission.vmPermissionDialogTitleKey) ?: ""
        dialogContent = intent.getStringExtra(VMPermission.vmPermissionDialogContentKey) ?: ""

        permissionList.clear()
        val list = intent.getParcelableArrayListExtra<VMPermissionBean>(VMPermission.vmPermissionListKey) ?: arrayListOf()
        permissionList.addAll(list)
        permissionListCopy = permissionList
        if (permissionList.isEmpty()) {
            finish()
            return
        }
        // 根据需要弹出说明对话框
        if (dialogEnable) {
            showPermissionDialog()
        } else {
            requestPermission(permissionArray, requestPermission)
        }
    }

    /**
     * 获取申请权限集合
     */
    private val permissionArray: Array<String?>
        get() {
            val permissionArray = arrayOfNulls<String>(permissionList.size)
            for (i in permissionList.indices) {
                val item = permissionList[i]
                permissionArray[i] = item.permission
            }
            return permissionArray
        }

    /**
     * 根据权限名获取申请权限的实体类
     */
    private fun getPermissionItem(permission: String): VMPermissionBean? {
        for (i in permissionList.indices) {
            val item = permissionList[i]
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
     * @param content 内容
     * @param negative 消极的按钮文本
     * @param positive 积极的按钮文本
     * @param listener 积极事件回调
     */
    private fun showAlertDialog(title: String, content: String, negative: String, positive: String, listener: View.OnClickListener) {
        mDialog = VMDefaultDialog(this)
        mDialog?.let { dialog ->
            dialog.backDismissSwitch = false
            dialog.touchDismissSwitch = false
            dialog.setTitle(title)
            dialog.setContent(content)
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
        if (dialogTitle.isNullOrEmpty()) {
            dialogTitle = VMStr.byRes(R.string.vm_permission_title)
        }
        if (dialogContent.isNullOrEmpty()) {
            dialogContent = VMStr.byResArgs(R.string.vm_permission_reason, appName)
        }
        mDialog = VMDefaultDialog(this)
        mDialog?.let { dialog ->
            dialog.touchDismissSwitch = false
            dialog.backDismissSwitch = false
            dialog.setTitle(dialogTitle)
            dialog.setContent(dialogContent)
            val viewGroup = LinearLayout(this)
            viewGroup.orientation = LinearLayout.VERTICAL

            for (bean in permissionList) {
                val itemView = VMLineView(this)
                itemView.setIconRes(bean.resId)
                itemView.setTitle(bean.name)
                val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                viewGroup.addView(itemView, lp)
            }
            dialog.setView(viewGroup)
            dialog.setNegative("")
            dialog.setPositive(VMStr.byRes(R.string.vm_i_know)) {
                // 开始申请权限
                requestPermission(permissionArray, requestPermission)
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
        val content = VMStr.byResArgs(R.string.vm_permission_again_reason, item.name, item.reason)
        showAlertDialog(alertTitle, content, VMStr.byResArgs(R.string.vm_btn_cancel), VMStr.byResArgs(R.string.vm_btn_confirm)) {
            requestPermission(arrayOf(item.permission), requestPermissionAgain)
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
            requestPermission -> {
                var i = 0
                while (i < grantResults.size) {

                    // 权限允许后，删除需要检查的权限
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        permissionList.remove(getPermissionItem(permissions[i]))
                    }
                    i++
                }
                if (permissionList.size > 0) {
                    //用户拒绝了某个或多个权限，重新申请
                    requestPermissionAgain(permissionList[mAgainIndex])
                } else {
                    onPermissionComplete()
                    finish()
                }
            }
            requestPermissionAgain -> {
                if (permissions.isEmpty() || grantResults.isEmpty()) {
                    onPermissionReject()
                    finish()
                    return
                }
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    // 重新申请后再次拒绝，弹框警告
                    // permissions 可能返回空数组，所以try-catch
                    val item = permissionList[0]
                    val title = VMStr.byResArgs(R.string.vm_permission_again_title, item.name)
                    val content = VMStr.byResArgs(R.string.vm_permission_denied_setting, appName, item.name)
                    showAlertDialog(title, content, VMStr.byRes(R.string.vm_btn_reject), VMStr.byRes(R.string.vm_btn_go_to_setting)) {
                        val packageURI = Uri.parse("package:$packageName")
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
                        startActivityForResult(intent, requestCode)
                        finish()
                    }
                } else {
                    if (mAgainIndex < permissionList.size - 1) {
                        // 继续申请下一个被拒绝的权限
                        requestPermissionAgain(permissionList[++mAgainIndex])
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
        if (requestCode == requestSetting) {
            mDialog?.dismiss()
            checkPermission()
            if (permissionList.size > 0) {
                mAgainIndex = 0
                requestPermissionAgain(permissionList[mAgainIndex])
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
        permissionList = permissionListCopy
        val iterator: MutableIterator<VMPermissionBean> = permissionList.listIterator()
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
        requestCallback.invoke(false)
    }

    /**
     * 回调权限申请通过
     */
    private fun onPermissionComplete() {
        requestCallback.invoke(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        mDialog?.dismiss()
    }
}