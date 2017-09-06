package com.vmloft.develop.library.simple.socket;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.vmloft.develop.library.simple.R;
import com.vmloft.develop.library.tools.VMActivity;
import com.vmloft.develop.library.tools.utils.VMLog;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

/**
 * UDP Socket Client，实现 UDP Socket 数据的收发
 * Created by lzan13 on 2017/3/12.
 */
public class VMUDPSocketClient extends VMActivity {

    private DatagramSocket udpSocket = null;
    // 服务器地址
    private String address = "";
    // 服务器监听端口号
    private int port = 5122;

    private EditText serverHostView;
    private EditText serverPortView;
    private EditText messageInputView;
    private TextView showMessageView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_udp);

        serverHostView = (EditText) findViewById(R.id.edit_server_host);
        serverPortView = (EditText) findViewById(R.id.edit_server_port);
        messageInputView = (EditText) findViewById(R.id.edit_message_input);
        showMessageView = (TextView) findViewById(R.id.text_show_message);
        showMessageView.setMovementMethod(ScrollingMovementMethod.getInstance());

        findViewById(R.id.btn_connect_server).setOnClickListener(viewListener);
        findViewById(R.id.btn_send_udp_message).setOnClickListener(viewListener);
    }

    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_connect_server:
                    new Thread(new Runnable() {
                        @Override public void run() {
                            connectServerWithUDPSocket();
                        }
                    }).start();
                    break;
                case R.id.btn_send_udp_message:
                    new Thread(new Runnable() {
                        @Override public void run() {
                            String time = new SimpleDateFormat("MM/dd HH:mm:ss").format(
                                    System.currentTimeMillis());
                            String content = messageInputView.getText().toString() + time;
                            sendMessageToServerWithUDP(content);
                        }
                    }).start();
                    break;
            }
        }
    };

    /**
     * 使用UDP socket 连接服务器
     */
    public void connectServerWithUDPSocket() {
        address = serverHostView.getText().toString();
        port = Integer.valueOf(serverPortView.getText().toString());

        if (TextUtils.isEmpty(address)) {
            Toast.makeText(activity, "Server host is empty", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            if (udpSocket != null) {
                Toast.makeText(activity, "已经连接服务器，不需要重连", Toast.LENGTH_LONG).show();
                return;
            }
            udpSocket = new DatagramSocket(null);
            // 解决端口已经被占用问题
            udpSocket.setReuseAddress(true);
            udpSocket.setBroadcast(true);
            udpSocket.bind(new InetSocketAddress(port));
            VMLog.d("VMUDPSocketClient Connect server success：");

            byte data[] = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            udpSocket.receive(packet);
            String message = new String(packet.getData(), packet.getOffset(), packet.getLength());

            updateShowMessageView("Receive from server： " + message);
            VMLog.d("VMUDPSocketClient Receive from server：" + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateShowMessageView(final String message) {
        runOnUiThread(new Runnable() {
            @Override public void run() {
                showMessageView.setText(showMessageView.getText().toString() + message + "\n");
            }
        });
    }

    /**
     * UDP 发送数据到服务器
     */
    public void sendMessageToServerWithUDP(String content) {
        try {
            InetAddress inetAddress = InetAddress.getByName(address);
            byte data[] = content.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, inetAddress, port);
            //发送数据到服务端
            udpSocket.send(packet);
            updateShowMessageView("Send to server：" + content);
            VMLog.d("VMUDPSocketClient Send to server：" + content);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 断开与服务器的链接
     */
    public void disconnect() {
        if (udpSocket != null) {
            udpSocket.close();
            udpSocket = null;
        }
    }

    /**
     * 拦截返回键，断开链接再关闭界面
     */
    @Override public void onBackPressed() {
        //super.onBackPressed();
        disconnect();
        finish();
    }
}
