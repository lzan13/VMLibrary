package com.vmloft.develop.plugin.config.plugin

import com.android.build.api.dsl.LibraryExtension

import com.vmloft.develop.plugin.config.VMConfig

import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.configure

import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

/**
 * Created by lzan13 on 2024/01/25
 * 描述：Library 相关 插件类
 */
open class VMLibrary : VMBase() {

    /**
     * 插件入口
     */
    override fun apply(project: Project) {
        // 加载 gradle 配置
        with(project) {
            // 加载插件
            loadPlugin(pluginManager)
            // 加载扩展配置
            loadExtensions(project)
        }
    }

    /**
     * 加载插件
     */
    override fun loadPlugin(pluginManager: PluginManager) {
        // 加载插件
        with(pluginManager) {
            apply("com.android.library")
            apply("org.jetbrains.kotlin.android")
            apply("org.jetbrains.kotlin.kapt")
            apply("org.jetbrains.kotlin.plugin.parcelize")
            // DRouter 插件，暂时不能在这里边引用，必须放在对应 module 的 build.gradle.kts 里引用
//            apply("com.didi.drouter")
        }
    }

    /**
     * 加载扩展配置
     */
    override fun loadExtensions(project: Project) {
        project.extensions.configure<LibraryExtension>() {
            // 设置 android sdk 相关版本
            compileSdk = VMConfig.compileSdk

            defaultConfig {
                minSdk = VMConfig.minSdk
            }

            buildTypes {
                getByName("release") {
                    isMinifyEnabled = false
                    proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
                    consumerProguardFiles("consumer-rules.pro")
                }
            }

            buildFeatures {
                // 开启 BuildConfig
                buildConfig = VMConfig.isBuildConfig
                // 启用 compose
                compose = VMConfig.isCompose
                // 开启 ViewBinding
                viewBinding = VMConfig.isViewBinding
            }

            // compose 配置
            composeOptions {
                kotlinCompilerExtensionVersion = VMConfig.composeCompilerVersion
            }

            // 配置 Java 编译版本
            compileOptions {
                sourceCompatibility = VMConfig.javaVersion
                targetCompatibility = VMConfig.javaVersion
            }

            // Kotlin 配置 默认没有，需要自己写个扩展
            kotlinOptions {
                jvmTarget = VMConfig.javaVersion.toString()
            }

            lint {
                // 编译忽略错误配置
                abortOnError = false
                // 设置目标 sdk 版本
                targetSdk = VMConfig.targetSdk
            }
        }
    }

    /**
     * 加载依赖
     */
    override fun loadDependencies(project: Project) {

    }

    /**
     * 自定义 KotlinOptions 扩展函数
     */
    fun LibraryExtension.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
        (this as ExtensionAware).extensions.configure("kotlinOptions", block)
    }
}