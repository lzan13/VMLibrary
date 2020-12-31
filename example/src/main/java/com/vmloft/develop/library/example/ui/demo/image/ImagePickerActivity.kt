package com.vmloft.develop.library.example.ui.demo.image

import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.base.BaseActivity
import com.vmloft.develop.library.example.image.IMGChoose
import com.vmloft.develop.library.example.image.IMGLoader
import com.vmloft.develop.library.example.router.AppRouter
import com.vmloft.develop.library.template.notify.NotifyManager
import com.vmloft.develop.library.tools.utils.bitmap.VMBitmap
import com.vmloft.develop.library.tools.utils.logger.VMLog
import com.vmloft.develop.library.tools.widget.toast.VMToast
import kotlinx.android.synthetic.main.activity_image_picker.*

import kotlinx.android.synthetic.main.activity_notify.*


/**
 * Created by lzan13 on 2020/5/18.
 * 描述：测试图片选择界面
 */
@Route(path = AppRouter.appImagePicker)
class ImagePickerActivity : BaseActivity() {

    override fun layoutId(): Int {
        return R.layout.activity_image_picker
    }

    override fun initUI() {
        super.initUI()
        setTopTitle("图片选择器")

        imagePickerSingle.setOnClickListener { single() }
        imagePickerCrop.setOnClickListener { singleCrop() }
        imagePickerMultiple.setOnClickListener { multiPicture() }
        imagePickerCamera.setOnClickListener { talkPicture() }
        imagePickerVideo.setOnClickListener { talkVideo() }
    }

    override fun initData() {

    }


    private fun single() {
        IMGChoose.singlePicture(this) {
            VMLog.d("单选结果 $it")
            val path = VMBitmap.compressTempImage(it, 1920)
            IMGLoader.loadCover(imagePickerIV, path)
        }
    }

    private fun singleCrop() {
        IMGChoose.singleCrop(this) {
            VMLog.d("裁剪结果 $it")
            val path = VMBitmap.compressTempImage(it, 512)
            IMGLoader.loadCover(imagePickerIV, it)
        }
    }

    private fun multiPicture() {
        IMGChoose.multiPictures(this) {
            VMLog.d("多选结果 $it")
        }
    }

    private fun talkPicture() {
        IMGChoose.takePicture(this) {
            VMLog.d("拍照结果 $it")
            IMGLoader.loadCover(imagePickerIV, it)
        }
    }

    private fun talkVideo() {
        IMGChoose.takePicture(this) {
            VMLog.d("拍摄结果 $it")
            IMGLoader.loadCover(imagePickerIV, it)
        }
    }
}