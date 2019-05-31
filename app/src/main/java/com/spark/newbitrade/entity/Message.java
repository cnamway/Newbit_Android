package com.spark.newbitrade.entity;

/**
 * Created by Administrator on 2018/2/6.
 */

public class Message {
    /**
     * id : 105
     * merchantCode : 20180904094615217
     * merchantName : 交易所2.0
     * title : 顶顶顶顶
     * flag : 1
     * content : <p>啛啛喳喳错错错错错</p>

     * createTime : 1536040653000
     * updateTime : null
     */

    private String id;
    private String merchantCode;
    private String merchantName;
    private String title;
    private int flag;
    private String content;
    private String createTime;
    private Object updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Object getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Object updateTime) {
        this.updateTime = updateTime;
    }
}
