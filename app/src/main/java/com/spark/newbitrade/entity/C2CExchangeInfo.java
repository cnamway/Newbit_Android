package com.spark.newbitrade.entity;

/**
 * Created by Administrator on 2018/3/7.
 */

public class C2CExchangeInfo {
    private String username;
    private int emailVerified;
    private int phoneVerified;
    private int idCardVerified;
    private int transactions;
    private int otcCoinId;
    private String unit;
    private String price;
    private String number;
    private String payMode;
    private String minLimit;
    private String maxLimit;
    private double timeLimit;
    private String remainAmount;
    private String localCurrency;
    private Double maxTradableAmount;
    //完成率
    private String percentageComplete;
    //30天成单量
    private Integer singleCount;
    //平均放行时间
    private String dischargedTime;

    public Double getMaxTradableAmount() {
        return maxTradableAmount;
    }

    private String country;
    private String remark;
    private String advertiseType;

    public String getAdvertiseType() {
        return advertiseType;
    }

    public String getUsername() {
        return username;
    }

    public int getTransactions() {
        return transactions;
    }

    public int getOtcCoinId() {
        return otcCoinId;
    }

    public String getUnit() {
        return unit;
    }

    public String getPrice() {
        return price;
    }

    public String getPayMode() {
        return payMode;
    }

    public String getMinLimit() {
        return minLimit;
    }

    public String getMaxLimit() {
        return maxLimit;
    }

    public String getLocalCurrency() {
        return localCurrency;
    }

    public String getRemark() {
        return remark;
    }

    public String getPercentageComplete() {
        return percentageComplete;
    }

    public Integer getSingleCount() {
        return singleCount;
    }

    public String getDischargedTime() {
        return dischargedTime;
    }
}
