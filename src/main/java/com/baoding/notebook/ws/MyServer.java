package com.baoding.notebook.ws;

import com.baoding.notebook.pojo.Block;
import com.baoding.notebook.pojo.MessageBean;
import com.baoding.notebook.pojo.NoteBook;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.List;

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
        try {
            if ("胸弟，留图不留种，那啥万人捅啊！".equals(message)){
                // 获取本节点的区块链数据 广播出去
                NoteBook noteBook=NoteBook.getInstance();
                List<Block> blocks = noteBook.showlist();
                ObjectMapper objectMapper=new ObjectMapper();
                String block = objectMapper.writeValueAsString(blocks);
                // 包装数据
                MessageBean bean=new MessageBean(1,block);
                String msg = objectMapper.writeValueAsString(bean);

                broadcast(msg);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


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
