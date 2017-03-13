package com.vmloft.develop.library.simple.socket;

import android.os.Bundle;
import com.vmloft.develop.library.simple.R;
import com.vmloft.develop.library.tools.VMBaseActivity;
import com.vmloft.develop.library.tools.utils.VMLog;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Socket Server，实现 TCP、UDP Socket 数据的接收与调度
 * Created by lzan13 on 2017/3/12.
 */
public class VMSocketServer extends VMBaseActivity {
    // TCP Socket 对象
    private Socket tcpSocket = null;
    // UDP Socket 对象
    private DatagramSocket udpSocket = null;

    // 服务器地址，以及监听端口
    private InetAddress inetAddress;
    private String address = "";
    private final int port = 5121;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_server);

        initHostAddress();
        startServer();
    }

    private void startServer() {
        String type = getIntent().getStringExtra("socket_type");
        if (type.equals("tcp")) {
            startTCPServer();
        } else {
            startUDPServer();
        }
    }

    private void startTCPServer() {

    }

    private void startUDPServer() {

    }

    /**
     * 获取当前设备 Host 地址
     */
    private void initHostAddress() {
        try {
            // 获取当前设备网络接口列表
            Enumeration<NetworkInterface> enumNI = NetworkInterface.getNetworkInterfaces();
            while (enumNI.hasMoreElements()) {
                NetworkInterface networkInterface = enumNI.nextElement();
                // 获取当前网络接口的枚举 InetAddress
                Enumeration<InetAddress> enumIA = networkInterface.getInetAddresses();
                while (enumIA.hasMoreElements()) {
                    InetAddress inetAddress = enumIA.nextElement();
                    // 判断当前地址是否是本地地址
                    if (inetAddress.isSiteLocalAddress()) {
                        address = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
