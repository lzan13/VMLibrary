package com.vmloft.develop.convention.extension

import com.vmloft.develop.convention.VMDependencies

import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project

/**
 * Created by lzan13 on 2024/1/26 11:42
 * 描述：依赖扩展管理
 */

/**
 * android 相关依赖
 */
fun DependencyHandlerScope.applicationDependencies() {
    // 依赖 jar 包
//    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))
    // 依赖 aar
//    implementation(group = "", name = "libraryname", ext = "aar")

    // 依赖 vmbase/vmcommon
    "implementation"(project(":basic:vmbase"))
    "implementation"(project(":basic:vmcommon"))

    // 依赖核心模块 vmimage/vmrequest
//    "implementation"(project(":core:vmads"))
//    "implementation"(project(":core:vmdata"))
//    "implementation"(project(":core:vmdb"))
//    "implementation"(project(":core:vmgift"))
    "implementation"(project(":core:vmimage"))
//    "implementation"(project(":core:vmpay"))
//    "implementation"(project(":core:vmqr"))
//    "implementation"(project(":core:vmreport"))
    "implementation"(project(":core:vmrequest"))

    // 依赖 im 模块
//    "implementation"(project(":feature:vmimem"))

}

/**
 * 基础依赖，这部分主要是一些基础依赖
 */
fun DependencyHandlerScope.baseDependencies() {
    // kotlin相关
    "api"(VMDependencies.coreKtx)
    "api"(VMDependencies.kotlinxCoroutinesCore)
    "api"(VMDependencies.kotlinxCoroutinesAndroid)

    // AndroidX 相关
    "api"(VMDependencies.appCompat)
    "api"(VMDependencies.constraintLayout)
    "api"(VMDependencies.coordinatorLayout)
    "api"(VMDependencies.localBroadcastManager)
    "api"(VMDependencies.material)

    // JetPack 相关
    "api"(VMDependencies.activityKtx)
    "api"(VMDependencies.fragmentKtx)
    "api"(VMDependencies.lifecycleExtensions)
    "api"(VMDependencies.lifecycleLivedataKtx)
    "api"(VMDependencies.lifecycleViewModelKtx)

    // 多类型适配，定义基类
    "api"(VMDependencies.multiType)

    // DiDi 路由
    "api"(VMDependencies.dRouter)
    // 阿里巴巴 ARouter 库 https://github.com/alibaba/ARouter
    "api"("com.alibaba:arouter-api:1.5.2")
    // ARouter 编译器，TODO 当代码中有一些自己没看到的错误的时候，ARouter 会拦截错误信息，看不到错误位置，把这行注释掉，重新编译
//    "kapt"("com.alibaba:arouter-compiler:1.5.2")

    // koin 依赖注入
    "api"(VMDependencies.koin)

    // 自定义工具库
//    "api"(VMDependencies.vmtools)
    // 源码依赖，开发调试
    "api"(project(":vmtools"))

}

/**
 * 通用依赖，这部分主要是一些常用布局库，和一些自定义三方库
 */
fun DependencyHandlerScope.commonDependencies() {
    // 三方控件
    "api"(VMDependencies.agentWeb)
    "api"(VMDependencies.bannerViewPager)
    "api"(VMDependencies.refreshLayoutKernel)
    "api"(VMDependencies.wheelPicker)

    // 生命周期 EventBus
    "api"(VMDependencies.liveEventBus)

    // gson 数据解析，这里依赖是为了实现数据解析工具封装
    "api"(VMDependencies.gson)

    // 依赖 vmbase
    "implementation"(project(":basic:vmbase"))
}

/**
 * 图片处理
 */
fun DependencyHandlerScope.imageDependencies() {
    // Glide 用来加载图片
    "api"(VMDependencies.glide)
    "kapt"(VMDependencies.glideCompiler)
    // 图片选择器
    "api"(VMDependencies.yImagePicker)
    // 展示图片，手势缩放等
    "api"(VMDependencies.photoView)

//    "api"(VMDependencies.hiltAndroid)
//    "kapt"(VMDependencies.hiltAndroidCompiler)

    // 依赖 vmbase/vmcommon/vmrequest
    "implementation"(project(":basic:vmbase"))
    "implementation"(project(":basic:vmcommon"))
    "implementation"(project(":core:vmrequest"))
}

/**
 * ads 广告相关
 */
