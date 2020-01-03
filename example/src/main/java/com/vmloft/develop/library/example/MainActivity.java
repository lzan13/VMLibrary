package com.vmloft.develop.library.example;

import android.Manifest;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;

import com.vmloft.develop.library.example.common.AppActivity;
import com.vmloft.develop.library.example.router.ARouter;
import com.vmloft.develop.library.tools.permission.VMPermission;
import com.vmloft.develop.library.tools.permission.VMPermissionBean;
import com.vmloft.develop.library.tools.utils.VMLog;
import com.vmloft.develop.library.tools.widget.VMTopBar;
import com.vmloft.develop.library.tools.widget.VMViewGroup;

import com.vmloft.develop.library.tools.widget.toast.VMToast;

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
        setTopTitle("工具库");
        setTopSubtitle("这个是我的工具类库入口");
        setTopIcon(0);
        getTopBar().setEndBtnListener("测试", v -> {
            VMToast.make(mActivity, "测试自定义 VMTopBar 右侧按钮样式");
        });
        getTopBar().setEndBtnBackground(R.drawable.rectangle_red_shape_bg);

        String[] btnArray = {
                "工具", "按钮样式", "描点控件", "详情控件", "自定义控件", "录制屏幕", "声音播放", "悬浮菜单", "Web 功能", "指示器", "识别验证码", "图片选择器", "单权限申请", "多权限申请", "线程测试","Scheme"
        };
        for (int i = 0; i < btnArray.length; i++) {
            Button btn = new Button(new ContextThemeWrapper(mActivity, R.style.VMBtn_Flat));
            btn.setText(btnArray[i]);
            btn.setId(CLICK_START_ID + i);
            btn.setOnClickListener(viewListener);
            viewGroup.addView(btn);
        }
    }

    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case CLICK_START_ID + 0:
                    ARouter.goTools(mActivity);
                    break;
                case CLICK_START_ID + 1:
                    ARouter.goBtnStyle(mActivity);
                    break;
                case CLICK_START_ID + 2:
                    ARouter.goDotLine(mActivity);
                    break;
                case CLICK_START_ID + 3:
                    ARouter.goDetails(mActivity);
                    break;
                case CLICK_START_ID + 4:
                    ARouter.goCustomView(mActivity);
                    break;
                case CLICK_START_ID + 5:
                    ARouter.goRecordScreen(mActivity);
                    break;
                case CLICK_START_ID + 6:
                    ARouter.goPlayAudio(mActivity);
                    break;
                case CLICK_START_ID + 7:
                    ARouter.goPWDialog(mActivity);
                    break;
                case CLICK_START_ID + 8:
                    ARouter.goWeb(mActivity);
                    break;
                case CLICK_START_ID + 9:
                    ARouter.goIndicator(mActivity);
                    break;
                case CLICK_START_ID + 10:
                    ARouter.goImageDiscern(mActivity);
                    break;
                case CLICK_START_ID + 11:
                    ARouter.goPicker(mActivity);
                    break;
                case CLICK_START_ID + 12:
                    checkOnePermissions();
                    break;
                case CLICK_START_ID + 13:
                    checkPermissions();
                    break;
                case CLICK_START_ID + 14:
                    ARouter.goThread(mActivity);
                    break;
                case CLICK_START_ID + 15:
                    ARouter.goScheme(mActivity);
                    break;
                default:
                    ARouter.goTools(mActivity);
                    break;
            }
        }
    };

    /**
     * 重写返回按键会退到桌面，并不结束 app
     */
    @Override
    public void onBackPressed() {
        ARouter.goLauncher(mActivity);
    }

    /**
     * 检查权限
     */
    private void checkOnePermissions() {
        VMPermissionBean bean = new VMPermissionBean(Manifest.permission.CAMERA, R.mipmap.ic_launcher_round, "访问相机",
                "扫描二维码需要使用到相机，请允许我们获取拍照权限");
        VMPermission.getInstance(mActivity)
                .setEnableDialog(true)
                .setTitle("权限申请")
                .setMessage("为保证应用的正常运行，请授予一下权限")
                .setPermission(bean)
                .requestPermission(new VMPermission.PCallback() {
                    @Override
                    public void onReject() {
                        VMToast.make(mActivity, "权限申请被拒绝").error();
                    }

                    @Override
                    public void onComplete() {
                        VMToast.make(mActivity, "权限申请完成").done();
                    }
                });
    }

    /**
     * 检查权限
     */
    private void checkPermissions() {
        List<VMPermissionBean> permissions = new ArrayList<>();
        permissions.add(new VMPermissionBean(Manifest.permission.CAMERA, R.mipmap.ic_launcher_round, "访问相机", "拍摄图片需要使用到相机，请允许我们获取访问相机"));
        permissions.add(new VMPermissionBean(Manifest.permission.READ_EXTERNAL_STORAGE, R.mipmap.ic_launcher_round, "读写存储",
                "我们需要将文件保存到你的设备，请允许我们获取存储权限"));
        permissions.add(new VMPermissionBean(Manifest.permission.RECORD_AUDIO, R.mipmap.ic_launcher_round, "访问麦克风",
                "发送语音消息需要录制声音，请允许我们访问设备麦克风"));
        VMPermission.getInstance(mActivity)
                .setEnableDialog(true)
                .setTitle("权限申请")
                .setMessage("为保证应用的正常运行，请您授予以下权限")
                .setPermissionList(permissions)
                .requestPermission(new VMPermission.PCallback() {
                    @Override
                    public void onReject() {
                        VMToast.make(mActivity, "权限申请被拒绝").error();
                    }

                    @Override
                    public void onComplete() {
                        VMToast.make(mActivity, "权限申请完成").done();
                    }
                });
    }
}
