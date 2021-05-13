package com.vmloft.develop.library.common.common

/**
 * Create by lzan13 on 2020/8/12 22:03
 * 描述：基本常量
 */
object CConstants {

    const val cacheImageDir = "images"

//    private const val baseTestUrl = "http://192.168.1.13:5926/"
    private const val baseTestUrl = "http://172.16.185.114:5926/"
    private const val baseReleaseUrl = "https://template.melove.net/"

    // 用户协议地址
    const val userAgreementUrl = "https://melove.net/privacy/policy-template.html"
    // 隐私政策地址
    const val privatePolicyUrl = "https://melove.net/privacy/policy-template.html"

    // 应用在 SDCard 创建区别其他项目目录，一般以项目名命名
    const val projectDir = "VMTemplate"


    // 时间常量
    const val timeSecond: Long = 1000
    const val timeMinute: Long = 60 * timeSecond
    const val timeHour: Long = 60 * timeMinute
    const val timeDay = 24 * timeHour // 天


    /**
     * 获取接口 host 地址，根据 debug 状态返回不同地址
     */
    fun baseHost(): String {
        return if (CSPManager.instance.isDebug()) {
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