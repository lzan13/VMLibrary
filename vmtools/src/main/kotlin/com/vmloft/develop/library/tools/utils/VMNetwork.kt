package com.vmloft.develop.library.tools.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo.State
import android.net.wifi.WifiManager

import com.vmloft.develop.library.tools.VMTools
import com.vmloft.develop.library.tools.VMTools.context

import java.util.*

/**
 * Created by lzan13 on 2016/12/7.
 * 自定义封装网络工具类
 */
object VMNetwork {

    /**
     * 检测网络是否连接
     */
    fun hasNetwork(context: Context = VMTools.context): Boolean {
        var flag = false
        //得到网络连接信息
        var manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //去进行判断网络是否连接
        manager.activeNetworkInfo?.let { flag = it.isAvailable }
        return flag
    }

    /**
     * 网络已经连接情况下，去判断是 WIFI 还是 GPRS
     * 可以根据返回情况做一些自己的逻辑调用
     */
    val isGPRSNetwork: Boolean
        get() {
            var gprs = State.DISCONNECTED
            var manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)?.let { gprs = it.state }
            return gprs == State.CONNECTED || gprs == State.CONNECTING
        }

    /**
     * 网络已经连接情况下，去判断是 WIFI 还是 GPRS
     * 可以根据返回情况做一些自己的逻辑调用
     */
    val isWIFINetwork: Boolean
        get() {
            var manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var wifi = State.DISCONNECTING
            manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)?.let { wifi = it.state }
            return wifi == State.CONNECTED || wifi == State.CONNECTING
        }

    /**
     * 获取本地 IP 地址
     */
    val localIP: String
        get() = getLocalIP(context)

    /**
     * 获取本地 IP 地址
     *
     * @param context 上下文对象
     */
    fun getLocalIP(context: Context): String {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val ipAddress = wifiInfo.ipAddress
        return String.format(Locale.getDefault(), "%d.%d.%d.%d", ipAddress and 0xff, ipAddress shr 8 and 0xff, ipAddress shr 16 and 0xff, ipAddress shr 24 and 0xff)
    }

    /**
     * 获取当前设备 mac 地址
     */
    val macAddress: String
        get() = getMacAddress(context)

    /**
     * 获取当前设备 mac 地址
     *
     * @param context 上下文对象
     */
    fun getMacAddress(context: Context): String {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        return wifiInfo.macAddress
    }
}