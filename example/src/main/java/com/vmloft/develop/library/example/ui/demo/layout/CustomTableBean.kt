package com.vmloft.develop.library.example.ui.demo.layout

/**
 * Create by lzan13 on 2021/7/15
 * 描述：表格数据bean
 */
data class CustomTableBean(
    val ccName: String = "客服名称",
    val sDate: String = "日期",
    val firstOnlineTime: String = "最早上线时间",
    val lastOfflineTime: String = "最晚离线时间",
    val onlineDuration: String = "在线时长",
    val busyDuration: String = "忙碌时长",
    val offlineDuration: String = "离线时长",

) {

}
