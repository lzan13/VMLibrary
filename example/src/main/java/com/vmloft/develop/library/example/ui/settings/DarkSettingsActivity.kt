package com.vmloft.develop.library.example.ui.settings

import android.view.View
import androidx.appcompat.app.AppCompatDelegate

import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.base.BaseActivity
import com.vmloft.develop.library.example.common.SPManager

import kotlinx.android.synthetic.main.activity_settings_dark.*

/**
 * Create by lzan13 on 2020/05/02 22:56
 * 描述：主题设置
 */
@Route(path = "/VMLoft/SettingsDark")
class DarkSettingsActivity : BaseActivity() {

    override fun layoutId(): Int = R.layout.activity_settings_dark

    override fun initUI() {
        super.initUI()
        setTopTitle(R.string.settings_dark)

        darkSystemSwitchLV.setOnClickListener {
            darkSystemSwitchLV.isActivated = !darkSystemSwitchLV.isActivated
            darkManualLL.visibility = if (SPManager.instance.getDarkModeSystemSwitch()) View.VISIBLE else View.GONE

            SPManager.instance.setDarkModeSystemSwitch(darkSystemSwitchLV.isActivated)
        }

        darkManualNormalLV.setOnClickListener {
            SPManager.instance.setDarkModeManual(AppCompatDelegate.MODE_NIGHT_NO)
            darkManualNormalLV.isActivated = SPManager.instance.getDarkModeManual() == AppCompatDelegate.MODE_NIGHT_NO
            darkManualDarkLV.isActivated = SPManager.instance.getDarkModeManual() == AppCompatDelegate.MODE_NIGHT_YES
        }
        darkManualDarkLV.setOnClickListener {
            SPManager.instance.setDarkModeManual(AppCompatDelegate.MODE_NIGHT_YES)
            darkManualNormalLV.isActivated = SPManager.instance.getDarkModeManual() == AppCompatDelegate.MODE_NIGHT_NO
            darkManualDarkLV.isActivated = SPManager.instance.getDarkModeManual() == AppCompatDelegate.MODE_NIGHT_YES
        }
    }

    override fun initData() {
        // 获取开关状态
        darkSystemSwitchLV.isActivated = SPManager.instance.getDarkModeSystemSwitch()

        darkManualLL.visibility = if (SPManager.instance.getDarkModeSystemSwitch()) View.GONE else View.VISIBLE

        darkManualNormalLV.isActivated = SPManager.instance.getDarkModeManual() == AppCompatDelegate.MODE_NIGHT_NO
        darkManualDarkLV.isActivated = SPManager.instance.getDarkModeManual() == AppCompatDelegate.MODE_NIGHT_YES
    }

}