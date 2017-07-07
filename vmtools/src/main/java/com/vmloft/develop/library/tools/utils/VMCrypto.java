package com.vmloft.develop.library.tools.utils;

/**
 * Created by lzan13 on 2017/7/7.
 * 使用本地原生开发封装加解密工具类
 */
public class VMCrypto {
    public static native String crypStr2SHA1(String string);
    public static native String crypStr2MD5(String string);
}
