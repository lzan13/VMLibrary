package com.vmloft.develop.library.example.image

import android.content.Context

import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.module.AppGlideModule

import com.vmloft.develop.library.example.common.Constants

/**
 * Create by lzan13 on 2020/04/06 17:56
 * 描述：使用 RenderScript 模糊图片
 */
@GlideModule
class IMGGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        //        super.applyOptions(context, builder);
        val size = 256 * 1024 * 1024
        val dir = Constants.cacheImageDir
        builder.setDiskCache(ExternalPreferredCacheDiskCacheFactory(context, dir, size.toLong()))
    }

}