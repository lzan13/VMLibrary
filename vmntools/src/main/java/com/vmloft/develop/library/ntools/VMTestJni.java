package com.vmloft.develop.library.ntools;

/**
 * Created by lzan13 on 2018/5/7.
 */

public class VMTestJni {

    public int age = 20;
    public String name = "lz";
    public static String say = "hello ";

    /**
     * 普通方法，测试通过 jni 调用 java 方法
     */
    public long sayTime() {
        long time = System.currentTimeMillis();
        return time;
    }

    /**
     * 普通方法，测试通过 jni 调用 java 方法
     */
    public String sayHello() {
        return "Hello jni";
    }

    /**
     * 普通的静态方法
     */
    public static String sayHelloStatic() {
        return "Hello static jni";
    }

    /**
     * 本地方法
     */
    public static native void giveArray(int[] array);

    public static native int[] getArray();

    public static native String sayHelloJni();

    public native void say();

    public native String getString(String[] strArr);

    public native String[] getStringArray();

    /**
     * 创建一个全局引用
     */
    public native void createGlobalRef();

    /**
     * 获取全局引用
     */
    public native String getGlobalRef();

    /**
     * 释放全局引用，全局引用必须手动释放
     */
    public native void deleteGlobalRef();
}
