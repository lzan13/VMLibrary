package  com.vmloft.develop.library.example.router

import android.content.Intent
import com.alibaba.android.arouter.launcher.ARouter
import com.vmloft.develop.library.common.router.CRouter

/**
 * Create by lzan13 on 2020-02-24 21:57
 * 描述：针对路由注解统一收口
 */
object AppRouter {
    // 界面 Demo
    const val appAccessibility = "/VMLoft/Accessibility"
    const val appBarrage = "/VMLoft/Barrage"
    const val appCustomView = "/VMLoft/CustomView"
    const val appCustomDrawView = "/VMLoft/CustomDrawView"
    const val appFloatMenu = "/VMLoft/FloatMenu"
    const val appIndicator = "/VMLoft/Indicator"
    const val appLoading = "/VMLoft/Loading"
    const val appLottieAnim = "/VMLoft/LottieAnim"
    const val appScheme = "/VMLoft/Scheme"
    const val appStyle = "/VMLoft/Style"
    const val appThread = "/VMLoft/Thread"
    const val appWebTest = "/VMLoft/Web"

    const val appImagePicker = "/VMLoft/ImagePicker"

    const val appSinglePermission = "/VMLoft/SinglePermission"
    const val appMultiPermission = "/VMLoft/MultiPermission"
    const val appOpenWeChat = "/VMLoft/OpenWeChat"

    const val appNotifyTest = "/VMLoft/Notify"


    const val appMain = "/App/Main"
    const val appGuide = "/App/Guide"

    // 设置
    const val appSettings = "/App/Settings"
    const val appSettingsDark = "/App/SettingsDark"
    const val appSettingsMedia = "/App/SettingsMedia"
    const val appSettingsNotify = "/App/SettingsNotify"
    const val appSettingsAbout = "/App/SettingsAbout"
    const val appFeedback = "/App/Feedback"
    const val appSettingsAgreementPolicy = "/App/SettingsAgreementPolicy"

    const val appUserInfo = "/App/appUserInfo"


    /**
     * 通用跳转
     * @param path 路径
     */
    fun go(path: String) {
        ARouter.getInstance().build(path).navigation()
    }

    /**
     * 主界面[MainActivity]
     * @param type 跳转类型 0-普通 1-清空登录信息
     */
    fun goMain(type: String = "0") {
        ARouter.getInstance().build(CRouter.appMain)
            .withString("type", type)
            .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .navigation()
    }



}