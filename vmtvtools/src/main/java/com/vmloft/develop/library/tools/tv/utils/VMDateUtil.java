package com.vmloft.develop.library.tools.tv.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/3/26.
 * 自定义封装时间工具处理类
 */
public class VMDateUtil {

    /**
     * 获取最近时间字符串
     */
    private static final long MINUTE = 60 * 1000;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;
    private static final long TwoDAY = 2 * 24 * HOUR;
    private static final long ThreeDAY = 3 * 24 * HOUR;
    private static final long WEEK = 7 * DAY;
    private static final long MONTH = 31 * DAY;
    private static final long YEAR = 12 * MONTH;

    /**
     * 定义时间的格式化不同样式
     */
    private static SimpleDateFormat formatDateTimeNormal = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static SimpleDateFormat formatDateTimeMilli = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static SimpleDateFormat formatDateTimeNoSpacing = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static SimpleDateFormat formatDateTimeNoYear = new SimpleDateFormat("MM/dd HH:mm:ss");
    private static SimpleDateFormat formatDateNormal = new SimpleDateFormat("yyyy/MM/dd");
    private static SimpleDateFormat formatDateNoYear = new SimpleDateFormat("MM/dd");
    private static SimpleDateFormat formatTimeNormal = new SimpleDateFormat("HH:mm:ss");

    /**
     * 获取当前格式化后的标准时间
     *
     * @return 返回格式化后的时间
     */
    public static String getCurrentDateTimeNormal() {
        return formatDateTimeNormal.format(new Date());
    }

    /**
     * 获取当前时间拼接的字符串，没有间隔，主要用于文件命名等
     *
     * @return 返回格式化后的时间
     */
    public static String getDateTimeNoSpacing() {
        return formatDateTimeNoSpacing.format(new Date());
    }

    /**
     * 获取当前时间的毫秒值
     */
    public static long getCurrentMillisecond() {
        return System.currentTimeMillis();
    }

    /**
     * 将给定的字符串型时间格式化为另一种样式
     *
     * @param format 原来的时间格式
     * @param dateStr 原来的时间
     * @return 返回格式化后的时间格式
     */
    public static String getDateTimeNormal(String format, String dateStr) {
        try {
            Date date = new SimpleDateFormat(format).parse(dateStr);
            return formatDateTimeNormal.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从 long 整型的时间戳里取出时间
     *
     * @param time 需要格式化的 long 型的时间
     * @return 返回得到的不包含年月日的时间值
     */
    public static String long2Time(long time) {
        Date date = new Date(time);
        return formatTimeNormal.format(date);
    }

    /**
     * 从 long 整型的时间格式化为正常时间
     *
     * @param time 需要格式化的时间毫秒值
     */
    public static String long2DateTime(long time) {
        Date date = new Date(time);
        return formatDateTimeNormal.format(date);
    }

    /**
     * 从 long 整型的时间格式化为正常时间，不包含年份
     *
     * @param time 需要格式化的时间毫秒值
     */
    public static String long2DateTimeNoYear(long time) {
        Date date = new Date(time);
        return formatDateTimeNoYear.format(date);
    }

    /**
     * 从 long 整型的时间戳里取出日期 不带有时间
     *
     * @param time 需要格式化的时间毫秒值
     */
    public static String long2Date(long time) {
        Date date = new Date(time);
        return formatDateNormal.format(date);
    }

    /**
     * 从 long 整型的时间戳里取出日期，这里取的不带有年份
     *
     * @param time 需要格式化的时间毫秒值
     */
    public static String long2DateNoYear(long time) {
        Date date = new Date(time);
        return formatDateNoYear.format(date);
    }

    /**
     * 获取相对时间
     *
     * @param time 需要判断的时间点的毫秒值
     * @return 返回相对时间
     */
    public static String getRelativeTime(long time) {
        long currentTime = System.currentTimeMillis();
        long offset = currentTime - time;
        String timeStr = null;
        if (isSameDate(currentTime, time)) {
            // 今天
            timeStr = long2Time(time);
        } else if (isSameDate(currentTime, time + DAY)) {
            // 昨天
            return "昨天 " + long2Time(time);
        } else if (isSameDate(currentTime, time + 2 * DAY)) {
            // 前天
            return "前天 " + long2Time(time);
        } else if (isSameDate(currentTime, time + YEAR)) {
            // XXXX年XX月XX日
            timeStr = long2DateTime(time);
        } else {
            // XX月XX日
            timeStr = long2DateTimeNoYear(time);
        }
        return timeStr;
    }

    /**
     * 判断两个日期是不是同一天
     *
     * @param time1 第一个日期
     * @param time2 第二个日期
     * @return 返回是否是同一天
     */
    public static boolean isSameDate(long time1, long time2) {
        // 求余取整，判断天数是否相等
        long day1 = time1 / DAY;
        long day2 = time2 / DAY;
        return day1 == day2;
    }
}
