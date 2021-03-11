package com.jlpay.kotlindemo.bean;

public class BResponse {

    private String msg;
    private String code;

    public BResponse() {
    }

    public BResponse(String msg, String code) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
