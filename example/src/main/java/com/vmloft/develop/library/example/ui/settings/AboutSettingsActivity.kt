package com.vmloft.develop.library.example.ui.settings

import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.base.BaseActivity
import com.vmloft.develop.library.example.common.Constants
import com.vmloft.develop.library.example.router.AppRouter
import com.vmloft.develop.library.example.utils.toast
import com.vmloft.develop.library.tools.utils.VMSystem

import kotlinx.android.synthetic.main.activity_settings_about.*

/**
 * Create by lzan13 on 2020/05/02 22:56
 * 描述：关于
 */
@Route(path = "/VMLoft/SettingsAbout")
class AboutSettingsActivity : BaseActivity() {

    private val maxCount = 7
    private val maxInterval = Constants.timeSecond
    private var mCount: Int = 0
    private var mTime: Long = 0L

    override fun layoutId(): Int = R.layout.activity_settings_about

    override fun initUI() {
        super.initUI()

        aboutIconIV.setOnClickListener { goDebug() }
        // 检查更新
        aboutCheckVersionLV.setOnClickListener { toast("已经是最新版本") }

        aboutFeedbackLV.setOnClickListener { AppRouter.goFeedback() }

        aboutPrivacyPolicyTV.setOnClickListener { AppRouter.goWeb(Constants.policyUrl) }
    }

    override fun initData() {
        aboutVersionTV.text = "Version ${VMSystem.versionName}"
    }

    /**
     * 打开调试界面
     */
    private fun goDebug() {
        val interval = System.currentTimeMillis() - mTime
        if (interval > maxInterval) {
            mCount = 0
        }
        if (mCount < maxCount) {
            mCount++
        } else {
            AppRouter.goSettingsDebug()
        }
        mTime = System.currentTimeMillis()
    }


}