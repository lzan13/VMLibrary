package com.vmloft.develop.library.example.socket;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.tools.VMActivity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * UDP Socket Client，实现 UDP Socket 数据的收发
 * Created by lzan13 on 2017/3/12.
 */
public class VMUDPSocketClient extends VMActivity {
    @BindView(R.id.edit_server_host) EditText hostEdit;
    @BindView(R.id.edit_server_port) EditText portEdit;
    @BindView(R.id.edit_message_input) EditText messageEdit;
    @BindView(R.id.text_show_message) TextView messageView;
    @BindView(R.id.btn_connect_server) Button connectBtn;

    // UDP Socket 对象
    private DatagramSocket socket = null;

    // 服务器地址
    private String host = "";
    // 服务器监听端口号
    private int port = 5122;
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
        hostEdit.setText(host);
        portEdit.setText(String.valueOf(port));
        connectBtn.setText("启动 UDP 服务");
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
                connectUDPServer();
            }
        }).start();
    }

    /**
     * 使用UDP socket 连接服务器
     */
    public void connectUDPServer() {
        try {
            if (socket != null) {
                updateShowMessage("已经连接服务器，不需要重连");
                return;
            }
            socket = new DatagramSocket(port);
//            // 解决端口已经被占用问题
//            socket.setReuseAddress(true);
//            socket.setBroadcast(true);
//            socket.bind(new InetSocketAddress(port));
            isConnected = true;
            updateShowMessage("启动 UDP 服务成功");
            while (true) {
                byte data[] = new byte[1024];
                DatagramPacket packet = new DatagramPacket(data, data.length);
                socket.receive(packet);
                String message = new String(packet.getData(), packet.getOffset(),
                        packet.getLength());
                updateShowMessage(String.format("收到:[%s]", message));
            }
        } catch (IOException e) {
            e.printStackTrace();
            updateShowMessage(String.format("Error: 服务器出现错误 %s", e.getMessage()));
        }
    }

    /**
     * UDP 发送数据到 UDP 服务器
     */
    public void sendMessage(String content) {
        if (socket == null) {
            updateShowMessage("Error: 未启动 UDP 服务，请先启动服务");
            return;
        }
        try {
            InetAddress inetAddress = InetAddress.getByName(host);
            byte data[] = content.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, inetAddress, port);
            //发送数据到服务端
            socket.send(packet);
            updateShowMessage(String.format("发送:[%s]", content));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 断开与服务器的链接
     */
    public void closeConnect() {
        connectBtn.setText("启动 UDP 服务");
        updateShowMessage(String.format("UDP 服务已停止"));
        isConnected = false;
        if (socket != null) {
            socket.close();
            socket = null;
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

    /**
     * 更新消息显示
     */
    private void updateShowMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isConnected) {
                    connectBtn.setText("停止 UDP 服务");
                } else {
                    connectBtn.setText("启动 UDP 服务");
                }
                messageView.setText(messageView.getText().toString() + message + "\n");
            }
        });
    }

    /**
     * 拦截返回键，断开链接再关闭界面
     */
    @Override
    public void onBackPressed() {
        closeConnect();
        finish();
    }
}
