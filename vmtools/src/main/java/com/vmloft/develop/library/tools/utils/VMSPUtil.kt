package com.vmloft.develop.library.tools.utils

import android.content.Context
import android.content.SharedPreferences
import com.vmloft.develop.library.tools.VMTools
import com.vmloft.develop.library.tools.VMTools.context

/**
 * Created by Administrator on 2015/5/12.
 * 封装 SharedPreferences 数据操作类
 */
object VMSPUtil {
    /**
     * 保存在手机里面的文件名
     */
    private const val VM_SP_PREFIX = "vm_sp_"
    private const val VM_SP_SUFFIX = ".sp"
    private fun getSharedPreferences(context: Context): SharedPreferences {
        val spFileName = VM_SP_PREFIX + context.packageName + VM_SP_SUFFIX
        return context.getSharedPreferences(spFileName, Context.MODE_PRIVATE)
    }

    /**
     * 保存数据的方法，这里不需要传入上下文对象
     */
    fun put(key: String?, obj: Any) {
        put(context, key, obj)
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     */
    fun put(context: Context, key: String?, obj: Any) {
        val sp = getSharedPreferences(context)
        val editor = sp.edit()
        if (obj is String) {
            editor.putString(key, obj)
        } else if (obj is Int) {
            editor.putInt(key, obj)
        } else if (obj is Boolean) {
            editor.putBoolean(key, obj)
        } else if (obj is Float) {
            editor.putFloat(key, obj)
        } else if (obj is Long) {
            editor.putLong(key, obj)
        } else {
            editor.putString(key, obj.toString())
        }
        editor.commit()
    }

    /**
     * 得到保存数据的方法，这里不需要传入上下文对象
     */
    fun get(key: String?, defaultObject: Any?): Any? {
        return get(context, key, defaultObject)
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
    fun get(context: Context, key: String?, defaultObject: Any?): Any? {
        val sp = getSharedPreferences(context)
        if (defaultObject is String) {
            return sp.getString(key, defaultObject as String?)
        } else if (defaultObject is Int) {
            return sp.getInt(key, (defaultObject as Int?)!!)
        } else if (defaultObject is Boolean) {
            return sp.getBoolean(key, (defaultObject as Boolean?)!!)
        } else if (defaultObject is Float) {
            return sp.getFloat(key, (defaultObject as Float?)!!)
        } else if (defaultObject is Long) {
            return sp.getLong(key, (defaultObject as Long?)!!)
        }
        return null
    }

    /**
     * 移除某个key值已经对应的值
     */
    fun remove(key: String?) {
        remove(context, key)
    }

    /**
     * 移除某个key值已经对应的值
     */
    fun remove(context: Context, key: String?) {
        val sp = getSharedPreferences(context)
        val editor = sp.edit()
        editor.remove(key)
        editor.commit()
    }

    /**
     * 清除所有数据
     */
    fun clear(context: Context = VMTools.context) {
        val sp = getSharedPreferences(context)
        val editor = sp.edit()
        editor.clear()
        editor.commit()
    }

    /**
     * 查询某个key是否已经存在
     */
    fun contains(key: String?): Boolean {
        return contains(context, key)
    }

    /**
     * 查询某个key是否已经存在
     */
    fun contains(context: Context, key: String?): Boolean {
        val sp = getSharedPreferences(context)
        return sp.contains(key)
    }

    /**
     * 返回所有的键值对
     */
    val all: Map<String, *>
        get() = getAll(context)

    /**
     * 返回所有的键值对
     */
    fun getAll(context: Context): Map<String, *> {
        val sp = getSharedPreferences(context)
        return sp.all
    }
}