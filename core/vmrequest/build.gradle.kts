plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.plugin.parcelize")
    // 自定义打包插件，用来统一管理配置和依赖
    id("vmbuild")
}

android {
    namespace ="com.vmloft.develop.library.request"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

//        // 通过配置文件设置请求 API 地址
//        buildConfigField("String", "baseDebugUrl", configs.baseDebugUrl)
//        buildConfigField("String", "baseReleaseUrl", configs.baseReleaseUrl)
//        // 资源文件 url 地址
//        buildConfigField("String", "mediaUrl", configs.mediaUrl)
    }
}

dependencies {
    // Retrofit https://github.com/square/retrofit
    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-gson:2.9.0")
    api("com.squareup.okhttp3:logging-interceptor:4.3.1")
    // Gson 解析 JSON https://github.com/google/gson
    api("com.google.code.gson:gson:2.8.6")

    // 依赖 base 库
    implementation(project(":base:vmbase"))
    // 依赖 common 库
    implementation(project(":base:vmcommon"))
}

// 引入路由公共配置
apply { from("${rootDir.absolutePath}/arouter.gradle") }
// 引入通用公共配置
apply { from("${rootDir.absolutePath}/common.gradle") }
