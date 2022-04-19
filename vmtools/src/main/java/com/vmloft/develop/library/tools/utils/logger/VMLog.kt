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
    private const val minStackOffset = 3

    // 默认日志 Tag
    private var mTag = "VMTools"

    private var mLevel = Level.DEBUG // 日志的等级，可以进行配置，最好在Application中进行全局的配置

    /**
     * 支持用户自己传tag，可扩展性更好
     * @param tag
     */
    fun init(level: Int = mLevel, tag: String = mTag) {
        mLevel = level
        mTag = tag
    }

    fun e(msg: String) {
        if (Level.ERROR >= mLevel) {
            if (msg.isNotBlank()) {
                log(Level.ERROR, msg);
            }
        }
    }

    fun w(msg: String) {
        if (Level.WARN >= mLevel) {
            if (msg.isNotBlank()) {
                log(Level.WARN, msg);
            }
        }
    }

    fun i(msg: String) {
        if (Level.INFO >= mLevel) {
            if (msg.isNotBlank()) {
                log(Level.INFO, msg);
            }

        }
    }

    fun d(msg: String) {
        if (Level.DEBUG >= mLevel) {
            if (msg.isNotBlank()) {
                log(Level.DEBUG, msg);
            }
        }
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
                log(Level.DEBUG, "格式化 JSON日志：\n┆ $message")
                return
            }
            if (jsonContent.startsWith("[")) {
                val jsonArray = JSONArray(jsonContent)
                var message = jsonArray.toString(jsonIndent)
                message = message.replace("\n".toRegex(), "\n║ ")
                log(Level.DEBUG, "格式化 JSON 日志：\n┆ $message")
                return
            }
            e("JSON 内容有错 $jsonContent")
        } catch (e: JSONException) {
            e("JSON 内容有错 ${e.message}")
        }

    }

    private fun log(level: Int, msg: String) {
        printLog(level, "┆──────────────────────────────────────────────────────────────────────────")
        printLog(level, "┆ Thread[${getThreadInfo()}] - ${getStackInfo()}")
        printLog(level, "┆ $msg")
        printLog(level, "┆──────────────────────────────────────────────────────────────────────────")

    }

    private fun printLog(level: Int, msg: String) {
        when (level) {
            Level.VERBOSE -> Log.v(mTag, msg)
            Level.DEBUG -> Log.d(mTag, msg)
            Level.INFO -> Log.i(mTag, msg)
            Level.ERROR -> Log.e(mTag, msg)
        }
    }

    /**
     * 获取线程信息
     */
    private fun getThreadInfo(): String? {
        val threadName = Thread.currentThread().name
        val threadId = Thread.currentThread().id
        return "$threadName:$threadId"
    }

    /**
     * 获取堆栈信息，方便点击跳转
     */
    private fun getStackInfo(): String? {
        val sElements = Thread.currentThread().stackTrace
        var stackOffset = getStackOffset(sElements)
        stackOffset++
        // 类名
        var className = sElements[stackOffset].className
        className = className.substring(className.lastIndexOf(".") + 1)
        // 方法名
        var methodName = sElements[stackOffset].methodName
        // 文件名
        var fileName = sElements[stackOffset].fileName
        // 行号
        var lineNumber = sElements[stackOffset].lineNumber

        return "$className.$methodName ($fileName:$lineNumber)"
    }

    /**
     * 日志级别
     */
    interface Level {
        companion object {
            const val VERBOSE = 0
            const val DEBUG = 1
            const val INFO = 2
            const val WARN = 3
            const val ERROR = 4
        }
    }

    /**
     * 获取堆栈信息偏移
     */
    private fun getStackOffset(trace: Array<StackTraceElement>): Int {
        var i = minStackOffset
        while (i < trace.size) {
            val e = trace[i]
            val name = e.className
            if (name != VMLog::class.java.name) {
                return --i
            }
            i++
        }
        return -1
    }

}