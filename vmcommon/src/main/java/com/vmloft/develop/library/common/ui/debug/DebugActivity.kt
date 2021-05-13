package com.vmloft.develop.library.common.ui.debug

import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.library.common.R
import com.vmloft.develop.library.common.base.BaseActivity
import com.vmloft.develop.library.common.common.CSPManager
import com.vmloft.develop.library.common.router.CRouter
import com.vmloft.develop.library.common.utils.showBar
import com.vmloft.develop.library.tools.utils.VMStr

import kotlinx.android.synthetic.main.activity_debug.*

/**
 * Create by lzan13 on 2020/05/02 22:56
 * 描述：调试设置
 */
@Route(path = CRouter.commonDebug)
class DebugActivity : BaseActivity() {

    override fun layoutId(): Int = R.layout.activity_debug

    override fun initUI() {
        super.initUI()
        setTopTitle(R.string.settings_debug)

        debugEnvLV.setOnClickListener {
            CSPManager.instance.setDebug(!CSPManager.instance.isDebug())
            debugEnvLV.setCaption(VMStr.byRes(if (CSPManager.instance.isDebug()) R.string.debug_env_debug else R.string.debug_env_release))
            showBar(R.string.debug_status_change_hint)
        }

    }

    override fun initData() {
        debugEnvLV.setCaption(VMStr.byRes(if (CSPManager.instance.isDebug()) R.string.debug_env_debug else R.string.debug_env_release))
    }

}