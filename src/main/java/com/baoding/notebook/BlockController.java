package com.baoding.notebook;

import com.baoding.notebook.pojo.Block;
import com.baoding.notebook.pojo.MessageBean;
import com.baoding.notebook.pojo.NoteBook;
import com.baoding.notebook.pojo.Transaction;
import com.baoding.notebook.ws.MyClient;
import com.baoding.notebook.ws.MyServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
@RestController
public class BlockController {
      NoteBook noteBook=NoteBook.getInstance();
      @PostMapping("addGenesis")
      public String addGenesis(String genesis){
          try {
              noteBook.addGenesis(genesis);
              return "添加成功";
          } catch (Exception e) {
              e.printStackTrace();
              return "添加失败"+e.getMessage();
          }
      }

    @PostMapping("addNote2")
    public String addNote2(String note){
        try {
            noteBook.addNote(note);
            return "添加成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "添加失败"+e.getMessage();
        }

    }
    @PostMapping("addNote")
    public String addNote(Transaction transaction){
//        System.out.println("errrrrrrrrrrrrrrrrrrrrrr");
        try {
            System.out.println(transaction.verify());
            if (!transaction.verify()){
                return "数据校验不成功";
            }
            noteBook.addNote(transaction.getContent());

            //并且广播给其它节点
            MessageBean bean=new MessageBean(4,transaction.getContent());
            ObjectMapper objectMapper=new ObjectMapper();
            String msg = objectMapper.writeValueAsString(bean);
            server.broadcast(msg);

            return "添加成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "添加失败"+e.getMessage();
        }
    }
    @GetMapping("showlist")
    public List<Block> showlist(){
       return noteBook.showlist();
    }

    @GetMapping("verify")
    public String  verify(){
        String verify = noteBook.verify();
        if (StringUtils.isEmpty(verify)){
            return "数据没有问题" ;
        }
        return verify;
    }


    //装所有节点的集合，这里只需要端口号就可以
    private HashSet<String> set = new HashSet<>();

    // 注册节点,就是将所有线上的其它server添加到集合
    @RequestMapping("/regist")
    public String regist(String port) {
        set.add(port);
        return "注册成功";
    }

    // 存放所有连接websocket服务端的客户端
    private List<MyClient> clients = new ArrayList<>();
    // 连接
    @RequestMapping("/conn")
    public String conn() throws Exception {
        for (String port : set) {
            URI uri=new URI("ws://localhost:"+port);
            MyClient myClient = new MyClient(uri, port);
            myClient.connect();
            clients.add(myClient);
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
        server = new MyServer(Integer.parseInt(NotebookApplication.port) + 1);
        server.start();
    }

    // 广播数据
    @RequestMapping("/broadcast")
    public String broadcast(String msg) {
        server.broadcast(msg);
        return "广播成功";
    }

    // 同步数据
    // 请求同步数据,
    // 模拟场景 : 全新的一个节点上线了,但是没有区块链的数据,发送一个请求,要求获取其他节点上存储的区块链数据
    @RequestMapping("/syncData")
    public String syncData() {
        for (MyClient client : clients) {
                  client.send("胸弟，留图不留种，那啥万人捅啊！");
        }
        return "同步成功";
    }
}
