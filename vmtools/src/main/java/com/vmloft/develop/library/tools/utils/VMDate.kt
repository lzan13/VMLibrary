package com.vmloft.develop.library.tools.utils

import com.vmloft.develop.library.tools.R
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
    val sdfFilenameDateTime = SimpleDateFormat("yyyyMMdd_HHmmssSSS")
    val sdfNormal = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    val sdfNormalSSS = SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS")
    val sdfUTC = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val sdfNoYear = SimpleDateFormat("MM/dd HH:mm")
    val sdfOnlyDate = SimpleDateFormat("yyyy/MM/dd")
    val sdfOnlyDateNoDay = SimpleDateFormat("yyyy/MM")
    val sdfOnlyDateNoYear = SimpleDateFormat("MM/dd")
    val sdfOnlyTime = SimpleDateFormat("HH:mm:ss")

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
     * 将给定的字符串型时间格式化为另一种样式
     *
     * @param srcFormat 原来的时间格式
     * @param desFormat 目标的时间格式
     * @param dateStr 原来的时间
     */
    fun convertDateTime(srcFormat: String, desFormat: String, dateStr: String): String {
        return try {
            val date = SimpleDateFormat(srcFormat).parse(dateStr)
            SimpleDateFormat(desFormat).format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * 根据格林尼格式的日志获取其毫秒表示值
     *
     * @param dateStr 需要转换的日期
     */
    fun utc2Long(dateStr: String): Long {
        return if (dateStr.isNullOrEmpty()) {
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
    fun milli2Str(time: Long): String {
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
    fun str2Milli(time: String): Long {
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
     * 时间戳转时间字符串
     *
     * @param time 需要格式化的时间毫秒值
     * @param sdf 格式化样式 默认为普通(yyyy/MM/dd HH:mm:ss)格式
     */
    fun long2Str(time: Long, sdf: SimpleDateFormat = sdfNormal): String {
        val date = Date(time)
        return sdf.format(date)
    }

    /**
     * 从 long 整型的时间戳里取出时间
     *
     * @param time 需要格式化的 long 型的时间
     * @return 返回得到的不包含年月日的时间值
     */
    fun long2Time(time: Long) = long2Str(time, sdfOnlyTime)

    /**
     * 从 long 整型的时间格式化为正常时间，不包含年份
     *
     * @param time 需要格式化的时间毫秒值
     */
    fun long2NormalNoYear(time: Long) = long2Str(time, sdfNoYear)

    /**
     * 从 long 整型的时间戳里取出日期 不带有时间
     *
     * @param time 需要格式化的时间毫秒值
     */
    fun long2Date(time: Long) = long2Str(time, sdfOnlyDate)

    /**
     * 从 long 整型的时间戳里取出日期，这里取的不带有年份
     *
     * @param time 需要格式化的时间毫秒值
     */
    fun long2DateNoYear(time: Long) = long2Str(time, sdfOnlyDateNoYear)

    /**
     * 从 long 整型的时间戳里取出日期，这里取的不带有日
     *
     * @param time 需要格式化的时间毫秒值
     */
    fun long2DateNoDay(time: Long) = long2Str(time, sdfOnlyDateNoDay)

    /**
     * 从 UTC 格式时间转为其他时间格式
     * @param time 需要格式化的时间 UTC 格式字符串
     *
     */
    fun utc2Str(time: String, sdf: SimpleDateFormat = sdfNormal): String {
        return try {
            val date = sdfUTC.parse(time)
            sdf.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * 获取相对时间
     *
     * @param time 需要判断的时间点的毫秒值
     * @return 返回相对时间
     */
    fun getRelativeTime(time: Long): String {
        val currentTime = System.currentTimeMillis()
        return when {
            isSameDate(currentTime, time) -> long2Time(time)
            isSameDate(currentTime, time + DAY) -> VMStr.byRes(R.string.vm_yesterday) + " " + long2Time(time)
            isSameDate(currentTime, time + YEAR) -> long2Str(time)
            else -> long2NormalNoYear(time)
        }
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