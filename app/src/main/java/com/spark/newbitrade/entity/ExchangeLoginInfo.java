package com.spark.newbitrade.entity;

/**
 * Created by Administrator on 2018/9/11 0011.
 */

public class ExchangeLoginInfo {
    private int type; // 登录方式：1-普通用户名密码登录；2-授权码登录；3-openid登录
    private String value;

    public void setType(int type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
