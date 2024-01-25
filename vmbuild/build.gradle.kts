plugins {
//    id("java-library")
//    id("org.jetbrains.kotlin.jvm")
    `kotlin-dsl`
}
//java {
//    sourceCompatibility = JavaVersion.VERSION_17
//    targetCompatibility = JavaVersion.VERSION_17
//}

//repositories {
//    // 国内仓库镜像
//    maven { setUrl("https://maven.aliyun.com/repository/central")}
//    maven { setUrl("https://maven.aliyun.com/repository/google")}
//    maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin")}
//    maven { setUrl("https://maven.aliyun.com/repository/public")}
//    // jitpack 仓库
//    maven { setUrl("https://jitpack.io")}
//
//    google()
//    mavenCentral()
//    // gradle 插件仓库
//    gradlePluginPortal()
//}

dependencies {
    //添加Gradle相关的API，否则无法自定义Plugin和Task
    implementation(gradleApi())
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
}

// 注册插件
gradlePlugin {
    plugins.register("vmbuild") {
        id = "vmbuild"
        implementationClass = "com.vmloft.develop.plugin.build.VMBuildPlugin"
    }
}
//gradlePlugin {
//    plugins {
//        create("vmbuild") {
//            //添加插件
//            id = "com.vmloft.develop.plugin.build"
//            //在根目录创建类 VersionPlugin 继承 Plugin<Project>
//            implementationClass = "com.vmloft.develop.plugin.build.VMBuildPlugin"
//        }
//
//    }
//}