fun DependencyHandlerScope.adsDependencies() {
    /**
     * 这里依赖要根据渠道不同加载不同的依赖，国内都是本地aar，国外环境直接远程依赖
     * https://docs.toponad.com/#/zh-cn/android/download/package?_t=tdGuaLczpgIXAmXLua6QxX6y93c983UW
     */
//    add{
//        "developImplementation",mapOf("name" to "anythink_banner", "ext" to "aar")
//    }
    // 国内环境依赖 需要 developImplementation 前缀
//    developImplementation(fileTree(dir: "libs", include: ["*.jar", "*.aar"]))
    "developImplementation"(mapOf("name" to "anythink_banner", "ext" to "aar"))
    "developImplementation"(mapOf("name" to "anythink_china_core", "ext" to "aar"))
    "developImplementation"(mapOf("name" to "anythink_core", "ext" to "aar"))
    "developImplementation"(mapOf("name" to "anythink_interstitial", "ext" to "aar"))
    "developImplementation"(mapOf("name" to "anythink_native", "ext" to "aar"))
    "developImplementation"(mapOf("name" to "anythink_network_mintegral_china", "ext" to "aar"))
    "developImplementation"(mapOf("name" to "anythink_network_sigmob", "ext" to "aar"))
    "developImplementation"(mapOf("name" to "anythink_rewardvideo", "ext" to "aar"))
    "developImplementation"(mapOf("name" to "anythink_splash", "ext" to "aar"))

    // Mintegral
    "developImplementation"(mapOf("name" to "mbridge_chinasame", "ext" to "aar"))
    "developImplementation"(mapOf("name" to "mbridge_dycreator", "ext" to "aar"))
    "developImplementation"(mapOf("name" to "mbridge_interstitial", "ext" to "aar"))
    "developImplementation"(mapOf("name" to "mbridge_mbbanner", "ext" to "aar"))
    "developImplementation"(mapOf("name" to "mbridge_mbbid", "ext" to "aar"))
    "developImplementation"(mapOf("name" to "mbridge_mbjscommon", "ext" to "aar"))
    "developImplementation"(mapOf("name" to "mbridge_mbnative", "ext" to "aar"))
    "developImplementation"(mapOf("name" to "mbridge_mbnativeadvanced", "ext" to "aar"))
    "developImplementation"(mapOf("name" to "mbridge_mbsplash", "ext" to "aar"))
    "developImplementation"(mapOf("name" to "mbridge_nativeex", "ext" to "aar"))
    "developImplementation"(mapOf("name" to "mbridge_newinterstitial", "ext" to "aar"))
    "developImplementation"(mapOf("name" to "mbridge_playercommon", "ext" to "aar"))
    "developImplementation"(mapOf("name" to "mbridge_reward", "ext" to "aar"))
    "developImplementation"(mapOf("name" to "mbridge_videocommon", "ext" to "aar"))
    "developImplementation"(mapOf("name" to "mbridge_videojs", "ext" to "aar"))

    // Sigmob
    "developImplementation"(mapOf("name" to "tramini_sdk", "ext" to "aar"))
    "developImplementation"(mapOf("name" to "wind-common-1.4.2", "ext" to "aar"))
    "developImplementation"(mapOf("name" to "wind-sdk-4.9.0", "ext" to "aar"))

    // 海外环境依赖 需要 googlePlayImplementation 前缀
    //Anythink (Necessary)
    "googlePlayImplementation"("com.anythink.sdk:core:6.2.21")
    "googlePlayImplementation"("com.anythink.sdk:nativead:6.2.21")
    "googlePlayImplementation"("com.anythink.sdk:banner:6.2.21")
    "googlePlayImplementation"("com.anythink.sdk:interstitial:6.2.21")
    "googlePlayImplementation"("com.anythink.sdk:rewardedvideo:6.2.21")
    "googlePlayImplementation"("com.anythink.sdk:splash:6.2.21")

    //Admob
    "googlePlayImplementation"("com.anythink.sdk:adapter-admob:6.2.21")
    "googlePlayImplementation"("com.google.android.gms:play-services-ads:22.1.0")

    //Mintegral
    "googlePlayImplementation"("com.anythink.sdk:adapter-mintegral-nonchina:6.2.21")
    "googlePlayImplementation"("com.mbridge.msdk.oversea:reward:16.4.31")
    "googlePlayImplementation"("com.mbridge.msdk.oversea:newinterstitial:16.4.31")
    "googlePlayImplementation"("com.mbridge.msdk.oversea:mbnative:16.4.31")
    "googlePlayImplementation"("com.mbridge.msdk.oversea:mbnativeadvanced:16.4.31")
    "googlePlayImplementation"("com.mbridge.msdk.oversea:mbsplash:16.4.31")
    "googlePlayImplementation"("com.mbridge.msdk.oversea:mbbanner:16.4.31")
    "googlePlayImplementation"("com.mbridge.msdk.oversea:mbbid:16.4.31")

    //Tramini
    "googlePlayImplementation"("com.anythink.sdk:tramini-plugin:6.2.21")

    // 基础模块
    "implementation"(project(":basic:vmbase")) // 依赖 base
    "implementation"(project(":basic:vmcommon")) // 依赖 common
    // 核心模块
    "implementation"(project(":core:vmimage")) // 依赖 image
}

