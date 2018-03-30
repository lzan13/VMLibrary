package com.vmloft.develop.library.example.socket;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.tools.VMActivity;
import com.vmloft.develop.library.tools.utils.VMLog;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * TCP Socket Client，实现 TCP Socket 数据的收发
 * Created by lzan13 on 2017/3/12.
 */
public class VMTCPSocketClient extends VMActivity {
    @BindView(R.id.edit_server_host) EditText hostEdit;
    @BindView(R.id.edit_server_port) EditText portEdit;
    @BindView(R.id.edit_message_input) EditText messageEdit;
    @BindView(R.id.text_show_message) TextView messageView;
    @BindView(R.id.btn_connect_server) Button connectBtn;

    // TCP Socket 对象
    private Socket socket = null;
    // 服务器地址
    private String host = "";
    // 服务器监听端口号
    private int port = 5121;
    private boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_tcp_client);
        ButterKnife.bind(activity);

        init();
    }

    private void init() {
        // 设置 TextView 可滚动
        messageView.setMovementMethod(ScrollingMovementMethod.getInstance());

        initHostAddress();
        printIpAddress();
        getWifiIPInfo();

        hostEdit.setText(host);
        portEdit.setText(String.valueOf(port));
        connectBtn.setText("连接服务器");
    }

    /**
     * 界面控件点击监听
     */
    @OnClick({R.id.btn_connect_server, R.id.btn_send_message})
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_connect_server:
            if (isConnected) {
                closeConnect();
            } else {
                startConnect();
            }
            break;
        case R.id.btn_send_message:
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String content = messageEdit.getText().toString();
                    sendMessage(content);
                }
            }).start();
            break;
        }
    }

    public void startConnect() {
        if (!checkEdit()) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                connectTCPServer();
            }
        }).start();
    }

    /**
     * 使用 TCP Socket 的方式连接服务器
     */
    public void connectTCPServer() {
        try {
            if (socket != null) {
                updateShowMessage("已经连接服务器，不需要重连");
                return;
            }
            // 实例化 Socket 对象，带上服务器地址和端口号
            socket = new Socket(host, port);
            isConnected = true;
            updateShowMessage("连接服务器成功");
            // 使用 InputStream 接收服务器发送过来的数据
            InputStream inputStream = socket.getInputStream();
            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                String message = new String(buffer, 0, len);
                updateShowMessage(String.format("收到:[%s]", message));
            }
        } catch (IOException e) {
            e.printStackTrace();
            updateShowMessage(String.format("Error: 服务器出现错误 %s", e.getMessage()));
        }
    }

    /**
     * 发送输入的消息到服务器；
     */
    public void sendMessage(String content) {
        if (socket == null) {
            updateShowMessage("Error: 未连接服务器，请连接");
            return;
        }
        try {
            // 获取 Socket 的 OutputStream 对象，用于向服务器发送数据
            OutputStream output = socket.getOutputStream();
            // 将数据写入到 OutputStream，发送到服务器
            output.write(content.getBytes());
            output.flush();
            updateShowMessage(String.format("发送:[%s]", content));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 断开与服务器的链接
     */
    public void closeConnect() {
        connectBtn.setText("连接服务器");
        updateShowMessage(String.format("服务器连接已断开"));
        isConnected = false;
        if (socket != null) {
            try {
                socket.close();
                socket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 输入框验证
     */
    private boolean checkEdit() {
        host = hostEdit.getText().toString();
        port = Integer.valueOf(portEdit.getText().toString());

        if (TextUtils.isEmpty(host)) {
            updateShowMessage("Error: 服务器地址不能为空");
            return false;
        }
        if (port < 1024 || port > 65536) {
            updateShowMessage("Error: 断开必须是 1024~65536 之间的数字");
            return false;
        }
        return true;
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
                        host = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(host)) {
            updateShowMessage("Error: 获取本机地址失败，请检查网络连接");
        }
    }

    public static void printIpAddress() {
        try {
            Enumeration<NetworkInterface> eni = NetworkInterface.getNetworkInterfaces();
            while (eni.hasMoreElements()) {

                NetworkInterface networkCard = eni.nextElement();
                if (!networkCard.isUp()) { // 判断网卡是否在使用
                    continue;
                }

                String DisplayName = networkCard.getDisplayName();

                List<InterfaceAddress> addressList = networkCard.getInterfaceAddresses();
                Iterator<InterfaceAddress> addressIterator = addressList.iterator();
                while (addressIterator.hasNext()) {
                    InterfaceAddress interfaceAddress = addressIterator.next();
                    InetAddress address = interfaceAddress.getAddress();
                    if (!address.isLoopbackAddress()) {
                        String hostAddress = address.getHostAddress();

                        if (hostAddress.indexOf(":") > 0) {
                        } else {
                            String maskAddress = calcMaskByPrefixLength(
                                    interfaceAddress.getNetworkPrefixLength());
                            String gateway = calcSubnetAddress(hostAddress, maskAddress);

                            String broadcastAddress = null;
                            InetAddress broadcast = interfaceAddress.getBroadcast();
                            if (broadcast != null) {
                                broadcastAddress = broadcast.getHostAddress();
                            }
                            StringBuilder sb = new StringBuilder("当前使用网络信息");
                            sb.append("\nDisplayName: " + DisplayName);
                            sb.append("\naddress: " + hostAddress);
                            sb.append("\nnetmask: " + maskAddress);
                            sb.append("\ngateway: " + gateway);
                            sb.append("\nbroadcast: " + broadcastAddress);
                            VMLog.d(sb.toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String calcMaskByPrefixLength(int length) {

        int mask = 0xffffffff << (32 - length);
        int partsNum = 4;
        int bitsOfPart = 8;
        int maskParts[] = new int[partsNum];
        int selector = 0x000000ff;

        for (int i = 0; i < maskParts.length; i++) {
            int pos = maskParts.length - 1 - i;
            maskParts[pos] = (mask >> (i * bitsOfPart)) & selector;
        }

        String result = "";
        result = result + maskParts[0];
        for (int i = 1; i < maskParts.length; i++) {
            result = result + "." + maskParts[i];
        }
        return result;
    }

    public static String calcSubnetAddress(String ip, String mask) {
        String result = "";
        try {
            // calc sub-net IP
            InetAddress ipAddress = InetAddress.getByName(ip);
            InetAddress maskAddress = InetAddress.getByName(mask);

            byte[] ipRaw = ipAddress.getAddress();
            byte[] maskRaw = maskAddress.getAddress();

            int unsignedByteFilter = 0x000000ff;
            int[] resultRaw = new int[ipRaw.length];
            for (int i = 0; i < resultRaw.length; i++) {
                resultRaw[i] = (ipRaw[i] & maskRaw[i] & unsignedByteFilter);
            }

            // make result string
            result = result + resultRaw[0];
            for (int i = 1; i < resultRaw.length; i++) {
                result = result + "." + resultRaw[i];
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void getWifiIPInfo() {
        WifiManager wifiManager = ((WifiManager) getApplicationContext().getSystemService(
                WIFI_SERVICE));
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        StringBuilder sb = new StringBuilder("WiFi网络信息");
        if (wifiManager.isWifiEnabled()) {
            sb.append("\nipAddress: " + intToIp(dhcpInfo.ipAddress));
            sb.append("\nnet mask: " + intToIp(dhcpInfo.netmask));
            sb.append("\ngateway: " + intToIp(dhcpInfo.gateway));
            sb.append("\nserverAddr: " + intToIp(dhcpInfo.serverAddress));
            sb.append("\nmacAddr: " + wifiInfo.getMacAddress());
            sb.append("\ndns1: " + intToIp(dhcpInfo.dns1));
            sb.append("\ndns2: " + intToIp(dhcpInfo.dns2));
            sb.append("\nlease: " + dhcpInfo.leaseDuration);
            sb.append("\n");
        }
        VMLog.d(sb.toString());
    }

    private String intToIp(int paramInt) {
        return (paramInt & 0xFF)
                + "."
                + (0xFF & paramInt >> 8)
                + "."
                + (0xFF & paramInt >> 16)
                + "."
                + (0xFF & paramInt >> 24);
    }

    /**
     * 更新消息显示
     */
    private void updateShowMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isConnected) {
                    connectBtn.setText("断开服务器");
                } else {
                    connectBtn.setText("连接服务器");
                }
                messageView.setText(messageView.getText().toString() + message + "\n");
            }
        });
    }

    /**
     * 拦截返回键，断开链接再关闭界面，其实这个断开连接没什么作用，因为服务器的连接还存在，当下次在进入洁面时，
     * 新的连接和服务器端保持的连接已经不是同一个链接了，因此服务器收收不到新连接发送的消息
     */
    @Override
    public void onBackPressed() {
        closeConnect();
        finish();
    }
}
