package com.baoding.notebook.pojo;

import com.baoding.notebook.utils.RSAUtils;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import jdk.nashorn.internal.ir.IfNode;
import jdk.nashorn.internal.ir.VarNode;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private String name;

    public Wallet(String name) {
        this.name = name;
        File privateKeyFile =new File(name+".pri");
        File publicKeyFile =new File(name+".pub");
        // 如果文件不存在,就重新生成公钥和私钥
        if (!privateKeyFile.exists()||!publicKeyFile.exists()||privateKeyFile.length()==0||publicKeyFile.length()==0){
            RSAUtils.generateKeysJS("RSA",name+".pri",name+".pub");
        }
        // 如果文件存在,就读取数据
        privateKey = RSAUtils.getPrivateKeyFromFile("RSA", name + ".pri");

        publicKey = RSAUtils.getPublicKeyFromFile("RSA", name + ".pub");
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // 付款方的公钥

    // 收款方的公钥
    // 金额/附加信息

    // 签名(私钥)
    public Transaction sendMoney(String receiverAddress, String content) {
       //发送方的公钥
       String senderAddress = Base64.encode(publicKey.getEncoded());
       //发送方使用密钥生成的签名
        String signature =RSAUtils.getSignature("SHA256withRSA",privateKey,content);
        return new  Transaction(senderAddress,receiverAddress,content,signature);
    }

    public static void main(String[] args) {
        Wallet a=new Wallet("a");
//        Wallet b=new Wallet("b");
//        PublicKey publicKey = b.getPublicKey();
//        byte[] encoded = publicKey.getEncoded();
//        String s = Base64.encode(encoded);
//        Transaction bb = a.sendMoney(s, "a给b转账100");
//        System.out.println(bb.verify());

    }
}
