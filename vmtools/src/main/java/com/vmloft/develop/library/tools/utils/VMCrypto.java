package com.vmloft.develop.library.tools.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2015/3/26.
 */
public class VMCrypto {

    private static final char[] HEX_ARRAY = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    /**
     * 将字符串通过java提供的MessageDigest类进行SHA1方式加密
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
     */
    public static String cryptoStr2MD5(String str) {
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
     */
    public static String byte2Hex(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            sb.append(HEX_ARRAY[(bytes[i] & 0xf0) >>> 4]);
            sb.append(HEX_ARRAY[bytes[i] & 0x0f]);
        }
        return sb.toString();
    }

    private static int objIdIndex = 0;

    /**
     * 生成唯一 id
     */
    public static String getObjectId() {
        StringBuilder builder = new StringBuilder();

        // 当前时间戳
        int time = (int) (System.currentTimeMillis() / 1000);
        builder.append(Integer.toHexString(time));

        //        VMLog.d("android.os.Build.BRAND: " + android.os.Build.BRAND);               // 获取设备品牌
        //        VMLog.d("android.os.Build.CPU_ABI: " + android.os.Build.CPU_ABI);           // 获取设备指令集名称（CPU的类型）
        //        VMLog.d("android.os.Build.DEVICE: " + android.os.Build.DEVICE);             // 获取设备驱动名称
        //        VMLog.d("android.os.Build.DISPLAY: " + android.os.Build.DISPLAY);           // 获取设备显示的版本包（在系统设置中显示为版本号）和ID一样
        //        VMLog.d("android.os.Build.FINGERPRINT: " + android.os.Build.FINGERPRINT);   // 设备的唯一标识。由设备的多个信息拼接合成。
        //        VMLog.d("android.os.Build.HARDWARE: " + android.os.Build.HARDWARE);         // 设备硬件名称,一般和基板名称一样（BOARD）
        //        VMLog.d("android.os.Build.HOST: " + android.os.Build.HOST);                 // 设备主机地址
        //        VMLog.d("android.os.Build.ID: " + android.os.Build.ID);                     // 设备版本号。
        //        VMLog.d("android.os.Build.MODEL : " + android.os.Build.MODEL);              // 获取手机的型号 设备名称。如：SM-N9100（三星Note4）
        //        VMLog.d("android.os.Build.MANUFACTURER: " + android.os.Build.MANUFACTURER); // 获取设备制造商。如：samsung
        //        VMLog.d("android.os.Build.PRODUCT: " + android.os.Build.PRODUCT);           // 整个产品的名称
        //        VMLog.d("android.os.Build.TAGS: " + android.os.Build.TAGS);                 // 设备标签。如release-keys 或测试的 test-keys
        //        VMLog.d("android.os.Build.TIME: " + android.os.Build.TIME);                 // 时间
        //        VMLog.d("android.os.Build.TYPE: " + android.os.Build.TYPE);                 // 设备版本类型 主要为"user" 或"eng".
        //        VMLog.d("android.os.Build.USER: " + android.os.Build.USER);                 // 设备用户名 基本上都为android-build
        //        VMLog.d("android.os.Build.VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE);           // 获取系统版本字符串。如4.1.2 或2.2 或2.3等
        //        VMLog.d("android.os.Build.VERSION.CODENAME: " + android.os.Build.VERSION.CODENAME);         // 设备当前的系统开发代号，一般使用REL代替
        //        VMLog.d("android.os.Build.VERSION.INCREMENTAL: " + android.os.Build.VERSION.INCREMENTAL);   // 系统源代码控制值，一个数字或者git hash值
        //        VMLog.d("android.os.Build.VERSION.SDK: " + android.os.Build.VERSION.SDK);                   // 系统的API级别 一般使用下面大的SDK_INT 来查看
        //        VMLog.d("android.os.Build.VERSION.SDK_INT: " + android.os.Build.VERSION.SDK_INT);           // 系统的API级别 数字表示 android.os.Build.VERSION_CODES 类中有所有的已公布的Android版本号。全部是Int常亮。可用于与SDK_INT进行比较来判断当前的系统版本
        // 设备唯一信息
        String brand = android.os.Build.BRAND + android.os.Build.VERSION.INCREMENTAL;
        byte[] brandBytes = brand.getBytes();
        for (int i = 0; i < 3; i++) {
            builder.append(HEX_ARRAY[(brandBytes[i] & 0xf0) >> 4]);
            builder.append(HEX_ARRAY[(brandBytes[i] & 0x0f) >> 0]);
        }

        // 进程 pid
        int pid = android.os.Process.myPid();
        builder.append(Integer.toHexString(pid));

        // 自增数
        objIdIndex++;
        byte[] indexBytes = String.format("%03d", objIdIndex).getBytes();
        for (int i = 0; i < indexBytes.length; i++) {
            builder.append(HEX_ARRAY[(indexBytes[i] & 0xf0) >> 4]);
            builder.append(HEX_ARRAY[(indexBytes[i] & 0x0f) >> 0]);
        }

        return builder.toString();
    }
}
