package com.spark.newbitrade.serivce.chatUtils;

import com.spark.newbitrade.factory.socket.ISocket;

/**
 * author: wuzongjie
 * time  : 2018/5/4 0004 09:35
 * desc  :
 */

public class SocketResponse {
    private short cmd; // 传的指令
    private String response; // 返回的参数
    private int code;

    public SocketResponse(short cmd, String response) {
        this.cmd = cmd;
        this.response = response;
    }

    public SocketResponse(short cmd, String response, int code) {
        this.cmd = cmd;
        this.response = response;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public short getCmd() {
        return cmd;
    }

    public void setCmd(short cmd) {
        this.cmd = cmd;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }


    @Override
    public String toString() {
        return "SocketResponse{" +
                "cmd=" + cmd +
                ", response='" + response + '\'' +
                ", code=" + code +
                '}';
    }
}
