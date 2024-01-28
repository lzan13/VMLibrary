package com.vmloft.develop.plugin.config.plugin

import com.android.build.api.dsl.LibraryExtension

import com.vmloft.develop.plugin.config.VMConfig

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.configure

import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

/**
 * Created by lzan13 on 2024/01/25
 * 描述：发布相关 插件类
 */
class VMPublish : Plugin<Project> {

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
            // 加载发布配置
            loadPublish(target)
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
     * 加载发布配置，不过这里暂时没找到怎么迁移到插件中，
     * TODO 只能暂时写在 build.gradle.kts
     */
    private fun loadPublish(project: Project) {
        // 发布配置
//        afterEvaluate {
//           publishing {
//                publications {
//                    create<MavenPublication>("release") {
//                        println("Components: ${components.names}")
//                        if (components.names.contains("release")) {
//                            from(components["release"])
//                            artifactId = VMConfigs.publishArtifactId // 项目名称（通常为类库模块名称，也可以任意）
//                            group = VMConfigs.publishGroup // 唯一标识（通常为模块包名，也可以任意）
//                            version = VMConfigs.versionName
//                        }
//                    }
//                }
//            }
//        }
    }

    /**
     * 自定义 KotlinOptions 扩展函数
     */
    private fun LibraryExtension.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
        (this as ExtensionAware).extensions.configure("kotlinOptions", block)
    }
}