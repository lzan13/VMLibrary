package com.vmloft.develop.library.tools.utils

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.vmloft.develop.library.tools.VMTools

/**
 * Created by Administrator on 2015/5/12.
 * 封装 SharedPreferences 数据操作工具类
 */
object VMSPUtil {
    /**
     * 保存 SharedPreferences 文件前缀
     */
    private const val vmSPPrefix = "vm_sp_"
    private val entryMap: MutableMap<String, SPEntry> = mutableMapOf()

    class SPEntry(name: String, context: Context = VMTools.context) {

        private var sp: SharedPreferences = context.getSharedPreferences(vmSPPrefix + name, Context.MODE_PRIVATE)

        /**
         * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
         */
        fun put(key: String?, obj: Any) {
            val editor = sp.edit()
            when (obj) {
                is String -> editor.putString(key, obj)
                is Int -> editor.putInt(key, obj)
                is Boolean -> editor.putBoolean(key, obj)
                is Float -> editor.putFloat(key, obj)
                is Long -> editor.putLong(key, obj)
                else -> editor.putString(key, obj.toString())
            }
            editor.commit()
        }

        /**
         * 异步保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
         */
        fun putAsync(key: String?, obj: Any) {
            val editor = sp.edit()
            when (obj) {
                is String -> editor.putString(key, obj)
                is Int -> editor.putInt(key, obj)
                is Boolean -> editor.putBoolean(key, obj)
                is Float -> editor.putFloat(key, obj)
                is Long -> editor.putLong(key, obj)
                else -> editor.putString(key, obj.toString())
            }
            editor.apply()
        }

        /**
         * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
         */
        fun get(key: String?, defaultObject: Any?): Any? {
            return when (defaultObject) {
                is String -> sp.getString(key, defaultObject as String?)
                is Int -> sp.getInt(key, (defaultObject as Int?)!!)
                is Boolean -> sp.getBoolean(key, (defaultObject as Boolean?)!!)
                is Float -> sp.getFloat(key, (defaultObject as Float?)!!)
                is Long -> sp.getLong(key, (defaultObject as Long?)!!)
                else -> null
            }
        }

        /**
         * 移除某个key值已经对应的值
         */
        fun remove(key: String?) {
            val editor = sp.edit()
            editor.remove(key)
            editor.commit()
        }

        /**
         * 异步移除某个key值已经对应的值
         */
        fun removeAsync(key: String?) {
            val editor = sp.edit()
            editor.remove(key)
            editor.apply()
        }

        /**
         * 清除所有数据
         */
        fun clear(context: Context = VMTools.context) {
            val editor = sp.edit()
            editor.clear()
            editor.commit()
        }

        /**
         * 清除所有数据
         */
        fun clearAsync(context: Context = VMTools.context) {
            val editor = sp.edit()
            editor.clear()
            editor.apply()
        }

        /**
         * 查询某个key是否已经存在
         */
        fun contains(key: String?): Boolean {
            return sp.contains(key)
        }

        /**
         * 返回所有的键值对
         */
        fun getAll(context: Context = VMTools.context): Map<String, *> {
            return sp.all
        }
    }

    /**
     * 获取 SharedPreferences 工具操作实体类
     */
    fun getEntry(name: String = ""): SPEntry {
        val spFileName = if (TextUtils.isEmpty(name)) {
            VMTools.context.packageName
        } else {
            name
        }
        var entry = entryMap[spFileName]
        if (entry == null) {
            entry = SPEntry(spFileName)
            entryMap[spFileName] = entry
        }
        return entry
    }

}