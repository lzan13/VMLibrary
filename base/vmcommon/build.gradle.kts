plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.plugin.parcelize")
    // 自定义打包插件，用来统一管理配置和依赖
    id("vmbuild")
}

android {
    namespace = "com.vmloft.develop.library.common"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

    }
}

dependencies {
    // ARouter 编译器，TODO 当代码中有一些自己没看到的错误的时候，ARouter 会拦截错误信息，看不到错误位置，把这行注释掉，重新编译
    kapt("com.alibaba:arouter-compiler:1.5.2")

//    // Retrofit https://github.com/square/retrofit
//    api "com.squareup.retrofit2:retrofit:2.9.0"
//    api "com.squareup.retrofit2:converter-gson:2.9.0"
//    api "com.squareup.okhttp3:logging-interceptor:4.3.1"
    // Gson 解析 JSON https://github.com/google/gson
    implementation("com.google.code.gson:gson:2.10")

    // 强大 WebView 库 https://github.com/J ustson/AgentWeb
    api("com.github.Justson.AgentWeb:agentweb-core:v4.1.9-androidx")

    // 滚动选择器库 https://github.com/AigeStudio/WheelPicker
    api("cn.aigestudio.wheelpicker:WheelPicker:1.1.3")

    // 动画库 https://github.com/LottieFiles/lottie-android
    api("com.airbnb.android:lottie:3.7.0")

    // 下拉刷新库 https://github.com/scwang90/SmartRefreshLayout
    api("com.scwang.smart:refresh-layout-kernel:2.0.3")

    // 依赖 base 库
    implementation(project(":base:vmbase"))

}

// 引入路由公共配置
apply { from("${rootDir.absolutePath}/arouter.gradle") }
// 引入通用公共配置
apply { from("${rootDir.absolutePath}/common.gradle") }