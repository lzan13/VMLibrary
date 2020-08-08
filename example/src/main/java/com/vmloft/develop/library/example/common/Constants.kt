package com.vmloft.develop.library.example.common

/**
 * Create by lzan13 on 2020-02-18 14:43
 * 常量类
 */
object Constants {

    // 隐私协议地址
    const val policyUrl = "https://melove.net/privacy/policy-template.html"

    // 应用在 SDCard 创建区别其他项目目录，一般以项目名命名
    const val projectDir = "VMTemplate"

    // 缓存图片目录
    const val cacheImageDir = "images"

    // 时间常量
    const val timeSecond: Long = 1000
    const val timeMinute: Long = 60 * timeSecond
    const val timeHour: Long = 60 * timeMinute

    // 选择图片请求码
    const val chooseRequestCode: Int = 1001

}