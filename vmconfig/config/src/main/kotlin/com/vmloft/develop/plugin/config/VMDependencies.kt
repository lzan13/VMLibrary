package com.vmloft.develop.plugin.config

/**
 * Created by lzan13 on 2024/01/25
 * 描述：依赖版本管理
 */
object VMDependencies {
    /**
     * kotlin 相关库
     */
    const val coreKtx = "androidx.core:core-ktx:1.10.1"

    // Kotlin 协同程序扩展
    // 介绍 https://www.kotlincn.net/docs/reference/coroutines/coroutines-guide.html
    const val kotlinxCoroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3"

    // 依赖当前平台所对应的平台库
    const val kotlinxCoroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"

    /**
     * AndroidX 库
     */
    const val appCompat = "androidx.appcompat:appcompat:1.6.1"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.4"
    const val coordinatorLayout = "androidx.coordinatorlayout:coordinatorlayout:1.2.0"
    const val localBroadcastManager = "androidx.localbroadcastmanager:localbroadcastmanager:1.1.0"
    const val material = "com.google.android.material:material:1.10.0"

    /**
     * Android Jetpack 相关
     */
    const val activityKtx = "androidx.activity:activity-ktx:1.8.0"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:1.6.2"
    const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:2.2.0"
    const val lifecycleLivedataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:2.6.2"
    const val lifecycleViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2"

    // 依赖注入框架
//    // Koin for Android
//    implementation "org.koin:koin-android:2.0.1"
//    // Koin for Kotlin
//    implementation "org.koin:koin-core:2.0.1"
//    // or Koin for Lifecycle scoping
//    implementation "org.koin:koin-androidx-scope:2.0.1"
//    // or Koin for Android Architecture ViewModel
    const val  koin = "org.koin:koin-androidx-viewmodel:2.0.1"

    // hilt 注解
    const val hiltAndroid = "com.google.dagger:hilt-android:2.48.1"
    const val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:2.48.1"

    // Android消息总线，基于LiveData https://github.com/JeremyLiao/LiveEventBus
    const val liveEventBus = "io.github.jeremyliao:live-event-bus-x:1.8.0"

    // Retrofit https://github.com/square/retrofit
    const val retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
    const val converterGson = "com.squareup.retrofit2:converter-gson:2.9.0"
    const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:4.9.2"

    // Gson 解析 JSON https://github.com/google/gson
    const val gson = "com.google.code.gson:gson:2.10"

    // Glide https://github.com/bumptech/glide
    const val glide = "com.github.bumptech.glide:glide:4.12.0"
    const val glideCompiler = "com.github.bumptech.glide:compiler:4.12.0"

    /**
     * 自定义控件库
     */
    // MultiType RecyclerView 多类型适配器 https://github.com/drakeet/MultiType
    const val multiType = "com.drakeet.multitype:multitype:4.2.0"

    // 强大 WebView 库 https://github.com/J ustson/AgentWeb
    const val agentWeb = "com.github.Justson.AgentWeb:agentweb-core:v4.1.9-androidx"

    // 滚动选择器库 https://github.com/AigeStudio/WheelPicker
    const val wheelPicker = "cn.aigestudio.wheelpicker:WheelPicker:1.1.3"

    // 动画库 https://github.com/LottieFiles/lottie-android
    const val lottie = "com.airbnb.android:lottie:3.7.0"

    // 下拉刷新库 https://github.com/scwang90/SmartRefreshLayout
    const val refreshLayoutKernel = "com.scwang.smart:refresh-layout-kernel:2.0.3"

    // 图片选择器 https://github.com/yangpeixing/YImagePicker
    const val yImagePicker = "com.ypx.yimagepicker:androidx:3.1.4"

    // 可缩放 PhotoView https://github.com/chrisbanes/PhotoView
    const val photoView = "com.github.chrisbanes:PhotoView:2.3.0"

    // Google 媒体播放器，官方推荐代替 MediaPlayer https://github.com/google/media
    const val media3ExoPlayer = "androidx.media3:media3-exoplayer:1.2.0"

    // (这里解释一下DASH（Dynamic Adaptive Streaming over HTTP）即自适应流媒体传输，
    // 什么意思呢，简单概括来说，就是在服务器端提前存好同一内容的不同码率、不同分辨率的多个分片以及相应的描述文件MPD，
    // 客户端在播放时即可以根据自身性能以及网络环境选择最适宜的版本)
    const val media3ExoPlayerDash = "androidx.media3:media3-exoplayer-dash:1.2.0"

    // 播放器 UI 组件 这里暂不需要
    const val media3UI = "androidx.media3:media3-ui:1.2.0"

    // 自定义工具库 https://gitee.com/lzan13/VMLibrary
    // 自定义工具库 https://github.com/lzan13/VMLibrary
    const val vmtools = "com.github.lzan13:VMLibrary:1.8.0"

}