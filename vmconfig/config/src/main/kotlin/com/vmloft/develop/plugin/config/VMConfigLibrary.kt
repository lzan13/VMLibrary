package com.vmloft.develop.plugin.config

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.utils.createPublishingInfoForLibrary

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.configure

import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

/**
 * Created by lzan13 on 2024/01/25
 * 描述：Library 插件类
 */
class VMConfigLibrary : Plugin<Project> {

    /**
     * 插件入口
     */
    override fun apply(target: Project) {
        // 加载 gradle 配置
        with(target) {
            // 加载插件
            loadPlugin(pluginManager)
            // 加载扩展配置
            loadExtensions(extensions)
        }
    }

    /**
     * 加载插件
     */
    private fun loadPlugin(pluginManager: PluginManager) {
        // 加载插件
        with(pluginManager) {
            apply("com.android.library")
            apply("org.jetbrains.kotlin.android")
            apply("org.jetbrains.kotlin.kapt")
            apply("org.jetbrains.kotlin.plugin.parcelize")
        }
    }

    /**
     * 加载扩展配置
     */
    private fun loadExtensions(extensions: ExtensionContainer) {
        extensions.configure<LibraryExtension>() {
            // 设置 android sdk 相关版本
            compileSdk = VMConfigs.compileSdk

            defaultConfig {
                minSdk = VMConfigs.minSdk
                targetSdk = VMConfigs.targetSdk
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
                buildConfig = VMConfigs.isBuildConfig
                // 启用 compose
                compose = VMConfigs.isCompose
                // 开启 ViewBinding
                viewBinding = VMConfigs.isViewBinding
            }

            // compose 配置
            composeOptions {
                kotlinCompilerExtensionVersion = VMConfigs.composeCompilerVersion
            }

            // 配置 Java 编译版本
            compileOptions {
                sourceCompatibility = VMConfigs.javaVersion
                targetCompatibility = VMConfigs.javaVersion
            }

            // Kotlin 配置 默认没有，需要自己写个扩展
            kotlinOptions {
                jvmTarget = VMConfigs.javaVersion.toString()
            }

            lint {
                // 编译忽略错误配置
                abortOnError = false
            }
        }
    }

    /**
     * 自定义 KotlinOptions 扩展函数
     */
    private fun LibraryExtension.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
        (this as ExtensionAware).extensions.configure("kotlinOptions", block)
    }
}