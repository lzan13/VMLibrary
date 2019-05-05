package com.vmloft.develop.library.tools.permission;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.base.VMActivity;
import com.vmloft.develop.library.tools.router.VMParams;
import com.vmloft.develop.library.tools.router.VMRouter;
import com.vmloft.develop.library.tools.utils.VMStr;
import com.vmloft.develop.library.tools.utils.VMSystem;
import com.vmloft.develop.library.tools.widget.VMViewGroup;

import java.util.Iterator;
import java.util.List;

/**
 * Create by lzan13 on 2019/04/25
 *
 * 处理权限请求界面
 */
public class VMPermissionActivity extends VMActivity {

    // 权限申请
    private static final int REQUEST_PERMISSION = 100;
    // 被拒绝后再次申请
    public static final int REQUEST_PERMISSION_AGAIN = 101;
    // 调起设置界面设置权限
    private static final int REQUEST_SETTING = 200;


    /**
     * 重新申请权限数组的索引
     */
    private int mAgainIndex;
    // 申请权限弹窗标题
    private String mTitle;
    // 申请权限弹窗描述
    private String mMessage;
    // 权限列表
    private List<String> mPermissions;
    // 授权提示框
    private AlertDialog mDialog;
    private String mAppName;
    private VMPermissionCallback mCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    /**
     * 权限请求初始化操作
     */
    private void init() {
        // 初始化获取数据
        mAppName = VMSystem.getAppName(activity);
        mCallback = VMPermission.getInstance(activity).getPermissionCallback();
        VMParams params = VMRouter.getParams(activity);
        if (params != null) {
            mTitle = params.str0;
            mMessage = params.str1;
            mPermissions = params.strList;
        }

        if (mPermissions == null || mPermissions.size() == 0) {
            return;
        }
        // 单个权限不弹出说明对话框，直接进行授权请求
        if (mPermissions.size() == 1) {
            requestPermission(VMStr.listToArray(mPermissions), REQUEST_PERMISSION);
        } else {
            showPermissionDialog();
        }
    }

    /**
     * 显示提醒对话框
     *
     * @param title     标题
     * @param message   内容
     * @param cancelStr 取消按钮文本
     * @param okStr     确认按钮文本
     * @param listener  确认事件回调
     */
    private void showAlertDialog(String title, String message, String cancelStr, String okStr, DialogInterface.OnClickListener listener) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(cancelStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        onClose();
                    }
                })
                .setPositiveButton(okStr, listener)
                .create();
        alertDialog.show();
    }

    /**
     * 弹出授权窗口
     */
    private void showPermissionDialog() {
        View view = LayoutInflater.from(activity).inflate(R.layout.vm_widget_permission_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        if (!VMStr.isEmpty(mTitle)) {
            builder.setTitle(mTitle);
        }
        builder.setView(view);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                if (mCallback != null) {
                    mCallback.onClose();
                }
                finish();
            }
        });
        if (!VMStr.isEmpty(mMessage)) {
            TextView contentView = view.findViewById(R.id.vm_text_permission_dialog_content);
            contentView.setText(mMessage);
        }
        VMViewGroup viewGroup = view.findViewById(R.id.vm_view_group);

        view.findViewById(R.id.vm_btn_i_know).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                // 开始申请权限
                requestPermission(VMStr.listToArray(mPermissions), REQUEST_PERMISSION);
            }
        });
        mDialog = builder.create();
        mDialog.show();
    }

    /**
     * 申请权限，内部方法
     *
     * @param permissions 权限列表
     * @param requestCode 请求码
     */
    private void requestPermission(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * 再次申请权限
     *
     * @param permission 要申请的权限
     */
    private void requestPermissionAgain(final String permission) {
        String alertTitle = String.format(getString(R.string.permission_title), permission);
        String msg = String.format(getString(R.string.permission_denied), permission, mAppName);
        showAlertDialog(alertTitle, msg, getString(R.string.vm_btn_cancel), getString(R.string.vm_btn_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                requestPermission(new String[]{permission}, REQUEST_PERMISSION_AGAIN);
            }
        });
    }

    /**
     * 申请权限回调
     *
     * @param requestCode  权限申请请求码
     * @param permissions  申请的权限数组
     * @param grantResults 授权结果集合
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION:
                for (int i = 0; i < grantResults.length; i++) {
                    // 权限允许后，删除需要检查的权限
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        mPermissions.remove(permissions[i]);
                        onGranted(permissions[i]);
                    } else {
                        onDenied(permissions[i]);
                    }
                }
                if (mPermissions.size() > 0) {
                    //用户拒绝了某个或多个权限，重新申请
                    requestPermissionAgain(mPermissions.get(mAgainIndex));
                } else {
                    if (mCallback != null) {
                        mCallback.onFinish();
                    }
                    finish();
                }
                break;
            case REQUEST_PERMISSION_AGAIN:
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    // 重新申请后再次拒绝，弹框警告
                    try {
                        //permissions可能返回空数组，所以try-catch
                        String name = permissions[0];
                        String title = String.format(getString(R.string.permission_title), name);
                        String msg = String.format(getString(R.string.permission_denied_with_naac), mAppName, name, mAppName);
                        showAlertDialog(title, msg, getString(R.string.vm_btn_reject), getString(R.string.vm_btn_go_to_setting), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Uri packageURI = Uri.parse("package:" + getPackageName());
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                                    startActivityForResult(intent, REQUEST_SETTING);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    onClose();
                                }
                            }
                        });
                        onDenied(permissions[0]);
                    } catch (Exception e) {
                        e.printStackTrace();
                        onClose();
                    }
                } else {
                    onGranted(permissions[0]);
                    if (mAgainIndex < mPermissions.size() - 1) {
                        //继续申请下一个被拒绝的权限
                        requestPermissionAgain(mPermissions.get(++mAgainIndex));
                    } else {
                        //全部允许了
                        if (mCallback != null) {
                            mCallback.onFinish();
                        }
                        finish();
                    }
                }
                break;
        }
    }


    /**
     * 接收打开设置界面授权结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SETTING) {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            checkPermission();
            if (mPermissions.size() > 0) {
                mAgainIndex = 0;
                requestPermissionAgain(mPermissions.get(mAgainIndex));
            } else {
                if (mCallback != null) {
                    mCallback.onFinish();
                }
                finish();
            }
        }
    }

    /**
     * 检查权限
     */
    private void checkPermission() {
        Iterator<String> iterator = mPermissions.listIterator();
        while (iterator.hasNext()) {
            int checkPermission = ContextCompat.checkSelfPermission(activity, iterator.next());
            if (checkPermission == PackageManager.PERMISSION_GRANTED) {
                iterator.remove();
            }
        }
    }


    private void onClose() {
        if (mCallback != null) {
            mCallback.onClose();
        }
        finish();
    }

    private void onDenied(String permission) {
        if (mCallback != null) {
            mCallback.onDenied(permission);
        }
    }

    private void onGranted(String permission) {
        if (mCallback != null) {
            mCallback.onGranted(permission);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCallback = null;
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

}
