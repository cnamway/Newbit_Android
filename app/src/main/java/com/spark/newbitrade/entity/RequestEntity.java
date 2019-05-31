package com.spark.newbitrade.entity;

/**
 * Created by Administrator on 2018/8/21 0021.
 */

public class RequestEntity {
//        "baseSymbol":"USDT",
//                "coinSymbol":"BTC",
//                "memberId":1,
//                "orderId":"1534744442070",
//                "orderQty":1,
//                "price":0,
//                "priceType":0,
//                "side":1,
//                "symbol":"BTC/USDT"
//

    private String symbol;
    private int side;
    private int priceType;
    private double price;
    private long memberId;
    private double orderQty;


    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public void setPriceType(int priceType) {
        this.priceType = priceType;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public void setOrderQty(double orderQty) {
        this.orderQty = orderQty;
    }
}
