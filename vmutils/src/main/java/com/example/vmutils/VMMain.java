package com.example.vmutils;


import com.example.vmutils.regex.VMRegex;

public class VMMain {

    public static void main(String[] args) {
        System.out.println("lzan13 - run java main");

        testRegexContent();
    }

    /**
     * 测试正则匹配内容
     */
    private static void testRegexContent() {
        String content = "#海贼王# #ONEPIECE# 第876话:仁义的#男子汉# #甚平# #大海 流通道";
//        String regStr = "#([\\S]*?)#";
        String regStr = "#[\\u4e00-\\u9fa5a-zA-Z0-9_-]{2,30}";
        new VMRegex().regContent(content, regStr);
    }

}
