package com.example.vmutils;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VMMain {

    public static void main(String[] args) {
        System.out.println("lzan13 - run java main");

        String content = "#海贼王# #ONEPIECE# 第876话:仁义的#男子汉# #甚平# #大海 流通道";
//        String regStr = "#([\\S]*?)#";
        String regStr = "#[\\u4e00-\\u9fa5a-zA-Z0-9_-]{2,30}";
        new VMMain().regContent(content, regStr);
    }

    public static List<String> regContent(String content, String regStr) {
        Pattern pattern = Pattern.compile(regStr);
        List<String> result = new ArrayList<>();
        Matcher matcher = pattern.matcher(content);
        int count = 0;
        while (matcher.find()) {
            int matchStart = matcher.start();
            int matchEnd = matcher.end();
            String temp = content.substring(matchStart, matchEnd).replaceAll("#", "");
            result.add(temp);
            System.out.println("匹配 " + count++ + "." + temp);
        }
        return result;
    }
}
