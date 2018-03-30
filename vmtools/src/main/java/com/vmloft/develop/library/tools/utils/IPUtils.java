package com.vmloft.develop.library.tools.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.vmloft.develop.library.tools.VMApplication;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;

/**
 * Created by lzan13 on 2018/3/18.
 */
public class IPUtils {

    public static String getLocalIP() {
        return getLocalIP(VMApplication.getContext());
    }

    /**
     * 获取本地 IP 地址
     */
    public static String getLocalIP(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String localIP = String.format(Locale.getDefault(), "%d.%d.%d.%d", (ipAddress & 0xff),
                (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        return localIP;
    }

    /**
     * 获取本机ip
     *
     * @param context
     * @return
     * @throws UnknownHostException
     */
    public static InetAddress getLocalIPAddress(Context context) throws UnknownHostException {
        return InetAddress.getByName(getLocalIP(context));
    }

    /**
     * 获取当前设备 mac 地址
     */
    public static String getMacAddress() {
        return getMacAddress(VMApplication.getContext());
    }

    /**
     * 获取当前设备 mac 地址
     */
    public static String getMacAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getMacAddress();
    }

}
