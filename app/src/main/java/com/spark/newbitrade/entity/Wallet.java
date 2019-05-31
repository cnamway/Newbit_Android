package com.spark.newbitrade.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/9/18 0018.
 */

public class Wallet implements Serializable {

    /**
     * address : 444
     * balance : 991.60297298
     * frozenBalance : 7.62
     * isLock : 1
     * memberId : 35
     * coinId : BTC
     * cnyRate : 0.0
     * usdRate : 0.0
     * canAutoWithdraw : 1
     * canRecharge : 1
     * canTransfer : 1
     * canWithdraw : 1
     * minRechargeAmount : 0.0
     */

    private String address;
    private double balance;
    private double frozenBalance;
    private int isLock;
    private int memberId;
    private String coinId;
    private double cnyRate;
    private double usdRate;
    private int canAutoWithdraw;
    private int canRecharge;
    private int canTransfer;
    private int canWithdraw;
    private double minRechargeAmount;
    private double totalLegalAssetBalance;
    private String name;
    private int isDefault;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public double getTotalLegalAssetBalance() {
        return totalLegalAssetBalance;
    }

    public void setTotalLegalAssetBalance(double totalLegalAssetBalance) {
        this.totalLegalAssetBalance = totalLegalAssetBalance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getFrozenBalance() {
        return frozenBalance;
    }

    public void setFrozenBalance(double frozenBalance) {
        this.frozenBalance = frozenBalance;
    }

    public int getIsLock() {
        return isLock;
    }

    public void setIsLock(int isLock) {
        this.isLock = isLock;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public double getCnyRate() {
        return cnyRate;
    }

    public void setCnyRate(double cnyRate) {
        this.cnyRate = cnyRate;
    }

    public double getUsdRate() {
        return usdRate;
    }

    public void setUsdRate(double usdRate) {
        this.usdRate = usdRate;
    }

    public int getCanAutoWithdraw() {
        return canAutoWithdraw;
    }

    public void setCanAutoWithdraw(int canAutoWithdraw) {
        this.canAutoWithdraw = canAutoWithdraw;
    }

    public int getCanRecharge() {
        return canRecharge;
    }

    public void setCanRecharge(int canRecharge) {
        this.canRecharge = canRecharge;
    }

    public int getCanTransfer() {
        return canTransfer;
    }

    public void setCanTransfer(int canTransfer) {
        this.canTransfer = canTransfer;
    }

    public int getCanWithdraw() {
        return canWithdraw;
    }

    public void setCanWithdraw(int canWithdraw) {
        this.canWithdraw = canWithdraw;
    }

    public double getMinRechargeAmount() {
        return minRechargeAmount;
    }

    public void setMinRechargeAmount(double minRechargeAmount) {
        this.minRechargeAmount = minRechargeAmount;
    }
}
