package com.spark.newbitrade.entity;

/**
 * Created by Administrator on 2018/2/25.
 */

public class Favorite {
    private long id;
    private String symbol;
    private long memberId;
    private String addTime;

    public long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public String getAddTime() {
        return addTime;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }


}
