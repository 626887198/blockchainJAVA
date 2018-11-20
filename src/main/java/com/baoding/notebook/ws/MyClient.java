package com.baoding.notebook.ws;

import com.baoding.notebook.pojo.Block;
import com.baoding.notebook.pojo.MessageBean;
import com.baoding.notebook.pojo.NoteBook;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.util.List;

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
        System.out.println("客户端___" + name + "___收到了消息:" + message);
        //收到别的节点服务器广播过来的数据
        NoteBook noteBook = NoteBook.getInstance();//本地数据
        if (!StringUtils.isEmpty(message)) {  //如果广播过来的数据不为空
            try {
                // 把收到的消息还原成对象
                ObjectMapper objectMapper = new ObjectMapper();

                MessageBean bean = objectMapper.readValue(message, MessageBean.class);


                if (bean.getCode()==1){   //如果类型是1 说明是区块数据
                    JavaType javaType=objectMapper.getTypeFactory().constructParametricType(List.class
                    ,Block.class);
                    List<Block> newlist = objectMapper.readValue(bean.getMsg(), javaType);
                    noteBook.checkNewList(newlist);
                } else if (bean.getCode()==4){  //如果类型是4 说明是转账数据
                    String msg = bean.getMsg();
                    noteBook.addNote(msg);

                }



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("客户端___" + name + "___关闭了一个连接");
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("客户端___" + name + "___发生了错误:" + ex.getMessage());
    }
}
