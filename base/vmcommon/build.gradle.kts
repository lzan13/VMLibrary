import com.vmloft.develop.plugin.config.extension.commonDependencies
plugins{
    // 自定义配置插件，用来统一管理配置和依赖
    id("com.vmloft.develop.plugin.config.library")
}
android{
    namespace = "com.vmloft.develop.library.common"
}

dependencies {
    commonDependencies()
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
//    namespace = "com.vmloft.develop.library.common"
//    compileSdk = 34
//
//    defaultConfig {
//        minSdk = 21
//    }
//
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
//    commonDependencies()
//}
