package com.vmloft.develop.app.example.ui.demo.image

import com.didi.drouter.annotation.Router

import com.vmloft.develop.library.base.BActivity
import com.vmloft.develop.library.base.utils.showBar
import com.vmloft.develop.app.example.common.Constants
import com.vmloft.develop.app.example.databinding.ActivityDemoImagePickerBinding
import com.vmloft.develop.app.example.router.AppRouter
import com.vmloft.develop.library.image.IMGChoose
import com.vmloft.develop.library.image.IMGLoader
import com.vmloft.develop.library.tools.utils.bitmap.VMBitmap
import com.vmloft.develop.library.tools.utils.logger.VMLog


/**
 * Created by lzan13 on 2020/5/18.
 * 描述：测试图片选择界面
 */
@Router(path = AppRouter.appImagePicker)
class ImagePickerActivity : BActivity<ActivityDemoImagePickerBinding>() {

    override fun initVB() = ActivityDemoImagePickerBinding.inflate(layoutInflater)

    override fun initUI() {
        super.initUI()
        setTopTitle("图片选择器")

        binding.pickerSingleBtn.setOnClickListener { single() }
        binding.pickerCropBtn.setOnClickListener { singleCrop() }
        binding.imagePickerMultiple.setOnClickListener { multiPicture() }
        binding.pickerCameraBtn.setOnClickListener { talkPicture() }
        binding.pickerVideoBtn.setOnClickListener { talkVideo() }
        binding.saveBtn.setOnClickListener { save() }
    }

    override fun initData() {

    }


    private fun single() {
        IMGChoose.singlePicture(this) {
            VMLog.d("单选结果 $it")
            val path = VMBitmap.compressTempImage(it, 1920)
            IMGLoader.loadCover(binding.imagePickerIV, path)
        }
    }

    private fun singleCrop() {
        IMGChoose.singleCrop(this) {
            VMLog.d("裁剪结果 $it")
            val path = VMBitmap.compressTempImage(it, 512)
            IMGLoader.loadCover(binding.imagePickerIV, it)
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
            IMGLoader.loadCover(binding.imagePickerIV, it)
        }
    }

    private fun talkVideo() {
        IMGChoose.takeVideo(this) {
            VMLog.d("拍摄结果 $it")
//            IMGLoader.loadCover(imagePickerIV, it)
        }
    }

    private fun save() {
        val bitmap = VMBitmap.loadCacheBitmapFromView(binding.imagePickerIV)
        bitmap?.let {
            val result = VMBitmap.saveBitmapToPictures(it, Constants.projectDir, "test.jpg")
            showBar(if (result != null) "保存成功" else "保存失败")
        }
    }
}