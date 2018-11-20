package com.baoding.websocker.ws;

import java.net.URI;
import java.net.URISyntaxException;

public class Test {


    // 我怎么知道网络上的其他节点呢 ?
    // 注册节点,
    // 广播数据

    public static void main(String[] args) throws Exception {
        //先开启服务
        MyServer myServer=new MyServer(8000);
        myServer.start();

        URI uri=new URI("ws://localhost:8000");
        MyClient myClient1=new MyClient(uri,"客户端1");
        MyClient myClient2=new MyClient(uri,"客户端2");
        //客户端发出连接请求
        myClient1.connect();
        myClient2.connect();

        Thread.sleep(3000);

        myClient1.send("吔屎啦梁非凡！");
        myServer.broadcast("这里是非凡");
    }
}
