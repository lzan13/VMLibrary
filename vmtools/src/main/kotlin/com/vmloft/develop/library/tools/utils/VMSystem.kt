package com.vmloft.develop.library.tools.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.NameNotFoundException
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.provider.Settings
import android.text.TextUtils

import com.vmloft.develop.library.tools.VMTools.context
import com.vmloft.develop.library.tools.utils.logger.VMLog

import java.util.concurrent.Executors

/**
 * Create by lzan13 at 2018/11/23
 * 系统相关工具类
 */
object VMSystem {
    // 在UI线程中处理
    private val handler = Handler(Looper.getMainLooper())

    // 线程池
    private val mExecutorPool = Executors.newCachedThreadPool()

    /**
     * 在 UI 线程中延迟执行
     */
    fun runInUIThread(runnable: Runnable, delay: Long = 0) {
        handler.postDelayed(runnable, delay)
    }

    /**
     * 异步任务
     */
    fun runTask(runnable: Runnable) {
        mExecutorPool.execute(runnable)
    }

    /**
     * 复制到剪贴板
     */
    fun copyToClipboard(content: String?): Boolean {
        return copyToClipboard(context, content)
    }

    /**
     * 复制到剪贴板
     */
    fun copyToClipboard(context: Context, content: String?): Boolean {
        try {
            val c = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            c.setPrimaryClip(ClipData.newPlainText(context.packageName, content))
            return true
        } catch (e: Exception) {
            VMLog.e("copyToClipboard " + e.message)
        }
        return false
    }

    /**
     * 获取线程池默认的大小
     */
    val threadPoolDefaultSize: Int
        get() = getThreadPoolDefaultSize(8)

    /**
     * 获取推荐的线程池大小
     *
     * @param max 线程池最大
     * @return 如果可用处理器*2 +1 小于最大线程数，则返回计算的线程池大小，否则返回传入的最大数
     */
    fun getThreadPoolDefaultSize(max: Int): Int {
        val availableProcessors = 2 * Runtime.getRuntime().availableProcessors() + 1
        return if (availableProcessors > max) max else availableProcessors
    }

    /**
     * 获取应用程序名称
     */
    val appName: String?
        get() = getAppName(context)

    /**
     * 获取应用程序名称
     *
     * @param context 上下文对象
     */
    fun getAppName(context: Context): String? {
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            val labelRes = packageInfo.applicationInfo.labelRes
            return context.resources.getString(labelRes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 获取应用当前版本号
     */
    val versionCode: Long
        get() = getVersionCode(context)

    /**
     * 获取应用当前版本号
     *
     * @param context 上下文对象
     */
    fun getVersionCode(context: Context): Long {
        val manager = context.packageManager
        var code: Long = 0
        try {
            val info = manager.getPackageInfo(context.packageName, 0)
            code = if (VERSION.SDK_INT >= VERSION_CODES.P) {
                info.longVersionCode
            } else {
                info.versionCode.toLong()
            }
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        }
        return code
    }

    /**
     * 获取当前应用版本名称
     */
    val versionName: String?
        get() = getVersionName(context)

    /**
     * 获取当前应用版本名称
     *
     * @param context 上下文对象
     */
    fun getVersionName(context: Context): String? {
        val manager = context.packageManager
        var name: String? = null
        try {
            val info = manager.getPackageInfo(context.packageName, 0)
            name = info.versionName
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        }
        return name
    }

    /**
     * 获取设备 Id，用户临时用户确认身份，或者限制但设备注册单用户
     */
    @SuppressLint("HardwareIds")
    fun deviceId(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    /**
     * 获取当前进程名
     */
    val processName: String?
        get() = getProcessName(context)

    /**
     * 获取当前进程名
     *
     * @param context 上下文对象
     */
    fun getProcessName(context: Context): String? {
        val pid = Process.myPid()
        var processName: String? = null
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val list: List<*> = activityManager.runningAppProcesses
        val i = list.iterator()
        while (i.hasNext()) {
            val info = i.next() as RunningAppProcessInfo
            try {
                if (info.pid == pid) {
                    // 根据进程的信息获取当前进程的名字
                    processName = info.processName
                    // 返回当前进程名
                    return processName
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        // 没有匹配的项，返回为null
        return processName
    }

    /**
     * 检查悬浮窗权限
     */
    fun checkOverlayPermission(context: Context, openSettings: Boolean = false): Boolean {
        if (VERSION.SDK_INT < VERSION_CODES.M) {
            return true
        }
        if (Settings.canDrawOverlays(context)) {
            return true
        }
        if (openSettings) {
            openOverlayPermissionSettings(context)
        }
        return false
    }

    /**
     * 打开悬浮窗权限设置
     */
    fun openOverlayPermissionSettings(context: Context) {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.packageName))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * 检查无障碍服务状态
     * @param openSettings 是否直接打开设置界面
     */
    fun checkAccessibilityStatus(context: Context, name: String, openSettings: Boolean = false): Boolean {
        val serviceName = "${context.packageName}/${name}"
        var status = Settings.Secure.getInt(context.applicationContext.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
        val splitter = TextUtils.SimpleStringSplitter(':')
        if (status == 1) {
            val settingValue = Settings.Secure.getString(context.applicationContext.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (!settingValue.isNullOrEmpty()) {
                splitter.setString(settingValue)
                while (splitter.hasNext()) {
                    val accessibilityService = splitter.next()
                    if (accessibilityService.equals(serviceName)) {
                        return true
                    }
                }
            }
        }
        if (openSettings) {
            openAccessibilitySettings(context)
        }
        return false
    }

    /**
     * 打开无障碍设置
     */
    fun openAccessibilitySettings(context: Context) {
        val settingIntent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        settingIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(settingIntent)
    }


}