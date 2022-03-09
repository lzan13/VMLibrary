package com.vmloft.develop.library.example.ui.main.home

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.content.ComponentName
import android.view.ViewGroup

import com.vmloft.develop.library.common.base.BFragment
import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.databinding.FragmentHomeBinding
import com.vmloft.develop.library.example.router.AppRouter
import com.vmloft.develop.library.example.utils.showBar
import com.vmloft.develop.library.example.utils.errorBar
import com.vmloft.develop.library.tools.VMTools
import com.vmloft.develop.library.tools.permission.VMPermission
import com.vmloft.develop.library.tools.permission.VMPermissionBean


/**
 * Create by lzan13 on 2020/05/02 11:54
 * 描述：首页
 */
class HomeFragment : BFragment<FragmentHomeBinding>() {

    override fun initVB(inflater: LayoutInflater, parent: ViewGroup?) = FragmentHomeBinding.inflate(inflater, parent, false)

    override fun initUI() {
        super.initUI()
        setTopTitle(R.string.nav_home)
        setTopSubtitle("这个是我的工具类库入口")

        addBtn("弹幕", AppRouter.appBarrage)
        addBtn("自定义控件", AppRouter.appCustomView)
        addBtn("绘图", AppRouter.appCustomDrawView)
        addBtn("悬浮菜单", AppRouter.appFloatMenu)
        addBtn("指示器", AppRouter.appIndicator)
        addBtn("Loading", AppRouter.appLoading)
        addBtn("Lottie 动画", AppRouter.appLottieAnim)
        addBtn("按钮", AppRouter.appStyle)
        addBtn("图片选择", AppRouter.appImagePicker)
        addBtn("辅助功能", AppRouter.appAccessibility)

        addBtn("单权限申请", AppRouter.appSinglePermission)
        addBtn("多权限申请", AppRouter.appMultiPermission)
        addBtn("打开微信", AppRouter.appOpenWeChat)

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
        mBinding.viewGroup.addView(btn)
    }

    override fun initData() {
    }

    private val viewListener = View.OnClickListener { v: View ->

        when (v.id) {
            AppRouter.appSinglePermission.hashCode() -> checkOnePermissions()
            AppRouter.appMultiPermission.hashCode() -> checkPermissions()
            AppRouter.appOpenWeChat.hashCode() -> openWeChat()
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
//            .setMessage("为了带跟你更好的使用体验，应用需要以下权限")
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

    private fun openAlipay() {
        /**
         * {
         *      "wxCode":"http://zyphoto.itluntan.cn/202108091681857",
         *      "zfbCode":"http://zyphoto.itluntan.cn/20210809161957",
         *      "zfbScheme":"alipayqr://platformapi/startapp?saId=10000007&qrcode=https%3A%2F%2Fqr.alipay.com%2Ftsx14575pzorxigphwtdj84",
         *      "afbPayUrl":"https://admin.zha nzhangfu.com/common/zfbuserid?zfbuserid=2088602250620913&price=1.00"
         * }
         *  微信扫一扫
         *  weixin://scanqrcode
         *  （跳转微信扫一扫）

         *  支付宝扫一扫
         *  alipays://platformapi/startapp?saId=10000007
         *  （跳转支付宝扫一扫）
         */
//        val uri = Uri.parse("weixin://dl/stickers")
        val uri = Uri.parse("alipayqr://platformapi/startapp?saId=10000007&qrcode=https%3a%2f%2fqr.alipay.com%2ftsx140593gavtpepmajwj09")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        VMTools.context.startActivity(intent)
    }

//    private const val WECHAT_APP_PACKAGE = "com.tencent.mm"
//    private const val WECHAT_LAUNCHER_UI_CLASS = "com.tencent.mm.ui.LauncherUI"
//    private const val WECHAT_OPEN_SCANER_NAME = "LauncherUI.From.Scaner.Shortcut"

    private fun openWeChat() {
        try {
            val intent = Intent()
            intent.component = ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
            intent.putExtra("LauncherUI.From.Scaner.Shortcut", true)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.action = "android.intent.action.VIEW"
            VMTools.context.startActivity(intent)
        } catch (e: Exception) {

        }
    }
}