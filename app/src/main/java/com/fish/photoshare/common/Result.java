package com.fish.photoshare.common;

public class Result <T> {
    private int code;
    private String msg;
    private T data;
    public Result(){}
    public Result(int code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
    public T getData() {
        return data;
    }
    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
