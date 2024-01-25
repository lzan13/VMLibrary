plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.plugin.parcelize")
    // 自定义打包插件，用来统一管理配置和依赖
    id("vmbuild")
}

android {
    namespace = "com.vmloft.develop.library.example"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.vmloft.develop.library.example"
        minSdk = 21
        targetSdk = 34
        versionCode = 180
        versionName = "1.8.0"

    }

    // 签名信息
    signingConfigs {
        // 签名信息，因为签名文件是保密信息，放在一个配置文件中进行读取，这个文件不会再版本库里存在
        create("release") {
            keyAlias = VMSignings.keyAlias
            keyPassword = VMSignings.keyPassword
            storeFile = file(VMSignings.storeFile)
            storePassword = VMSignings.storePassword
        }
    }

    // 编译配置
    buildTypes {
        getByName("debug") {
            // 是否开启混淆
            isMinifyEnabled = false
            // 打包时删除无用资源 依赖于混淆，必须和 minifyEnabled 一起使用
            isShrinkResources = false
            // Debug 打包签名信息，这里和 release 配置相同方便三方登录与分享调试
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("release") {
            // 是否开启混淆
            isMinifyEnabled = true
            // 打包时删除无用资源 依赖于混淆，必须和 minifyEnabled 一起使用
            isShrinkResources = true
            // 设置正式打包的签名
            signingConfig = signingConfigs.getByName("release")
            // 混淆文件
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

//            // 修改生成的 apk 名字，格式为:项目-版本-时间-渠道名.apk
//            android.applicationVariants.all { variant ->
//                variant.outputs.all {
//                    outputFileName = "${rootProject.name}V${defaultConfig.versionName}.${getTime()}." + variant.productFlavors[0].name + ".apk"
//                }
//            }
        }
    }

}

dependencies {
    // 依赖 jar 包
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))
    // 依赖 aar
//    implementation (group="",name="libraryname",ext = "aar")

    // Google 媒体播放器，官方推荐代替 MediaPlayer https://github.com/google/media
    implementation(VMDependencies.media3)

    // 依赖 base 库
    implementation(project(":base:vmbase"))
    // 依赖 common 库
    implementation(project(":base:vmcommon"))
    // 依赖 image 库
    implementation(project(":core:vmimage"))
    // 依赖 qr 库
//    implementation(project(":core:vmqr"))
    // 依赖 report 库
//    implementation(project(":core:vmreport"))
    // 依赖 request 库
//    implementation(project(":core:vmrequest"))
}

//// 获取当前系统时间
//val getTime () {
//    return new Date ().format("yyyyMMddHHmm", TimeZone.getTimeZone("UTC"))
//}


// 引入路由公共配置
apply { from("${rootDir.path}/arouter.gradle") }
// 引入通用公共配置
apply { from("${rootDir.path}/common.gradle") }
