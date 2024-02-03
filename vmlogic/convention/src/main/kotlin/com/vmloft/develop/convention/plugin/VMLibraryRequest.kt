package com.vmloft.develop.convention.plugin

import com.android.build.api.dsl.LibraryExtension

import com.vmloft.develop.convention.VMConfig
import com.vmloft.develop.convention.VMDependencies

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

/**
 * Created by lzan13 on 2024/01/25
 * 描述：Library 相关 插件类
 */
class VMLibraryRequest : VMLibrary() {

    /**
     * 加载扩展配置
     */
    override fun loadExtensions(project: Project) {
        super.loadExtensions(project)
        project.extensions.configure<LibraryExtension>() {
            defaultConfig {
                // 通过配置文件设置请求 API 地址
                buildConfigField("String", "baseDebugUrl", VMConfig.baseUrlDebug)
                buildConfigField("String", "baseReleaseUrl", VMConfig.baseUrlRelease)
                // 资源文件 url 地址
                buildConfigField("String", "mediaUrl", VMConfig.mediaUrl)
            }
        }
    }

    /**
     * 加载依赖
     */
    override fun loadDependencies(target: Project) {
        super.loadDependencies(target)
        target.dependencies {
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
    }
}