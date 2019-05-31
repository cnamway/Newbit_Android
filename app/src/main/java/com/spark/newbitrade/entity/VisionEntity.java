package com.spark.newbitrade.entity;

/**
 * App版本更新
 */

public class VisionEntity {


    /**
     * code : 200
     * data : {"updateRemark":"更新","updateTime":1557212752000,"version":"2.0","url":"https://bitpay.oss-cn-hangzhou.aliyuncs.com/bench/payment.ipa"}
     */

    private int code;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * updateRemark : 更新
         * updateTime : 1557212752000
         * version : 2.0
         * url : https://bitpay.oss-cn-hangzhou.aliyuncs.com/bench/payment.ipa
         */

        private String updateRemark;
        private long updateTime;
        private String version;
        private String url;

        public String getUpdateRemark() {
            return updateRemark;
        }

        public void setUpdateRemark(String updateRemark) {
            this.updateRemark = updateRemark;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
