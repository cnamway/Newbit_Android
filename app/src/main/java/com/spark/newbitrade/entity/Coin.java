package com.spark.newbitrade.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/2/27.
 */

public class Coin implements Serializable {
    private int id;
    private int memberId;
    private CoinBean coin;
    private double balance;
    private double frozenBalance;
    private String address;
    private String coinId;
    private double cnyRate;
    private double usdRate;
    private int canRecharge;
    private int canWithdraw;
    private double minRechargeAmount;

    public double getMinRechargeAmount() {
        return minRechargeAmount;
    }

    public String getCoinId() {
        return coinId;
    }

    public int getId() {
        return id;
    }

    public int getMemberId() {
        return memberId;
    }

    public int getCanWithdraw() {
        return canWithdraw;
    }

    public int getCanRecharge() {
        return canRecharge;
    }

    public String getUnit() {
        return coinId;
    }

    public double getCnyRate() {
        return cnyRate;
    }

    public double getUsdRate() {
        return usdRate;
    }

    public double getBalance() {
        return balance;
    }

    public double getFrozenBalance() {
        return frozenBalance;
    }

    public String getAddress() {
        return address;
    }

    public static class CoinBean implements Serializable {
        private String name;
        private String nameCn;
        private String unit;
        private int status;
        private double minTxFee;
        private double cnyRate;
        private double maxTxFee;
        private double usdRate;
        private double jpyRate;
        private int enableRpc;
        private int sort;
        private int canWithdraw;
        private int canRecharge;
        private int canAutoWithdraw;
        private String withdrawThreshold;
        private String minWithdrawAmount;
        private String maxWithdrawAmount;

        public double getJpyRate() {
            return jpyRate;
        }

        public String getUnit() {
            return unit;
        }

        public double getCnyRate() {
            return cnyRate;
        }

        public double getUsdRate() {
            return usdRate;
        }

        public int getCanWithdraw() {
            return canWithdraw;
        }

        public int getCanRecharge() {
            return canRecharge;
        }

    }
}

