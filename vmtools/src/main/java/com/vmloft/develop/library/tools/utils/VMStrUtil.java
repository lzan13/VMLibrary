package com.vmloft.develop.library.tools.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lzan13 on 2017/1/29.
 * 封装字符串操作类，对字符串进行一些简单的转换操作
 */
public class VMStrUtil {

    /**
     * 字符串转数组
     *
     * @param string 字符串
     * @param separator 分隔符
     * @return 数组
     */
    public static String[] strToArray(String string, String separator) {
        return string.split(separator);
    }

    /**
     * 字符串数组转字符串
     *
     * @param array 字符串数组
     * @param separator 分隔符
     * @return 字符串
     */
    public static String arrayToStr(String[] array, String separator) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(array[i]);
        }
        return sb.toString();
    }

    /**
     * 字符串数组转集合
     *
     * @param array 字符串数组
     * @return 集合
     */
    public static List<String> arrayToList(String[] array) {
        return Arrays.asList(array);
    }

    /**
     * 字符串集合转数组
     *
     * @param list 字符串集合
     * @return 数组
     */
    public static String[] listToArray(List<String> list) {
        return list.toArray(new String[list.size()]);
    }
}
