package com.vmloft.develop.library.example.ui.demo.accessibility

import android.os.Build

import com.alibaba.android.arouter.facade.annotation.Route
import com.vmloft.develop.library.common.base.BActivity

import com.vmloft.develop.library.common.utils.showBar
import com.vmloft.develop.library.common.widget.CommonDialog
import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.databinding.ActivityDemoTbLiveMsgBinding
import com.vmloft.develop.library.example.router.AppRouter
import com.vmloft.develop.library.tools.utils.VMStr
import com.vmloft.develop.library.tools.utils.VMSystem



/**
 * Create by lzan13 on 2021/8/13
 * 描述：辅助功能
 */
@Route(path = AppRouter.appAccessibility)
class TBLiveMsgActivity : BActivity<ActivityDemoTbLiveMsgBinding>() {

    override fun initVB() = ActivityDemoTbLiveMsgBinding.inflate(layoutInflater)

    override fun initUI() {
        super.initUI()
        //屏幕横滑手势
        mBinding.accessibilityMotionBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                TBLiveMsgManager.motionAction()
            } else {
                showBar("7.0及以上才能使用手势")
            }
        }
        mBinding.accessibilityClickBtn.setOnClickListener { TBLiveMsgManager.clickAction() }
        mBinding.accessibilityLongClickBtn.setOnClickListener { TBLiveMsgManager.longClickAction() }
        mBinding.accessibilityBackBtn.setOnClickListener { TBLiveMsgManager.backAction() }
        mBinding.accessibilityShowFloatBtn.setOnClickListener { showFloatView() }
        mBinding.accessibilityHideFloatBtn.setOnClickListener { hideFloatView() }

        mBinding.accessibilityTestBtn.setOnClickListener { showBar("测试按钮被点击") }
        mBinding.accessibilityTestBtn.setOnLongClickListener {
            showBar("测试按钮被长按")
            false
        }
    }

    override fun initData() {

    }

    override fun onResume() {
        super.onResume()
        // 检查是否开启了辅助功能
        if (VMSystem.checkAccessibilitySetting(mActivity, true)) {
            showFloatView()
        }
    }

    /**
     * 显示悬浮窗
     */
    fun showFloatView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!VMSystem.checkAlertSetting(mActivity)) {
                val dialog = CommonDialog(mActivity)
                dialog.setTitle(VMStr.byRes(R.string.accessibility_permission_hint))
                dialog.setPositive(listener = {
                    VMSystem.checkAlertSetting(mActivity, true)
                })
                dialog.show()
            }
        } else {
            TBLiveMsgManager.init(this)
            TBLiveMsgManager.setupFloatWindow()
        }
    }

    /**
     * 隐藏悬浮窗
     */
    private fun hideFloatView() {
        TBLiveMsgManager.removeFloatView()
    }

}