package com.vmloft.develop.library.request

import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response


/**
 * Create by lzan13 on 2024/1/31 1:13
 * 描述：基础参数请求拦截器
 */
class BParamsInterceptor : Interceptor {
    // 公共头参数
    val commonHeadersMap = mutableMapOf<String, String>()

    // 公共参数
    val commonParamsMap = mutableMapOf<String, String>()

    // get请求参数
    val queryParamsMap = mutableMapOf<String, String>()

    // post请求 form 参数
    val formParamsMap = mutableMapOf<String, String>()

    // post请求 multipart 参数
    val multipartParamsMap = mutableMapOf<String, String>()

    override fun intercept(chain: Interceptor.Chain): Response {
        val orgRequest: Request = chain.request()
        if (orgRequest.method == "POST" || orgRequest.method == "PUT") {
            // 有请求体的公共参数处理，一般是 POST 和 PUT 请求
            val body = orgRequest.body
            if (body != null) {
                var newBody: RequestBody? = null
                // 构建新的请求体
                if (body is FormBody) {
                    newBody = addParamsToFormBody(body)
                } else if (body is MultipartBody) {
                    newBody = addParamsToMultipartBody(body)
                }
                if (newBody != null) {
                    // 创建新的请求返回
                    val newRequest = orgRequest.newBuilder()
                        .url(orgRequest.url)
                        .method(orgRequest.method, newBody)
                        .build()
                    return chain.proceed(newRequest)
                }
            }
        } else {
            // 没有请求体的公共参数处理，一般是 GET 和 DELETE 请求
            val paramsUrlBuilder = orgRequest.url.newBuilder()
            paramsUrlBuilder.scheme(orgRequest.url.scheme)
            paramsUrlBuilder.host(orgRequest.url.host)
            // 添加公共参数
            commonParamsMap.map {
                paramsUrlBuilder.addQueryParameter(it.key, it.value)
            }
            // 添加 query 参数
            queryParamsMap.map {
                paramsUrlBuilder.addQueryParameter(it.key, it.value)
            }
            // 构建新的 request
            val newRequest: Request = orgRequest.newBuilder()
                .method(orgRequest.method, orgRequest.body)
                .url(paramsUrlBuilder.build())
                .build()
            return chain.proceed(newRequest)
        }
        // 兜底处理
        return chain.proceed(orgRequest)
    }

    /**
     * 为FormBody类型请求体添加参数
     */
    private fun addParamsToFormBody(body: FormBody): FormBody {
        val builder = FormBody.Builder()
        // 添加公共参数
        commonParamsMap.map {
            builder.add(it.key, it.value)
        }
        // 添加 form 参数
        formParamsMap.map {
            builder.add(it.key, it.value)
        }

        // 添加原请求体
        for (i in 0 until body.size) {
            builder.addEncoded(body.encodedName(i), body.encodedValue(i))
        }
        return builder.build()
    }

    /**
     * 为MultipartBody类型请求体添加参数
     */
    private fun addParamsToMultipartBody(body: MultipartBody): MultipartBody {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        // 添加公共参数
        commonParamsMap.map {
            builder.addFormDataPart(it.key, it.value)
        }
        // 添加 multipart 参数
        formParamsMap.map {
            builder.addFormDataPart(it.key, it.value)
        }

        // 添加原请求体
        for (i in 0 until body.size) {
            builder.addPart(body.part(i))
        }
        return builder.build()
    }

    /**
     * 公共参数拦截器构建者，主要用来添加公共参数
     */
    class Builder {

        private val interceptor: BParamsInterceptor = BParamsInterceptor()

        /**
         * 添加公共头参数
         */
        fun addCommonHeaders(key: String, value: String): Builder {
            interceptor.commonHeadersMap[key] = value
            return this
        }

        /**
         * 添加公共参数
         */
        fun addCommonParams(key: String, value: String): Builder {
            interceptor.commonParamsMap[key] = value
            return this
        }

        /**
         * 添加 query 参数
         */
        fun addQueryParams(key: String, value: String): Builder {
            interceptor.queryParamsMap[key] = value
            return this
        }

        /**
         * 添加 form 参数
         */
        fun addFormParams(key: String, value: String): Builder {
            interceptor.formParamsMap[key] = value
            return this
        }

        /**
         * 添加 multipart 参数
         */
        fun addMultipartParams(key: String, value: String): Builder {
            interceptor.multipartParamsMap[key] = value
            return this
        }

        fun build(): BParamsInterceptor {
            return interceptor
        }
    }
}