package com.vmloft.develop.app.example.ui.splash

import com.vmloft.develop.library.base.BActivity
import com.vmloft.develop.library.base.router.CRouter
import com.vmloft.develop.app.example.common.SPManager
import com.vmloft.develop.app.example.router.AppRouter
import com.vmloft.develop.app.example.ui.widget.AgreementPolicyDialog
import com.vmloft.develop.app.example.databinding.ActivitySplashBinding

/**
 * Create by lzan13 2021/5/17
 * 描述：闪屏页，做承接调整用
 */
class SplashActivity : BActivity<ActivitySplashBinding>() {

    override fun initVB() = ActivitySplashBinding.inflate(layoutInflater)

    override fun initUI() {
        super.initUI()
        if (SPManager.isAgreementPolicy()) {
            jump()
        } else {
            showAgreementPolicy()
        }
    }

    override fun initData() {

    }

    private fun jump() {
        if (SPManager.isGuideShow()) {
            CRouter.go(AppRouter.appGuide)
//        } else if (!SignManager.isSingIn()) {
//            CRouter.go(AppRouter.appSignGuide)
        } else {
            CRouter.goMain()
        }
        finish()
    }

    private fun showAgreementPolicy() {
        mDialog = AgreementPolicyDialog(this)
        (mDialog as AgreementPolicyDialog).let { dialog ->
            dialog.setNegative("不同意") {
                finish()
            }
            dialog.setPositive("同意") {
                SPManager.setAgreementPolicy()
                jump()
            }
            dialog.show()
        }
    }
}