package com.vmloft.develop.library.common

import com.vmloft.develop.library.base.BuildConfig
import com.vmloft.develop.library.tools.utils.VMDimen
import com.vmloft.develop.library.tools.utils.VMSP
import com.vmloft.develop.library.tools.utils.VMSystem

/**
 * Create by lzan13 on 2020/08/11
 *
 * 描述：SharedPreferences 配置管理类
 */
object CSPManager {
    /**
     * 记录设置项
     */
    private val settingsEntry = "settings"

    // debug 开关
    private val debugKey = "debugKey"

    // 资源设置 key
    private val mediaAutoLoadKey = "mediaAutoLoadKey"
    private val mediaSaveDICMKey = "mediaSaveDICMKey"

    // 通知开关
    private val notifyMsgSwitchKey = "notifyMsgSwitchKey"
    private val notifyMsgDetailSwitchKey = "notifyMsgDetailSwitchKey"

    // 引导前缀
    private val guideKeyPrefix = "guideKeyPrefix"

    // 本地版本
    private val localVersionKey = "localVersionKey"

    // 隐私协议状态
    private val agreementPolicyKey = "policyStatusKey"

    // 深色模式
    private val darkModeSystemSwitchKey = "darkModeSystemSwitchKey"
    private val darkModeManualKey = "darkModeManualKey"

    // 记录键盘高度
    private val keyboardHeightKey = "keyboardHeightKey"

    /**
     * 保存键盘高度
     */
    fun putKeyboardHeight(height: Int) {
        VMSP.getEntry(settingsEntry).putAsync(keyboardHeightKey, height)
    }

    fun getKeyboardHeight(): Int =
        VMSP.getEntry(settingsEntry).get(keyboardHeightKey, VMDimen.dp2px(256)) as Int

    /**
     * -------------------------------------------------------------------------------
     * 记录设置项
     */
    /**
     * Debug 状态
     */
    fun setDebug(debug: Boolean) {
        VMSP.getEntry(settingsEntry).putAsync(debugKey, debug)
    }

    fun isDebug(): Boolean = VMSP.getEntry(settingsEntry).get(debugKey, false) as Boolean

    /**
     * 资源相关配置
     */
    fun isAutoLoad(): Boolean = VMSP.getEntry(settingsEntry).get(mediaAutoLoadKey, true) as Boolean
    fun setAutoLoad(auto: Boolean) {
        VMSP.getEntry(settingsEntry).putAsync(mediaAutoLoadKey, auto)
    }

    fun isSaveDICM(): Boolean = VMSP.getEntry(settingsEntry).get(mediaSaveDICMKey, true) as Boolean
    fun setSaveDICM(auto: Boolean) {
        VMSP.getEntry(settingsEntry).putAsync(mediaSaveDICMKey, auto)
    }


    /**
     * 通知开关
     */
    fun isNotifyMsgSwitch(): Boolean = get(settingsEntry, notifyMsgSwitchKey, true) as Boolean
    fun setNotifyMsgSwitch(status: Boolean) {
        putAsync(settingsEntry, notifyMsgSwitchKey, status)
    }

    fun isNotifyMsgDetailSwitch(): Boolean = get(settingsEntry, notifyMsgDetailSwitchKey, true) as Boolean
    fun setNotifyMsgDetailSwitch(status: Boolean) {
        putAsync(settingsEntry, notifyMsgDetailSwitchKey, status)
    }


    /**
     * 检查指定模块是否需要显示引导
     */
    fun isNeedGuide(module: String): Boolean = get(settingsEntry, guideKeyPrefix + module, true) as Boolean
    fun setNeedGuide(module: String, need: Boolean) {
        putAsync(settingsEntry, guideKeyPrefix + module, need)
    }

    /**
     * 获取当前运行的版本号
     */
    fun getLocalVersion(): Long = get(settingsEntry, localVersionKey, 0L) as Long
    fun setLocalVersion(version: Long) {
        putAsync(settingsEntry, localVersionKey, version)
    }


    /**
     * 判断启动时是否需要展示引导界面，这里根据本地记录的 appVersion 以及运行 APP 获取到的 appVersion 对比
     */
    fun isGuideShow(): Boolean {
        // 上次运行保存的版本号
        val localVersion = getLocalVersion()
        // 程序当前版本
        val version = VMSystem.versionCode
        return version > localVersion + 5 // 超过5个小版本之后再次显示引导界面
    }

    /**
     * 隐藏引导界面
     */
    fun setGuideHide() {
        setLocalVersion(VMSystem.versionCode)
    }

    /**
     * 协议与政策状态
     */
    fun isAgreementPolicy(): Boolean = get(settingsEntry, agreementPolicyKey, false) as Boolean
    fun setAgreementPolicy() {
        putAsync(settingsEntry, agreementPolicyKey, true)
    }


    /**
     * 深色模式
     */
    fun isDarkModeSystemSwitch(): Boolean = get(settingsEntry, darkModeSystemSwitchKey, true) as Boolean
    fun setDarkModeSystemSwitch(status: Boolean) {
        putAsync(settingsEntry, darkModeSystemSwitchKey, status)
    }

    fun getDarkModeManual(): Int = get(settingsEntry, darkModeManualKey, -1) as Int
    fun setDarkModeManual(mode: Int) {
        putAsync(settingsEntry, darkModeManualKey, mode)
    }

    /**
     * -------------------------------------------------------------------------------
     * 通用的几个方法
     */
    /**
     * 通用获取数据
     */
    fun get(entry: String, key: String, default: Any): Any? {
        return VMSP.getEntry(entry).get(key, default)
    }

    /**
     * 通用设置数据
     */
    fun put(entry: String, key: String, value: Any) {
        VMSP.getEntry(entry).put(key, value)
    }

    /**
     * 通用设置数据，异步
     */
    fun putAsync(entry: String, key: String, value: Any) {
        VMSP.getEntry(entry).putAsync(key, value)
    }
}