/**
 * 数据库相关
 */
fun DependencyHandlerScope.dataDependencies() {
    "kapt"(VMDependencies.roomCompiler)

    // 依赖 vmbase/vmcommon/vmdb/vmpay/vmqr/vmrequest
    "implementation"(project(":basic:vmbase"))
    "implementation"(project(":basic:vmcommon"))

    "implementation"(project(":core:vmdb"))
    "implementation"(project(":core:vmpay"))
    "implementation"(project(":core:vmqr"))
    "implementation"(project(":core:vmrequest"))
}

/**
 * 数据库相关
 */
fun DependencyHandlerScope.dbDependencies() {
    "api"(VMDependencies.wcdb)
    "api"(VMDependencies.wcdbRoom)
    "kapt"(VMDependencies.roomCompiler)
    "api"(VMDependencies.roomKtx)
    // 依赖 vmbase
    "implementation"(project(":basic:vmbase"))
}

/**
 * 礼物相关
 */
fun DependencyHandlerScope.giftDependencies() {
    // 依赖 vmbase/vmcommon/vmdata/vmimage/vmrequest
    "implementation"(project(":basic:vmbase"))
    "implementation"(project(":basic:vmcommon"))

    "implementation"(project(":core:vmdata"))
    "implementation"(project(":core:vmimage"))
    "implementation"(project(":core:vmrequest"))
}

/**
 * 支付相关
 */
fun DependencyHandlerScope.payDependencies() {
    "implementation"(VMDependencies.alipay)

    // 依赖 vmcommon/vmrequest
    "implementation"(project(":basic:vmcommon"))

    "implementation"(project(":core:vmrequest"))

}

/**
 * 推送相关
 */
fun DependencyHandlerScope.pushDependencies() {
    /**
     * 这里依赖要根据渠道不同加载不同的依赖
     * https://docs.getui.com/getui/mobile/android/androidstudio/
     */
    // 国内环境依赖
    "developImplementation"(VMDependencies.gt) // 个推SDK
    "developImplementation"(VMDependencies.gtCore) // 个推核心组件

    // 国外环境依赖 因为海外和国内sdk仓库不同，这里不能同时使用远程依赖
    "googlePlayImplementation"(VMDependencies.gtGooglePlay)
    // 海外环境依赖
//    googlePlayApi(fileTree(dir: "libs", include: ["*.jar", "*.aar"]))

    // 依赖 vmbase
    "implementation"(project(":basic:vmbase"))
}

/**
 * 二维码相关
 */
fun DependencyHandlerScope.qrDependencies() {
    "implementation"(VMDependencies.zXing)

    // 依赖 vmbase
    "implementation"(project(":basic:vmbase"))
    "implementation"(project(":basic:vmcommon"))

    "implementation"(project(":core:vmimage"))
}

/**
 * 上报相关
 */
fun DependencyHandlerScope.reportDependencies() {
    "implementation"(VMDependencies.bugly)
    "implementation"(VMDependencies.umengCommon)
    "implementation"(VMDependencies.umengAsms)
//    "implementation"(VMDependencies.umengUYuMao)
//    "implementation"(VMDependencies.umengABTest)

    // 依赖 vmbase
    "implementation"(project(":basic:vmbase"))
    "implementation"(project(":basic:vmcommon"))
}

/**
 * 网络请求
 */
fun DependencyHandlerScope.requestDependencies() {
    // 网络请求
    "api"(VMDependencies.retrofit)
    "api"(VMDependencies.converterGson)
    "api"(VMDependencies.loggingInterceptor)
    // 这里依赖是为了解析请求结果
    "api"(VMDependencies.gson)

    // 依赖 vmbase/vmcommon
    "implementation"(project(":basic:vmbase"))
    "implementation"(project(":basic:vmcommon"))

}

/**
 * im相关
 */
fun DependencyHandlerScope.imDependencies() {
    "api"(VMDependencies.im)
    "implementation"(VMDependencies.rtc)

    // 依赖 vmbase/vmcommon
    "implementation"(project(":basic:vmbase"))
    "implementation"(project(":basic:vmcommon"))

    "implementation"(project(":core:vmdata"))
    "implementation"(project(":core:vmgift"))
    "implementation"(project(":core:vmimage"))
    "implementation"(project(":core:vmrequest"))

}