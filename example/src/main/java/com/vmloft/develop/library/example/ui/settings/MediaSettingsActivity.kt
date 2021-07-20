package com.vmloft.develop.library.example.ui.settings

import com.alibaba.android.arouter.facade.annotation.Route
import com.vmloft.develop.library.common.base.BaseActivity
import com.vmloft.develop.library.common.common.CConstants
import com.vmloft.develop.library.common.common.CSPManager
import com.vmloft.develop.library.common.utils.showBar
import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.router.AppRouter
import com.vmloft.develop.library.tools.utils.VMFile

import kotlinx.android.synthetic.main.activity_settings_media.*

import java.io.File

/**
 * Create by lzan13 on 2020/05/02 22:56
 * 描述：图片资源设置
 */
@Route(path = AppRouter.appSettingsMedia)
class MediaSettingsActivity : BaseActivity() {
    // 缓存地址
    private val cachePath = "${VMFile.cachePath}${CConstants.cacheImageDir}"

    override fun layoutId(): Int = R.layout.activity_settings_media

    override fun initUI() {
        super.initUI()
        setTopTitle(R.string.settings_media)

        pictureAutoLoadLV.setOnClickListener {
            CSPManager.setAutoLoad(!CSPManager.isAutoLoad())
            pictureAutoLoadLV.isActivated = CSPManager.isAutoLoad()
        }

        pictureSaveDICMLV.setOnClickListener {
            CSPManager.setSaveDICM(!CSPManager.isSaveDICM())
            pictureSaveDICMLV.isActivated = CSPManager.isSaveDICM()
        }
        pictureClearCacheLV.setOnClickListener {
            VMFile.deleteFolder(cachePath, false)
            showBar(R.string.media_clear_toast)
            bindCache()
        }

    }

    override fun initData() {
        pictureAutoLoadLV.isActivated = CSPManager.isAutoLoad()
        pictureSaveDICMLV.isActivated = CSPManager.isSaveDICM()

        bindCache()
    }

    private fun bindCache() {
        // 缓存大小
        var cacheStr = VMFile.formatSize(VMFile.getFolderSize(File(cachePath)))
        pictureClearCacheLV.setCaption(cacheStr)
    }

}