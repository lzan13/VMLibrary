package com.vmloft.develop.plugin.config.extension

import com.vmloft.develop.plugin.config.VMDependencies

import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project

/**
 * Created by lzan13 on 2024/1/26 11:42
 * 描述：依赖扩展管理
 */

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
    "api"(VMDependencies.wheelPicker)
    "api"(VMDependencies.refreshLayoutKernel)

    // 生命周期 EventBus
    "api"(VMDependencies.liveEventBus)

    // gson 数据解析，这里依赖是为了实现数据解析工具封装
    "api"(VMDependencies.gson)

    // 依赖 vmbase
    "implementation"(project(":base:vmbase"))
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
    "implementation"(project(":base:vmbase"))
    "implementation"(project(":base:vmcommon"))
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
    "implementation"(project(":base:vmbase"))
    "implementation"(project(":base:vmcommon"))
    "implementation"(project(":core:vmrequest"))
}

/**
 * android 相关依赖
 */
fun DependencyHandlerScope.applicationDependencies() {
//    "api"(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))

    // 依赖 vmbase/vmcommon/vmrequest/vmimage
    "implementation"(project(":base:vmbase"))
    "implementation"(project(":base:vmcommon"))
    "implementation"(project(":core:vmimage"))
    "implementation"(project(":core:vmrequest"))

}
