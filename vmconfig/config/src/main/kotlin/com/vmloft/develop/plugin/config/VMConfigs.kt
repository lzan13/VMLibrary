package com.vmloft.develop.plugin.config

import org.gradle.api.JavaVersion


/**
 * Created by lzan13 on 2024/01/25
 * 描述：自定义配置
 */
object VMConfigs {
    const val applicationId = "com.vmloft.develop.library.example"

    const val buildTools = "34.0.0"
    const val compileSdk = 34
    const val minSdk = 21
    const val targetSdk = 34
    const val versionCode = 181
    const val versionName = "1.8.1"

    const val publishGroup = "com.github.lzan13"
    const val publishArtifactId = "vmtools"

    // BuildFeatures 配置
    const val isBuildConfig = true // 是否启用 BuildConfig Gradle 8.x 默认关闭了
    const val isCompose = false // 是否启用 compose
    const val isViewBinding = true // 是否启用 ViewBinding
    // 查看 compose Compiler 版本 https://developer.android.com/jetpack/androidx/releases/compose?hl=zh-cn
    const val composeCompilerVersion = "1.5.8"

    // Java 版本配置
    val javaVersion = JavaVersion.VERSION_17

    /**
     * 签名文件由 AndroidStudio 生成，然后使用 keytool 命令转换格式
     * keytool -importkeystore -srckeystore ./vmloft.debug.jks -destkeystore ./vmloft.debug.keystore -deststoretype pkcs12
     * 签名配置，这里是默认添加的 debug 签名，方便打包测试，发布时需生成自己的签名文件，记得将签名文件复制到项目中
     */
    const val signingsKeyAlias = "vmloft"
    const val signingsKeyPassword = "123456"
    const val signingsStoreFile = "vmconfig/vmloft.debug.jks"
    const val signingsStorePassword = "123456"

}
