// Top-level build file where you can add configuration options common to all sub-projects/modules.
//buildscript {
//    from("config.gradle.kts")
//    // 三方插件添加方式 参考 https://blog.csdn.net/u013762572/article/details/124775166
////    dependencies{
////        classpath "io.github.didi:drouter-plugin:1.4.0"
////    }
//}
// 最新添加插件方式
plugins {
    id("com.android.application") version "8.2.1" apply false
    id("com.android.library") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.21" apply false
    id("org.jetbrains.kotlin.jvm") version "1.9.21" apply false
}
