package com.vmloft.develop.library.example.ui.main.home

import android.Manifest
import android.view.LayoutInflater
import android.view.View
import android.widget.Button

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.common.base.BaseFragment
import com.vmloft.develop.library.example.router.AppRouter
import com.vmloft.develop.library.example.utils.showBar
import com.vmloft.develop.library.example.utils.errorBar
import com.vmloft.develop.library.tools.permission.VMPermission
import com.vmloft.develop.library.tools.permission.VMPermissionBean
import com.vmloft.develop.library.tools.utils.logger.VMLog

import kotlinx.android.synthetic.main.fragment_home.*


/**
 * Create by lzan13 on 2020/05/02 11:54
 * 描述：首页
 */
class HomeFragment : BaseFragment() {
    override var centerTitle: Boolean = false

    override fun layoutId() = R.layout.fragment_home

    override fun initUI() {
        super.initUI()
        setTopTitle(R.string.nav_home)
        setTopSubtitle("这个是我的工具类库入口")

        addBtn("弹幕", AppRouter.appBarrage)
        addBtn("自定义控件", AppRouter.appCustomView)
        addBtn("悬浮菜单", AppRouter.appFloatMenu)
        addBtn("指示器", AppRouter.appIndicator)
        addBtn("Loading", AppRouter.appLoading)
        addBtn("Lottie 动画", AppRouter.appLottieAnim)
        addBtn("媒体播放", AppRouter.appMediaPlay)
        addBtn("Scheme", AppRouter.appScheme)
        addBtn("按钮", AppRouter.appStyle)
        addBtn("线程测试", AppRouter.appThread)
        addBtn("Web 功能", AppRouter.appWebTest)
        addBtn("图片选择", AppRouter.appImagePicker)
        addBtn("表格", AppRouter.appCustomTable)

        addBtn("单权限申请", AppRouter.appSinglePermission)
        addBtn("多权限申请", AppRouter.appMultiPermission)

    }

    /**
     * 添加一个按钮
     */
    private fun addBtn(title: String, tag: String) {
        val inflater = LayoutInflater.from(activity)
        val btn = inflater.inflate(R.layout.item_button, null, false) as Button
        btn.text = title
        btn.id = tag.hashCode()
        btn.tag = tag
        btn.setOnClickListener(viewListener)
        viewGroup.addView(btn)
    }

    override fun initData() {
    }

    private val viewListener = View.OnClickListener { v: View ->

        when (v.id) {
            AppRouter.appSinglePermission.hashCode() -> checkOnePermissions()
            AppRouter.appMultiPermission.hashCode() -> checkPermissions()
            else -> AppRouter.go(v.tag as String)
        }
    }

    /**
     * 检查权限
     */
    private fun checkOnePermissions() {
        val bean = VMPermissionBean(Manifest.permission.CAMERA, "访问相机", "扫描二维码需要使用到相机，请允许我们获取拍照权限", R.drawable.ic_camera)
        VMPermission.getInstance(activity)
            .setEnableDialog(true)
//                .setTitle("权限申请")
//                .setMessage("为了带跟你更好的使用体验，应用需要以下权限")
            .setPermission(bean)
            .requestPermission(object : VMPermission.PCallback {
                override fun onReject() {
                    errorBar("权限申请被拒绝")
                }

                override fun onComplete() {
                    showBar("权限申请完成")
                }
            })
    }

    /**
     * 检查权限
     */
    private fun checkPermissions() {
        val permissions: MutableList<VMPermissionBean> = ArrayList()
        permissions.add(VMPermissionBean(Manifest.permission.CAMERA, "访问相机", "拍摄图片需要使用到相机，请允许我们获取访问相机", R.drawable.ic_camera))
        permissions.add(VMPermissionBean(Manifest.permission.READ_EXTERNAL_STORAGE, "读写存储", "我们需要将文件保存到你的设备，请允许我们获取存储权限", R.drawable.ic_explore))
        permissions.add(VMPermissionBean(Manifest.permission.RECORD_AUDIO, "访问麦克风", "发送语音消息需要录制声音，请允许我们访问设备麦克风", R.drawable.ic_video))
        VMPermission.getInstance(activity)
            .setEnableDialog(true)
//                .setTitle("权限申请")
            .setMessage("为了带跟你更好的使用体验，应用需要以下权限")
            .setPermissionList(permissions)
            .requestPermission(object : VMPermission.PCallback {
                override fun onReject() {
                    errorBar("权限申请被拒绝")
                }

                override fun onComplete() {
                    showBar("权限申请完成")
                }
            })
    }
}