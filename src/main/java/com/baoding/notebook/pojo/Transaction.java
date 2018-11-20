package com.baoding.notebook.pojo;

import com.baoding.notebook.utils.RSAUtils;

import java.security.PrivateKey;
import java.security.PublicKey;

public class Transaction {
    // 付款方的公钥地址
    private String senderAddress;
    // 收款方的公钥地址
    private String receiverAddress;
    // 金额/附加信息
    private String content;
    // 签名(私钥)
    private String signature;

    public Transaction() {
    }

    public Transaction(String senderAddress, String receiverAddress, String content, String signature) {
        this.senderAddress = senderAddress;
        this.receiverAddress = receiverAddress;
        this.content = content;
        this.signature = signature;
    }

    public String getSenderAddress() {

        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
    @Override
    public String toString() {
        return "Transaction{" +
                "senderAddress='" + senderAddress + '\'' +
                ", receiverAddress='" + receiverAddress + '\'' +
                ", content='" + content + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }


    // 验证交易是否合法
    public boolean verify() {
        //获取公钥
        PublicKey publicKey = RSAUtils.getPublicKeyFromString("RSA",senderAddress);
        //使用公钥和内容验证签名是否正确
       return RSAUtils.verifyDataJS("SHA256withRSA",publicKey,content,signature);
    }
}
