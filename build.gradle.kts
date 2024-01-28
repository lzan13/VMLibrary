// Top-level build file where you can add configuration options common to all sub-projects/modules.
// 最新添加插件方式
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("com.android.library") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("org.jetbrains.kotlin.jvm") version "1.9.22" apply false
    // 引入 drouter 插件，不过暂不支持这样
    //    id("io.github.didi") version "1.4.0" apply false
}
// 一些未发布到插件中心的插件需要通过下方方式依赖
buildscript {
    dependencies {
        classpath("io.github.didi:drouter-plugin:1.4.0")
    }
}