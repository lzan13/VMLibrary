package com.vmloft.develop.library.simple.socket;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.vmloft.develop.library.simple.R;
import com.vmloft.develop.library.tools.VMBaseActivity;
import com.vmloft.develop.library.tools.utils.VMLog;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;

/**
 * TCP Socket Client，实现 TCP Socket 数据的收发
 * Created by lzan13 on 2017/3/12.
 */
public class VMTCPSocketClient extends VMBaseActivity {

    private VMBaseActivity activity;

    // TCP Socket 对象
    private Socket tcpSocket = null;
    // 服务器地址
    private String address = "";
    // 服务器监听端口号
    private int port = 5121;

    private EditText serverHostView;
    private EditText serverPortView;
    private EditText messageInputView;
    private TextView showMessageView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_tcp);

        activity = this;

        serverHostView = (EditText) findViewById(R.id.edit_server_host);
        serverPortView = (EditText) findViewById(R.id.edit_server_port);
        messageInputView = (EditText) findViewById(R.id.edit_message_input);
        showMessageView = (TextView) findViewById(R.id.text_show_message);
        showMessageView.setMovementMethod(ScrollingMovementMethod.getInstance());

        findViewById(R.id.btn_connect_server).setOnClickListener(viewListener);
        findViewById(R.id.btn_send_tcp_message).setOnClickListener(viewListener);
    }

    /**
     * 界面控件点击监听
     */
    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_connect_server:
                    new Thread(new Runnable() {
                        @Override public void run() {
                            connectServerWithTCPSocket();
                        }
                    }).start();
                    break;
                case R.id.btn_send_tcp_message:
                    new Thread(new Runnable() {
                        @Override public void run() {
                            String time = new SimpleDateFormat("MM/dd HH:mm:ss").format(
                                    System.currentTimeMillis());
                            String content = messageInputView.getText().toString() + time;
                            sendMessageToServerWithTCP(content);
                        }
                    }).start();
                    break;
            }
        }
    };

    /**
     * 使用 TCP Socket 的方式连接服务器
     */
    public void connectServerWithTCPSocket() {
        address = serverHostView.getText().toString();
        port = Integer.valueOf(serverPortView.getText().toString());

        if (TextUtils.isEmpty(address)) {
            Toast.makeText(activity, "Server host is empty", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            if (tcpSocket != null) {
                Toast.makeText(activity, "已经连接服务器，不需要重连", Toast.LENGTH_LONG).show();
                return;
            }
            // 实例化 Socket 对象，带上服务器地址和端口号
            tcpSocket = new Socket(address, port);
            VMLog.d("VMTCPSocketClient Connect server success：");
            // 使用 InputStream 接收服务器发送过来的数据
            InputStream inputStream = tcpSocket.getInputStream();
            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                String message = new String(buffer, 0, len);
                updateShowMessageView("Receive from server： " + message);
                VMLog.d("VMTCPSocketClient Receive from server：" + message);
            }
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
     * 发送输入的消息到服务器；
     */
    public void sendMessageToServerWithTCP(String content) {
        try {
            // 获取 Socket 的 OutputStream 对象，用于向服务器发送数据
            OutputStream output = tcpSocket.getOutputStream();
            // 将数据写入到 OutputStream，发送到服务器
            output.write(content.getBytes());
            output.flush();
            updateShowMessageView("Send to server：" + content);
            VMLog.d("VMTCPSocketClient Send to server：" + content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 断开与服务器的链接
     */
    public void disconnect() {
        if (tcpSocket != null) {
            try {
                tcpSocket.close();
                tcpSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 拦截返回键，断开链接再关闭界面，其实这个断开连接没什么作用，因为服务器的连接还存在，当下次在进入洁面时，
     * 新的连接和服务器端保持的连接已经不是同一个链接了，因此服务器收收不到新连接发送的消息
     */
    @Override public void onBackPressed() {
        //super.onBackPressed();
        disconnect();
        finish();
    }
}
