package com.spark.newbitrade.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/29.
 */

public class Currency implements Serializable {
    private boolean isCollect; // 是否收藏
    private String symbol; // 币种
    private Double open; // 开盘价
    private Double high; // 最高价
    private Double low; // 最低价
    private Double close; // 现价
    private Double chg; // 涨幅
    private Double change; // 改变量24h
    private BigDecimal volume; // 交易量
    private Double turnover; // 交易额
    private Double lastDayClose; // 昨日收盘价
    private Double usdRate;
    private String baseCoin; //1 个BTC 等于多少个USDT
    private String otherCoin;
    private Double baseUsdRate = 1.0;
    private int baseCoinScale;
    private int coinScale;

    public int getCoinScale() {
        return coinScale;
    }

    public int getBaseCoinScale() {
        return baseCoinScale;
    }

    public Double getBaseUsdRate() {
        return baseUsdRate == 0 ? 1 : baseUsdRate;
    }

    public Double getUsdRate() {
        return usdRate;
    }

    public Currency() {

    }

    public static Currency shallowClone(Currency origin, Currency target) {
        origin.symbol = target.symbol;
        origin.open = target.open;
        origin.high = target.high;
        origin.low = target.low;
        origin.close = target.close;
        origin.chg = target.chg;
        origin.change = target.change;
        origin.volume = target.volume;
        origin.turnover = target.turnover;
        origin.lastDayClose = target.lastDayClose;
        origin.baseUsdRate = target.baseUsdRate;
        return origin;
    }

    public Currency(boolean isCollect) {
        this.isCollect = isCollect;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    public String getSymbol() {
        return symbol;
    }

    public Double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public Double getClose() {
        return close;
    }

    public Double getChg() {
        return chg;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public String getBaseCoin() {
        return baseCoin;
    }

    public String getOtherCoin() {
        return otherCoin;
    }

    public static void buildCurrency(List<Currency> currencies) {
        for (Currency currency : currencies) {
            String[] coins = currency.getSymbol().split("/");
            currency.baseCoin = coins[1];
            currency.otherCoin = coins[0];
        }
    }

    public static List<Currency> baseCurrencies(List<Currency> currencies, String baseCoin) {
        List<Currency> result = new ArrayList<>();
        buildCurrency(currencies);
        for (Currency currency : currencies) {
            if (currency.baseCoin.equals(baseCoin)) result.add(currency);
        }
        return result;
    }

    public double getLastDayClose() {
        return lastDayClose;
    }

}
