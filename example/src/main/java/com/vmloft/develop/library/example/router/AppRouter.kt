package  com.vmloft.develop.library.example.router

import com.alibaba.android.arouter.launcher.ARouter

/**
 * Create by lzan13 on 2020-02-24 21:57
 * 描述：针对路由注解统一收口
 */
object AppRouter {
    const val appBarrage = "/VMLoft/Barrage"
    const val appCustomView = "/VMLoft/CustomView"
    const val appFloatMenu = "/VMLoft/FloatMenu"
    const val appIndicator = "/VMLoft/Indicator"
    const val appLoading = "/VMLoft/Loading"
    const val appLottieAnim = "/VMLoft/LottieAnim"
    const val appMediaPlay = "/VMLoft/MediaPlay"
    const val appScheme = "/VMLoft/Scheme"
    const val appStyle = "/VMLoft/Style"
    const val appThread = "/VMLoft/Thread"
    const val appWebTest = "/VMLoft/Web"

    const val appImagePicker = "/VMLoft/ImagePicker"

    const val appSinglePermission = "/VMLoft/SinglePermission"
    const val appMultiPermission = "/VMLoft/MultiPermission"


    const val appNotifyTest = "/VMLoft/Notify"

    /**
     * 通用跳转
     * @param path 路径
     */
    fun go(path: String) {
        ARouter.getInstance().build(path).navigation()
    }

    /**
     * 主界面[MainActivity]
     */
    fun goMain() {
        ARouter.getInstance().build("/VMLoft/Main").navigation()
    }

    /**
     * 引导[GuideActivity]
     */
    fun goGuide() {
        ARouter.getInstance().build("/VMLoft/Guide").navigation()
    }

    /**
     * 设置[SettingsActivity]
     */
    fun goSettings() {
        ARouter.getInstance().build("/VMLoft/Settings").navigation()
    }

    /**
     * 个人信息设置[InfoSettingsActivity]
     */
    fun goSettingsInfo() {
        ARouter.getInstance().build("/VMLoft/SettingsInfo").navigation()
    }

    /**
     * 个人信息昵称设置[EditNicknameActivity]
     */
    fun goEditNickname(nickname: String?) {
        ARouter.getInstance().build("/VMLoft/EditNickname").withString("nickname", nickname).navigation()
    }

    /**
     * 个人信息签名设置[EditSignatureActivity]
     */
    fun goEditSignature(signature: String?) {
        ARouter.getInstance().build("/VMLoft/EditSignature").withString("signature", signature).navigation()
    }

    /**
     * 个人信息地址设置[EditAddressActivity]
     */
    fun goEditAddress(address: String?) {
        ARouter.getInstance().build("/VMLoft/EditAddress").withString("address", address).navigation()
    }

    /**
     * 通知[NotifySettingsActivity]
     */
    fun goSettingsNotify() {
        ARouter.getInstance().build("/VMLoft/SettingsNotify").navigation()
    }

    /**
     * 深色模式[DarkSettingsActivity]
     */
    fun goSettingsDark() {
        ARouter.getInstance().build("/VMLoft/SettingsDark").navigation()
    }

    /**
     * 深色模式[PictureSettingsActivity]
     */
    fun goSettingsPicture() {
        ARouter.getInstance().build("/VMLoft/SettingsPicture").navigation()
    }

    /**
     * 关于[AboutSettingsActivity]
     */
    fun goSettingsAbout() {
        ARouter.getInstance().build("/VMLoft/SettingsAbout").navigation()
    }

    /**
     * 调试[DebugSettingsActivity]
     */
    fun goSettingsDebug() {
        ARouter.getInstance().build("/VMLoft/SettingsDebug").navigation()
    }

    /**
     * 问题反馈[FeedbackActivity]
     */
    fun goFeedback() {
        ARouter.getInstance().build("/VMLoft/Feedback").navigation()
    }

    /**
     * 打开 Web 页面[WebActivity]
     */
    fun goWeb(url: String) {
        ARouter.getInstance().build("/VMLoft/Web").withString("url", url).navigation()
    }

    /**
     * 展示多图[DisplayMultiActivity]
     */
    fun goDisplayMulti(index: String, list: List<String>) {
        ARouter.getInstance().build("/VMLoft/DisplayMulti")
                .withString("index", index)
                .withObject("pictureList", list)
                .navigation()
    }

    /**
     * 展示单图[DisplaySingleActivity]
     */
    fun goDisplaySingle(url: String) {
        ARouter.getInstance().build("/VMLoft/DisplaySingle")
                .withString("url", url)
                .navigation()
    }

}