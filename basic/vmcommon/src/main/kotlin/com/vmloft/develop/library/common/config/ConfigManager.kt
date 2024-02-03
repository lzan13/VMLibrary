package com.vmloft.develop.library.common.config

import com.vmloft.develop.library.common.CConstants
import com.vmloft.develop.library.common.event.LDEventBus
import com.vmloft.develop.library.common.utils.JsonUtils
import com.vmloft.develop.library.tools.utils.logger.VMLog

/**
 * Create by lzan13 on 2022/3/4
 * 描述：客户端配置数据管理，需要从服务器获取，目前规则是 1 小时去服务器获取一次，其他时间都从本地获取
 */
object ConfigManager {
    const val appConfigEvent = "appConfigEvent"

    private var _appConfig: AppConfig = AppConfig()
    val appConfig get() = _appConfig

    private var sensitiveWordMap = mutableMapOf<String, Any>()

    /**
     * 装载配置信息
     */
    fun setupConfig(content: String) {
        if (content.isEmpty()) {
            return
        }
        // 解析配置
        _appConfig = JsonUtils.fromJson(content, AppConfig::class.java) ?: AppConfig()
        // 构建敏感词库
        makeSensitiveMap(_appConfig.sensitiveWords)

        // 通知配置更新
        LDEventBus.post(appConfigEvent)
    }

    /**
     * 构造敏感词 map
     */
    private fun makeSensitiveMap(list: List<String>) {
        for (word in list) {
            var map = sensitiveWordMap
            for (char in word) {
                // 判断是否存在
                if (map[char.toString()] !== null) {
                    // 获取下一层节点
                    map = map[char.toString()] as MutableMap<String, Any>
                } else {
                    // 将当前节点设置为非结尾节点
                    map["end"] = false
                    val item = mutableMapOf<String, Any>()
                    // 新增节点默认为结尾节点
                    item["end"] = true
                    map[char.toString()] = item
                    map = map[char.toString()] as MutableMap<String, Any>
                }
            }
        }
        VMLog.i(sensitiveWordMap.toString())
    }

    /**
     * 检查敏感词是否存在
     */
    fun checkSensitiveWord(content: String, index: Int): String {
        var currentMap = sensitiveWordMap
        var keyNum = 0 // 记录匹配字数
        var sensitiveWord = "" // 记录过滤出来的敏感词
        for (i in index until content.length) {
            val keyChar = content[i]
            if (currentMap[keyChar.toString()] != null) {
                keyNum++
                sensitiveWord += keyChar
                currentMap = currentMap[keyChar.toString()] as MutableMap<String, Any>
                if (currentMap["end"] as Boolean) {
                    // 表示已到词的结尾
                    break
                }
            } else {
                break
            }
        }
        // 两字成词
        return if (keyNum < 2) "" else sensitiveWord
    }

    /**
     * 判断文本中是否存在敏感词
     */
    fun filterSensitiveWord(content: String): String {
        val matchs = mutableListOf<String>()
        // 过滤掉除了中文、英文、数字之外的
        val contentTrim = content.replace("/[^\\u4e00-\\u9fa5\\u0030-\\u0039\\u0061-\\u007a\\u0041-\\u005a]+/g", "")
        for (index in contentTrim.indices) {
            val match = this.checkSensitiveWord(contentTrim, index)
            if (match.isNotEmpty()) {
                matchs.add(match)
            }
        }
        var contentReplace = content
        matchs.forEach { match ->
            contentReplace = contentReplace.replace(match, "**")
        }
        return contentReplace
    };
}

/**
 * 客户端配置数据 Bean，这里是通过服务器下发的配置解析出来的，有默认值
 */
data class AppConfig(
    /*
    {
        "adsConfig": {
            "splashEntry": false,
            "exploreEntry": true,
            "goldEntry": false
        },
        "chatConfig": {
            "chatEntry": true,
            "voiceEntry": true,
            "pictureEntry": true,
            "callEntry": true,
            "giftEntry": true,
            "voiceLimit": 2,
            "pictureLimit": 2,
            "callLimit": 5
        },
        "homeConfig": {
            "randomEntry": true,
            "chatFastEntry": true,
            "relaxationEntry": false,
            "roomEntry": false
        },
        "tradeConfig": {
            "scoreEntry": false,
            "tradeEntry": true,
            "vipEntry": true
        },
        "sensitiveWords": ["习小平", "鸡鸡", "鸡巴", "后入", "做爱", "小穴", "骚逼", "肛交", "群交", "口交", "乳交", "颜射", "观音坐莲", "老汉推车", "传教式", "乳头", "咪咪", "奶子", "乃子", "奶头", "阴道", "阴蒂", "骚货", "母狗", "公狗", "骚母狗", "肉便器", "吞精"]
    }
    */
    var adsConfig: ADSConfig = ADSConfig(), // 广告相关配置
    var chatConfig: ChatConfig = ChatConfig(), // 聊天相关配置
    var commonConfig: CommonConfig = CommonConfig(), // 通用相关配置
    var homeConfig: HomeConfig = HomeConfig(), // 首页相关配置
    var tradeConfig: TradeConfig = TradeConfig(), // 交易相关配置
    var sensitiveWords: MutableList<String> = mutableListOf(), // 敏感词配置
)

/**
 * 广告部分入口
 */
data class ADSConfig(
    var splashEntry: Boolean = false, // 开屏广告入口
    var exploreEntry: Boolean = true, // 发现内容入口
    var goldEntry: Boolean = true, // 金币获取入口
)

/**
 * 聊天配置
 */
data class ChatConfig(
    var chatEntry: Boolean = true, // 聊天入口
    var voiceEntry: Boolean = true, // 语音入口
    var pictureEntry: Boolean = true, // 图片入口
    var callEntry: Boolean = true, // 聊天通话入口
    var giftEntry: Boolean = true, // 礼物入口

    var voiceLimit: Int = 2, // 聊天图片锁 限制数
    var pictureLimit: Int = 2, // 聊天图片锁 限制数
    var callLimit: Int = 5, // 聊天语音通话 锁限制
)

/**
 * 通用配置
 */
data class CommonConfig(
    var publishInterval: Long = CConstants.timeMinute, // 发布内容时间间隔
)

/**
 * 首页部分入口
 */
data class HomeConfig(
    var randomEntry: Boolean = true, // 随机入口
    var chatFastEntry: Boolean = true, // 闪聊入口
    var relaxationEntry: Boolean = false, // 娱乐入口
    var roomEntry: Boolean = false, // 聊天房入口
)

/**
 * 交易配置
 */
data class TradeConfig(
    var scoreEntry: Boolean = true, // 积分相关入口
    var tradeEntry: Boolean = false, // 交易相关入口
    var vipEntry: Boolean = true, // VIP相关入口
)