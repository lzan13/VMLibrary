package com.vmloft.develop.app.example.ui.demo.dialog

import android.view.Gravity
import android.view.MotionEvent
import android.view.View

import com.didi.drouter.annotation.Router
import com.vmloft.develop.library.base.BActivity
import com.vmloft.develop.library.base.utils.showBar

import com.vmloft.develop.app.example.R
import com.vmloft.develop.app.example.databinding.ActivityDemoFloatMenuBinding
import com.vmloft.develop.app.example.router.AppRouter
import com.vmloft.develop.app.example.ui.widget.CustomGravityDialog
import com.vmloft.develop.library.tools.utils.logger.VMLog
import com.vmloft.develop.library.tools.widget.VMFloatMenu
import com.vmloft.develop.library.tools.widget.VMFloatMenu.IItemClickListener
import com.vmloft.develop.library.tools.widget.VMFloatMenu.ItemBean

/**
 * Created by lzan13 on 2017/6/20.
 *
 * 测试悬浮菜单
 */
@Router(path = AppRouter.appFloatMenu)
class FloatMenuActivity : BActivity<ActivityDemoFloatMenuBinding>() {

    private var touchX = 0
    private var touchY = 0
    private lateinit var floatMenu: VMFloatMenu
    private lateinit var customFloatMenu: VMFloatMenu

    @Override
    override fun initVB() = ActivityDemoFloatMenuBinding.inflate(layoutInflater)

    @Override
    override fun initUI() {
        super.initUI()
        setTopTitle("自定义悬浮菜单")

        initFloatMenu()

        binding.leftTopBtn.setOnTouchListener { view, event -> onTouch(view, event) }
        binding.leftBottomBtn.setOnTouchListener { view, event -> onTouch(view, event) }
        binding.centerUpBtn.setOnTouchListener { view, event -> onTouch(view, event) }
        binding.centerBtn.setOnTouchListener { view, event -> onTouch(view, event) }
        binding.centerDownBtn.setOnTouchListener { view, event -> onTouch(view, event) }
        binding.rightTopBtn.setOnTouchListener { view, event -> onTouch(view, event) }
        binding.rightBottomBtn.setOnTouchListener { view, event -> onTouch(view, event) }

        binding.leftBottom.setOnTouchListener { view, event -> onTouch(view, event) }
        binding.leftTop.setOnTouchListener { view, event -> onTouch(view, event) }
        binding.rightBottom.setOnTouchListener { view, event -> onTouch(view, event) }
        binding.rightTop.setOnTouchListener { view, event -> onTouch(view, event) }

        binding.leftTopBtn.setOnClickListener { view -> onClick(view) }
        binding.leftBtn1.setOnClickListener { view -> onClick(view) }
        binding.leftBtn2.setOnClickListener { view -> onClick(view) }

        binding.leftBottom.setOnClickListener { view -> onClick(view)  }
        binding.leftTop.setOnClickListener { view -> onClick(view)  }
        binding.rightBottom.setOnClickListener { view -> onClick(view)  }
        binding.rightTop.setOnClickListener { view -> onClick(view)  }

        binding.leftBottomBtn.setOnClickListener { view -> onClick(view) }

        binding.centerUpBtn.setOnClickListener { view -> onClick(view) }
        binding.centerBtn.setOnClickListener { view -> onClick(view) }
        binding.centerDownBtn.setOnClickListener { view -> onClick(view) }

        binding.rightTopBtn.setOnClickListener { view -> onClick(view) }

        binding.rightBottomBtn.setOnClickListener { view -> onClick(view) }
    }


    @Override
    override fun initData() {
    }

    private fun initFloatMenu() {
        customFloatMenu = VMFloatMenu(mActivity)

        floatMenu = VMFloatMenu(mActivity)

        floatMenu.addItem(ItemBean(1, "悬浮菜单"))
        floatMenu.addItem(ItemBean(2, "悬浮"))
        floatMenu.addItem(ItemBean(3, "悬浮菜单", com.vmloft.develop.library.tools.R.color.vm_red))
        floatMenu.addItem(ItemBean(4, "悬浮菜单", com.vmloft.develop.library.base.R.color.app_accent, com.vmloft.develop.library.common.R.drawable.ic_close))
//        floatMenu.addItem(ItemBean(5, "悬浮菜单"))
//        floatMenu.addItem(ItemBean(6, "菜单"))
//        floatMenu.addItem(ItemBean(7, "悬浮菜单"))
//        floatMenu.addItem(ItemBean(8, "浮菜单"))

        floatMenu.setItemClickListener(object : IItemClickListener() {
            override fun onItemClick(id: Int) {
                VMLog.d("点击了悬浮菜单 $id")
                showBar("点击了悬浮菜单 $id")
            }
        })
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
            R.id.centerBtn -> showCustomFloatMenu(view)
            R.id.leftBtn1 -> showTopDialog(view)
            R.id.leftBtn2 -> showBottomDialog(view)
            R.id.leftBottom->showLeftBottom(view)
            R.id.leftTop->showLeftTop(view)
            R.id.rightBottom->showRightBottom(view)
            R.id.rightTop->showRightTop(view)
        }
    }

    /**
     * 测试弹出 PopupWindow 菜单
     */
    private fun showFloatMenu(view: View) {
        floatMenu.setCustomBackgroundShadow(0.3f)
        floatMenu.showAtLocation(view, touchX, touchY)
    }

    private fun showLeftBottom(view:View){
        floatMenu.setCustomBackgroundShadow(0.4f)
        floatMenu.showAtLeftBottom(view, touchX, touchY)
    }
    private fun showLeftTop(view:View){
        floatMenu.setCustomBackgroundShadow(0.6f)
        floatMenu.showAtLeftTop(view, touchX, touchY)
    }
    private fun showRightBottom(view:View){
        floatMenu.setCustomBackgroundShadow(0.8f)
        floatMenu.showAtRightBottom(view, touchX, touchY)
    }
    private fun showRightTop(view:View){
        floatMenu.setCustomBackgroundShadow(1.0f)
        floatMenu.showAtRightTop(view, touchX, touchY)
    }

    /**
     * 测试弹出自定义 PopupWindow 菜单
     */
    private fun showCustomFloatMenu(view: View) {
        customFloatMenu.setCustomView(R.layout.widget_custom_gravity_dialog)
        customFloatMenu.showAtLocation(view, view.x.toInt(), view.y.toInt())
    }

    /**
     * 测试弹出自定义底部dialog
     */
    private fun showTopDialog(view: View) {
        val dialog = CustomGravityDialog(this)
        dialog.show(Gravity.TOP)
    }
    /**
     * 测试弹出自定义底部dialog
     */
    private fun showBottomDialog(view: View) {
        val dialog = CustomGravityDialog(this)
        dialog.show(Gravity.BOTTOM)
    }
}