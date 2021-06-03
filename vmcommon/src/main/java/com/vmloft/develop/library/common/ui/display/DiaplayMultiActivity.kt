package com.vmloft.develop.library.common.ui.display

import androidx.viewpager2.widget.ViewPager2

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter

import com.drakeet.multitype.MultiTypeAdapter

import com.vmloft.develop.library.common.R
import com.vmloft.develop.library.common.base.BVMActivity
import com.vmloft.develop.library.common.base.BViewModel
import com.vmloft.develop.library.common.common.CConstants
import com.vmloft.develop.library.common.router.CRouter
import com.vmloft.develop.library.tools.utils.VMColor
import com.vmloft.develop.library.tools.utils.VMFile

import kotlinx.android.synthetic.main.activity_display_multi.*
import kotlinx.android.synthetic.main.widget_common_top_bar.*

import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * Create by lzan13 on 2020/4/21 10:19
 * 描述：展示多图片界面
 */
@Route(path = CRouter.commonDisplayMulti)
class DisplayMultiActivity : BVMActivity<DisplayViewModel>() {

    @Autowired
    lateinit var index: String

    @Autowired
    lateinit var pictureList: List<String>

    private val mAdapter by lazy { MultiTypeAdapter() }
    private val mItems = ArrayList<Any>()

    override fun initVM(): DisplayViewModel = getViewModel()

    override fun layoutId(): Int = R.layout.activity_display_multi

    override fun initUI() {
        super.initUI()
        commonTopBar.setIconColor(VMColor.byRes(R.color.app_title_display))
        commonTopBar.setTitleColor(R.color.app_title_display)
        commonTopBar.setBackgroundColor(VMColor.byRes(R.color.app_bg_transparent_dark))
        commonTopBar.setEndIcon(R.drawable.ic_download_picture)
        commonTopBar.setEndIconListener { savePicture() }

        initViewPager()
    }

    override fun initData() {
        ARouter.getInstance().inject(this)

        mItems.addAll(pictureList)
        mAdapter.notifyDataSetChanged()

        displayViewPager.currentItem = index.toInt()

//        ADSManager.instance.loadBannerADS(ADSManager.adsBannerId, adsContainer)
    }

    override fun onModelRefresh(model: BViewModel.UIModel) {
    }

    /**
     * 设置标题
     */
    private fun setTitleIndex() {
        setTopTitle("$index/${pictureList.size}")
    }

    /**
     * 初始化 ViewPager
     */
    private fun initViewPager() {
        mAdapter.register(DisplayItemDelegate())
        mAdapter.items = mItems
        displayViewPager.adapter = mAdapter
        // 设置 ViewPager 切换监听回调
        displayViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                index = "${position + 1}"
                setTitleIndex()
            }
        })
    }

    /**
     * 保存当前浏览单张图片
     */
    private fun savePicture() {
        // 保存当前图片
        val url = pictureList[index.toInt()]
        mViewModel.savePictureSingle(mActivity, url)

        // 上报下载图片事件
//        ReportManager.instance.reportDownloadPicture(url)
    }

    /**
     * 保存当前整套图片
     */
    private fun savePictures() {
        var urlList: MutableList<String> = mutableListOf()
        var pathList: MutableList<String> = mutableListOf()
        for (picture in pictureList) {
            urlList.add(picture)

            val path = "${VMFile.pictures}${CConstants.projectDir}${picture.substring(picture.lastIndexOf("/"))}"
            pathList.add(path)
        }
        mViewModel.savePictureMulti(this, urlList, pathList)
    }

}