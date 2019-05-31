package com.spark.newbitrade.entity;

/**
 * Created by Administrator on 2018/10/31 0031.
 */

public class HomeNewTenBean {

    /**
     * id : 144
     * memberId : 215
     * username : 测试Q
     * advertiseType : 0
     * country : China
     * coinName : BTC
     * level : 0
     * payMode : alipay,wechat,card
     * status : 0
     * number : 100.0
     * dealAmount : 0.0
     * remainAmount : 100.0
     * maxLimit : 2222.0
     * minLimit : 222.0
     * premiseRate : null
     * price : 3000.0
     * priceType : 0
     * autoReply : 0
     * autoword :
     * createTime : 1540952017000
     * timeLimit : 30
     * updateTime : 1540952024000
     * remark :
     * countryZhName : null
     * payModes : null
     * finalPrice : null
     */

    private int id;
    private int memberId;
    private String username;
    private int advertiseType;
    private String country;
    private String coinName;
    private int level;
    private String payMode;
    private int status;
    private double number;
    private double dealAmount;
    private double remainAmount;
    private double maxLimit;
    private double minLimit;
    private Object premiseRate;
    private double price;
    private int priceType;
    private int autoReply;
    private String autoword;
    private long createTime;
    private int timeLimit;
    private long updateTime;
    private String remark;
    private Object countryZhName;
    private Object payModes;
    private Object finalPrice;
    //完成率
    private String percentageComplete;
    //30天成单量
    private Integer singleCount;
    private String avatar;

    public String getPercentageComplete() {
        return percentageComplete;
    }

    public Integer getSingleCount() {
        return singleCount;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAdvertiseType() {
        return advertiseType;
    }

    public void setAdvertiseType(int advertiseType) {
        this.advertiseType = advertiseType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    public double getDealAmount() {
        return dealAmount;
    }

    public void setDealAmount(double dealAmount) {
        this.dealAmount = dealAmount;
    }

    public double getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(double remainAmount) {
        this.remainAmount = remainAmount;
    }

    public double getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(double maxLimit) {
        this.maxLimit = maxLimit;
    }

    public double getMinLimit() {
        return minLimit;
    }

    public void setMinLimit(double minLimit) {
        this.minLimit = minLimit;
    }

    public Object getPremiseRate() {
        return premiseRate;
    }

    public void setPremiseRate(Object premiseRate) {
        this.premiseRate = premiseRate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPriceType() {
        return priceType;
    }

    public void setPriceType(int priceType) {
        this.priceType = priceType;
    }

    public int getAutoReply() {
        return autoReply;
    }

    public void setAutoReply(int autoReply) {
        this.autoReply = autoReply;
    }

    public String getAutoword() {
        return autoword;
    }

    public void setAutoword(String autoword) {
        this.autoword = autoword;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Object getCountryZhName() {
        return countryZhName;
    }

    public void setCountryZhName(Object countryZhName) {
        this.countryZhName = countryZhName;
    }

    public Object getPayModes() {
        return payModes;
    }

    public void setPayModes(Object payModes) {
        this.payModes = payModes;
    }

    public Object getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Object finalPrice) {
        this.finalPrice = finalPrice;
    }
}
