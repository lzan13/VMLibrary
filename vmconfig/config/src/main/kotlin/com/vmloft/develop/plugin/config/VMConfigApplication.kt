package com.vmloft.develop.plugin.config

import com.android.build.api.dsl.ApplicationExtension

import com.vmloft.develop.plugin.config.extension.applicationDependencies

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

import java.io.File

/**
 * Created by lzan13 on 2024/01/25
 * 描述：Application 相关 插件类
 */
class VMConfigApplication : Plugin<Project> {
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
            // 加载依赖
            loadDependencies(target)
        }
    }

    /**
     * 加载插件
     */
    private fun loadPlugin(pluginManager: PluginManager) {
        // 加载插件
        with(pluginManager) {
            apply("com.android.application")
            apply("org.jetbrains.kotlin.android")
            apply("org.jetbrains.kotlin.kapt")
            apply("org.jetbrains.kotlin.plugin.parcelize")
        }
    }

    /**
     * 加载扩展配置
     */
    private fun loadExtensions(extensions: ExtensionContainer) {
        extensions.configure<ApplicationExtension>() {
            // 设置 android sdk 相关版本
            compileSdk = VMConfigs.compileSdk

            defaultConfig {
                applicationId = VMConfigs.applicationId
                minSdk = VMConfigs.minSdk
                targetSdk = VMConfigs.targetSdk

                versionCode = VMConfigs.versionCode
                versionName = VMConfigs.versionName
            }
            // 签名信息
            signingConfigs {
                // 签名信息，因为签名文件是保密信息，放在一个配置文件中进行读取，这个文件不会再版本库里存在
                create("release") {
                    keyAlias = VMConfigs.signingsKeyAlias
                    keyPassword = VMConfigs.signingsKeyPassword
                    storeFile = File(VMConfigs.signingsStoreFile)
                    storePassword = VMConfigs.signingsStorePassword
                }
            }

            buildTypes {
                getByName("debug") {
                    // 是否开启混淆
                    isMinifyEnabled = false
                    // 打包时删除无用资源 依赖于混淆，必须和 minifyEnabled 一起使用
                    isShrinkResources = false
                    // Debug 打包签名信息，这里和 release 配置相同方便三方登录与分享调试
                    signingConfig = signingConfigs.getByName("release")
                }
                getByName("release") {
                    // 是否开启混淆
                    isMinifyEnabled = true
                    // 打包时删除无用资源 依赖于混淆，必须和 minifyEnabled 一起使用
                    isShrinkResources = true
                    // 设置正式打包的签名
                    signingConfig = signingConfigs.getByName("release")
                    // 混淆文件
                    proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

//                    // 修改生成的 apk 名字，格式为:项目-版本-时间-渠道名.apk
//                    android.applicationVariants.all { variant ->
//                        variant.outputs.all {
//                            outputFileName = "${rootProject.name}V${defaultConfig.versionName}.${getTime()}." + variant.productFlavors[0].name + ".apk"
//                        }
//                    }
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
     * 加载依赖
     */
    private fun loadDependencies(target: Project) {
        target.dependencies {
            applicationDependencies()
        }
    }

    /**
     * 自定义 KotlinOptions 扩展函数
     */
    private fun ApplicationExtension.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
        (this as ExtensionAware).extensions.configure("kotlinOptions", block)
    }
}