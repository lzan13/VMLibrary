package com.vmloft.develop.library.example.socket;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Socket Server，实现 TCP、UDP Socket 数据的接收与调度
 * Created by lzan13 on 2017/3/12.
 */
public class VMUDPSocketServer extends VMActivity {
    @BindView(R.id.edit_server_host) EditText hostEdit;
    @BindView(R.id.edit_server_port) EditText portEdit;
    @BindView(R.id.text_server_state) TextView stateView;
    @BindView(R.id.text_show_message) TextView messageView;
    @BindView(R.id.btn_start_server) Button serverBtn;

    // UDP Socket 对象
    private DatagramSocket serverUDPSocket = null;
    private boolean isRunning = false;

    // 服务器地址，以及监听端口
    private String host = "";
    private int port = 5122;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_server);

        ButterKnife.bind(activity);

        init();
    }

    private void init() {
        // 设置 TextView 可滚动
        messageView.setMovementMethod(ScrollingMovementMethod.getInstance());

        initHostAddress();
        hostEdit.setText(host);
        portEdit.setText(String.valueOf(port));
        serverBtn.setText("启动 UDP 服务");
    }

    @OnClick({R.id.btn_start_server})
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.btn_start_server:
            if (isRunning) {
                stopServer();
            } else {
                startServer();
            }
            break;
        }
    }

    /**
     * 开启服务
     */
    private void startServer() {
        if (!checkEdit()) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                startUDPServer();
            }
        }).start();
    }

    /**
     * 开启 UDP 服务器
     */
    private void startUDPServer() {
        try {
            // 实例化 DatagramSocket 对象，并指定监听端口
            serverUDPSocket = new DatagramSocket(port);
            isRunning = true;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    stateView.setText(String.valueOf(isRunning));
                    stateView.setTextColor(Color.GREEN);
                    serverBtn.setText("停止 UDP 服务");
                }
            });
            updateShowMessage(String.format("UDP 服务启动成功，监听端口 %d", port));
            while (true) {
                // 创建一个空的字节数组，用来接收其他端发来的消息
                byte[] data = new byte[1024];
                DatagramPacket packet = new DatagramPacket(data, data.length);
                /**
                 * 调用 UDP Socket 的 receive() 接收消息方法，
                 * 此方法类似于 TCP 的 accept()，没有连接时都处于阻塞状态
                 */
                serverUDPSocket.receive(packet);
                // 获取当前链接到服务器的客户端地址
                InetAddress inetAddress = packet.getAddress();
                // 获取消息内容
                String message = String.format("收到:[%s] 来自:[%s:%d]",
                        new String(packet.getData(), packet.getOffset(), packet.getLength()),
                        inetAddress.getHostAddress(), packet.getPort());
                // 更新 UI 显示
                updateShowMessage(message);

                // 收到消息后，给客户端回一个消息
                String replyMsg = "Hello client!";
                data = new String(replyMsg).getBytes();
                packet = new DatagramPacket(data, data.length, inetAddress, port);
                //发送数据到客户端
                serverUDPSocket.send(packet);
                // 更新 UI 显示
                updateShowMessage(String.format("回复:[%s]", replyMsg));
            }
        } catch (IOException e) {
            updateShowMessage(String.format("Error: 服务器出现错误 %s", e.getMessage()));
            e.printStackTrace();
        }
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
     * 更新消息显示
     */
    private void updateShowMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageView.setText(messageView.getText().toString() + message + "\n");
            }
        });
    }

    /**
     * 停止服务
     */
    private void stopServer() {
        serverBtn.setText("启动 UDP 服务");
        updateShowMessage(String.format("UDP 服务器已停止"));
        if (serverUDPSocket != null) {
            serverUDPSocket.close();
            serverUDPSocket = null;
        }

        isRunning = false;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stateView.setText(String.valueOf(isRunning));
                stateView.setTextColor(Color.RED);
            }
        });
    }

    /**
     * 拦截 back 按键
     */
    @Override
    public void onBackPressed() {
        stopServer();
        finish();
    }
}
