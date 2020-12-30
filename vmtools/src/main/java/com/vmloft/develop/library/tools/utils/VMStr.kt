package com.vmloft.develop.library.tools.utils

import android.content.Context
import com.vmloft.develop.library.tools.VMTools.context
import java.util.Arrays

/**
 * Created by lzan13 on 2017/1/29.
 * 封装字符串操作类，对字符串进行一些简单的操作
 */
object VMStr {
    /**
     * 字符串格式化代码
     *
     * %s   字符串      mingrisoft
     * %c   字符        m
     * %b   布尔        true
     * %d   整数十进制   99
     * %x   整数十六进制 FF
     * %o   整数八进制
     * 77
     * %f   浮点数        99.99
     * %a   浮点数十六进制 FF.35AE
     * %e   指数          9.38e+5
     * %g   通用浮点类型（f和e类型中较短的）
     * %h   散列码
     * %%   百分比
     * ％
     * %n   换行符
     * %tx  日期与时间类型（x代表不同的日期与时间转换符
     */
    /**
     * 字符串转数组
     *
     * @param string    字符串
     * @param separator 分隔符
     * @return 数组
     */
    fun strToArray(string: String, separator: String?): Array<String> {
        return string.split(separator!!).toTypedArray()
    }

    /**
     * 字符串数组转字符串
     *
     * @param array     字符串数组
     * @param separator 分隔符
     * @return 字符串
     */
    fun arrayToStr(array: Array<String?>?, separator: String?): String? {
        if (array == null || array.isEmpty()) {
            return null
        }
        val sb = StringBuffer()
        for (i in array.indices) {
            if (i != 0) {
                sb.append(",")
            }
            sb.append(array[i])
        }
        return sb.toString()
    }

    /**
     * 集合转字符串拼接
     *
     * @param list     集合
     * @param splitStr 分隔符
     */
    fun listToStr(list: List<String?>?, splitStr: String?): String? {
        if (list == null || list.isEmpty()) {
            return null
        }
        val sb = StringBuffer()
        for (i in list.indices) {
            if (i != 0) {
                sb.append(splitStr)
            }
            sb.append(list[i])
        }
        return sb.toString()
    }

    /**
     * 字符串集合转数组
     *
     * @param list 字符串集合
     * @return 数组
     */
    fun listToArray(list: List<String>): Array<String> {
        return list.toTypedArray()
    }

    /**
     * 根据参数格式化字符串
     */
    fun byArgs(str: String?, vararg args: Any?): String {
        return String.format(str!!, *args)
    }

    /**
     * 根据资源 id 获取字符串
     */
    fun byRes(resId: Int): String {
        return byRes(context, resId)
    }

    /**
     * 根据资源 id 获取字符串
     */
    fun byRes(context: Context, resId: Int): String {
        return context.getString(resId)
    }

    /**
     * 根据资源 id 格式化字符串
     */
    fun byResArgs(resId: Int, vararg args: Any?): String {
        return byResArgs(context, resId, *args)
    }

    /**
     * 根据资源 id 格式化字符串
     */
    fun byResArgs(context: Context, resId: Int, vararg args: Any?): String {
        return context.getString(resId, *args)
    }

    /**
     * 检测字符串是否为空白字符串
     */
    fun isEmpty(str: CharSequence?): Boolean {
        if (str == null || "" == str) {
            return true
        }
        for (element in str) {
            val c = element
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false
            }
        }
        return true
    }
}