package com.vmloft.develop.library.simple.socket;

import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import com.vmloft.develop.library.simple.R;
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

/**
 * Socket Server，实现 TCP、UDP Socket 数据的接收与调度
 * Created by lzan13 on 2017/3/12.
 */
public class VMSocketServer extends VMActivity {
    // TCP Socket 对象
    private ServerSocket serverTCPSocket = null;
    // UDP Socket 对象
    private DatagramSocket serverUDPSocket = null;

    // 服务器地址，以及监听端口
    private String address = "";
    private final int tcpPort = 5121;
    private final int udpPort = 5122;
    private String type = "";

    private boolean isRunning = false;

    private TextView serverHostView;
    private TextView serverPortView;
    private TextView serverTypeView;
    private TextView serverStateView;
    private TextView showMessageView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_server);

        initHostAddress();

        type = getIntent().getStringExtra("socket_type");

        serverHostView = (TextView) findViewById(R.id.text_server_host);
        serverPortView = (TextView) findViewById(R.id.text_server_port);
        serverTypeView = (TextView) findViewById(R.id.text_server_type);
        serverStateView = (TextView) findViewById(R.id.text_server_state);
        showMessageView = (TextView) findViewById(R.id.text_show_message);
        showMessageView.setMovementMethod(ScrollingMovementMethod.getInstance());

        serverHostView.setText("Host: " + address);
        if (type.equals("TCP")) {
            serverPortView.setText("Port: " + tcpPort);
        } else {
            serverPortView.setText("Port: " + udpPort);
        }
        serverTypeView.setText("Type: " + type);

        findViewById(R.id.btn_start_server).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startServer();
            }
        });
        findViewById(R.id.btn_stop_server).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                stopServer();
            }
        });
    }

    /**
     * 开启服务
     */
    private void startServer() {
        new Thread(new Runnable() {
            @Override public void run() {
                isRunning = true;
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        serverStateView.setText("Server is running: " + isRunning);
                        serverStateView.setTextColor(Color.GREEN);
                        showMessageView.setText("Server is running: " + isRunning + "\n");
                    }
                });
                String type = getIntent().getStringExtra("socket_type");
                if (type.equals("TCP")) {
                    startTCPServer();
                } else {
                    startUDPServer();
                }
            }
        }).start();
    }

    /**
     * 开启 TCP 服务器
     */
    private void startTCPServer() {
        try {
            // 实例化 ServerSocket，服务器开始监听定义的端口
            serverTCPSocket = new ServerSocket(tcpPort);
            VMLog.d("VMSocketServer 启动成功，监听端口：" + tcpPort);
            /**
             * 调用 ServerSocket 的 accept 方法获取新的连接，
             * 这个方法会等待客户端的连接请求，如果没有新消息，会阻塞当前线程
             */
            Socket socket = serverTCPSocket.accept();
            System.out.println("VMSocketServer 有新链接进入："
                    + socket.getInetAddress().getHostAddress()
                    + ":"
                    + socket.getPort());
            // 通过 InputStream 从连接到服务器的 Socket 读取数据
            InputStream inputStream = socket.getInputStream();
            int len = -1;
            byte[] buffer = new byte[1024];
            /**
             * inputStream.read()这个方法也是会阻塞线程的，当此 Socket 没有新的消息时，会一直停在这里，
             * 如果要实现多客户端连接服务器，就需要使用线程池操作
             */
            while ((len = inputStream.read(buffer)) != -1 && isRunning) {
                String message = "VMSocketServer Receive from client："
                        + new String(buffer, 0, len)
                        + " "
                        + socket.getInetAddress().getHostAddress()
                        + ":"
                        + socket.getPort();
                VMLog.d(message);
                // 更新 UI 显示
                updateShowMessage(message);

                // 收到消息后，给客户端回一个消息
                String time =
                        new SimpleDateFormat("MM/dd HH:mm:ss").format(System.currentTimeMillis());
                // 获取 Socket 的 OutputStream 对象，用于当前 Socket 链接发送数据
                OutputStream outputStream = socket.getOutputStream();
                // 将数据写入到 OutputStream，发送到服务器
                outputStream.write(new String("Server message " + time).getBytes());
                outputStream.flush();
                // 更新 UI 显示
                updateShowMessage("Server message " + time);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启 UDP 服务器
     */
    private void startUDPServer() {
        try {
            // 实例化 DatagramSocket 对象，并指定监听端口
            serverUDPSocket = new DatagramSocket(udpPort);
            VMLog.d("VMSocketServer 启动成功，监听端口：" + udpPort);
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
                String message =
                        "VMSocketServer Receive from client："
                                + new String(packet.getData(), packet.getOffset(),
                                packet.getLength())
                                + " "
                                + inetAddress.getHostAddress()
                                + ":"
                                + packet.getPort();
                VMLog.d(message);
                // 更新 UI 显示
                updateShowMessage(message);

                // 收到消息后，给客户端回一个消息
                String time =
                        new SimpleDateFormat("MM/dd HH:mm:ss").format(System.currentTimeMillis());
                data = new String("Server message " + time).getBytes();
                packet = new DatagramPacket(data, data.length, inetAddress, udpPort);
                //发送数据到客户端
                serverUDPSocket.send(packet);
                // 更新 UI 显示
                updateShowMessage("Server message " + time);
            }
        } catch (SocketException e) {
            VMLog.d("VMSocketServer 出现异常停止！！！" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            VMLog.d("VMSocketServer 出现异常停止！！！" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 更新消息显示
     */
    private void updateShowMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override public void run() {
                showMessageView.setText(showMessageView.getText().toString() + message + "\n");
            }
        });
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

    /**
     * 断开连接
     */
    private void stopServer() {
        if (serverTCPSocket != null) {
            try {
                serverTCPSocket.close();
                serverTCPSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (serverUDPSocket != null) {
            serverUDPSocket.close();
            serverUDPSocket = null;
        }

        isRunning = false;
        runOnUiThread(new Runnable() {
            @Override public void run() {
                serverStateView.setText("Server is running: " + isRunning);
                serverStateView.setTextColor(Color.RED);
            }
        });
    }

    /**
     * 拦截 back 按键
     */
    @Override public void onBackPressed() {
        //super.onBackPressed();
        stopServer();
        finish();
    }
}
