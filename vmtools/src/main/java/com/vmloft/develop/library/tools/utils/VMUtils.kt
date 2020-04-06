package com.vmloft.develop.library.tools.utils

import java.util.Random

/**
 * Create by lzan13 on 2019/6/6 12:51
 *
 * 一个工具类的封装
 */
object VMUtils {
    /**
     * 生成 [0, max) 范围内的随机数
     *
     * @param max 最大值（不包含）
     */
    @JvmStatic
    fun random(max: Int): Int {
        return random(0, max)
    }

    /**
     * 生成制定范围内的随机数 范围 [start, end)
     *
     * @param min 最小值（包含）
     * @param max 最大值（不包含）
     */
    @JvmStatic
    fun random(min: Int, max: Int): Int {
        val random = Random()
        return min + random.nextInt(max - min)
    }
}