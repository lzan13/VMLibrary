package com.vmloft.develop.library.example.ui.demo.image

import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.library.base.BActivity
import com.vmloft.develop.library.base.utils.showBar
import com.vmloft.develop.library.example.common.Constants
import com.vmloft.develop.library.example.databinding.ActivityDemoImagePickerBinding
import com.vmloft.develop.library.example.router.AppRouter
import com.vmloft.develop.library.image.IMGChoose
import com.vmloft.develop.library.image.IMGLoader
import com.vmloft.develop.library.tools.utils.bitmap.VMBitmap
import com.vmloft.develop.library.tools.utils.logger.VMLog


/**
 * Created by lzan13 on 2020/5/18.
 * 描述：测试图片选择界面
 */
@Route(path = AppRouter.appImagePicker)
class ImagePickerActivity : BActivity<ActivityDemoImagePickerBinding>() {

    override fun initVB() = ActivityDemoImagePickerBinding.inflate(layoutInflater)

    override fun initUI() {
        super.initUI()
        setTopTitle("图片选择器")

        mBinding.pickerSingleBtn.setOnClickListener { single() }
        mBinding.pickerCropBtn.setOnClickListener { singleCrop() }
        mBinding.imagePickerMultiple.setOnClickListener { multiPicture() }
        mBinding.pickerCameraBtn.setOnClickListener { talkPicture() }
        mBinding.pickerVideoBtn.setOnClickListener { talkVideo() }
        mBinding.saveBtn.setOnClickListener { save() }
    }

    override fun initData() {

    }


    private fun single() {
        IMGChoose.singlePicture(this) {
            VMLog.d("单选结果 $it")
            val path = VMBitmap.compressTempImage(it, 1920)
            IMGLoader.loadCover(mBinding.imagePickerIV, path)
        }
    }

    private fun singleCrop() {
        IMGChoose.singleCrop(this) {
            VMLog.d("裁剪结果 $it")
            val path = VMBitmap.compressTempImage(it, 512)
            IMGLoader.loadCover(mBinding.imagePickerIV, it)
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
            IMGLoader.loadCover(mBinding.imagePickerIV, it)
        }
    }

    private fun talkVideo() {
        IMGChoose.takeVideo(this) {
            VMLog.d("拍摄结果 $it")
//            IMGLoader.loadCover(imagePickerIV, it)
        }
    }

    private fun save() {
        val bitmap = VMBitmap.loadCacheBitmapFromView(mBinding.imagePickerIV)
        bitmap?.let {
            val result = VMBitmap.saveBitmapToPictures(it, Constants.projectDir, "test.jpg")
            showBar(if (result != null) "保存成功" else "保存失败")
        }
    }
}