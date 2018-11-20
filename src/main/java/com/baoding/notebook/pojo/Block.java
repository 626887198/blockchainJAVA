package com.baoding.notebook.pojo;

public class Block {

    public int id;   //id
    public String content ;  //内容
    public String hash;  //哈希
    public int nonce; //工作量证明
    public String preHash; //前一个区块的哈希

    public Block() {
    }

    public Block(int id, String content, String hash, int nonce, String preHash) {
        this.id = id;
        this.content = content;
        this.hash = hash;
        this.nonce = nonce;
        this.preHash = preHash;
    }
}
