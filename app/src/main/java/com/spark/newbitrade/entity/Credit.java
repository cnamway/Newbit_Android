package com.spark.newbitrade.entity;

/**
 * Created by Administrator on 2018/4/24 0024.
 */

public class Credit {

    /**
     * data : {"id":30,"realName":"陈琪","idCard":"342524199207256222","identityCardImgFront":"http://xinhuo-xindai.oss-cn-hangzhou.aliyuncs.com/2018/03/21/556dc806-edae-4494-83d0-2cae6a69dda8.jpg","identityCardImgReverse":"http://xinhuo-xindai.oss-cn-hangzhou.aliyuncs.com/2018/03/21/4eebd775-b8bf-458b-8271-75759958e547.jpg","identityCardImgInHand":"http://xinhuo-xindai.oss-cn-hangzhou.aliyuncs.com/2018/03/21/2a08c78d-39de-4d5b-8641-c6417c0c15fa.jpg","auditStatus":2,"member":{"id":51,"username":"陈七七","realName":"陈琪","idNumber":"342524199207256222","email":"1030726326@qq.com","mobilePhone":"18255176187","location":{"country":"中国","province":null,"city":null,"district":null},"memberLevel":2,"status":0,"registrationTime":"2018-03-21 13:34:34","lastLoginTime":null,"token":"ada1db4e213f9945f6ea3eccbcdccd2a","transactions":29,"bankInfo":null,"alipay":{"aliNo":"18255478978","qrCodeUrl":null},"wechatPay":null,"appealTimes":0,"appealSuccessTimes":0,"inviterId":null,"promotionCode":"U0000517T","firstLevel":1,"secondLevel":0,"thirdLevel":0,"country":{"zhName":"中国","enName":"China","areaCode":"86","language":"zh_CN","localCurrency":"CNY","sort":0},"realNameStatus":2,"loginCount":0,"certifiedBusinessStatus":2,"certifiedBusinessApplyTime":"2018-03-28 17:58:41","avatar":"http://caymanex.oss-cn-hangzhou.aliyuncs.com/2018/03/27/8b76070d-4908-4f0e-b7f9-e67dc1d56c45.jpg","tokenExpireTime":"2018-05-01 09:38:53"},"rejectReason":null,"createTime":"2018-03-21 14:08:14","updateTime":"2018-03-21 14:08:40"}
     * code : 0
     * message : SUCCESS
     */

    private DataBean data;
    private int code;
    private String message;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 30
         * realName : 陈琪
         * idCard : 342524199207256222
         * identityCardImgFront : http://xinhuo-xindai.oss-cn-hangzhou.aliyuncs.com/2018/03/21/556dc806-edae-4494-83d0-2cae6a69dda8.jpg
         * identityCardImgReverse : http://xinhuo-xindai.oss-cn-hangzhou.aliyuncs.com/2018/03/21/4eebd775-b8bf-458b-8271-75759958e547.jpg
         * identityCardImgInHand : http://xinhuo-xindai.oss-cn-hangzhou.aliyuncs.com/2018/03/21/2a08c78d-39de-4d5b-8641-c6417c0c15fa.jpg
         * auditStatus : 2
         * member : {"id":51,"username":"陈七七","realName":"陈琪","idNumber":"342524199207256222","email":"1030726326@qq.com","mobilePhone":"18255176187","location":{"country":"中国","province":null,"city":null,"district":null},"memberLevel":2,"status":0,"registrationTime":"2018-03-21 13:34:34","lastLoginTime":null,"token":"ada1db4e213f9945f6ea3eccbcdccd2a","transactions":29,"bankInfo":null,"alipay":{"aliNo":"18255478978","qrCodeUrl":null},"wechatPay":null,"appealTimes":0,"appealSuccessTimes":0,"inviterId":null,"promotionCode":"U0000517T","firstLevel":1,"secondLevel":0,"thirdLevel":0,"country":{"zhName":"中国","enName":"China","areaCode":"86","language":"zh_CN","localCurrency":"CNY","sort":0},"realNameStatus":2,"loginCount":0,"certifiedBusinessStatus":2,"certifiedBusinessApplyTime":"2018-03-28 17:58:41","avatar":"http://caymanex.oss-cn-hangzhou.aliyuncs.com/2018/03/27/8b76070d-4908-4f0e-b7f9-e67dc1d56c45.jpg","tokenExpireTime":"2018-05-01 09:38:53"}
         * rejectReason : null
         * createTime : 2018-03-21 14:08:14
         * updateTime : 2018-03-21 14:08:40
         */

        private int id;
        private String realName;
        private String idCard;
        private String identityCardImgFront;
        private String identityCardImgReverse;
        private String identityCardImgInHand;
        private int auditStatus;
        private Object rejectReason;
        private String createTime;
        private String updateTime;
        private String certifiedType;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getIdentityCardImgFront() {
            return identityCardImgFront;
        }

        public void setIdentityCardImgFront(String identityCardImgFront) {
            this.identityCardImgFront = identityCardImgFront;
        }

        public String getIdentityCardImgReverse() {
            return identityCardImgReverse;
        }

        public void setIdentityCardImgReverse(String identityCardImgReverse) {
            this.identityCardImgReverse = identityCardImgReverse;
        }

        public String getIdentityCardImgInHand() {
            return identityCardImgInHand;
        }

        public void setIdentityCardImgInHand(String identityCardImgInHand) {
            this.identityCardImgInHand = identityCardImgInHand;
        }

        public int getAuditStatus() {
            return auditStatus;
        }

        public void setAuditStatus(int auditStatus) {
            this.auditStatus = auditStatus;
        }

        public Object getRejectReason() {
            return rejectReason;
        }

        public void setRejectReason(Object rejectReason) {
            this.rejectReason = rejectReason;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getCertifiedType() {
            return certifiedType;
        }

        public void setCertifiedType(String certifiedType) {
            this.certifiedType = certifiedType;
        }
    }
}
