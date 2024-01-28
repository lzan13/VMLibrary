plugins {
    // 自定义配置插件，用来统一管理配置和依赖
    id("com.vmloft.develop.plugin.config.application")
    // DRouter 插件，暂时不能放在自定义插件里，必须在这里引用，否则会报找不到插件
//    id("com.didi.drouter")
}
android {
    namespace = "com.vmloft.develop.app.example"
}
