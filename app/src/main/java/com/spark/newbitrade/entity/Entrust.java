package com.spark.newbitrade.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/1/30.
 */

public class Entrust implements Serializable {

    /**
     * list : [{"baseSymbol":"USDT","canceledTime":0,"coinSymbol":"ETH","completedTime":0,"details":[],"memberId":93,"orderId":"E153786541776736","orderQty":5,"orderType":0,"price":1,"priceType":1,"remark":"","side":0,"status":1,"symbol":"ETH/USDT","tradedAmount":0,"transactTime":1537865417775,"turnover":0},{"baseSymbol":"USDT","canceledTime":0,"coinSymbol":"ETH","completedTime":0,"details":[],"memberId":93,"orderId":"E153786749561898","orderQty":0.5888,"orderType":0,"price":1,"priceType":1,"remark":"","side":0,"status":1,"symbol":"ETH/USDT","tradedAmount":0,"transactTime":1537867495623,"turnover":0},{"baseSymbol":"USDT","canceledTime":0,"coinSymbol":"ETH","completedTime":0,"details":[],"memberId":93,"orderId":"E153786760056770","orderQty":0.5689,"orderType":0,"price":1,"priceType":1,"remark":"","side":0,"status":1,"symbol":"ETH/USDT","tradedAmount":0,"transactTime":1537867600576,"turnover":0},{"baseSymbol":"USDT","canceledTime":0,"coinSymbol":"ETH","completedTime":0,"details":[],"memberId":93,"orderId":"E153786852044671","orderQty":0.5889,"orderType":0,"price":1,"priceType":1,"remark":"","side":0,"status":1,"symbol":"ETH/USDT","tradedAmount":0,"transactTime":1537868520454,"turnover":0}]
     * pageNo : 1
     * pageSize : 10
     * totalNum : 4
     * totalPage : 1
     */

    private int pageNo;
    private int pageSize;
    private int totalNum;
    private int totalPage;
    private List<ListBean> list;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable{
        /**
         * baseSymbol : USDT
         * canceledTime : 0
         * coinSymbol : ETH
         * completedTime : 0
         * details : []
         * memberId : 93
         * orderId : E153786541776736
         * orderQty : 5
         * orderType : 0
         * price : 1
         * priceType : 1
         * remark :
         * side : 0
         * status : 1
         * symbol : ETH/USDT
         * tradedAmount : 0
         * transactTime : 1537865417775
         * turnover : 0
         */

        private String baseSymbol;
        private String canceledTime;
        private String coinSymbol;
        private String completedTime;
        private int memberId;
        private String orderId;
        private double orderQty;
        private int orderType;
        private double price;
        private int priceType;
        private String remark;
        private int side;
        private int status;
        private String symbol;
        private double tradedAmount;
        private long transactTime;
        private double turnover;
        private List<DetailBean> details;

        public String getBaseSymbol() {
            return baseSymbol;
        }

        public String getCanceledTime() {
            return canceledTime;
        }

        public String getCoinSymbol() {
            return coinSymbol;
        }

        public String getCompletedTime() {
            return completedTime;
        }

        public int getMemberId() {
            return memberId;
        }

        public String getOrderId() {
            return orderId;
        }

        public double getOrderQty() {
            return orderQty;
        }

        public int getOrderType() {
            return orderType;
        }

        public double getPrice() {
            return price;
        }

        public int getPriceType() {
            return priceType;
        }

        public String getRemark() {
            return remark;
        }

        public int getSide() {
            return side;
        }

        public int getStatus() {
            return status;
        }

        public String getSymbol() {
            return symbol;
        }

        public double getTradedAmount() {
            return tradedAmount;
        }

        public long getTransactTime() {
            return transactTime;
        }

        public double getTurnover() {
            return turnover;
        }

        public List<DetailBean> getDetails() {
            return details;
        }

        public static class DetailBean implements Serializable{
            /**
             * orderId : E152464256123665
             * price : 6748.71
             * amount : 1
             * fee : 0.001
             * time : 1524642561695
             */

            private String orderId;
            private double price;
            private double amount;
            private double fee;
            private long time;

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public double getFee() {
                return fee;
            }

            public void setFee(double fee) {
                this.fee = fee;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }
        }
    }
}
