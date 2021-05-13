package com.vmloft.develop.library.common.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.vmloft.develop.library.tools.utils.VMDate
import com.vmloft.develop.library.tools.utils.VMTheme
import java.io.File
import java.util.*

/**
 * Create by lzan13 on 2020-02-15 19:29
 * 描述：格式化工具类
 */
object FormatUtils {
    /**
     * 设置状态栏黑色模式
     */
    @JvmStatic
    fun relativeTime(date: String): String {
        return VMDate.getRelativeTime(VMDate.milliFormUTC(date)) ?: date
    }
}