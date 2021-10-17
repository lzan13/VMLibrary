package com.vmloft.develop.library.common.ui.display


import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter

import com.vmloft.develop.library.common.R
import com.vmloft.develop.library.common.base.BVMActivity
import com.vmloft.develop.library.common.base.BViewModel
import com.vmloft.develop.library.common.databinding.ActivityDisplaySingleBinding
import com.vmloft.develop.library.common.image.IMGLoader
import com.vmloft.develop.library.common.router.CRouter
import com.vmloft.develop.library.tools.utils.VMColor

import org.koin.androidx.viewmodel.ext.android.getViewModel


/**
 * Create by lzan13 on 2020/4/23 22:56
 * 描述：展示单图
 */
@Route(path = CRouter.commonDisplaySingle)
class DisplaySingleActivity : BVMActivity<ActivityDisplaySingleBinding, DisplayViewModel>() {

    @Autowired
    lateinit var url: String

    override fun initVB() = ActivityDisplaySingleBinding.inflate(layoutInflater)

    override fun initVM(): DisplayViewModel = getViewModel()

    override fun initUI() {
        super.initUI()
        commonTopBar?.setIconColor(VMColor.byRes(R.color.app_title_display))
        commonTopBar?.setBackgroundColor(VMColor.byRes(R.color.app_bg_transparent_dark))
        commonTopBar?.setEndIcon(R.drawable.ic_download_picture)
        commonTopBar?.setEndIconListener { savePicture() }

    }

    override fun initData() {
        ARouter.getInstance().inject(this)

        IMGLoader.loadCover(mBinding.displayIV, url)

//        ADSManager.loadBannerADS(ADSManager.adsBannerId, adsContainer)
    }

    override fun onModelRefresh(model: BViewModel.UIModel) {

    }

    /**
     * 保存当前图片
     */
    private fun savePicture() {
        mViewModel.savePictureSingle(this, url)

        // 上报下载图片事件
//        ReportManager.reportDownloadPicture(url)
    }


}