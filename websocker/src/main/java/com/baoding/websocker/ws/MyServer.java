package com.baoding.websocker.ws;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class MyServer extends WebSocketServer {
    private int port;

    public MyServer(int port) {
        super(new InetSocketAddress(port));
        this.port = port;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("服务端___" + port + "___打开了一个连接");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("服务端___" + port + "___关闭了一个连接");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("服务端___" + port + "___收到了消息:"+message);
        //如果有别的节点向我发送消息



    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println("服务端___" + port + "___发生了错误"+ex.getMessage());
    }

    @Override
    public void onStart() {
        System.out.println("服务端___" + port + "___启动成功");
    }
}
