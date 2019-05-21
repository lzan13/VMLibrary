package com.vmloft.develop.library.tools.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.vmloft.develop.library.tools.VMTools;

import java.util.Map;

/**
 * Created by Administrator on 2015/5/12.
 * 封装 SharedPreferences 数据操作类
 */
public class VMSPUtil {

    /**
     * 保存在手机里面的文件名
     */
    private static final String VM_SP_PREFIX = "vm_sp_";
    private static final String VM_SP_SUFFIX = ".sp";

    private static SharedPreferences getSharedPreferences(Context context) {
        String spFileName = VM_SP_PREFIX + context.getPackageName() + VM_SP_SUFFIX;
        return context.getSharedPreferences(spFileName, Context.MODE_PRIVATE);
    }

    /**
     * 保存数据的方法，这里不需要传入上下文对象
     */
    public static void put(String key, Object object) {
        put(VMTools.getContext(), key, object);
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     */
    public static void put(Context context, String key, Object object) {
        SharedPreferences sp = getSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        editor.commit();
    }

    /**
     * 得到保存数据的方法，这里不需要传入上下文对象
     */
    public static Object get(String key, Object defaultObject) {
        return get(VMTools.getContext(), key, defaultObject);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = getSharedPreferences(context);
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    /**
     * 移除某个key值已经对应的值
     */
    public static void remove(String key) {
        remove(VMTools.getContext(), key);
    }

    /**
     * 移除某个key值已经对应的值
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = getSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 清除所有数据
     */
    public static void clear() {
        clear(VMTools.getContext());
    }

    /**
     * 清除所有数据
     */
    public static void clear(Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 查询某个key是否已经存在
     */
    public static boolean contains(String key) {
        return contains(VMTools.getContext(), key);
    }

    /**
     * 查询某个key是否已经存在
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public static Map<String, ?> getAll() {
        return getAll(VMTools.getContext());
    }

    /**
     * 返回所有的键值对
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getAll();
    }
}
