plugins {
    `kotlin-dsl`
}

// 配置 Java 编译版本
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}


// 添加插件依赖
dependencies {
    implementation(gradleApi())
    implementation(gradleKotlinDsl())
    implementation("com.android.tools.build:gradle:8.2.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
}

// 注册插件
gradlePlugin {
    // 声明插件信息，这里的 register 参数可随意填写，摘要应用在 id
    plugins.register("vmApplication") {
        // 定义插件id
        id = "com.vmloft.develop.plugin.config.application"
        // 插件实现类
        implementationClass = "com.vmloft.develop.plugin.config.VMConfigApplication"
    }
    // 声明插件信息，这里的 register 参数可随意填写，摘要应用在 id
    plugins.register("vmLibrary") {
        // 定义插件id
        id = "com.vmloft.develop.plugin.config.library"
        // 插件实现类
        implementationClass = "com.vmloft.develop.plugin.config.VMConfigLibrary"
    }
    // 声明插件信息，这里的 register 参数可随意填写，摘要应用在 id
    plugins.register("vmPublish") {
        // 定义插件id
        id = "com.vmloft.develop.plugin.config.publish"
        // 插件实现类
        implementationClass = "com.vmloft.develop.plugin.config.VMConfigPublish"
    }
}
