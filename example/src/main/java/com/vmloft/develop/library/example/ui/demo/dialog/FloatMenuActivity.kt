package com.vmloft.develop.library.example.ui.demo.dialog

import android.view.MotionEvent
import android.view.View

import com.alibaba.android.arouter.facade.annotation.Route
import com.vmloft.develop.library.common.base.BActivity
import com.vmloft.develop.library.common.utils.showBar

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.databinding.ActivityDemoFloatMenuBinding
import com.vmloft.develop.library.example.router.AppRouter
import com.vmloft.develop.library.tools.utils.logger.VMLog
import com.vmloft.develop.library.tools.widget.VMFloatMenu
import com.vmloft.develop.library.tools.widget.VMFloatMenu.IItemClickListener
import com.vmloft.develop.library.tools.widget.VMFloatMenu.ItemBean

/**
 * Created by lzan13 on 2017/6/20.
 *
 * 测试悬浮菜单
 */
@Route(path = AppRouter.appFloatMenu)
class FloatMenuActivity : BActivity<ActivityDemoFloatMenuBinding>() {

    private var touchX = 0
    private var touchY = 0
    private lateinit var floatMenu: VMFloatMenu

    @Override
    override fun initVB() = ActivityDemoFloatMenuBinding.inflate(layoutInflater)

    @Override
    override fun initUI() {
        super.initUI()
        setTopTitle("自定义悬浮菜单")

        floatMenu = VMFloatMenu(mActivity)
        floatMenu.setItemClickListener(object : IItemClickListener() {
            override fun onItemClick(id: Int) {
                VMLog.d("点击了悬浮菜单 $id")
                showBar("点击了悬浮菜单 $id")
            }
        })

        mBinding.leftTopBtn.setOnTouchListener { view, event -> onTouch(view, event) }
        mBinding.leftBottomBtn.setOnTouchListener { view, event -> onTouch(view, event) }
        mBinding.centerUpBtn.setOnTouchListener { view, event -> onTouch(view, event) }
        mBinding.centerDownBtn.setOnTouchListener { view, event -> onTouch(view, event) }
        mBinding.rightTopBtn.setOnTouchListener { view, event -> onTouch(view, event) }
        mBinding.rightBottomBtn.setOnTouchListener { view, event -> onTouch(view, event) }

        mBinding.leftTopBtn.setOnClickListener { view -> onClick(view) }
        mBinding.leftBottomBtn.setOnClickListener { view -> onClick(view) }
        mBinding.centerUpBtn.setOnClickListener { view -> onClick(view) }
        mBinding.centerDownBtn.setOnClickListener { view -> onClick(view) }
        mBinding.rightTopBtn.setOnClickListener { view -> onClick(view) }
        mBinding.rightBottomBtn.setOnClickListener { view -> onClick(view) }
    }

    @Override
    override fun initData() {
    }

    private fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            touchX = event.rawX.toInt()
            touchY = event.rawY.toInt()
        }
        return false
    }

    private fun onClick(view: View) {
        when (view.id) {
            R.id.leftTopBtn, R.id.leftBottomBtn, R.id.centerUpBtn, R.id.centerDownBtn, R.id.rightTopBtn, R.id.rightBottomBtn -> showFloatMenu(view)
        }
    }

    /**
     * 测试弹出 PopupWindow 菜单
     */
    private fun showFloatMenu(view: View) {
        floatMenu.clearAllItem()
        floatMenu.addItem(ItemBean(1, "悬浮菜单"))
        floatMenu.addItem(ItemBean(2, "悬浮"))
        floatMenu.addItem(ItemBean(3, "悬浮菜单", R.color.vm_red))
        floatMenu.addItem(ItemBean(4, "悬浮菜单", R.color.vm_accent, R.drawable.ic_close))
        floatMenu.addItem(ItemBean(5, "悬浮菜单"))
        floatMenu.addItem(ItemBean(6, "菜单"))
        floatMenu.addItem(ItemBean(7, "悬浮菜单"))
        floatMenu.addItem(ItemBean(8, "浮菜单"))
        floatMenu.showAtLocation(view, touchX, touchY)
    }
}