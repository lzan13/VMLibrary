package com.vmloft.develop.library.tools.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lzan13 on 2018/4/17.
 *
 * 正则相关工具类
 */
public class VMReg {

    /**
     * 判断是否是邮箱
     *
     * @param email 需要判断的邮箱
     */
    public static boolean isEmail(String email) {
        Pattern p = Pattern.compile("^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
        return p.matcher(email).matches();
    }

    /**
     * 是否是手机号
     *
     * @param phoneNumber 要判断的手机号
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$");
        return p.matcher(phoneNumber).matches();
    }

    /**
     * 判断是否是正常的密码，这里正常是指包含 0-9 a-z A-Z，以及不能含有除了"_"和"."这两个特殊字符以外的任何字符
     *
     * @param password 要判断的密码
     */
    public static boolean isNormalPassword(String password) {
        Pattern p = Pattern.compile("^[0-9a-zA-Z_.]{6,16}$");
        return p.matcher(password).matches();
    }

    /**
     * 是否满足正则
     *
     * @param content 内容
     * @param regStr  正则字符串
     * @return
     */
    public static boolean isCommonReg(String content, String regStr) {
        Pattern p = Pattern.compile(regStr);
        return p.matcher(content).matches();
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
    public static List<String> regexContent(String content, String regex) {
        Pattern pattern = Pattern.compile(regex);
        List<String> result = new ArrayList<>();
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            int matchStart = matcher.start();
            int matchEnd = matcher.end();
            String temp = content.substring(matchStart, matchEnd);
            result.add(temp);
        }
        return result;
    }

}