package com.vmloft.develop.library.tools.utils

import java.util.ArrayList
import java.util.regex.Pattern

/**
 * Created by lzan13 on 2018/4/17.
 *
 * 正则相关工具类
 */
object VMReg {
    /**
     * 判断是否是邮箱
     * @param email 需要判断的邮箱
     */
    fun isEmail(email: String): Boolean {
        val p = Pattern.compile("^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$")
        return p.matcher(email).matches()
    }

    /**
     * 严格手机号判断
     * @param phoneNumber 要判断的手机号
     */
    fun isPhoneMobileNumber(phoneNumber: String): Boolean {
        val p = Pattern.compile("^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$")
        return p.matcher(phoneNumber).matches()
    }

    /**
     * 简单手机号判断
     * @param phoneNumber 要判断的手机号
     */
    fun isSimpleMobileNumber(phoneNumber: String): Boolean {
        val p = Pattern.compile("^[1]([3-9])[0-9]{9}$")
        return p.matcher(phoneNumber).matches()
    }

    /**
     * 判断是否是正常的密码，这里正常是指包含 0-9 a-z A-Z，以及不能含有除了"_"和"."这两个特殊字符以外的任何字符
     * @param password 要判断的密码
     */
    fun isNormalPassword(password: String): Boolean {
        val p = Pattern.compile("^[0-9a-zA-Z_.]{6,32}$")
        return p.matcher(password).matches()
    }

    /**
     * 是否满足正则
     * @param content 内容
     * @param regStr  正则字符串
     * @return
     */
    fun isCommonReg(content: String, regStr: String): Boolean {
        val p = Pattern.compile(regStr)
        return p.matcher(content).matches()
    }

    /**
     * 正则匹配字符串内容，例如：
     * String content = "#海贼王# #ONEPIECE# 第876话:仁义的男子汉 #甚平# 大海流通道";
     * String regex = "#([\\S]*?)#";
     *
     * @param content 需要匹配的字符串
     * @param regex   正则表达式
     * @return 返回匹配的集合
     */
    fun regexContent(content: String, regex: String): List<String> {
        val pattern = Pattern.compile(regex)
        val result: MutableList<String> = ArrayList()
        val matcher = pattern.matcher(content)
        while (matcher.find()) {
            val matchStart = matcher.start()
            val matchEnd = matcher.end()
            val temp = content.substring(matchStart, matchEnd)
            result.add(temp)
        }
        return result
    }
}