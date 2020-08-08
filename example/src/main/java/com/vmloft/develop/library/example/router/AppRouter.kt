package  com.vmloft.develop.library.example.router

import com.alibaba.android.arouter.launcher.ARouter
//import com.vmloft.develop.library.example.ui.display.DisplayMultiActivity
//import com.vmloft.develop.library.example.ui.display.DisplaySingleActivity
//import com.vmloft.develop.library.example.ui.feedback.FeedbackActivity
//import com.vmloft.develop.library.example.ui.guide.GuideActivity
//import com.vmloft.develop.library.example.ui.main.MainActivity
//import com.vmloft.develop.library.example.ui.main.explore.PublishActivity
//import com.vmloft.develop.library.example.ui.settings.*
//import com.vmloft.develop.library.example.ui.settings.info.EditAddressActivity
//import com.vmloft.develop.library.example.ui.settings.info.EditNicknameActivity
//import com.vmloft.develop.library.example.ui.settings.info.EditSignatureActivity
//import com.vmloft.develop.library.example.ui.settings.info.InfoSettingsActivity
//import com.vmloft.develop.library.example.ui.sign.SignInActivity
//import com.vmloft.develop.library.example.ui.sign.SignUpActivity
//import com.vmloft.develop.library.example.ui.web.WebActivity

/**
 * Create by lzan13 on 2020-02-24 21:57
 * 描述：针对路由注解统一收口
 */
object AppRouter {

    /**
     * 通用跳转
     * @param path 路径
     */
    fun go(path: String){
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
     * 登录[SignInActivity]
     */
    fun goSignIn() {
        ARouter.getInstance().build("/VMLoft/SignIn").navigation()
    }

    /**
     * 注册[SignUpActivity]
     */
    fun goSignUp() {
        ARouter.getInstance().build("/VMLoft/SignUp").navigation()
    }

    /**
     * 发布[PublishActivity]
     */
    fun goPublishPost() {
        ARouter.getInstance().build("/VMLoft/PublishPost").navigation()
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