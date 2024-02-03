package com.vmloft.develop.library.request

import com.vmloft.develop.library.tools.utils.VMFile

import okhttp3.*

import java.io.File


/**
 * Create by lzan13 on 2023/03/28 23:08
 * 描述：默认 API 请求类
 */
open class DefaultRequest : BaseRequest() {

    /**
     * 处理 Builder
     */
    override fun handleBuilder(builder: OkHttpClient.Builder) {
        val cacheSize = 30 * 1024 * 1024L
        val cacheDirectory = File(VMFile.cachePath, "responses")
        val cache = Cache(cacheDirectory, cacheSize)
        // 缓存配置
        builder.cache(cache).addInterceptor { chain ->
            var request = chain.request()
            val response = chain.proceed(request)

            val maxStale = 60 * 60 * 24 * 28 // tolerate 4-weeks stale
            response.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .build()
            response
        }
    }

    /**
     * 添加拦截器
     */
    override fun addInterceptor(builder: OkHttpClient.Builder) {}
}