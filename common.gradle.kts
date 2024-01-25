android {
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            // 配置 Java 编译版本
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            consumerProguardFiles("consumer-rules.pro")
        }
    }

    buildFeatures {
        // gradle 8.x 默认关闭了 buildConfig
        buildConfig = true
        // 开启 ViewBinding
        viewBinding = true
    }

    // 配置 Java 编译版本
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // 编译忽略错误配置
    lintOptions {
        abortOnError = false
    }
    // kotlin 配置
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17
    }
}