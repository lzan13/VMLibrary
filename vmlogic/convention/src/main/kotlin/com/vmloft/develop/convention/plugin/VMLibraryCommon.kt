package com.vmloft.develop.convention.plugin

import com.vmloft.develop.convention.VMDependencies

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

/**
 * Created by lzan13 on 2024/01/25
 * 描述：Library 相关 插件类
 */
class VMLibraryCommon : VMLibrary() {

    /**
     * 加载依赖
     */
    override fun loadDependencies(target: Project) {
        super.loadDependencies(target)
        target.dependencies {
            // 三方控件
            "api"(VMDependencies.agentWeb)
            "api"(VMDependencies.bannerViewPager)
            "api"(VMDependencies.refreshLayoutKernel)
            "api"(VMDependencies.refreshLayoutTwoLevel)
            "api"(VMDependencies.wheelPicker)

            // 生命周期 EventBus
            "api"(VMDependencies.liveEventBus)

            // gson 数据解析，这里依赖是为了实现数据解析工具封装
            "api"(VMDependencies.gson)

            // 依赖 vmbase
            "implementation"(project(":basic:vmbase"))
        }
    }
}