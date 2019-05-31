package com.spark.newbitrade.event;

/**
 * 检测登录状态
 */

public class CheckLoginEvent {

    public String type;

    public CheckLoginEvent(String t) {
        type = t;
    }
}
