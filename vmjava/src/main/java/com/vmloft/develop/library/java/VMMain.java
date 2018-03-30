package com.vmloft.develop.library.java;

import com.vmloft.develop.library.java.socket.VMTCPSocketServer;
import com.vmloft.develop.library.java.socket.VMUDPSocketServer;

public class VMMain {

    private static VMTCPSocketServer tcpSocketServer = null;
    private static VMUDPSocketServer udpSocketServer = null;

    public static void main(String[] args) {
        System.out.println("this is java main method!");

        tcpSocketServer = new VMTCPSocketServer();
        udpSocketServer = new VMUDPSocketServer();

        new Thread(new Runnable() {
            @Override public void run() {
                // 启动 TCP Socket Server
                tcpSocketServer.startTCPSocketServer();
            }
        }).start();

//        new Thread(new Runnable() {
//            @Override public void run() {
//                // 启动 UDP Socket Server
//                udpSocketServer.startUDPSocketServer();
//            }
//        }).start();
    }
}
