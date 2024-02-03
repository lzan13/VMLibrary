package com.vmloft.develop.convention.plugin

import com.vmloft.develop.convention.VMDependencies

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

/**
 * Created by lzan13 on 2024/01/25
 * 描述：Library 相关 插件类
 */
class VMLibraryImage : VMLibrary() {

    /**
     * 加载依赖
     */
    override fun loadDependencies(target: Project) {
        super.loadDependencies(target)
        target.dependencies {

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
    }
}