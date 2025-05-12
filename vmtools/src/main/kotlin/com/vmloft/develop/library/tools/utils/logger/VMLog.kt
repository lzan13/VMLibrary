package com.vmloft.develop.library.tools.utils.logger

import android.util.Log

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by lzan13 on 2014/12/16.
 *
 * log 日志输出封装类
 */
object VMLog {
    // 缩进
    private const val jsonIndent = 2

    // 堆栈信息偏移量
    private var minStackOffset = 5
    private var mCustomOffset = 0
    private var mLogClassName = VMLog::class.java.name

    // 默认日志 Tag
    private var mTag = "VMLog"

    private var mLevel = Log.WARN // 日志的等级，可以进行配置，最好在Application中进行全局的配置

    /**
     * 支持用户自己传tag，可扩展性更好
     * @param tag 标签
     * @param className 自定义包装类名
     */
    fun init(
        level: Int = mLevel,
        tag: String = mTag,
        offset: Int = 1,
        className: String = ""
    ) {
        mLevel = level
        mTag = tag
        mCustomOffset = offset
        if (className.isNotEmpty()) {
            mLogClassName = className
        }
    }

    fun f(msg: String) {
        log(Log.ASSERT, msg);
    }

    fun e(msg: String) {
        log(Log.ERROR, msg);
    }

    fun w(msg: String) {
        log(Log.WARN, msg);
    }

    fun i(msg: String) {
        log(Log.INFO, msg);
    }

    fun d(msg: String) {
        log(Log.DEBUG, msg);
    }

    fun v(msg: String) {
        log(Log.VERBOSE, msg);
    }

    /**
     * 格式化 JSON 日志输出
     */
    fun json(json: String) {
        var jsonContent = json

        if (jsonContent.isBlank()) {
            e("JSON 日志内容为空，无法格式化输出")
            return
        }

        try {
            jsonContent = json.trim { it <= ' ' }
            if (json.startsWith("{")) {
                val jsonObject = JSONObject(jsonContent)
                var message = jsonObject.toString(jsonIndent)
                message = message.replace("\n".toRegex(), "\n┆ ")
                log(Log.DEBUG, "格式化 JSON日志：\n┆ $message")
                return
            }
            if (jsonContent.startsWith("[")) {
                val jsonArray = JSONArray(jsonContent)
                var message = jsonArray.toString(jsonIndent)
                message = message.replace("\n".toRegex(), "\n║ ")
                log(Log.DEBUG, "格式化 JSON 日志：\n┆ $message")
                return
            }
            e("JSON 内容有错 $jsonContent")
        } catch (e: JSONException) {
            e("JSON 内容有错 ${e.message}")
        }

    }

    private fun log(level: Int, msg: String) {
//        printLog(level, "┆──────────────────────────────────────────────────────────────────────────")
//        printLog(level, "┆ Thread[${getThreadInfo()}] - ${getStackInfo()}")
//        printLog(level, "┆──────────────────────────────────────────────────────────────────────────")
        printLog(level, "┆ ${getStackInfo()} $msg")

    }

    /**
     * 日志收口
     */
    private fun printLog(level: Int, msg: String) {
        if (level < mLevel || msg.isEmpty()) return
        when (level) {
            Log.VERBOSE -> Log.v(mTag, msg)
            Log.DEBUG -> Log.d(mTag, msg)
            Log.INFO -> Log.i(mTag, msg)
            Log.WARN -> Log.w(mTag, msg)
            Log.ERROR -> Log.e(mTag, msg)
            Log.ASSERT -> Log.wtf(mTag, msg)
        }
    }

    /**
     * 获取线程信息
     */
    private fun getThreadInfo(): String {
        val threadName = Thread.currentThread().name
        val threadId = Thread.currentThread().id
        return "$threadName:$threadId"
    }

    /**
     * 获取堆栈信息，方便点击跳转
     */
    private fun getStackInfo(): String {
        val sElements = Thread.currentThread().stackTrace
        var stackOffset = getStackOffset(sElements)
        stackOffset++
        // 类名
        var className = sElements[stackOffset].className
        className = className.substring(className.lastIndexOf(".") + 1)
        // 方法名
        val methodName = sElements[stackOffset].methodName
        // 文件名
        val fileName = sElements[stackOffset].fileName
        // 行号
        val lineNumber = sElements[stackOffset].lineNumber

        return "$className.$methodName ($fileName:$lineNumber)"
    }

    /**
     * 获取堆栈信息偏移
     */
    private fun getStackOffset(trace: Array<StackTraceElement>): Int {
        var i = minStackOffset
        while (i < trace.size) {
            val e = trace[i]
            val name = e.className
            if (name != mLogClassName || i == minStackOffset + mCustomOffset) {
                return --i
            }
            i++
        }
        return -1
    }

}