package com.baoding.websocker.ws;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import javax.naming.Name;
import java.net.URI;

public class MyClient extends WebSocketClient {
    private String name;

    public MyClient(URI serverUri, String name) {
        super(serverUri);
        this.name = name;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("客户端___" + name + "___打开了一个连接");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("客户端___" + name + "___收到了消息:"+message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("客户端___" + name + "___关闭了一个连接");
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("客户端___" + name + "___发生了错误:"+ex.getMessage());
    }
}
