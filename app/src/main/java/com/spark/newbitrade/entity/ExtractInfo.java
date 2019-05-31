package com.spark.newbitrade.entity;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/8.
 */

public class ExtractInfo {
    private String coinName ;
    private String coinType ;
    private BigDecimal dailyMaxWithdrawAmount ;
    private BigDecimal depositFee ;
    private Integer depositFeeType ;
    private Integer enableAutoWithdraw ;
    private Integer enableGenerate ;
    private Integer enableWithdraw ;
    private String iconUrl ;
    private Integer isPlatformCoin ;
    @SerializedName("mainCoinType")
    private String mainCoinType ;
    @SerializedName("maxDepositFee")
    private BigDecimal maxDepositFee ;
    @SerializedName("maxWithdrawAmount")
    private BigDecimal maxWithdrawAmount ;
    @SerializedName("maxWithdrawFee")
    private BigDecimal maxWithdrawFee ;
    @SerializedName("minDepositAmount")
    private BigDecimal minDepositAmount ;
    @SerializedName("minDepositFee")
    private BigDecimal minDepositFee ;
    @SerializedName("minWithdrawAmount")
    private BigDecimal minWithdrawAmount ;
    @SerializedName("minWithdrawFee")
    private BigDecimal minWithdrawFee ;
    @SerializedName("nameCn")
    private String nameCn ;
    @SerializedName("rpcType")
    private Integer rpcType ;
    @SerializedName("scale")
    private Integer scale ;
    @SerializedName("sort")
    private Integer sort ;
    @SerializedName("status")
    private Integer status ;
    @SerializedName("unit")
    private String unit ;
    @SerializedName("withdrawFee")
    private BigDecimal withdrawFee ;
    @SerializedName("withdrawFeeType")
    private Integer withdrawFeeType ;
    @SerializedName("withdrawThreshold")
    private BigDecimal withdrawThreshold ;

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public BigDecimal getDailyMaxWithdrawAmount() {
        return dailyMaxWithdrawAmount;
    }

    public void setDailyMaxWithdrawAmount(BigDecimal dailyMaxWithdrawAmount) {
        this.dailyMaxWithdrawAmount = dailyMaxWithdrawAmount;
    }

    public BigDecimal getDepositFee() {
        return depositFee;
    }

    public void setDepositFee(BigDecimal depositFee) {
        this.depositFee = depositFee;
    }

    public Integer getDepositFeeType() {
        return depositFeeType;
    }

    public void setDepositFeeType(Integer depositFeeType) {
        this.depositFeeType = depositFeeType;
    }

    public Integer getEnableAutoWithdraw() {
        return enableAutoWithdraw;
    }

    public void setEnableAutoWithdraw(Integer enableAutoWithdraw) {
        this.enableAutoWithdraw = enableAutoWithdraw;
    }

    public Integer getEnableGenerate() {
        return enableGenerate;
    }

    public void setEnableGenerate(Integer enableGenerate) {
        this.enableGenerate = enableGenerate;
    }

    public Integer getEnableWithdraw() {
        return enableWithdraw;
    }

    public void setEnableWithdraw(Integer enableWithdraw) {
        this.enableWithdraw = enableWithdraw;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Integer getIsPlatformCoin() {
        return isPlatformCoin;
    }

    public void setIsPlatformCoin(Integer isPlatformCoin) {
        this.isPlatformCoin = isPlatformCoin;
    }

    public String getMainCoinType() {
        return mainCoinType;
    }

    public void setMainCoinType(String mainCoinType) {
        this.mainCoinType = mainCoinType;
    }

    public BigDecimal getMaxDepositFee() {
        return maxDepositFee;
    }

    public void setMaxDepositFee(BigDecimal maxDepositFee) {
        this.maxDepositFee = maxDepositFee;
    }

    public BigDecimal getMaxWithdrawAmount() {
        return maxWithdrawAmount;
    }

    public void setMaxWithdrawAmount(BigDecimal maxWithdrawAmount) {
        this.maxWithdrawAmount = maxWithdrawAmount;
    }

    public BigDecimal getMaxWithdrawFee() {
        return maxWithdrawFee;
    }

    public void setMaxWithdrawFee(BigDecimal maxWithdrawFee) {
        this.maxWithdrawFee = maxWithdrawFee;
    }

    public BigDecimal getMinDepositAmount() {
        return minDepositAmount;
    }

    public void setMinDepositAmount(BigDecimal minDepositAmount) {
        this.minDepositAmount = minDepositAmount;
    }

    public BigDecimal getMinDepositFee() {
        return minDepositFee;
    }

    public void setMinDepositFee(BigDecimal minDepositFee) {
        this.minDepositFee = minDepositFee;
    }

    public BigDecimal getMinWithdrawAmount() {
        return minWithdrawAmount;
    }

    public void setMinWithdrawAmount(BigDecimal minWithdrawAmount) {
        this.minWithdrawAmount = minWithdrawAmount;
    }

    public BigDecimal getMinWithdrawFee() {
        return minWithdrawFee;
    }

    public void setMinWithdrawFee(BigDecimal minWithdrawFee) {
        this.minWithdrawFee = minWithdrawFee;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public Integer getRpcType() {
        return rpcType;
    }

    public void setRpcType(Integer rpcType) {
        this.rpcType = rpcType;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getWithdrawFee() {
        return withdrawFee;
    }

    public void setWithdrawFee(BigDecimal withdrawFee) {
        this.withdrawFee = withdrawFee;
    }

    public Integer getWithdrawFeeType() {
        return withdrawFeeType;
    }

    public void setWithdrawFeeType(Integer withdrawFeeType) {
        this.withdrawFeeType = withdrawFeeType;
    }

    public BigDecimal getWithdrawThreshold() {
        return withdrawThreshold;
    }

    public void setWithdrawThreshold(BigDecimal withdrawThreshold) {
        this.withdrawThreshold = withdrawThreshold;
    }
}
