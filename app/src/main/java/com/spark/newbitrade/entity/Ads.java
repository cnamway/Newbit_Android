package com.spark.newbitrade.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/2/5.
 */

public class Ads implements Serializable {
    private long id;
    private int advertiseType;// 0 BUY 1  SELL
    private double minLimit;
    private double maxLimit;
    private double remainAmount;
    private String createTime;
    private String country; // 国家字段
    private int priceType; // 0  固定 1 变动
    private double price;
    private String remark;
    private int timeLimit;
    private double premiseRate;
    private String payMode;
    private double number;
    private String localCurrency; // 币种单位
    private String coinName;
    private BigDecimal planFrozenFee;
    private BigDecimal remainFrozenFee;

    private int status;// 0 上架 1 下架 2 关闭

//    private String coinUnit;
//    private String coinNameCn;
//    private double marketPrice;
//    private int autoReply;
//    private String autoword;
//    private String zhName; // 国家中文名


    public String getCoinName() {
        return coinName;
    }

    public String getLocalCurrency() {
        return localCurrency;
    }

    public long getId() {
        return id;
    }

    public int getAdvertiseType() {
        return advertiseType;
    }

    public double getMinLimit() {
        return minLimit;
    }

    public double getMaxLimit() {
        return maxLimit;
    }

    public double getRemainAmount() {
        return remainAmount;
    }

    public String getCountry() {
        return country;
    }

    public int getPriceType() {
        return priceType;
    }

    public double getPrice() {
        return price;
    }

    public String getRemark() {
        return remark;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public double getPremiseRate() {
        return premiseRate;
    }

    public String getPayMode() {
        return payMode;
    }

    public double getNumber() {
        return number;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getMarketPrice() {
//        return marketPrice;
        return 1;
    }

    public int getAuto() {
//        return autoReply;
        return 0;
    }

    public String getAutoword() {
//        return autoword;
        return "";
    }


}
