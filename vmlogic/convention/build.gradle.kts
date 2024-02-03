plugins {
    `kotlin-dsl`
}

// 配置 Java 编译版本
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
//kotlin{
//    jvmToolchain(17)
//}

// 添加插件依赖
dependencies {
    implementation("com.android.tools.build:gradle:8.2.2")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
}

// 注册插件
gradlePlugin {
    // 声明插件信息，
    plugins {
        // 这里的 register 参数可随意填写，摘要应用在 id
        register("vmApp") {
            // 定义插件id
            id = "com.vmloft.develop.plugin.app"
            // 插件实现类
            implementationClass = "com.vmloft.develop.convention.plugin.VMApp"
        }
        // Library 插件
        register("vmLibrary") {
            id = "com.vmloft.develop.plugin.library"
            implementationClass = "com.vmloft.develop.convention.plugin.VMLibrary"
        }
        // base
        register("vmLibraryBase") {
            id = "com.vmloft.develop.plugin.library.base"
            implementationClass = "com.vmloft.develop.convention.plugin.VMLibraryBase"
        }
        // common
        register("vmLibraryCommon") {
            id = "com.vmloft.develop.plugin.library.common"
            implementationClass = "com.vmloft.develop.convention.plugin.VMLibraryCommon"
        }
        // image
        register("vmLibraryImage") {
            id = "com.vmloft.develop.plugin.library.image"
            implementationClass = "com.vmloft.develop.convention.plugin.VMLibraryImage"
        }
        // Request
        register("vmLibraryRequest") {
            id = "com.vmloft.develop.plugin.library.request"
            implementationClass = "com.vmloft.develop.convention.plugin.VMLibraryRequest"
        }
        // 这里的 register 参数可随意填写，摘要应用在 id
        register("vmPublish") {
            id = "com.vmloft.develop.plugin.publish"
            implementationClass = "com.vmloft.develop.convention.plugin.VMPublish"
        }
    }
}
