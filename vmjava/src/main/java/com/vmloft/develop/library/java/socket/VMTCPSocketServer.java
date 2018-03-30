package com.vmloft.develop.library.java.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;

/**
 * Created by lzan13 on 2017/3/12.
 */

public class VMTCPSocketServer {

    // TCP Socket 对象
    private ServerSocket serverSocket = null;
    // 当前链接到服务器的客户端
    private Socket clientSocket = null;
    // TCP Socket 相关端口
    private final int port = 5121;

    /**
     * 启动 TCP Socket 服务器
     */
    public void startTCPSocketServer() {
        try {
            // 实例化 ServerSocket，服务器开始监听定义的端口
            serverSocket = new ServerSocket(port);
            System.out.println("TCP服务器启动成功，监听端口：" + port);
            /**
             * 调用 ServerSocket 的 accept 方法获取新的连接，
             * 这个方法会等待客户端的连接请求，如果没有新消息，会阻塞当前线程
             */
            clientSocket = serverSocket.accept();
            System.out.println(
                    String.format("新链接: %s:%d", clientSocket.getInetAddress().getHostAddress(),
                            clientSocket.getPort()));
            // 通过 InputStream 从连接到服务器的 Socket 读取数据
            InputStream inputStream = clientSocket.getInputStream();
            int len = -1;
            byte[] buffer = new byte[1024];
            /**
             * inputStream.read()这个方法也是会阻塞线程的，当此 Socket 没有新的消息时，会一直停在这里，
             * 如果要实现多客户端连接服务器，就需要使用线程池操作
             */
            while ((len = inputStream.read(buffer)) != -1) {
                String message = new String(buffer, 0, len);
                System.out.println(String.format("收到消息: %s by %s:%d", message,
                        clientSocket.getInetAddress().getHostAddress(), clientSocket.getPort()));

                // 收到消息后，给客户端回一个消息
                String time = new SimpleDateFormat("MM/dd HH:mm:ss").format(
                        System.currentTimeMillis());
                sendMessageToClient("Server message " + time);
            }
        } catch (IOException e) {
            System.out.println("VMTCPSocketServer 出现异常停止！！！" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 发送消息给客户端
     *
     * @param content 需要发送的消息内容
     */
    public void sendMessageToClient(String content) {
        try {
            // 获取 Socket 的 OutputStream 对象，用于当前 Socket 链接发送数据
            OutputStream outputStream = clientSocket.getOutputStream();
            // 将数据写入到 OutputStream，发送到服务器
            outputStream.write(content.getBytes());
            outputStream.flush();
            System.out.println("VMTCPSocketServer Send to client：" + content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
