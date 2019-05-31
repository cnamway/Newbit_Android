package com.spark.newbitrade.entity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/6/4 0004.
 */

public class DepthResult {
    private ArrayList<DepthListInfo> ask;
    private ArrayList<DepthListInfo> bid;

    public ArrayList<DepthListInfo> getAsk() {
        return ask;
    }

    public ArrayList<DepthListInfo> getBid() {
        return bid;
    }

    public class DepthList {
        private double minAmount;
        private double highestPrice;
        private double lowestPrice;
        private double maxAmount;
        private int direction;
        private String symbol;
        private ArrayList<DepthListInfo> items;

        public String getSymbol() {
            return symbol;
        }

        public double getMinAmount() {
            return minAmount;
        }

        public double getHighestPrice() {
            return highestPrice;
        }

        public double getLowestPrice() {
            return lowestPrice;
        }

        public double getMaxAmount() {
            return maxAmount;
        }

        public ArrayList<DepthListInfo> getItems() {
            return items;
        }

        public int getDirection() {
            return direction;
        }
    }

    public class DepthListInfo {
        private double price;
        private double amount;

        public double getPrice() {
            return price;
        }

        public double getAmount() {
            return amount;
        }


        public void setPrice(double price) {
            this.price = price;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }


}
