plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.plugin.parcelize")
    // 自定义打包插件，用来统一管理配置和依赖
    id("vmbuild")
}

android {
    namespace = "com.vmloft.develop.library.image"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
    }
}

dependencies {
    // ARouter 编译器，TODO 当代码中有一些自己没看到的错误的时候，ARouter 会拦截错误信息，看不到错误位置，把这行注释掉，重新编译
    kapt("com.alibaba:arouter-compiler:1.5.2")

    // Glide https://github.com/bumptech/glide
    api("com.github.bumptech.glide:glide:4.12.0")
    kapt("com.github.bumptech.glide:compiler:4.12.0")

    // 图片选择器 https://github.com/yangpeixing/YImagePicker
    api("com.ypx.yimagepicker:androidx:3.1.4")

    // 可缩放 PhotoView https://github.com/chrisbanes/PhotoView
    api("com.github.chrisbanes:PhotoView:2.3.0")

    // 依赖 base 库
    implementation(project(":base:vmbase"))
    // 依赖 common 库
    implementation(project(":base:vmcommon"))
    // 依赖 request 库 用到了里边的 mediaHost()
    compileOnly(project(":core:vmrequest"))

}

// 引入路由公共配置
apply { from("${rootDir.absolutePath}/arouter.gradle") }
// 引入通用公共配置
apply { from("${rootDir.absolutePath}/common.gradle") }