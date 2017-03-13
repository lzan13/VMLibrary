package com.vmloft.develop.library.java.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

/**
 * Created by lzan13 on 2017/3/12.
 */

public class VMUDPSocketServer {

    // UDP Socket 对象
    private DatagramSocket socket;
    // 当前发过来 UDP 数据的 InetAddress
    private InetAddress address = null;
    // UDP Socket 相关端口
    private final int port = 5122;

    /**
     * 启动 UDP Socket 服务器
     */
    public void startUDPSocketServer() {
        try {
            // 实例化 DatagramSocket 对象，并指定监听端口
            socket = new DatagramSocket(port);
            System.out.println("VMUDPSocketServer 启动成功，监听端口：" + port);
            while (true) {
                // 创建一个空的字节数组，用来接收其他端发来的消息
                byte data[] = new byte[1024];
                DatagramPacket packet = new DatagramPacket(data, data.length);
                /**
                 * 调用 UDP Socket 的 receive() 接收消息方法，
                 * 此方法类似于 TCP 的 accept()，没有连接时都处于阻塞状态
                 */
                socket.receive(packet);
                // 获取当前链接到服务器的客户端地址
                address = packet.getAddress();
                // 获取消息内容
                String message = new String(packet.getData(), packet.getOffset(), packet.getLength());
                System.out.println("VMUDPSocketServer Receive from client："
                        + message
                        + " "
                        + address.getHostAddress()
                        + ":"
                        + packet.getPort());

                // 收到消息后，给客户端回一个消息
                String time = new SimpleDateFormat("MM/dd HH:mm:ss").format(System.currentTimeMillis());
                sendMessageToClient("Server message " + time);
            }
        } catch (SocketException e) {
            System.out.println("VMUDPSocketServer 出现异常停止！！！" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("VMUDPSocketServer 出现异常停止！！！" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * UDP 发送数据到客户端
     *
     * @param content 发送内容
     */
    public void sendMessageToClient(String content) {
        try {
            byte data[] = content.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            //发送数据到客户端
            socket.send(packet);
            System.out.println("VMUDPSocketServer Send to client：" + content);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
