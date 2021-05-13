package com.vmloft.develop.library.common.image

import android.widget.ImageView
import androidx.databinding.BindingAdapter


/**
 * Create by lzan13 on 2020/4/7 16:08
 * 描述：databinding 绑定图片加载
 */
object IMGLoaderAdapter {

    @BindingAdapter("android:src")
    @JvmStatic
    fun setSrc(view: ImageView, resId: Int) {
        view.setImageResource(resId)
    }

    @BindingAdapter("app:avatarUrl")
    @JvmStatic
    fun loadAvatar(view: ImageView, url: String?) {
        IMGLoader.loadAvatar(view, url)
    }

    /**
     * 加载封面图
     */
    @BindingAdapter("app:coverUrl")
    @JvmStatic
    fun loadCover(view: ImageView, url: String) {
        IMGLoader.loadCover(view, url)
    }

    /**
     * 加载模糊封面图
     */
    @BindingAdapter("app:coverUrl", "app:blur")
    @JvmStatic
    fun loadBlurCover(view: ImageView, url: String, blur: Boolean) {
        IMGLoader.loadCover(view, url, isBlur = blur)
    }

    /**
     * 加载瀑布流 Item 封面图
     */
    @BindingAdapter("app:itemCoverUrl")
    @JvmStatic
    fun loadItemCover(view: ImageView, url: String) {
        IMGLoader.loadCover(view, url, isRadius = true, radiusTL = 8, radiusTR = 8)
    }


}