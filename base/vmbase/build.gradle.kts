plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.plugin.parcelize")
    // 自定义打包插件，用来统一管理配置和依赖
    id("vmbuild")
}

android {
    namespace = "com.vmloft.develop.library.base"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
    }
}

dependencies {
    // AndroidX 扩展库
    api("androidx.activity:activity-ktx:1.8.0")
    api("androidx.appcompat:appcompat:1.6.1")
    api("androidx.constraintlayout:constraintlayout:2.1.4")
    api("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    api("androidx.core:core-ktx:1.10.1")
    api("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")
    api("com.google.android.material:material:1.10.0")

//    api "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version"
    // Kotlin 协同程序扩展
    // 介绍 https://www.kotlincn.net/docs/reference/coroutines/coroutines-guide.html
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    // 依赖当前平台所对应的平台库
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // 依赖注入框架
//    // Koin for Android
//    implementation "org.koin:koin-android:2.0.1"
//    // Koin for Kotlin
//    implementation "org.koin:koin-core:2.0.1"
//    // or Koin for Lifecycle scoping
//    implementation "org.koin:koin-androidx-scope:2.0.1"
//    // or Koin for Android Architecture ViewModel
    api("org.koin:koin-androidx-viewmodel:2.0.1")

    // Android Jetpack 相关
    api("androidx.lifecycle:lifecycle-extensions:2.2.0")
    api("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    // MultiType RecyclerView 多类型适配器 https://github.com/drakeet/MultiType
    api("com.drakeet.multitype:multitype:4.2.0")

    // 阿里巴巴 ARouter 库 https://github.com/alibaba/ARouter
    api("com.alibaba:arouter-api:1.5.2")
    // ARouter 编译器，TODO 当代码中有一些自己没看到的错误的时候，ARouter 会拦截错误信息，看不到错误位置，把这行注释掉，重新编译
    kapt("com.alibaba:arouter-compiler:1.5.2")

    // Android消息总线，基于LiveData https://github.com/JeremyLiao/LiveEventBus
    api("io.github.jeremyliao:live-event-bus-x:1.8.0")

    // 自定义工具库 https://gitee.com/lzan13/VMLibrary
    // 自定义工具库 https://github.com/lzan13/VMLibrary
//    api "com.github.lzan13:VMLibrary:1.6.0"
    // 引入自己封装的工具库源码，便与调试
    api(project(":vmtools"))
}

// 引入路由公共配置
apply { from("${rootDir.absolutePath}/arouter.gradle") }
// 引入通用公共配置
apply { from("${rootDir.absolutePath}/common.gradle") }