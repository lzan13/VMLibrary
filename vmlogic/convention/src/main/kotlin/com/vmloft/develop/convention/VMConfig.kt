package com.vmloft.develop.convention

import org.gradle.api.JavaVersion

/**
 * Created by lzan13 on 2024/01/25
 * 描述：自定义配置
 */
object VMConfig {
    const val applicationId = "com.vmloft.develop.app.example"

    const val buildTools = "34.0.0"
    const val compileSdk = 34
    const val minSdk = 21
    const val targetSdk = 34
    const val versionCode = 189
    const val versionName = "1.8.9"

    // 发布到 jitpack 仓库所需配置，一般项目不需要
    const val publishGroup = "com.github.lzan13"
    const val publishArtifactId = "vmtools"

    // BuildFeatures 配置
    const val isBuildConfig = true // 是否启用 BuildConfig Gradle 8.x 默认关闭了 9.0将移除
    const val isResValues = false // 是否启用 resValues // 用来代替 buildConfigField
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
    const val signingsStoreFile = "vmlogic/vmloft.debug.jks"
    const val signingsStorePassword = "123456"


    // 然后这里地址需要和服务器配置一致，看服务器项目的 default 配置 subSite 字段，如果是二级目录需要带上完整的路径如：https://xxx.com/api/
    // 调试服务器环境配置，这里的IP是和你设备同路由下服务器 IP
//    const val baseUrlDebug = "\"http://172.16.184.68:5920/\""
//    const val baseUrlDebug = "\"http://172.16.180.166:5920/\""
//    const val baseUrlDebug = "\"http://192.168.31.10:5920/\""
    const val baseUrlDebug = "\"http://api.nepenthe.vmloft.com/\""

    // 线上服务器地址 对应服务器nginx反向代理 domain + api 部分
//    const val baseUrlRelease = "\"http://api.nepenthe.vmloft.com/\""
    const val baseUrlRelease = "\"https://api.nepenthe.vmloft.com/\""

    // IM Debug 环境 host 配置，这里的IP是和你设备同路由下服务器 IP
    const val imHostDebug = "\"ws://172.16.184.68:5920/\""

    //    const val imHostDebug = "\"ws://172.16.180.166:5920/\""
//    const val imHostDebug = "\"ws://192.168.31.107:5920/\""            // IM 线上环境 host 配置 对应服务器nginx反向代理 domain + im 部分
    const val imHostRelease = "\"wss://nepenthe.vmloft.com\""

    // 访问资源域名地址 这里需要写入 BuildConfig 只会写入双引号内部字段，所以需要多包一层""
    const val mediaUrl = "\"https://data.nepenthe.vmloft.com\""
}
