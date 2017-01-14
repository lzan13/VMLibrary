package com.vmloft.develop.library.tools.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2015/3/26.
 */
public class VMCryptoUtil {

    private static final char[] HEX_ARRAY = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 将字符串通过java提供的MessageDigest类进行SHA1方式加密
     * @param str
     * @return
     */
    public static String cryptoStr2SHA1(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            byte[] bytes = md.digest();
            return byte2Hex(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将字符串通过MessageDigest 进行MD5方式加密，并返回Hex十六进制式字符串
     * @param str
     * @return
     */
    public static String cryptoStr2MD5(String str){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] bytes = md.digest();
            return byte2Hex(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过位运算将byte类型转化为hex16进制表示的字符串
     * @param bytes
     * @return
     */
    public static String byte2Hex(byte[] bytes){
        StringBuffer sb = new StringBuffer(bytes.length * 2);
        for(int i=0; i<bytes.length; i++) {
            sb.append(HEX_ARRAY[(bytes[i] & 0xf0) >>> 4]);
            sb.append(HEX_ARRAY[bytes[i] & 0x0f]);
        }
        return sb.toString();
    }

}
