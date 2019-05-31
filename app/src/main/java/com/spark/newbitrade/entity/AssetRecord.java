package com.spark.newbitrade.entity;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * 提币信息
 */

public class AssetRecord {
    private String address;
    private double amount;
    private String businessId;
    private String coinName;
    private Date createTime;
    private double fee;
    private Long id;
    private Long memberId;
    private Integer subType;
    private Integer type;


    @ApiModelProperty("充值或提现地址、或转账地址")
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @ApiModelProperty("充币金额")
    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @ApiModelProperty("业务编号")
    public String getBusinessId() {
        return this.businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    @ApiModelProperty("币种名称")
    public String getCoinName() {
        return this.coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    @ApiModelProperty("创建时间")
    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @ApiModelProperty("手续费")
    public double getFee() {
        return this.fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    @ApiModelProperty("id")
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ApiModelProperty("会员id")
    public Long getMemberId() {
        return this.memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @ApiModelProperty("子业务类型")
    public Integer getSubType() {
        return this.subType;
    }

    public void setSubType(Integer subType) {
        this.subType = subType;
    }

    @ApiModelProperty("业务类型 0充值1.提现2转到合约账户，3.币币交易，4.法币买入，5.法币卖出，6.活动奖励，7.推广奖励，8.分红，9.投票，10.人工充值，11配对      * 12.缴纳商家认证保证金，13.退回商家认证保证金，15.法币充值，16.币币兑换，17.渠道推广，19.注册奖励，20.实名奖励，21.推广注册奖励，22.推广实名奖励，23.推广法币交易奖励，24.推广币币交易奖励，25.空投，26.锁仓")
    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
