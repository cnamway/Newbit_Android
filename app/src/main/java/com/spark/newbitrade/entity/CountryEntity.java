package com.spark.newbitrade.entity;

import java.io.Serializable;

/**
 * 国家
 */

public class CountryEntity implements Serializable {
    private String zhName;
    private String enName;
    private String jpLanguage;
    private String areaCode;
    private String language;
    private String sort;
    private String localCurrency;

    public String getJpLanguage() {
        return jpLanguage;
    }

    public String getZhName() {
        return zhName;
    }

    public void setZhName(String zhName) {
        this.zhName = zhName;
    }

    public String getEnName() {
        return enName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public String getLocalCurrency() {
        return localCurrency;
    }

    public void setLocalCurrency(String localCurrency) {
        this.localCurrency = localCurrency;
    }
}
