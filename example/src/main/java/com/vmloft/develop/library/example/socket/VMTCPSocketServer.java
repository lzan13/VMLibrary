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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Socket Server，实现 TCP Socket 数据的接收与调度
 * Created by lzan13 on 2017/3/12.
 */
public class VMTCPSocketServer extends VMActivity {
    @BindView(R.id.edit_server_host) EditText hostEdit;
    @BindView(R.id.edit_server_port) EditText portEdit;
    @BindView(R.id.text_server_state) TextView stateView;
    @BindView(R.id.text_show_message) TextView messageView;
    @BindView(R.id.btn_start_server) Button serverBtn;

    // TCP Socket 对象
    private ServerSocket serverSocket = null;
    private boolean isRunning = false;

    // 服务器地址，以及监听端口
    private String host = "";
    private int port = 5121;


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
        serverBtn.setText("启动 TCP 服务");
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
                startTCPServer();
            }
        }).start();
    }

    /**
     * 开启 TCP 服务器
     */
    private void startTCPServer() {
        try {
            // 实例化 ServerSocket，服务器开始监听定义的端口
            serverSocket = new ServerSocket(port);
            isRunning = true;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    stateView.setText(String.valueOf(isRunning));
                    stateView.setTextColor(Color.GREEN);
                    serverBtn.setText("停止 TCP 服务");
                }
            });
            updateShowMessage(String.format("TCP 服务启动成功，监听端口 %d", port));
            /**
             * 调用 ServerSocket 的 accept 方法获取新的连接，
             * 这个方法会等待客户端的连接请求，如果没有新消息，会阻塞当前线程
             */
            Socket socket = serverSocket.accept();
            updateShowMessage(String.format("新链接:[%s:%d]", socket.getInetAddress().getHostAddress(),
                    socket.getPort()));
            // 通过 InputStream 从连接到服务器的 Socket 读取数据
            InputStream inputStream = socket.getInputStream();
            int len = -1;
            byte[] buffer = new byte[1024];
            /**
             * inputStream.read()这个方法也是会阻塞线程的，当此 Socket 没有新的消息时，会一直停在这里，
             * 如果要实现多客户端连接服务器，就需要使用线程池操作
             */
            while ((len = inputStream.read(buffer)) != -1 && isRunning) {
                String message = String.format("收到:[%s] 来自:[%s:%d]", new String(buffer, 0, len),
                        socket.getInetAddress().getHostAddress(), socket.getPort());
                // 更新 UI 显示
                updateShowMessage(message);

                // 获取 Socket 的 OutputStream 对象，用于当前 Socket 链接发送数据
                OutputStream outputStream = socket.getOutputStream();
                // 将数据写入到 OutputStream，发送到服务器
                String replyMsg = "Hello Client!";
                outputStream.write(replyMsg.getBytes());
                outputStream.flush();
                // 更新 UI 显示
                updateShowMessage(String.format("回复:[%s]", replyMsg));
            }
        } catch (IOException e) {
            e.printStackTrace();
            updateShowMessage(String.format("Error: 服务器出现错误 %s", e.getMessage()));
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
        serverBtn.setText("启动 TCP 服务");
        updateShowMessage(String.format("TCP 服务器已停止"));
        if (serverSocket != null) {
            try {
                serverSocket.close();
                serverSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
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
