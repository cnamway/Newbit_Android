package com.spark.newbitrade.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/2/28.
 */

public class CoinInfo implements Serializable {
    private String unit;
    private String marketPrice;
    private String usdMarketPrice;
    private String jpyMarketPrice;
    private String hkMarketPrice;
    private String sell_min_amount;
    private String name;
    private String nameCn;
    private String id;
    private String sort;
    private String buy_min_amount;
    private boolean isSelected;

    public String getName() {
        return name;
    }

    public String getUsdMarketPrice() {
        return usdMarketPrice;
    }

    public String getJpyMarketPrice() {
        return jpyMarketPrice;
    }

    public String getHkMarketPrice() {
        return hkMarketPrice;
    }

    public String getUnit() {
        return unit;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public String getId() {
        return id;
    }

}
