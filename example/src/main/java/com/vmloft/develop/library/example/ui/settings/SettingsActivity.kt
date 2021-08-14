package com.vmloft.develop.library.example.ui.settings

import android.content.Intent
import android.os.Build
import android.provider.Settings
import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.library.common.base.BaseActivity
import com.vmloft.develop.library.common.notify.NotifyManager
import com.vmloft.develop.library.common.router.CRouter
import com.vmloft.develop.library.common.widget.CommonDialog
import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.router.AppRouter
import com.vmloft.develop.library.tools.utils.VMStr
import com.vmloft.develop.library.tools.utils.logger.VMLog

import kotlinx.android.synthetic.main.activity_settings.*


/**
 * Create by lzan13 on 2020/06/07 21:06
 * 描述：设置
 */
@Route(path = AppRouter.appSettings)
class SettingsActivity : BaseActivity() {

    override fun layoutId(): Int = R.layout.activity_settings

    override fun initUI() {
        super.initUI()
        setTopTitle(R.string.settings)

        settingsAccessibilityLV.setOnClickListener { openAccessibilitySetting() }

        settingsDarkLV.setOnClickListener { CRouter.go(AppRouter.appSettingsDark) }
        settingsNotifyLV.setOnClickListener { CRouter.go(AppRouter.appSettingsNotify) }
        settingsPictureLV.setOnClickListener { CRouter.go(AppRouter.appSettingsMedia) }
        settingsAboutLV.setOnClickListener { CRouter.go(AppRouter.appSettingsAbout) }

        settingsSignOutLV.setOnClickListener { showSignOut() }
    }

    override fun initData() {
    }

    /**
     * 打开辅助设置
     */
    private fun openAccessibilitySetting() {
//        CRouter.go(AppRouter.appAccessibility)
        val intent = Intent()
        try {
            intent.action = Settings.ACTION_ACCESSIBILITY_SETTINGS
        } catch (e: Exception) {
            VMLog.e(e.message ?: "打开设置出错")
            // 其他低版本或者异常情况，走该节点。进入APP设置界面
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.putExtra("package", packageName)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    /**
     * 退出登录二次弹窗
     */
    private fun showSignOut() {
        mDialog = CommonDialog(this)
        (mDialog as CommonDialog).let { dialog ->
            dialog.setTitle(VMStr.byRes(R.string.sign_out_hint))
            dialog.setPositive(listener = {
                // 清空登录信息统一交给 Main 界面处理
                CRouter.goMain(1)
            })
            dialog.show()
        }
    }

}