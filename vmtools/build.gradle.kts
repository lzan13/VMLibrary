import com.vmloft.develop.plugin.config.VMDependencies

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("maven-publish")
    // 自定义打包插件，用来统一管理配置和依赖
    id("com.vmloft.develop.plugin.config.library")
}

android {
    namespace = "com.vmloft.develop.library.tools"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
    }

//    sourceSets.main {
//        jni.srcDirs = [] // 设置 jni 源码目录，不设置会自动生成
//        jniLibs.srcDir = "src/main/jniLibs" // 设置 so 库目录
//    }
}
dependencies {

    implementation(VMDependencies.coreKtx)
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.10.0")

    // Google 媒体播放器，官方推荐代替 MediaPlayer https://github.com/google/media
    implementation("androidx.media3:media3-exoplayer:1.2.0")
    // (这里解释一下DASH（Dynamic Adaptive Streaming over HTTP）即自适应流媒体传输，
    // 什么意思呢，简单概括来说，就是在服务器端提前存好同一内容的不同码率、不同分辨率的多个分片以及相应的描述文件MPD，
    // 客户端在播放时即可以根据自身性能以及网络环境选择最适宜的版本)
//    implementation("androidx.media3:media3-exoplayer-dash:1.X.X")
    // 播放器 UI 组件 这里暂不需要
//    implementation("androidx.media3:media3-ui:1.X.X")
}

/**
 * 发布配置
 */
//afterEvaluate {
//    publishing {
//        publications {
//            release(MavenPublication) {
//                from components.release
//                groupId = "com.github.lzan13"
//                artifactId = "vmtools"
//                version = configs.versionName
//            }
//        }
//    }
//}

// 引入通用公共配置
//apply { from("${rootDir.absolutePath}/common.gradle") }