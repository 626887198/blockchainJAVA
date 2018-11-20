package com.baoding.notebook.pojo;


import com.baoding.notebook.utils.HashUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NoteBook {

    private List<Block> list = new ArrayList<>();

    //构造函数读取数据
    //单例模式
    private NoteBook() {
        File file = new File("a.json");

        //确保文件存在并且有数据
        if (file.exists() && file.length() > 0) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, Block.class);
               list= objectMapper.readValue(file, javaType);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static volatile NoteBook instance;

    public static NoteBook getInstance() {
        if (instance==null){
            synchronized (NoteBook.class){
                if (instance==null) {
                    instance = new NoteBook();
                }
            }
        }
        return instance;
    }

    // 封面
    // 给账本起个名字
    // 前提 : 这个账本必须是新的
    // 相当于比特币中的创世区块
    public void addGenesis(String genesis) {
        if (list.size() > 0) {
            throw new RuntimeException("添加封面必须是一个新的账本");
        }
        //创世区块没有前一个区块
        String preHash = "0000000000000000000000000000000000000000000000000000000000000000";
        // 前面以四个0开头的hash值
        int nonce = mine(genesis, preHash);

        // 添加封面
        list.add(new Block(
                list.size() + 1,
                genesis,//内容
                HashUtils.sha256(genesis + preHash+nonce), //新区块的hash
                nonce, //工作量证明
                preHash  //前一个区块的hash
        ));

        // 保存数据
        saveDisk();
    }

    // 笔记
    // 添加一条条的交易记录
    // 前提 : 账本至少已经起了一个名字
    // 相当于比特币中的区块
    public void addNote(String note) {
        if (list.size() == 0) {
            throw new RuntimeException("账本必须要有一个封面数据");
        }
        //获取前一个区块数据
        Block preblock=list.get(list.size()-1);
       //获取前一个区块的哈希值
        String prehash=preblock.hash;
        int nonce=mine(note,prehash);
        // 添加交易记录
        list.add(new Block(
                list.size()+1,
                note,
                HashUtils.sha256(note+prehash+nonce),
                nonce,
                prehash
        ));
        // 保存数据
        saveDisk();
    }


    // 挖矿
    // 挖矿的过程就是在进行数学运算
    // 求取一个以特定规则开头的hash值
    // 得到的i值就是工作量证明
    // 求i值的过程就叫做挖矿
    private int mine(String genesis, String preHash) {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            String hash = HashUtils.sha256(genesis + preHash + i);
            if (hash.startsWith("0000")) {
                System.out.println("挖矿成功：" + i);
                return i;
            } else {
                System.out.println("第" + i + "次尝试挖矿");
            }
        }
        throw new RuntimeException("挖矿失败");
    }

    //保存数据
    public void saveDisk() {
        //使用spring框架带的Jackson
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File("a.json"), list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 查看数据
    public List<Block> showlist(){
        return list;
    }

    //校验数据
    public String verify() {
       StringBuilder sb=new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            Block block =list.get(i);
            String hash =block.hash;
            String prehash=block.preHash;
            int nonce =block.nonce;
            String content =block.content;
            int id =block.id;
            // 验证hash
            String chash=HashUtils.sha256(content+prehash+nonce);
            if (!chash.equals(hash)){
                sb.append("ID为" + id + "的区块数据中hash有问题,请注意检查<br/>");
            }
            // 验证prehash, 创世区块之后的区块
            if (i>0){
                //获取上一个区块
                Block preblock =list.get(i-1);
             String  preblockhash =preblock.hash;
             if (prehash!=preblockhash){
                 sb.append("ID为" + id + "的区块数据中prehash有问题,请注意检查<br/>");
             }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        NoteBook noteBook=new NoteBook();
//        noteBook.addGenesis("沙币");
//        noteBook.addNote("张三给李四转账10块");
        System.out.println(noteBook.verify());

    }


    // 要去做数据的比对,然后判断是否接受对方传递过来的区块链数据
    // 1.比对长度
    // 2.数据校验
    public void checkNewList(List<Block> newList) {
        if (list.size()<newList.size()){
            list=newList;
        }else {
            return;
        }
    }
}
