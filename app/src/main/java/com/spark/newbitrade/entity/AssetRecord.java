package com.spark.newbitrade.entity;

import com.google.gson.annotations.SerializedName;
import com.spark.library.ac.model.MemberTransactionVo;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * 提币信息
 */

public class AssetRecord {
    @SerializedName("address")
    private String address = null;
    @SerializedName("amount")
    private BigDecimal amount = null;
    @SerializedName("businessId")
    private String businessId = null;
    @SerializedName("coinName")
    private String coinName = null;
    @SerializedName("createTime")
    private Date createTime = null;
    @SerializedName("fee")
    private BigDecimal fee = null;
    @SerializedName("id")
    private Long id = null;
    @SerializedName("memberId")
    private Long memberId = null;
    @SerializedName("subType")
    private Integer subType = null;
    @SerializedName("type")
    private Integer type = null;

    @ApiModelProperty("充值或提现地址、或转账地址")
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @ApiModelProperty("充币金额")
    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
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
    public BigDecimal getFee() {
        return this.fee;
    }

    public void setFee(BigDecimal fee) {
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

    @ApiModelProperty("1-充币 2-提币 3-资金划转转入 4-资金划转转出 5-资金交易扣除 6-资金交易增加")
    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MemberTransactionVo {\n");
        sb.append("  address: ").append(this.address).append("\n");
        sb.append("  amount: ").append(this.amount).append("\n");
        sb.append("  businessId: ").append(this.businessId).append("\n");
        sb.append("  coinName: ").append(this.coinName).append("\n");
        sb.append("  createTime: ").append(this.createTime).append("\n");
        sb.append("  fee: ").append(this.fee).append("\n");
        sb.append("  id: ").append(this.id).append("\n");
        sb.append("  memberId: ").append(this.memberId).append("\n");
        sb.append("  subType: ").append(this.subType).append("\n");
        sb.append("  type: ").append(this.type).append("\n");
        sb.append("}\n");
        return sb.toString();
    }

}
