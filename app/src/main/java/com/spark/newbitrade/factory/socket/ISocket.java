package com.spark.newbitrade.factory.socket;

/**
 * Created by Administrator on 2018/4/12.
 */

public interface ISocket extends Runnable {
    // TODO: 2018/4/18 0018  30s一次心跳发送   11004
    int MARKET = 1;
    int C2C = 2;
    int GETCHAT = 3;
    int GROUP = 3;
    int HEART = 4;

    void sendRequest(CMD cmd, byte[] body, TCPCallback callback);

    interface TCPCallback {
        void dataSuccess(CMD cmd, String response);

        void dataFail(int code, CMD cmd, String errorInfo);

    }


    enum CMD {
        HEART_BEAT((short) 11004),
        SUBSCRIBE_EXCHANGE_TRADE((short) 21000),
        UNSUBSCRIBE_EXCHANGE_TRADE((short) 21001),
        PUSH_TRADE((short) 22102),
        PUSH_EXCHANGE_PLATE((short) 21100),
        PUSH_CHAT((short) 20033),
        SEND_CHAT((short) 20034),
        PUSH_EXCHANGE_ORDER_COMPLETED((short) 20201),//完成订单回报
        PUSH_EXCHANGE_ORDER_CANCELED((short) 20202),//撤单回报
        PUSH_EXCHANGE_ORDER_TRADE((short) 20200),//成交回报
        ACCEPT_ORDER((short)20204),//下单成功回报
        SUBSCRIBE_GROUP_CHAT((short) 20035), UNSUBSCRIBE_GROUP_CHAT((short) 20036),
        PUSH_GROUP_CHAT((short) 20039), PUSH_EXCHANGE_DEPTH((short) 21101), ENTER_ORDER((short) 20000), CANCEL_ORDER((short) 20001), CURRENT_ORDER((short) 20101),
        HISTORY_ORDER((short) 20100), ORDER_DETAIL((short) 20102), PUSH_REQUEST((short) 11003), PUSH_THUMB((short) 22101), PUSH_KLINE((short) 22100),
        SUBSCRIBE_KLINE((short) 22001), UN_SUBSCRIBE_KLINE((short) 22002), GET_OVERVIEW_THUMB((short) 22005), GET_ALL_THUMB((short) 22003), ENABLE_SYMBOL((short) 20009),
        SYMBOL_INFO((short) 20010), JSONLOGIN((short) 11000), GET_TRADE_PLATE((short) 21002),
        GET_KLINE_HISTORY((short) 22006), ADD_FAVOR((short) 20002), DELETE_FAVOR((short) 20003), USD_CNY_RATE((short) 22008),
        FIND_FAVOR((short) 20103), LATEST_TRADE((short) 22007), SUBSCRIBE_THUMB((short) 22011), UN_SUBSCRIBE_THUMB((short) 22012),
        UN_Definition_Code((short) 99999);
        private short code;

        CMD(short code) {
            this.code = code;
        }

        public short getCode() {
            return code;
        }

        public static CMD findObjByCode(short code) {
            for (CMD cmd : CMD.values()) {
                if (cmd.getCode() == code) return cmd;
            }
            return UN_Definition_Code;
        }

    }

}
