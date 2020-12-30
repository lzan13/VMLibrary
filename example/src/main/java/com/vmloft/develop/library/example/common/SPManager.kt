package com.vmloft.develop.library.example.common

import com.vmloft.develop.library.example.BuildConfig
import com.vmloft.develop.library.tools.utils.VMSPUtil
import com.vmloft.develop.library.tools.utils.VMSystem

/**
 * Create by lzan13 on 2020/4/25 19:48
 * 描述：SharedPreference 管理
 */
class SPManager {
    // debug 开关
    private val debugKey = "debugKey"

    // 本地版本
    private val localVersionKey = "localVersionKey"
    // 隐私协议状态
    private val policyStatusKey = "policyStatusKey"

    // debug 开关状态
    private val debugStatusKey = "debugStatusKey"

    // 通知开关
    private val notifyMsgSwitchKey = "notifyMsgSwitchKey"
    private val notifyMsgDetailSwitchKey = "notifyMsgDetailSwitchKey"

    // 深色模式
    private val darkModeSystemSwitchKey = "darkModeSystemSwitchKey"
    private val darkModeManualKey = "darkModeManualKey"

    // 图片资源设置 key
    private val pictureAutoLoadKey = "pictureAutoLoadKey"
    private val pictureSaveDICMKey = "pictureSaveDICMKey"

    // 记录账户信息
    private val currUserKey = "currUserKey"
    private val prevUserKey = "prevUserKey"

    companion object {
        val instance: SPManager by lazy {
            SPManager()
        }
    }

    /**
     * Debug 状态
     */
    fun setDebug(debug: Boolean) {
        VMSPUtil.getEntry().putAsync(debugKey, debug)
    }

    fun isDebug(): Boolean = VMSPUtil.getEntry().get(debugKey, com.vmloft.develop.library.tools.BuildConfig.DEBUG) as Boolean

    /**
     * 保存当前运行版本号
     */
    fun setLocalVersion(version: Long) {
        VMSPUtil.getEntry().putAsync(localVersionKey, version)
    }

    /**
     * 获取当前运行的版本号
     */
    fun getLocalVersion(): Long {
        return VMSPUtil.getEntry().get(localVersionKey, 0L) as Long
    }

    /**
     * 判断启动时是否需要展示引导界面，这里根据本地记录的 appVersion 以及运行 APP 获取到的 appVersion 对比
     */
    fun isGuideShow(): Boolean {
        // 上次运行保存的版本号
        val localVersion = getLocalVersion()
        // 程序当前版本
        val version = VMSystem.versionCode
        return version > localVersion
    }

    /**
     * 隐藏引导界面
     */
    fun setGuideHide() {
        // 保存新的版本号
        setLocalVersion(VMSystem.versionCode)
    }

    /**
     * 设置隐私协议状态
     */
    fun setPolicyStatus() {
        VMSPUtil.getEntry().putAsync(policyStatusKey, true)
    }

    fun getPolicyStatus(): Boolean {
        return VMSPUtil.getEntry().get(policyStatusKey, false) as Boolean
    }

    /**
     * Debug 状态
     */
    fun setDebugStatus(debug: Boolean) {
        VMSPUtil.getEntry().putAsync(debugStatusKey, debug)
    }

    fun getDebugStatus(): Boolean {
        return VMSPUtil.getEntry().get(debugStatusKey, BuildConfig.DEBUG) as Boolean
    }

    /**
     * 通知开关
     */
    fun setNotifyMsgSwitch(status: Boolean) {
        VMSPUtil.getEntry().putAsync(notifyMsgSwitchKey, status)
    }

    fun getNotifyMsgSwitch(): Boolean {
        return VMSPUtil.getEntry().get(notifyMsgSwitchKey, true) as Boolean
    }

    fun setNotifyMsgDetailSwitch(status: Boolean) {
        VMSPUtil.getEntry().putAsync(notifyMsgDetailSwitchKey, status)
    }

    fun getNotifyMsgDetailSwitch(): Boolean {
        return VMSPUtil.getEntry().get(notifyMsgDetailSwitchKey, true) as Boolean
    }

    /**
     * 深色模式
     */
    fun setDarkModeSystemSwitch(status: Boolean) {
        VMSPUtil.getEntry().putAsync(darkModeSystemSwitchKey, status)
    }

    fun getDarkModeSystemSwitch(): Boolean {
        return VMSPUtil.getEntry().get(darkModeSystemSwitchKey, true) as Boolean
    }

    fun setDarkModeManual(mode: Int) {
        VMSPUtil.getEntry().putAsync(darkModeManualKey, mode)
    }

    fun getDarkModeManual(): Int {
        return VMSPUtil.getEntry().get(darkModeManualKey, -1) as Int
    }

    /**
     * 资源开关
     */
    fun setAutoLoad(auto: Boolean) {
        VMSPUtil.getEntry().putAsync(pictureAutoLoadKey, auto)
    }

    fun isAutoLoad(): Boolean {
        return VMSPUtil.getEntry().get(pictureAutoLoadKey, true) as Boolean
    }

    fun setSaveDICM(auto: Boolean) {
        VMSPUtil.getEntry().putAsync(pictureSaveDICMKey, auto)
    }

    fun isSaveDICM(): Boolean {
        return VMSPUtil.getEntry().get(pictureSaveDICMKey, true) as Boolean
    }

    /**
     * 上一个账户登录记录
     *
     * @return 如果为空，说明没有登录记录
     */
    fun getPrevUser(): String {
        return VMSPUtil.getEntry().get(prevUserKey, "") as String
    }

    fun putPrevUser(userJson: String) {
        VMSPUtil.getEntry().putAsync(prevUserKey, userJson)
    }

    /**
     * 当前账户登录记录
     *
     * @return 如果为空，说明没有登录记录
     */
    fun getCurrUser(): String {
        return VMSPUtil.getEntry().get(currUserKey, "") as String
    }

    fun putCurrUser(userJson: String) {
        VMSPUtil.getEntry().putAsync(currUserKey, userJson)
    }
}