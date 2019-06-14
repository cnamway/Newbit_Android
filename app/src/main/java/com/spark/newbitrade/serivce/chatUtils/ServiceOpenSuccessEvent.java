package com.spark.newbitrade.serivce.chatUtils;

/**
 * 打开成功的service通知
 */
public class ServiceOpenSuccessEvent {
    /**type：1WebSocketService 2GuardService**/
    public int type;

    public ServiceOpenSuccessEvent(int t) {
        type = t;
    }

}
