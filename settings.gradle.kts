pluginManagement {
    includeBuild("vmconfig")
    repositories {
        // 国内仓库镜像
        maven { setUrl("https://maven.aliyun.com/repository/central") }
        maven { setUrl("https://maven.aliyun.com/repository/google") }
        maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { setUrl("https://maven.aliyun.com/repository/public") }
        // jitpack 仓库
        maven { setUrl("https://jitpack.io") }

        google()
        mavenCentral()
        // gradle 插件仓库
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // 国内仓库镜像
        maven { setUrl("https://maven.aliyun.com/repository/central") }
        maven { setUrl("https://maven.aliyun.com/repository/google") }
        maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { setUrl("https://maven.aliyun.com/repository/public") }
        // jitpack 仓库
        maven { setUrl("https://jitpack.io") }

        google()
        mavenCentral()
    }
    // 解决本地依赖 aar 打包报错 其实就是将 libs 作为本地仓库，然后引用参考下的库文件。但是编译时候会有这个提示
    // Using flatDir should be avoided because it doesn"t support any meta-data formats.
//    repositories {
//        flatDir {
//            dirs new File(rootProject.projectDir.getAbsolutePath() + "/aar")
//        }
//    }
}

rootProject.name = "VMLibrary"

include(":example")

include(":base:vmbase")
include(":base:vmcommon")

include(":core:vmdb")
include(":core:vmimage")
include(":core:vmqr")
include(":core:vmreport")
include(":core:vmrequest")

include(":vmtools")
