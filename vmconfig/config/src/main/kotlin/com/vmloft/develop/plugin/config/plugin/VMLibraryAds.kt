package com.vmloft.develop.plugin.config.plugin

import com.android.build.api.dsl.LibraryExtension

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Created by lzan13 on 2024/01/25
 * 描述：Library 相关 插件类
 */
class VMLibraryAds : VMLibrary() {

    /**
     * 加载扩展配置
     */
    override fun loadExtensions(project: Project) {
        super.loadExtensions(project)
        project.extensions.configure<LibraryExtension>() {

            // 配置渠道包
            flavorDimensions += "version"
            productFlavors {
                create("develop") {
                    dimension = "version"
//                    // TopOn 聚合平台配置
//                    buildConfigField("String", "topOnAppId", configs.topOnAppId)
//                    buildConfigField("String", "topOnAppKey", configs.topOnAppKey)
//                    buildConfigField("String", "adsSecKey", configs.adsSecKey)
                }
                create("googlePlay") {
                    dimension = "version"
//                    manifestPlaceholders = [
//                        admobAppId: configs.admobAppId,
//                    ]
//                    // TopOn 聚合平台配置
//                    buildConfigField("String", "topOnAppId", configs.topOnAppIdGP)
//                    buildConfigField("String", "topOnAppKey", configs.topOnAppKeyGP)
//                    buildConfigField("String", "adsSecKey", configs.adsSecKey)
                }
            }

        }
    }

}