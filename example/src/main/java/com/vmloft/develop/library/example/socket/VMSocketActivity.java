package com.vmloft.develop.library.example.socket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.tools.VMActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 测试 TCP 和 UDP Socket 连接收发数据
 * Created by lzan13 on 2017/3/12.
 */
public class VMSocketActivity extends VMActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);

        ButterKnife.bind(activity);
    }

    /**
     * 按钮点击监听
     */
    @OnClick({R.id.btn_tcp_socket_server, R.id.btn_tcp_socket_client, R.id.btn_udp_socket_server,
                     R.id.btn_udp_socket_client})
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_tcp_socket_server:
            onStartActivity(activity, new Intent(activity, VMTCPSocketServer.class));
            break;
        case R.id.btn_tcp_socket_client:
            onStartActivity(activity, new Intent(activity, VMTCPSocketClient.class));
            break;
        case R.id.btn_udp_socket_server:
            onStartActivity(activity, new Intent(activity, VMUDPSocketServer.class));
            break;
        case R.id.btn_udp_socket_client:
            onStartActivity(activity, new Intent(activity, VMUDPSocketClient.class));
            break;
        }
    }
}
