package com.spark.newbitrade.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/5/2 0002.
 */

public class AccountSetting implements Serializable {
    private String realName;
    private boolean cardVerified;
    private boolean alipayVerified;
    private boolean wechatVerified;
    private BankInfoBean bankInfo;
    private AlipayBean alipay;
    private WeChatBean wechatPay;
    private List<AccountInfo> payTypes;

    public List<AccountInfo> getPayTypes() {
        return payTypes;
    }

    public String getRealName() {
        return realName;
    }

    public boolean isCardVerified() {
        return cardVerified;
    }

    public boolean isAlipayVerified() {
        return alipayVerified;
    }

    public boolean isWechatVerified() {
        return wechatVerified;
    }

    public BankInfoBean getBankInfo() {
        return bankInfo;
    }

    public AlipayBean getAlipay() {
        return alipay;
    }

    public WeChatBean getWechatPay() {
        return wechatPay;
    }


    public static class BankInfoBean implements Serializable {
        /**
         * bank : 中国工商银行
         * branch : 4156453123132
         * cardNo : 123456789632145678
         */

        private String bank;
        private String branch;
        private String cardNo;

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getBranch() {
            return branch;
        }

        public String getCardNo() {
            return cardNo;
        }

    }

    public static class AlipayBean implements Serializable {
        /**
         * aliNo : 18255478978
         * qrCodeUrl : null
         */

        private String aliNo;
        private String qrCodeUrl;

        public String getAliNo() {
            return aliNo;
        }

    }

    public static class WeChatBean implements Serializable {
        /**
         * aliNo : 18255478978
         * qrCodeUrl : null
         */

        private String wechat;
        private String qrCodeUrl;

        public String getweChat() {
            return wechat;
        }

    }

    public class AccountInfo implements Serializable{
        private int id;
        private int memberId;
        private String payAddress;
        private String qrCodeUrl;
        private String bank;
        private String branch;
        private String payType;


        public String getPayType() {
            return payType;
        }

        public int getId() {
            return id;
        }

        public int getMemberId() {
            return memberId;
        }

        public String getPayAddress() {
            return payAddress;
        }

        public String getQrCodeUrl() {
            return qrCodeUrl;
        }

        public String getBank() {
            return bank;
        }

        public String getBranch() {
            return branch;
        }
    }

}
