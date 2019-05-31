package com.spark.newbitrade.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/3.
 */

public class OrderDetial implements Serializable {

    /**
     * orderId : 54
     * orderSn : 98381542838280192
     * type : 0
     * unit : BTC
     * status : 2
     * price : 300.0
     * money : 200.0
     * amount : 0.66666667
     * commission : 0
     * payInfo : {"aliAddress":"18856231940","weAddress":"18856231940","cardAddress":null,"aliCodeUrl":"http://xinhuo-xindai.oss-cn-hangzhou.aliyuncs.com/2018/08/18/eb6b3353-abab-45e1-82ac-637b4663d0a9.jpg","weCodeUrl":"http://xinhuo-xindai.oss-cn-hangzhou.aliyuncs.com/2018/08/18/eb6b3353-abab-45e1-82ac-637b4663d0a9.jpg","bank":null,"branch":null}
     * createTime : 2018-09-19 11:33:08
     * payTime : 1537328687000
     * timeLimit : 15
     * otherSide : Xp123456
     * myId : 34
     * hisId : 37
     * remark : bb
     * realName : Xp123456
     * localCurrency : CNY
     */

    private String orderId;
    private String orderSn;
    private int type;
    private String unit;
    private int status;
    private double price;
    private double money;
    private double amount;
    private double commission;
    private PayInfoBean payInfo;
    private String createTime;
    private String payTime;
    private long releaseTime;
    private int timeLimit;
    private String otherSide;
    private String myId;
    private String hisId;
    private String remark;
    private String realName;
    private String localCurrency;
    private String actualPayment;
    private String avatar;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public PayInfoBean getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(PayInfoBean payInfo) {
        this.payInfo = payInfo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getOtherSide() {
        return otherSide;
    }

    public void setOtherSide(String otherSide) {
        this.otherSide = otherSide;
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public String getHisId() {
        return hisId;
    }

    public void setHisId(String hisId) {
        this.hisId = hisId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getLocalCurrency() {
        return localCurrency;
    }

    public void setLocalCurrency(String localCurrency) {
        this.localCurrency = localCurrency;
    }

    public String getActualPayment() {
        return actualPayment;
    }

    public String getAvatar() {
        return avatar;
    }

    public long getReleaseTime() {
        return releaseTime;
    }

    public static class PayInfoBean implements Serializable{
        /**
         * aliAddress : 18856231940
         * weAddress : 18856231940
         * cardAddress : null
         * aliCodeUrl : http://xinhuo-xindai.oss-cn-hangzhou.aliyuncs.com/2018/08/18/eb6b3353-abab-45e1-82ac-637b4663d0a9.jpg
         * weCodeUrl : http://xinhuo-xindai.oss-cn-hangzhou.aliyuncs.com/2018/08/18/eb6b3353-abab-45e1-82ac-637b4663d0a9.jpg
         * bank : null
         * branch : null
         */

        private String aliAddress;
        private String weAddress;
        private String cardAddress;
        private String aliCodeUrl;
        private String weCodeUrl;
        private String bank;
        private String branch;

        public String getAliAddress() {
            return aliAddress;
        }

        public void setAliAddress(String aliAddress) {
            this.aliAddress = aliAddress;
        }

        public String getWeAddress() {
            return weAddress;
        }

        public void setWeAddress(String weAddress) {
            this.weAddress = weAddress;
        }

        public String getCardAddress() {
            return cardAddress;
        }

        public void setCardAddress(String cardAddress) {
            this.cardAddress = cardAddress;
        }

        public String getAliCodeUrl() {
            return aliCodeUrl;
        }

        public void setAliCodeUrl(String aliCodeUrl) {
            this.aliCodeUrl = aliCodeUrl;
        }

        public String getWeCodeUrl() {
            return weCodeUrl;
        }

        public void setWeCodeUrl(String weCodeUrl) {
            this.weCodeUrl = weCodeUrl;
        }

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getBranch() {
            return branch;
        }

        public void setBranch(String branch) {
            this.branch = branch;
        }
    }
}
