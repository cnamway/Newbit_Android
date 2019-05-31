package com.spark.newbitrade.entity;

/**
 * author: wuzongjie
 * time  : 2018/4/24 0024 11:36
 * desc  :
 */

public class SymbolInfo {
    private String symbol;
    private String coinSymbol;
    private String baseSymbol;
    private int enable;
    private double fee;
    private int sort;
    private int coinScale; // 这就是我要的
    private int baseCoinScale; //
    private double minSellPrice;
    private double enableMarketSell;
    private double enableMarketBuy;
    private double maxTradingTime;
    private double maxTradingOrder;
    private int flag;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public String getBaseSymbol() {
        return baseSymbol;
    }

    public void setBaseSymbol(String baseSymbol) {
        this.baseSymbol = baseSymbol;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getCoinScale() {
        return coinScale;
    }

    public void setCoinScale(int coinScale) {
        this.coinScale = coinScale;
    }

    public int getBaseCoinScale() {
        return baseCoinScale;
    }

    public void setBaseCoinScale(int baseCoinScale) {
        this.baseCoinScale = baseCoinScale;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public double getMinSellPrice() {
        return minSellPrice;
    }

    public double getEnableMarketSell() {
        return enableMarketSell;
    }

    public double getEnableMarketBuy() {
        return enableMarketBuy;
    }

    public double getMaxTradingTime() {
        return maxTradingTime;
    }

    public double getMaxTradingOrder() {
        return maxTradingOrder;
    }
}
