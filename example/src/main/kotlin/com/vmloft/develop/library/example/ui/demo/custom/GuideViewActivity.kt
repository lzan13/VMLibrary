package com.vmloft.develop.library.example.ui.demo.custom

import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.library.base.BActivity
import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.databinding.ActivityDemoGuideViewBinding
import com.vmloft.develop.library.example.router.AppRouter
import com.vmloft.develop.library.tools.utils.VMDimen
import com.vmloft.develop.library.tools.utils.logger.VMLog
import com.vmloft.develop.library.tools.widget.VMFloatMenu
import com.vmloft.develop.library.tools.widget.guide.GuideItem
import com.vmloft.develop.library.tools.widget.guide.VMGuide
import com.vmloft.develop.library.tools.widget.guide.VMGuideView

/**
 * Created by lzan13 on 2017/6/20.
 *
 * 测试悬浮菜单
 */
@Route(path = AppRouter.appCustomGuideView)
class GuideViewActivity : BActivity<ActivityDemoGuideViewBinding>() {

    @Override
    override fun initVB() = ActivityDemoGuideViewBinding.inflate(layoutInflater)

    @Override
    override fun initUI() {
        super.initUI()
        setTopTitle("自定义遮罩引导")

        mBinding.leftTopBtn.setOnClickListener { showGuide(true) }
        // mBinding.leftBtn1
        // mBinding.leftBtn2
        mBinding.leftBottomBtn.setOnClickListener { showGuide(false) }
        // mBinding.centerUpBtn
        // mBinding.centerBtn
        // mBinding.centerDownBtn
        mBinding.rightTopBtn.setOnClickListener { showGuide(true, true) }
        mBinding.rightBottomBtn.setOnClickListener { showGuide(false, true) }
        showGuide()
    }


    @Override
    override fun initData() {
    }

    /**
     * 显示引导
     */
    private fun showGuide(oneByOne: Boolean = true, isImg: Boolean = false) {
        val list = mutableListOf<GuideItem>()
        list.add(GuideItem(mBinding.leftTopBtn, "这是 leftTopBtn 引导文案\n这是第二行文案\n这是第三行文案", if (isImg) R.drawable.img_default_avatar else 0, offY = VMDimen.dp2px(24)))
//        list.add(GuideItem(mBinding.leftBtn1, "这是 leftBtn1 引导文案，",if(isImg)R.drawable.img_default_avatar else 0))
//        list.add(GuideItem(mBinding.leftBtn2, "这是 leftBtn2 引导文案，",if(isImg)R.drawable.img_default_avatar else 0))
        list.add(GuideItem(mBinding.leftBottomBtn, "这是 leftBottomBtn 引导文案，", if (isImg) R.drawable.img_default_avatar else 0))
        list.add(GuideItem(mBinding.centerUpBtn, "这是 centerUpBtn 引导文案，", if (isImg) R.drawable.img_default_avatar else 0))
//        list.add(GuideItem(mBinding.centerBtn, "这是 centerBtn 引导文案，",if(isImg)R.drawable.img_default_avatar else 0))
        list.add(GuideItem(mBinding.centerDownBtn, "这是 centerDownBtn 引导文案，", if (isImg) R.drawable.img_default_avatar else 0))
        list.add(GuideItem(mBinding.rightTopBtn, "这是 rightTopBtn 引导文案，", if (isImg) R.drawable.img_default_avatar else 0))
        list.add(GuideItem(mBinding.rightBottomBtn, "这是 rightBottomBtn 引导文案，\n这是第二行文案\n这是第三行文案", if (isImg) R.drawable.img_default_avatar else 0, offY = VMDimen.dp2px(-24)))
        VMGuide.Builder(this).setOneByOne(oneByOne).setGuideViews(list).setGuideListener(object : VMGuideView.GuideListener {
            override fun onNext(index: Int) {
                VMLog.i("onNext $index")
            }

            override fun onFinish() {
                VMLog.i("onFinish")
            }

        }).build().show()
    }
//        Guide.ViewParams viewParams = new Guide.ViewParams(findViewById(R.id.ivBack));
//    new Guide.Builder(this)
//    .guideSingelView(viewParams)            //设置单个引导
//    .build()
//    .show();
//    )

}