
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // 国内仓库镜像
        maven { setUrl("https://maven.aliyun.com/repository/central")}
        maven { setUrl("https://maven.aliyun.com/repository/google")}
        maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin")}
        maven { setUrl("https://maven.aliyun.com/repository/public")}
        // jitpack 仓库
        maven { setUrl("https://jitpack.io")}

        google()
        mavenCentral()
    }
}
rootProject.name = "vmlogic"
include (":convention")