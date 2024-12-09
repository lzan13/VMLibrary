package com.vmloft.develop.convention.plugin

import com.vmloft.develop.convention.VMDependencies

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

/**
 * Created by lzan13 on 2024/01/25
 * 描述：Library 相关 插件类
 */
class VMLibraryBase : VMLibrary() {

    /**
     * 加载依赖
     */
    override fun loadDependencies(target: Project) {
        super.loadDependencies(target)
        target.dependencies {
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

            // koin 依赖注入
            "api"(VMDependencies.koin)

            "api"(VMDependencies.immersionbar)
            "api"(VMDependencies.immersionbarKtx)

            // 自定义工具库
//            "api"(VMDependencies.vmtools)
            // 源码依赖，开发调试
            "api"(project(":vmtools"))
        }
    }
}