package com.vmloft.develop.library.example;

import android.Manifest;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.vmloft.develop.library.example.common.AppActivity;
import com.vmloft.develop.library.example.router.ARouter;
import com.vmloft.develop.library.tools.permission.VMPermission;
import com.vmloft.develop.library.tools.permission.VMPermissionCallback;
import com.vmloft.develop.library.tools.widget.VMToast;
import com.vmloft.develop.library.tools.widget.VMViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends AppActivity {

    @BindView(R.id.view_group)
    VMViewGroup viewGroup;

    // 定义动态添加的控件起始 id
    private final int CLICK_START_ID = 100;

    @Override
    protected int layoutId() {
        // 修改界面主题
        setTheme(R.style.AppTheme);
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        getToolbar().setTitle("VMLibrary");
        setSupportActionBar(getToolbar());

        String[] btnArray = {
                "工具", "描点控件", "详情控件", "自定义控件", "录制屏幕", "声音播放", "按钮样式", "弹出窗口", "Web 功能", "指示器", "识别验证码", "单权限申请", "多权限申请"
        };
        for (int i = 0; i < btnArray.length; i++) {
            Button btn = new Button(new ContextThemeWrapper(activity, R.style.VMBtn_Flat));
            btn.setText(btnArray[i]);
            btn.setId(CLICK_START_ID + i);
            btn.setOnClickListener(viewListener);
            viewGroup.addView(btn);
        }

//        checkPermissions();
    }

    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case CLICK_START_ID + 0:
                    ARouter.goTools(activity);
                    break;
                case CLICK_START_ID + 1:
                    ARouter.goDotLine(activity);
                    break;
                case CLICK_START_ID + 2:
                    ARouter.goDetails(activity);
                    break;
                case CLICK_START_ID + 3:
                    ARouter.goCustomView(activity);
                    break;
                case CLICK_START_ID + 4:
                    ARouter.goRecordScreen(activity);
                    break;
                case CLICK_START_ID + 5:
                    ARouter.goPlayAudio(activity);
                    break;
                case CLICK_START_ID + 6:
                    ARouter.goBtnStyle(activity);
                    break;
                case CLICK_START_ID + 7:
                    ARouter.goPWDialog(activity);
                    break;
                case CLICK_START_ID + 8:
                    ARouter.goWeb(activity);
                    break;
                case CLICK_START_ID + 9:
                    ARouter.goIndicator(activity);
                    break;
                case CLICK_START_ID + 10:
                    ARouter.goImageDiscern(activity);
                    break;
                case CLICK_START_ID + 11:
                    checkOnePermissions();
                    break;
                case CLICK_START_ID + 12:
                    checkPermissions();
                    break;
                default:
                    ARouter.goTools(activity);
                    break;
            }
        }
    };

    /**
     * 重写返回按键会退到桌面，并不结束 app
     */
    @Override
    public void onBackPressed() {
        ARouter.goLauncher(activity);
    }

    /**
     * 检查权限
     */
    private void checkOnePermissions() {
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.CAMERA);
//        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//        VMPermission.getInstance(activity)
//                .setTitle("权限申请")
//                .setMessage("为保证应用的正常运行，请授予一下权限")
//                .setPermissions(permissions)
//                .checkPermission(new VMPermissionCallback() {
//                    @Override
//                    public void onClose() {
//                        VMToast.make("授权被关闭").showError();
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        VMToast.make("授权完成").show();
//                    }
//
//                    @Override
//                    public void onDenied(String permission) {
//                        VMToast.make("拒绝了权限 %s", permission).showError();
//                    }
//
//                    @Override
//                    public void onGranted(String permission) {
//                        VMToast.make("同意了权限 %s", permission).show();
//                    }
//                });
    }

    /**
     * 检查权限
     */
    private void checkPermissions() {
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//        VMPermission.getInstance(activity)
//                .setTitle("权限申请")
//                .setMessage("为保证应用的正常运行，请授予一下权限")
//                .setPermissions(permissions)
//                .checkPermission(new VMPermissionCallback() {
//                    @Override
//                    public void onClose() {
//                        VMToast.make("授权被关闭").showError();
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        VMToast.make("授权完成").show();
//                    }
//
//                    @Override
//                    public void onDenied(String permission) {
//                        VMToast.make("拒绝了权限 %s", permission).showError();
//                    }
//
//                    @Override
//                    public void onGranted(String permission) {
//                        VMToast.make("同意了权限 %s", permission).show();
//                    }
//                });
    }
}
