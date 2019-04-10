package com.example.vmutils.algorithm;

public class VMString {

    private char[] cc = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    public static void main(String args[]) {
        String str = "42";

        int result = myAtoi(str);
        System.out.println("字符串转数字 " + result);
    }

    public static int myAtoi(String str) {
        int result = 0;
        // 去除前后空格
        str = str.trim();
        if (str.length() < 1) {
            return 0;
        }
        // 截取第一段
        int index = str.indexOf(" ");
        if (index != -1) {
            str = str.substring(0, index);
        }

        index = 0;
        // 正负标记
        int flag = 1;
        if (str.substring(0, 1).equals("-")) {
            flag = -1;
            index = 1;
        }
        while (index < str.length()) {
            char c = str.charAt(index);
            if (c >= '0' && c <= '9') {
                result = result * 10 + Character.digit(c, 10);
            } else {
                return 0;
            }
        }
        result = result * flag;
        if (result > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (result < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return result;
    }
}
