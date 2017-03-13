package com.vmloft.develop.library.simple.socket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.vmloft.develop.library.simple.R;
import com.vmloft.develop.library.tools.VMBaseActivity;

/**
 * 测试 TCP 和 UDP Socket 连接收发数据
 * Created by lzan13 on 2017/3/12.
 */
public class VMSocketActvity extends VMBaseActivity {

    private VMBaseActivity activity;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);

        activity = this;

        findViewById(R.id.btn_socket_server_tcp).setOnClickListener(viewListener);
        findViewById(R.id.btn_socket_server_udp).setOnClickListener(viewListener);
        findViewById(R.id.btn_socket_tcp).setOnClickListener(viewListener);
        findViewById(R.id.btn_socket_udp).setOnClickListener(viewListener);
    }

    /**
     * 按钮点击监听
     */
    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_socket_server_tcp:
                    startActivity(new Intent(activity, VMSocketServer.class).putExtra("socket_type",
                            "tcp"));
                    break;
                case R.id.btn_socket_server_udp:
                    startActivity(new Intent(activity, VMSocketServer.class).putExtra("socket_type",
                            "udp"));
                    break;
                case R.id.btn_socket_tcp:
                    startActivity(new Intent(activity, VMTCPSocketClient.class));
                    break;
                case R.id.btn_socket_udp:
                    startActivity(new Intent(activity, VMUDPSocketClient.class));
                    break;
            }
        }
    };
}
