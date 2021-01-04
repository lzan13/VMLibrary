package com.vmloft.develop.library.example.common

/**
 * Create by lzan13 on 2020-02-18 14:43
 * 常量类
 */
object Constants {

    private const val baseTestUrl = "http://172.16.186.127:5926/"
    private const val baseReleaseUrl = "https://template.melove.net/"

    // 隐私协议地址
    const val policyUrl = "https://melove.net/privacy/policy-template.html"

    // 应用在 SDCard 创建区别其他项目目录，一般以项目名命名
    const val projectDir = "VMLibrary"

    // 缓存图片目录
    const val cacheImageDir = "images"

    // 时间常量
    const val timeSecond: Long = 1000
    const val timeMinute: Long = 60 * timeSecond
    const val timeHour: Long = 60 * timeMinute

    // 选择图片请求码
    const val chooseRequestCode: Int = 1001

    /**
     * 获取接口 host 地址，根据 debug 状态返回不同地址
     */
    fun baseHost(): String {
        return if (SPManager.instance.isDebug()) {
            baseTestUrl
        } else {
            baseReleaseUrl
        }
    }

    /**
     * 获取媒体资源 host 地址
     */
    fun mediaHost(): String {
        return baseHost() + "public"
    }
}