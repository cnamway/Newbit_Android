package com.spark.newbitrade.entity;

/**
 * Created by Administrator on 2018/4/26 0026.
 */

public class MarketSymbol {

    /**
     * symbol : BTC/USDT
     * coinSymbol : BTC
     * baseSymbol : USDT
     * enable : 1
     * fee : 0.001
     * sort : 1
     * coinScale : 2
     * baseCoinScale : 2
     * minSellPrice : 0
     * enableMarketSell : 1
     * enableMarketBuy : 1
     * maxTradingTime : 0
     * maxTradingOrder : 0
     * flag : 1
     */

    private String symbol;
    private String coinSymbol;
    private String baseSymbol;
    private double enable;
    private double fee;
    private double sort;
    private int coinScale;
    private int baseCoinScale;
    private double minSellPrice;
    private double enableMarketSell;
    private double enableMarketBuy;
    private double maxTradingTime;
    private double maxTradingOrder;
    private double flag;

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

    public double getEnable() {
        return enable;
    }

    public void setEnable(double enable) {
        this.enable = enable;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getSort() {
        return sort;
    }

    public void setSort(double sort) {
        this.sort = sort;
    }

    public int getCoinScale() {
        return coinScale;
    }


    public int getBaseCoinScale() {
        return baseCoinScale;
    }

    public double getMinSellPrice() {
        return minSellPrice;
    }

    public void setMinSellPrice(double minSellPrice) {
        this.minSellPrice = minSellPrice;
    }

    public double getEnableMarketSell() {
        return enableMarketSell;
    }

    public void setEnableMarketSell(double enableMarketSell) {
        this.enableMarketSell = enableMarketSell;
    }

    public double getEnableMarketBuy() {
        return enableMarketBuy;
    }

    public void setEnableMarketBuy(double enableMarketBuy) {
        this.enableMarketBuy = enableMarketBuy;
    }

    public double getMaxTradingTime() {
        return maxTradingTime;
    }

    public void setMaxTradingTime(double maxTradingTime) {
        this.maxTradingTime = maxTradingTime;
    }

    public double getMaxTradingOrder() {
        return maxTradingOrder;
    }

    public void setMaxTradingOrder(double maxTradingOrder) {
        this.maxTradingOrder = maxTradingOrder;
    }

    public double getFlag() {
        return flag;
    }

    public void setFlag(double flag) {
        this.flag = flag;
    }
}
