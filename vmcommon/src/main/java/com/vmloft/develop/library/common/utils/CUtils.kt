package com.vmloft.develop.library.common.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.vmloft.develop.library.tools.utils.VMTheme
import java.io.File
import java.util.*

/**
 * Create by lzan13 on 2020-02-15 19:29
 * 描述：通用工具类
 */
object CUtils {
    /**
     * 设置状态栏黑色模式
     */
    fun setDarkMode(activity: Activity, dark: Boolean) {
        VMTheme.setDarkStatusBar(activity, dark)
    }

    /**
     * 生成 [0, max) 范围内的随机数
     *
     * @param max 最大值（不包含）
     */
    fun random(max: Int): Int {
        return random(0, max)
    }

    /**
     * 生成制定范围内的随机数 范围 [start, end)
     *
     * @param min 最小值（包含）
     * @param max 最大值（不包含）
     */
    fun random(min: Int, max: Int): Int {
        val random = Random()
        return min + random.nextInt(max - min)
    }

    /**
     * 通知相册刷新
     */
    fun notifyAlbum(context: Context, path: String) {
        var intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(path)))
        context.sendBroadcast(intent)
    }

    /**
     * 发送广播
     */
    fun sendBroadcast(context: Context, intent: Intent) {
        context.sendBroadcast(intent)
    }
}