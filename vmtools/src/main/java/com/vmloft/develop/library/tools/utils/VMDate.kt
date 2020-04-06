package com.vmloft.develop.library.tools.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

/**
 * Created by Administrator on 2015/3/26.
 * 自定义封装时间工具处理类
 */
object VMDate {
    /**
     * 获取最近时间字符串
     */
    private const val MINUTE = 60 * 1000.toLong()
    private const val HOUR = 60 * MINUTE
    private const val DAY = 24 * HOUR
    private const val TwoDAY = 2 * 24 * HOUR
    private const val ThreeDAY = 3 * 24 * HOUR
    private const val WEEK = 7 * DAY
    private const val MONTH = 31 * DAY
    private const val YEAR = 12 * MONTH

    /**
     * 定义时间的格式化不同样式
     */
    var sdfFilenameDateTime = SimpleDateFormat("yyyyMMdd_HHmmssSSS")
    var sdfFilenameDate = SimpleDateFormat("yyyy-MM-dd")
    var sdfNormal = SimpleDateFormat("yyyy/MM/dd HH:mm")
    var sdfUTC = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    var sdfDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    var sdfNoYear = SimpleDateFormat("MM/dd HH:mm")
    var sdfOnlyDate = SimpleDateFormat("yyyy/MM/dd")
    var sdfOnlyDateNoDay = SimpleDateFormat("yyyy月MM日")
    var sdfOnlyDateNoYear = SimpleDateFormat("MM/dd")
    var sdfOnlyTime = SimpleDateFormat("HH:mm")

    /**
     * 获取当前时间的毫秒值
     */
    fun currentMilli(): Long {
        return System.currentTimeMillis()
    }

    /**
     * 获取当前格式化后的标准时间
     */
    fun currentDateTime(): String {
        return sdfNormal.format(Date())
    }

    /**
     * 获取当前时间的 UTC 格式字符串表示
     */
    fun currentUTCDateTime(): String {
        sdfUTC.timeZone = TimeZone.getTimeZone("UTC")
        return sdfUTC.format(Date())
    }

    /**
     * 获取当前时间拼接的字符串，没有间隔，主要用于文件命名等
     */
    fun filenameDateTime(): String {
        return sdfFilenameDateTime.format(Date())
    }

    /**
     * 获取当前事件拼接的字符串，只有日期
     */
    fun filenameDate(): String {
        return sdfFilenameDate.format(Date())
    }

    /**
     * 将给定的字符串型时间格式化为另一种样式
     *
     * @param srcFormat 原来的时间格式
     * @param desFormat 目标的时间格式
     * @param dateStr 原来的时间
     */
    fun convertDateTime(srcFormat: String?, desFormat: String?, dateStr: String?): String? {
        return try {
            val date = SimpleDateFormat(srcFormat).parse(dateStr)
            SimpleDateFormat(desFormat).format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 根据格林尼格式的日志获取其毫秒表示值
     *
     * @param dateStr 需要转换的日期
     */
    fun milliFormUTC(dateStr: String?): Long {
        return if (VMStr.isEmpty(dateStr)) {
            0L
        } else try {
            sdfUTC.timeZone = TimeZone.getTimeZone("UTC")
            val date = sdfUTC.parse(dateStr)
            date.time
        } catch (e: ParseException) {
            e.printStackTrace()
            0L
        }
    }

    /**
     * 获取时间的字符串格式，将秒单位的时间转为如 "00:00:00"这样的格式
     *
     * @param time 时间，单位 秒
     */
    fun toTimeString(time: Long): String {
        val seconds = time % 60
        val minutes = time / 60 % 60
        val hours = time / 3600
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    /**
     * 获取时间的秒数，将形如 "00:00:00"这样格式的时间转为秒
     *
     * @param time 字符串格式的时间
     */
    fun fromTimeString(time: String): Long {
        // Handle "00:00:00.000" pattern, drop the milliseconds
        var time = time
        if (time.lastIndexOf(".") != -1) {
            time = time.substring(0, time.lastIndexOf("."))
        }
        val split = time.split(":").toTypedArray()
        require(split.size == 3) { "Can't parse time string: $time" }
        val h = split[0].toLong() * 3600
        val m = split[1].toLong() * 60
        val s = split[2].toLong()
        return h + m + s
    }

    /**
     * 从 long 整型的时间戳里取出时间
     *
     * @param time 需要格式化的 long 型的时间
     * @return 返回得到的不包含年月日的时间值
     */
    fun long2Time(time: Long): String {
        val date = Date(time)
        return sdfOnlyTime.format(date)
    }

    /**
     * 从 long 整型的时间格式化为正常时间
     *
     * @param time 需要格式化的时间毫秒值
     */
    fun long2Normal(time: Long): String {
        val date = Date(time)
        return sdfNormal.format(date)
    }

    /**
     * 从 long 整型的时间格式化为正常时间，不包含年份
     *
     * @param time 需要格式化的时间毫秒值
     */
    fun long2NormalNoYear(time: Long): String {
        val date = Date(time)
        return sdfNoYear.format(date)
    }

    /**
     * 从 long 整型的时间戳里取出日期 不带有时间
     *
     * @param time 需要格式化的时间毫秒值
     */
    fun long2Date(time: Long): String {
        val date = Date(time)
        return sdfOnlyDate.format(date)
    }

    /**
     * 从 long 整型的时间戳里取出日期，这里取的不带有年份
     *
     * @param time 需要格式化的时间毫秒值
     */
    fun long2DateNoYear(time: Long): String {
        val date = Date(time)
        return sdfOnlyDateNoYear.format(date)
    }

    /**
     * 从 long 整型的时间戳里取出日期，这里取的不带有日
     *
     * @param time 需要格式化的时间毫秒值
     */
    fun long2DateNoDay(time: Long): String {
        val date = Date(time)
        return sdfOnlyDateNoDay.format(date)
    }

    /**
     * 获取相对时间
     *
     * @param time 需要判断的时间点的毫秒值
     * @return 返回相对时间
     */
    fun getRelativeTime(time: Long): String? {
        val currentTime = System.currentTimeMillis()
        val offset = currentTime - time
        var timeStr: String? = null
        timeStr = if (isSameDate(currentTime, time)) {
            // 今天
            long2Time(time)
        } else if (isSameDate(currentTime, time + DAY)) {
            // 昨天
            return "昨天 " + long2Time(time)
        } else if (isSameDate(currentTime, time + 2 * DAY)) {
            // 前天
            return "前天 " + long2Time(time)
        } else if (isSameDate(currentTime, time + YEAR)) {
            // XXXX年XX月XX日
            long2Normal(time)
        } else {
            // XX月XX日
            long2NormalNoYear(time)
        }
        return timeStr
    }

    /**
     * 判断两个日期是不是同一天
     *
     * @param time1 第一个日期
     * @param time2 第二个日期
     * @return 返回是否是同一天
     */
    fun isSameDate(time1: Long, time2: Long): Boolean {
        // 求余取整，判断天数是否相等
        val day1 = time1 / DAY
        val day2 = time2 / DAY
        return day1 == day2
    }
}