package com.vmloft.develop.library.tools.utils;

import java.util.Random;

/**
 * Create by lzan13 on 2019/6/6 12:51
 *
 * 一个工具类的封装
 */
public class VMUtils {

    /**
     * 生成 [0, max) 范围内的随机数
     *
     * @param max 最大值（不包含）
     */
    public static int random(int max) {
        return random(0, max);
    }

    /**
     * 生成制定范围内的随机数 范围 [start, end)
     *
     * @param min 最小值（包含）
     * @param max 最大值（不包含）
     */
    public static int random(int min, int max) {
        Random random = new Random();
        return min + random.nextInt(max - min);
    }
}
