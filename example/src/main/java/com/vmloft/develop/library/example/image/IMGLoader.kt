package com.vmloft.develop.library.example.image

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.common.Constants
import com.vmloft.develop.library.tools.utils.VMDimen


/**
 * Create by lzan13 on 2019/5/22 13:24
 * 图片加载简单封装
 */
object IMGLoader {

    /**
     * 创建控件
     */
    fun createView(context: Context): ImageView {
        return ImageView(context)
    }

    /**
     * 加载圆形图，一般是头像
     *
     * @param iv 目标 view
     * @param avatar 图片地址
     */
    fun loadAvatar(
            iv: ImageView,
            avatar: Any?,
            isCircle: Boolean = false,
            isRadius: Boolean = false,
            radiusSize: Int = 4
    ) {
        val options = Options(avatar, R.drawable.img_default_avatar, isCircle, isRadius, radiusSize)
        load(options, iv)
    }

    /**
     * 加载封面图
     *
     * @param iv 目标 view
     * @param cover 图片地址
     * @param isRadius 是否圆角
     * @param radiusSize 圆角大小
     * @param isBlur 是否模糊
     */
    fun loadCover(iv: ImageView, cover: Any?, isRadius: Boolean = false, radiusSize: Int = 4, isBlur: Boolean = false) {
        val options = Options(cover, isRadius = isRadius, radiusSize = radiusSize, isBlur = isBlur)
        load(options, iv)
    }

    /**
     * 加载缩略图
     *
     * @param iv 目标 view
     * @param path 图片地址
     * @param isRadius 是否圆角
     * @param radiusSize 圆角大小
     * @param isBlur 是否模糊
     */
    fun loadThumbnail(
            iv: ImageView,
            path: Any?,
            isRadius: Boolean = true,
            radiusSize: Int = 4,
            size: Int = 256
    ) {
        val options = Options(path, isRadius = isRadius, radiusSize = radiusSize, isThumbnail = true, thumbnailSize = size)
        load(options, iv)
    }

//    /**
//     * 保存图片
//     */
//    suspend fun savePicture(context: Context, url: String, path: String) = withContext(Dispatchers.IO) {
//        // 保存图片结果
//        var apiResult: APIResult<String> = APIResult.Success(path)
//        val glideUrl = GlideUrl(url,
//            headers(url)
//        )
//        // 阻塞线层将 callback 转为同步
//        val countDownLatch = CountDownLatch(1)
//
//        GlideApp.with(context).downloadOnly()
//            // 设置监听
//            .listener(object : RequestListener<File> {
//                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<File>?, isFirstResource: Boolean): Boolean {
//                    VMLog.e("保存图片失败 ${e?.message}")
//                    apiResult = APIResult.Error("保存图片失败 ${e?.message}")
//                    // 解除锁
//                    countDownLatch.countDown()
//                    return true
//                }
//
//                override fun onResourceReady(resource: File?, model: Any?, target: Target<File>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
//                    val fis = FileInputStream(resource)
//                    val bmp = BitmapFactory.decodeStream(fis)
//                    var result = VMBitmap.saveBitmapToSDCard(bmp, path)
//                    if (result) {
//                        VMLog.d("保存图片成功 $path")
//                    } else {
//                        VMLog.e("保存图片失败")
//                        apiResult = APIResult.Error("保存图片失败")
//                    }
//                    if (result) {
//                        // 保存完成通知相册刷新
//                        CUtils.notifyAlbum(context, path)
//                    }
//                    // 解除锁
//                    countDownLatch.countDown()
//                    return true
//                }
//            })
//            // 开始加载
//            .load(glideUrl).preload()
//        // 等待回调结束
//        countDownLatch.await()
//        apiResult
//    }

    /**
     * 加载图片
     *
     * @param options   加载图片配置
     * @param imageView 目标 view
     */
    private fun load(options: Options, imageView: ImageView) {
        val requestOptions = RequestOptions()
        if (options.isCircle) {
            requestOptions.circleCrop()
        } else if (options.isRadius) {
            requestOptions.transform(MultiTransformation(CenterCrop(), RoundedCorners(VMDimen.dp2px(options.radiusSize))))
        }
        if (options.isBlur) {
            requestOptions.transform(BlurTransformation())
        }
        if (options.isThumbnail) {
            requestOptions.format(DecodeFormat.PREFER_RGB_565).override(options.thumbnailSize)
        }

        val wOptions = wrapOptions(options)
        if (options.referer.isNotEmpty()) {
            val glideUrl = GlideUrl(wOptions.res as String, headers(options.referer))

            GlideApp.with(imageView.context)
                    .load(glideUrl)
                    .placeholder(placeholder(imageView.context, options).fallbackDrawable)
//                    .thumbnail(placeholder(imageView.context, options))
                    .apply(requestOptions)
                    .into(imageView)
        } else {
            GlideApp.with(imageView.context)
                    .load(wOptions.res)
                    .placeholder(placeholder(imageView.context, options).fallbackDrawable)
//                    .thumbnail(placeholder(imageView.context, options))
                    .apply(requestOptions)
                    .into(imageView)
        }
    }

    /**
     * 处理占位图
     *
     * @param context 上下文对象
     * @param options 加载配置
     * @param resId   默认资源图
     * @return
     */
    private fun placeholder(context: Context, options: Options): RequestBuilder<Drawable> {
        val requestOptions = RequestOptions()
        if (options.isCircle) {
            requestOptions.circleCrop()
        } else if (options.isRadius) {
            requestOptions.transform(MultiTransformation(CenterCrop(), RoundedCorners(VMDimen.dp2px(options.radiusSize))))
        }
        if (options.isBlur) {
            requestOptions.transform(BlurTransformation())
        }
        return GlideApp.with(context).load(options.defaultResId).apply(requestOptions)
    }

    /**
     * 统一处理加载图片请求头
     */
    private fun headers(referer: String): LazyHeaders {
        return LazyHeaders.Builder()
                .addHeader("accept-encoding", "gzip, deflate, br")
                .addHeader("accept-language", "zh-CN,zh;q=0.9")
                .addHeader("referer", referer)
                .build()
    }

    /**
     * 包装下图片加载属性
     */
    private fun wrapOptions(options: Options): Options {
        if (options.res is String && (options.res as String).indexOf("/uploads/") == 0) {
            options.res = Constants.mediaHost() + options.res
        }
        return options
    }

    /**
     * 加载图片配置
     */
    data class Options(
            // 图片资源，可以为 Uri/String/resId
            var res: Any?,
            // 默认资源
            var defaultResId: Int = R.drawable.img_default,
            // 是否圆形
            var isCircle: Boolean = false,
            // 圆角
            var isRadius: Boolean = false,
            var radiusSize: Int = 4,
            var radiusTL: Int = 4,
            var radiusTR: Int = 4,
            var radiusBL: Int = 4,
            var radiusBR: Int = 4,
            // 是否模糊
            var isBlur: Boolean = false,
            var isThumbnail: Boolean = false,
            var thumbnailSize: Int = 256,

            // 参考参数，防盗链使用
            var referer: String = ""
    )

}