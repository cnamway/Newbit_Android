package com.spark.newbitrade.serivce;

import com.spark.newbitrade.factory.socket.ISocket;

/**
 * author: wuzongjie
 * time  : 2018/5/4 0004 09:35
 * desc  :
 */

public class SocketResponse {
    private ISocket.CMD cmd; // 传的指令
    private String response; // 返回的参数
    private int code;

    public SocketResponse(ISocket.CMD cmd, String response) {
        this.cmd = cmd;
        this.response = response;
    }

    public SocketResponse(ISocket.CMD cmd, String response, int code) {
        this.cmd = cmd;
        this.response = response;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public ISocket.CMD getCmd() {
        return cmd;
    }

    public void setCmd(ISocket.CMD cmd) {
        this.cmd = cmd;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
