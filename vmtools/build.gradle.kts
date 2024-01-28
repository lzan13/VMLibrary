import com.vmloft.develop.plugin.config.VMConfig
import com.vmloft.develop.plugin.config.VMDependencies

plugins {
    `maven-publish`
    // 自定义打包插件，用来统一管理配置和依赖
    id("com.vmloft.develop.plugin.config.publish")
}
android {
    namespace = "com.vmloft.develop.library.tools"
}

dependencies {
    implementation(VMDependencies.coreKtx)
    implementation(VMDependencies.lifecycleExtensions)
    implementation(VMDependencies.constraintLayout)
    implementation(VMDependencies.material)

    // 官方推荐代替 MediaPlayer 播放库
    implementation(VMDependencies.media3ExoPlayer)
}
// 发布配置
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                println("Components: ${components.names}")
                if (components.names.contains("release")) {
                    from(components["release"])
                    artifactId = VMConfig.publishArtifactId // 项目名称（通常为类库模块名称，也可以任意）
                    group = VMConfig.publishGroup // 唯一标识（通常为模块包名，也可以任意）
                    version = VMConfig.versionName
                }
            }
        }
    }
}

tasks.register("comps") {
    println("Components: ${components.names}")
}

//plugins {
//    id("com.android.library")
//    id("org.jetbrains.kotlin.android")
//    id("org.jetbrains.kotlin.kapt")
//    id("org.jetbrains.kotlin.plugin.parcelize")
//    id("maven-publish")
//    // 自定义打包插件，用来统一管理配置和依赖
////    id("com.vmloft.develop.plugin.config.library")
//}
//
//android {
//    namespace = "com.vmloft.develop.library.tools"
//    compileSdk = 34
//
//    defaultConfig {
//        minSdk = 21
//    }
//    publishing {
//        singleVariant("release") {
//            withSourcesJar()
//            withJavadocJar()
//        }
//    }
////    sourceSets.main {
////        jni.srcDirs = [] // 设置 jni 源码目录，不设置会自动生成
////        jniLibs.srcDir = "src/main/jniLibs" // 设置 so 库目录
////    }
//    buildTypes {
//        getByName("release") {
//            isMinifyEnabled = false
//            // 配置 Java 编译版本
//            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
//            consumerProguardFiles("consumer-rules.pro")
//        }
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
//
//    // kotlin 配置
//    kotlinOptions {
//        jvmTarget = JavaVersion.VERSION_17.toString()
//    }
//}
//
//dependencies {
////    implementation(VMDependencies.coreKtx)
////    implementation(VMDependencies.lifecycleExtensions)
////    implementation(VMDependencies.constraintLayout)
////    implementation(VMDependencies.material)
////
////    // 官方推荐代替 MediaPlayer 播放库
////    implementation(VMDependencies.media3ExoPlayer)
//}
//// 发布配置
//publishing {
//    publications {
//        create<MavenPublication>("release") {
//            println("Components: ${components.names}")
//            components.forEach {
//                println(it.name)
//                if (it.name == "release") {
////                    if (components.names.contains("release")) {
////                        from(components["release"])
////                        artifactId = VMConfig.publishArtifactId // 项目名称（通常为类库模块名称，也可以任意）
////                        group = VMConfig.publishGroup // 唯一标识（通常为模块包名，也可以任意）
////                        version = VMConfig.versionName
////                    }
//                }
//            }
//        }
//    }
//}
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
