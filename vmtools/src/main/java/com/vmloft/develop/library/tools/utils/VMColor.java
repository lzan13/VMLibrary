package com.vmloft.develop.library.tools.utils;

import android.content.Context;
import androidx.core.content.ContextCompat;

import com.vmloft.develop.library.tools.VMTools;

/**
 * Created by lzan13 on 2018/4/26.
 * 颜色工具类
 */
public class VMColor {

    /**
     * 通过资源 id 获取颜色值
     */
    public static int byRes(int resId) {
        return byRes(VMTools.getContext(), resId);
    }

    /**
     * 通过资源 id 获取颜色值
     */
    public static int byRes(Context context, int resId) {
        return ContextCompat.getColor(VMTools.getContext(), resId);
    }

}
