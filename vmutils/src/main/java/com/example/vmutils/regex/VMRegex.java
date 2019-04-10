package com.example.vmutils.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create by lzan13 on 2019/04/10
 *
 * 正则操作类，测试记录正则表达式相关代码
 */
public class VMRegex {

    /**
     * 通过正则表达式匹配内容，并返回匹配结果
     *
     * @param content 需要匹配的内容
     * @param regStr  正则字符串
     * @return 匹配的集合
     */
    public List<String> regContent(String content, String regStr) {
        Pattern pattern = Pattern.compile(regStr);
        List<String> result = new ArrayList<>();
        Matcher matcher = pattern.matcher(content);
        int count = 0;
        while (matcher.find()) {
            int matchStart = matcher.start();
            int matchEnd = matcher.end();
            String temp = content.substring(matchStart, matchEnd).replaceAll("#", "");
            result.add(temp);
        }
        return result;
    }
}
