package com.vmloft.develop.library.example

import android.Manifest.permission
import android.view.ContextThemeWrapper
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button

import com.alibaba.android.arouter.launcher.ARouter

import com.vmloft.develop.library.example.base.AppActivity
import com.vmloft.develop.library.tools.permission.VMPermission
import com.vmloft.develop.library.tools.permission.VMPermission.PCallback
import com.vmloft.develop.library.tools.permission.VMPermissionBean
import com.vmloft.develop.library.tools.utils.logger.VMLog
import com.vmloft.develop.library.tools.widget.toast.VMToast

import java.util.ArrayList

import kotlinx.android.synthetic.main.activity_main.viewGroup

/**
 * Create by lzan13 on 2018/4/13
 */
class MainActivity : AppActivity() {
    // 定义动态添加的控件 Id
    private val btnStyleBtnId = 0x100;
    private val customViewBtnId = 0x102;
    private val playAudioBtnId = 0x103;
    private val floatMenuBtnId = 0x104;
    private val webBtnId = 0x105;
    private val indicatorBtnId = 0x106;
    private val threadBtnId = 0x108;
    private val schemeBtnId = 0x109;

    private val singlePermissionBtnId = 0x200;
    private val multiPermissionBtnId = 0x201;

    override fun layoutId(): Int {
        // 修改界面主题
        setTheme(R.style.AppTheme)
        return R.layout.activity_main
    }

    override fun initUI() {
        super.initUI()
        setTopTitle("工具库")
        setTopSubtitle("这个是我的工具类库入口")
        setTopIcon(0)
        topBar?.setEndBtnListener("测试", OnClickListener {
            VMToast.make(mActivity, "测试自定义 VMTopBar 右侧按钮样式").show()
            VMLog.json("{'app':'VMLibrary', 'version':'1.0.0', 'tag':['tools','kotlin','android']}")
        })

        addBtn("按钮样式", btnStyleBtnId)
        addBtn("自定义控件", customViewBtnId)
        addBtn("声音播放", playAudioBtnId)
        addBtn("悬浮菜单", floatMenuBtnId)
        addBtn("Web 功能", webBtnId)
        addBtn("指示器", indicatorBtnId)
        addBtn("线程测试", threadBtnId)
        addBtn("Scheme", schemeBtnId)

        addBtn("单权限申请", singlePermissionBtnId)
        addBtn("多权限申请", multiPermissionBtnId)
    }

    /**
     * 添加一个按钮
     */
    private fun addBtn(title: String, id: Int) {
        val btn = Button(ContextThemeWrapper(mActivity, R.style.VMBtn_Flat))
        btn.text = title
        btn.id = id
        btn.setOnClickListener(viewListener)
        viewGroup.addView(btn)
    }

    override fun initData() {}
    private val viewListener = OnClickListener { v: View ->
        when (v.id) {
            btnStyleBtnId -> ARouter.getInstance().build("/VMLoft/BtnStyle").navigation()
            customViewBtnId -> ARouter.getInstance().build("/VMLoft/CustomView").navigation()
            playAudioBtnId -> ARouter.getInstance().build("/VMLoft/MediaPlay").navigation()
            floatMenuBtnId -> ARouter.getInstance().build("/VMLoft/FloatMenu").navigation()
            webBtnId -> ARouter.getInstance().build("/VMLoft/Web").navigation()
            indicatorBtnId -> ARouter.getInstance().build("/VMLoft/Indicator").navigation()
            threadBtnId -> ARouter.getInstance().build("/VMLoft/Thread").navigation()
            schemeBtnId -> ARouter.getInstance().build("/VMLoft/Scheme").navigation()

            singlePermissionBtnId -> checkOnePermissions()
            multiPermissionBtnId -> checkPermissions()
        }
    }

    /**
     * 检查权限
     */
    private fun checkOnePermissions() {
        val bean = VMPermissionBean(permission.CAMERA, "访问相机", "扫描二维码需要使用到相机，请允许我们获取拍照权限", R.mipmap.ic_launcher_round)
        VMPermission.getInstance(mActivity)
            .setEnableDialog(true)
            .setTitle("权限申请")
            .setMessage("为保证应用的正常运行，请授予一下权限")
            .setPermission(bean)
            .requestPermission(object : PCallback {
                override fun onReject() {
                    VMToast.make(mActivity, "权限申请被拒绝").error()
                }

                override fun onComplete() {
                    VMToast.make(mActivity, "权限申请完成").done()
                }
            })
    }

    /**
     * 检查权限
     */
    private fun checkPermissions() {
        val permissions: MutableList<VMPermissionBean> = ArrayList()
        permissions.add(VMPermissionBean(permission.CAMERA, "访问相机", "拍摄图片需要使用到相机，请允许我们获取访问相机", R.mipmap.ic_launcher_round))
        permissions.add(VMPermissionBean(permission.READ_EXTERNAL_STORAGE, "读写存储", "我们需要将文件保存到你的设备，请允许我们获取存储权限", R.mipmap.ic_launcher_round))
        permissions.add(VMPermissionBean(permission.RECORD_AUDIO, "访问麦克风", "发送语音消息需要录制声音，请允许我们访问设备麦克风", R.mipmap.ic_launcher_round))
        VMPermission.getInstance(mActivity)
            .setEnableDialog(true)
            .setTitle("权限申请")
            .setMessage("为保证应用的正常运行，请您授予以下权限")
            .setPermissionList(permissions)
            .requestPermission(object : PCallback {
                override fun onReject() {
                    VMToast.make(mActivity, "权限申请被拒绝").error()
                }

                override fun onComplete() {
                    VMToast.make(mActivity, "权限申请完成").done()
                }
            })
    }
}