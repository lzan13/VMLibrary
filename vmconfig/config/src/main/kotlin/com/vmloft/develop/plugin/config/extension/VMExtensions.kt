package com.vmloft.develop.plugin.config.extension

import com.vmloft.develop.plugin.config.VMDependencies
import org.gradle.kotlin.dsl.DependencyHandlerScope

/**
 * Created by lzan13 on 2024/1/26 11:42
 * 描述：依赖扩展管理
 */

/**
 * 基础依赖
 */
fun DependencyHandlerScope.baseDependencies() {
    // kotlin相关
    "implementation"(VMDependencies.coreKtx)
    "implementation"(VMDependencies.kotlinxCoroutinesCore)
    "implementation"(VMDependencies.kotlinxCoroutinesAndroid)

    //
}

/**
 * android 相关依赖
 */
fun DependencyHandlerScope.androidProject() {
    "implementation"(VMDependencies.coreKtx)
}
