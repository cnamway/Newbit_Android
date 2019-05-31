package com.spark.newbitrade.entity;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MyAdvertiseShowVo implements Serializable {
    private Integer advertiseType;
    private String coinName;
    private String country;
    private Date createTime;
    private Long id;
    private String localCurrency;
    private BigDecimal maxLimit;
    private BigDecimal minLimit;
    private BigDecimal number;
    private String payMode;
    private BigDecimal planFrozenFee;
    private BigDecimal premiseRate;
    private BigDecimal price;
    private Integer priceType;
    private Integer rangeTimeOrder;
    private Integer rangeTimeSuccessOrder;
    private String realName;
    private BigDecimal remainAmount;
    private BigDecimal remainFrozenFee;
    private String remark;
    private Integer timeLimit;
    private String username;
    private Long memberId;

    public MyAdvertiseShowVo(Long memberId, Integer advertiseType, String coinName, String country, Date createTime, Long id, String localCurrency, BigDecimal maxLimit, BigDecimal minLimit, BigDecimal number, String payMode, BigDecimal planFrozenFee, BigDecimal premiseRate, BigDecimal price, Integer priceType, Integer rangeTimeOrder, Integer rangeTimeSuccessOrder, String realName, BigDecimal remainAmount, BigDecimal remainFrozenFee, String remark, Integer timeLimit, String username) {
        this.memberId = memberId;
        this.advertiseType = advertiseType;
        this.coinName = coinName;
        this.country = country;
        this.createTime = createTime;
        this.id = id;
        this.localCurrency = localCurrency;
        this.maxLimit = maxLimit;
        this.minLimit = minLimit;
        this.number = number;
        this.payMode = payMode;
        this.planFrozenFee = planFrozenFee;
        this.premiseRate = premiseRate;
        this.price = price;
        this.priceType = priceType;
        this.rangeTimeOrder = rangeTimeOrder;
        this.rangeTimeSuccessOrder = rangeTimeSuccessOrder;
        this.realName = realName;
        this.remainAmount = remainAmount;
        this.remainFrozenFee = remainFrozenFee;
        this.remark = remark;
        this.timeLimit = timeLimit;
        this.username = username;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Integer getAdvertiseType() {
        return advertiseType;
    }

    public void setAdvertiseType(Integer advertiseType) {
        this.advertiseType = advertiseType;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocalCurrency() {
        return localCurrency;
    }

    public void setLocalCurrency(String localCurrency) {
        this.localCurrency = localCurrency;
    }

    public BigDecimal getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(BigDecimal maxLimit) {
        this.maxLimit = maxLimit;
    }

    public BigDecimal getMinLimit() {
        return minLimit;
    }

    public void setMinLimit(BigDecimal minLimit) {
        this.minLimit = minLimit;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public BigDecimal getPlanFrozenFee() {
        return planFrozenFee;
    }

    public void setPlanFrozenFee(BigDecimal planFrozenFee) {
        this.planFrozenFee = planFrozenFee;
    }

    public BigDecimal getPremiseRate() {
        return premiseRate;
    }

    public void setPremiseRate(BigDecimal premiseRate) {
        this.premiseRate = premiseRate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getPriceType() {
        return priceType;
    }

    public void setPriceType(Integer priceType) {
        this.priceType = priceType;
    }

    public Integer getRangeTimeOrder() {
        return rangeTimeOrder;
    }

    public void setRangeTimeOrder(Integer rangeTimeOrder) {
        this.rangeTimeOrder = rangeTimeOrder;
    }

    public Integer getRangeTimeSuccessOrder() {
        return rangeTimeSuccessOrder;
    }

    public void setRangeTimeSuccessOrder(Integer rangeTimeSuccessOrder) {
        this.rangeTimeSuccessOrder = rangeTimeSuccessOrder;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public BigDecimal getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(BigDecimal remainAmount) {
        this.remainAmount = remainAmount;
    }

    public BigDecimal getRemainFrozenFee() {
        return remainFrozenFee;
    }

    public void setRemainFrozenFee(BigDecimal remainFrozenFee) {
        this.remainFrozenFee = remainFrozenFee;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
