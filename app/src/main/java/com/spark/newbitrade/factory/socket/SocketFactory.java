package com.spark.newbitrade.factory.socket;

/**
 * Created by Administrator on 2018/4/12.
 */

public class SocketFactory {
    /**
     * 1 行情   2 C2C   3 组聊天 4 心跳包
     */
    public static ISocket produceSocket(int type) {
        switch (type) {
            case ISocket.C2C:
                return C2CSocket.getInstance();
        }
        return null;
    }

}
