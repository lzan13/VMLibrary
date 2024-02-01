import com.vmloft.develop.convention.extension.requestDependencies
plugins{
    // 自定义配置插件，用来统一管理配置和依赖
    id("com.vmloft.develop.plugin.library")
}
android{
    namespace = "com.vmloft.develop.library.request"
}

dependencies {
    requestDependencies()
}

//plugins {
//    id("com.android.library")
//    id("org.jetbrains.kotlin.android")
//    id("org.jetbrains.kotlin.kapt")
//    id("org.jetbrains.kotlin.plugin.parcelize")
//    // 自定义打包插件，用来统一管理配置和依赖
//    id("com.vmloft.develop.plugin.config.library")
//}
//
//android {
//    namespace ="com.vmloft.develop.library.request"
//    compileSdk = 34
//
//    defaultConfig {
//        minSdk = 21
//
////        // 通过配置文件设置请求 API 地址
////        buildConfigField("String", "baseDebugUrl", configs.baseDebugUrl)
////        buildConfigField("String", "baseReleaseUrl", configs.baseReleaseUrl)
////        // 资源文件 url 地址
////        buildConfigField("String", "mediaUrl", configs.mediaUrl)
//    }
//
//    buildFeatures {
//        // gradle 8.x 默认关闭了 buildConfig
//        buildConfig = true
//        // 开启 ViewBinding
//        viewBinding = true
//    }
//
//    // 配置 Java 编译版本
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_17
//        targetCompatibility = JavaVersion.VERSION_17
//    }
////
////    // 编译忽略错误配置
////    lintOptions {
////        abortOnError = false
////    }
//    // kotlin 配置
//    kotlinOptions {
//        jvmTarget = JavaVersion.VERSION_17.toString()
//    }
//}
//
//dependencies {
//    requestDependencies()
//}