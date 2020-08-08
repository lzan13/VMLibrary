package com.vmloft.develop.library.example.ui.settings

import com.alibaba.android.arouter.facade.annotation.Route
import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.base.BaseActivity
import com.vmloft.develop.library.example.common.SPManager
import com.vmloft.develop.library.example.utils.toast
import kotlinx.android.synthetic.main.activity_settings_debug.*

/**
 * Create by lzan13 on 2020/05/02 22:56
 * 描述：调试设置
 */
@Route(path = "/VMLoft/SettingsDebug")
class DebugSettingsActivity : BaseActivity() {

    override fun layoutId(): Int = R.layout.activity_settings_debug

    override fun initUI() {
        super.initUI()
        setTopTitle(R.string.settings_debug)

        debugEnvLV.setOnClickListener {
            SPManager.instance.setDebugStatus(!SPManager.instance.getDebugStatus())
            debugEnvLV.setCaption(if (SPManager.instance.getDebugStatus()) "Debug" else "Release")
            toast("切换环境成功，重启生效")
        }

    }

    override fun initData() {
        debugEnvLV.setCaption(if (SPManager.instance.getDebugStatus()) "Debug" else "Release")
    }

}