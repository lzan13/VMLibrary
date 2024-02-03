package com.vmloft.develop.convention

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
    const val koin = "org.koin:koin-androidx-viewmodel:2.0.1"

    // hilt 注解
    const val hiltAndroid = "com.google.dagger:hilt-android:2.48.1"
    const val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:2.48.1"

    // Android消息总线，基于LiveData https://github.com/JeremyLiao/LiveEventBus
    const val liveEventBus = "io.github.jeremyliao:live-event-bus-x:1.8.0"

    // 引用 Room 数据库操作库
//    implementation "androidx.room:room-runtime:2.4.2"
    // WCDB 数据库 https://github.com/Tencent/wcdb
    const val wcdb = "com.tencent.wcdb:wcdb-android:1.0.8"

    // WCDB room 代替上边 room-runtime https://github.com/Tencent/wcdb
    const val wcdbRoom = "com.tencent.wcdb:room:1.0.8"
    const val roomCompiler = "androidx.room:room-compiler:2.6.1"
    const val roomKtx = "androidx.room:room-ktx:2.6.1"

    // Glide https://github.com/bumptech/glide
    const val glide = "com.github.bumptech.glide:glide:4.12.0"
    const val glideCompiler = "com.github.bumptech.glide:compiler:4.12.0"

    // DRouter https://github.com/didi/DRouter/wiki
    const val dRouter = "io.github.didi:drouter-api:2.4.5"

    // 阿里云支付
    const val alipay = "com.alipay.sdk:alipaysdk-android:15.8.17@aar"

    /**
     * 推送相关
     * 这里依赖要根据渠道不同加载不同的依赖
     * https://docs.getui.com/getui/mobile/android/androidstudio/
     */
    // 国内环境依赖
    const val gt = "com.getui:gtsdk:3.2.10.0"  //个推SDK
    const val gtCore = "com.getui:gtc:3.1.9.0"  //个推核心组件

    // 国外环境依赖 因为海外和国内sdk仓库不同，这里不能同时使用远程依赖
    const val gtGooglePlay = "com.getui:sdk-for-google-play:4.3.9.0"

    /**
     * 二维码扫描
     */
    const val zXing = "com.github.bingoogolapple.BGAQRCode-Android:zxing:1.3.8"

    /**
     * 上报组件
     */
    // Bugly https://bugly.qq.com/docs/user-guide/instruction-manual-android/?v=20200203205953
    const val bugly = "com.tencent.bugly:crashreport:4.0.4"

    // umeng sdk https://developer.umeng.com/docs/119267/detail/118584
    const  val umengCommon = "com.umeng.umsdk:common:9.6.3" // 必选
    const  val umengAsms = "com.umeng.umsdk:asms:1.8.0" // 必选
    // 高级运营分析功能依赖库（可选）。使用卸载分析、开启反作弊能力请务必集成，以免影响高级功能使用。
    // common需搭配v9.6.3及以上版本，asms需搭配v1.7.0及以上版本。需更新隐私声明。需配置混淆，以避免依赖库无法生效，见本文下方【混淆设置】部分。
    const val umengUYuMao = "com.umeng.umsdk:uyumao:1.1.2"
    const  val umengABTest = "com.umeng.umsdk:abtest:1.0.0" //使用U-App中ABTest能力（可选）

    /**
     * 网络请求
     */
    // OKHttp https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
    const val okHttp = "com.squareup.okhttp3:okhttp:4.12.0"

    // Retrofit https://github.com/square/retrofit
    const val retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
    const val converterGson = "com.squareup.retrofit2:converter-gson:2.9.0"
    const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:4.9.2"

    // Gson 解析 JSON https://github.com/google/gson
    const val gson = "com.google.code.gson:gson:2.10"

    /**
     * 即时通讯
     */
    // 环信聊天 IM 库 Easemob https://www.easemob.com/download/im
    const val im = "io.hyphenate:hyphenate-chat:3.8.9.1"

    // 声网音视频通话 https://docs.agora.io/cn/Voice/start_call_audio_android?platform=Android
    const val rtc = "io.agora.rtc:voice-sdk:3.7.0.2"

    /**
     * 自定义控件库
     */
    // 强大 WebView 库 https://github.com/J ustson/AgentWeb
    const val agentWeb = "com.github.Justson.AgentWeb:agentweb-core:v4.1.9-androidx"

    // 轮播控件 https://github.com/zhpanvip/BannerViewPager
    const val bannerViewPager = "com.github.zhpanvip:bannerviewpager:3.5.12"

    // 动画库 https://github.com/LottieFiles/lottie-android
    const val lottie = "com.airbnb.android:lottie:3.7.0"

    // MultiType RecyclerView 多类型适配器 https://github.com/drakeet/MultiType
    const val multiType = "com.drakeet.multitype:multitype:4.2.0"

    // 下拉刷新库 https://github.com/scwang90/SmartRefreshLayout
    const val refreshLayoutKernel = "io.github.scwang90:refresh-layout-kernel:2.1.0"
    const val refreshLayoutTwoLevel = "io.github.scwang90:refresh-header-two-level:2.1.0"

    // 滚动选择器库 https://github.com/AigeStudio/WheelPicker
    const val wheelPicker = "cn.aigestudio.wheelpicker:WheelPicker:1.1.3"

    // 图片选择器 https://github.com/yangpeixing/YImagePicker
    const val yImagePicker = "com.ypx.yimagepicker:androidx:3.1.4"

    // 可缩放 PhotoView https://github.com/chrisbanes/PhotoView
    const val photoView = "com.github.chrisbanes:PhotoView:2.3.0"

    /**
     * 多媒体库
     */
    // Google 媒体播放器，官方推荐代替 MediaPlayer https://github.com/google/media
    const val media3ExoPlayer = "androidx.media3:media3-exoplayer:1.2.0"

    // (这里解释一下DASH（Dynamic Adaptive Streaming over HTTP）即自适应流媒体传输，
    // 什么意思呢，简单概括来说，就是在服务器端提前存好同一内容的不同码率、不同分辨率的多个分片以及相应的描述文件MPD，
    // 客户端在播放时即可以根据自身性能以及网络环境选择最适宜的版本)
    const val media3ExoPlayerDash = "androidx.media3:media3-exoplayer-dash:1.2.0"

    // 播放器 UI 组件 这里暂不需要
    const val media3UI = "androidx.media3:media3-ui:1.2.0"

    // webrtc 库
    const val webrtc = "org.webrtc:google-webrtc:1.0.32006"

    // 自定义工具库 https://gitee.com/lzan13/VMLibrary
    // 自定义工具库 https://github.com/lzan13/VMLibrary
    const val vmtools = "com.github.lzan13:VMLibrary:1.8.3"

}