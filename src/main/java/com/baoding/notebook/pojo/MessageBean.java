package com.baoding.notebook.pojo;

public class MessageBean {
    //信息类型
    private int code;
    //信息
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MessageBean() {
    }

    public MessageBean(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
