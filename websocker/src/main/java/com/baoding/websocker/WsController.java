package com.baoding.websocker;

import com.baoding.websocker.ws.MyClient;
import com.baoding.websocker.ws.MyServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;

@RestController
public class WsController {
    //装所有节点的集合，这里只需要端口号就可以
    private HashSet<String> set = new HashSet<>();

    // 注册节点,就是将所有线上的其它server添加到集合
    @RequestMapping("/regist")
    public String regist(String port) {
        set.add(port);
        return "注册成功";
    }

    // 连接
    @RequestMapping("/conn")
    public String conn() throws Exception {
        for (String port : set) {
            URI uri=new URI("ws://localhost:"+port);
            MyClient myClient = new MyClient(uri, port);
            myClient.connect();
        }
        return "连接成功";
    }

    private MyServer server;

    // Controller创建后立刻调用该方法
    //被创造后相当于节点上线，启动一个server
    //server的端口号
    @PostConstruct
    public void init() {
        // 将springboot启动的端口号+1,作为WebSocket服务端启动时使用的端口号
        server = new MyServer(Integer.parseInt(WebsockerApplication.port) + 1);
        server.start();
    }

    // 广播数据
    @RequestMapping("/broadcast")
    public String broadcast(String msg) {
        server.broadcast(msg);
        return "广播成功";
    }
}
