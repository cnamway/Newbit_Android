package com.spark.newbitrade.serivce.chatUtils;


/**
 * 发送消息的监听
 * Created by daiyy on 20190121.
 */

public interface SendMsgListener {

    void onMessageResponse(SocketResponse socketResponse);

    void error();
}
