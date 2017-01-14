package com.vmloft.develop.library.tools.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

/**
 * Created by lzan13 on 2016/12/7.
 * 自定义封装网络工具类
 */
public class VMNetUtil {
    //检测网络连接状态
    private static ConnectivityManager manager;

    /**
     * 检测网络是否连接
     */
    public static boolean hasNetwork(Context appContext) {
        boolean flag = false;
        //得到网络连接信息
        manager = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        //去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }
        return flag;
    }

    /**
     * 网络已经连接情况下，去判断是 WIFI 还是 GPRS
     * 可以根据返回情况做一些自己的逻辑调用
     */
    private boolean isGPRSNetwork() {
        State gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (gprs == State.CONNECTED || gprs == State.CONNECTING) {
            return true;
        }
        return false;
    }

    /**
     * 网络已经连接情况下，去判断是 WIFI 还是 GPRS
     * 可以根据返回情况做一些自己的逻辑调用
     */
    private boolean isWIFINetwork() {
        State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
            return true;
        }
        return false;
    }
}
