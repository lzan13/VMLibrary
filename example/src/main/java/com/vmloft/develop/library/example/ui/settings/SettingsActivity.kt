package com.vmloft.develop.library.example.ui.settings

import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.base.BaseActivity
import com.vmloft.develop.library.example.router.AppRouter

import kotlinx.android.synthetic.main.activity_settings.*


/**
 * Create by lzan13 on 2020/06/07 21:06
 * 描述：设置
 */
@Route(path = "/VMLoft/Settings")
class SettingsActivity : BaseActivity() {

    override fun layoutId(): Int = R.layout.activity_settings

    override fun initUI() {
        super.initUI()
        setTopTitle(R.string.settings)

        settingsDarkLV.setOnClickListener { AppRouter.goSettingsDark() }
        settingsNotifyLV.setOnClickListener { AppRouter.goSettingsNotify() }
        settingsPictureLV.setOnClickListener {AppRouter.goSettingsPicture() }
        settingsAboutLV.setOnClickListener { AppRouter.goSettingsAbout() }
    }

    override fun initData() {
    }

}