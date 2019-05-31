package com.spark.newbitrade.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/2/28.
 */

public class C2C {
    private int currentPage;
    private int totalPage;
    private int pageNumber;
    private int totalElement;
    private List<C2CBean> records;

    public List<C2CBean> getRecords() {
        return records;
    }

    public static class C2CBean implements Serializable {
        private String advertiseType;
        private String memberName;
        private String avatar;
        private int advertiseId;
        private int transactions;
        private double price;
        private double minLimit;
        private double maxLimit;
        private double remainAmount;
        private String createTime;
        private String payMode;
        private int coinId;
        private String unit;
        private String coinName;
        private String coinNameCn;
        //完成率
        private String percentageComplete;
        //30天成单量
        private Integer singleCount;
        //平均放行时间
        private String dischargedTime;
        private String localCurrency;

        public String getLocalCurrency() {
            return localCurrency;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getAdvertiseType() {
            return advertiseType;
        }


        public String getMemberName() {
            return memberName;
        }


        public int getAdvertiseId() {
            return advertiseId;
        }


        public int getTransactions() {
            return transactions;
        }


        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getMinLimit() {
            return minLimit;
        }


        public double getMaxLimit() {
            return maxLimit;
        }


        public double getRemainAmount() {
            return remainAmount;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getPayMode() {
            return payMode;
        }


        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getPercentageComplete() {
            return percentageComplete;
        }

        public Integer getSingleCount() {
            return singleCount;
        }

        public String getDischargedTime() {
            return dischargedTime;
        }
    }

}
