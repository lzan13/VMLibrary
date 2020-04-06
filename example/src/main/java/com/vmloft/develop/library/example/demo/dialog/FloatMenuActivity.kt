package com.vmloft.develop.library.example.demo.dialog

import android.view.MotionEvent
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.library.example.R.color
import com.vmloft.develop.library.example.R.id
import com.vmloft.develop.library.example.R.layout
import com.vmloft.develop.library.example.base.AppActivity
import com.vmloft.develop.library.tools.utils.logger.VMLog
import com.vmloft.develop.library.tools.widget.VMFloatMenu
import com.vmloft.develop.library.tools.widget.VMFloatMenu.ItemBean
import com.vmloft.develop.library.tools.widget.toast.VMToast

import kotlinx.android.synthetic.main.activity_float_menu.centerBtn
import kotlinx.android.synthetic.main.activity_float_menu.leftBottomBtn
import kotlinx.android.synthetic.main.activity_float_menu.leftTopBtn
import kotlinx.android.synthetic.main.activity_float_menu.rightBottomBtn
import kotlinx.android.synthetic.main.activity_float_menu.rightTopBtn

/**
 * Created by lzan13 on 2017/6/20.
 *
 * 测试悬浮菜单
 */
@Route(path = "/VMLoft/FloatMenu")
class FloatMenuActivity : AppActivity() {

    private var touchX = 0
    private var touchY = 0
    private var mFloatMenu: VMFloatMenu? = null

    @Override
    public override fun layoutId(): Int {
        return layout.activity_float_menu
    }

    @Override
    public override fun initUI() {
        super.initUI()
        mFloatMenu = VMFloatMenu(mActivity)
        mFloatMenu!!.setItemClickListener { id: Int ->
            VMLog.d("点击了悬浮菜单 $id")
            VMToast.make(mActivity, "点击了悬浮菜单 $id").done()
        }
        leftTopBtn.setOnTouchListener { view, event -> onTouch(view, event) }
        leftBottomBtn.setOnTouchListener { view, event -> onTouch(view, event) }
        centerBtn.setOnTouchListener { view, event -> onTouch(view, event) }
        rightTopBtn.setOnTouchListener { view, event -> onTouch(view, event) }
        rightBottomBtn.setOnTouchListener { view, event -> onTouch(view, event) }

        leftTopBtn.setOnClickListener { view -> onClick(view) }
        leftBottomBtn.setOnClickListener { view -> onClick(view) }
        centerBtn.setOnClickListener { view -> onClick(view) }
        rightTopBtn.setOnClickListener { view -> onClick(view) }
        rightBottomBtn.setOnClickListener { view -> onClick(view) }
    }

    @Override
    public override fun initData() {
    }

    fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            touchX = event.rawX.toInt()
            touchY = event.rawY.toInt()
        }
        return false
    }

    fun onClick(view: View) {
        when (view.id) {
            id.leftTopBtn, id.leftBottomBtn, id.centerBtn, id.rightTopBtn, id.rightBottomBtn -> showFloatMenu(view)
        }
    }

    /**
     * 测试弹出 PopupWindow 菜单
     */
    private fun showFloatMenu(view: View) {
        mFloatMenu!!.clearAllItem()
        mFloatMenu!!.addItem(ItemBean(1, "悬浮菜单"))
        mFloatMenu!!.addItem(ItemBean(2, "悬浮"))
        mFloatMenu!!.addItem(ItemBean(3, "悬浮菜单", color.vm_red))
        mFloatMenu!!.addItem(ItemBean(4, "悬浮菜单菜单"))
        mFloatMenu!!.addItem(ItemBean(5, "悬浮菜单"))
        mFloatMenu!!.addItem(ItemBean(6, "菜单"))
        mFloatMenu!!.addItem(ItemBean(7, "悬浮菜单"))
        mFloatMenu!!.addItem(ItemBean(8, "浮菜单"))
        mFloatMenu!!.showAtLocation(view, touchX, touchY)
    }
}