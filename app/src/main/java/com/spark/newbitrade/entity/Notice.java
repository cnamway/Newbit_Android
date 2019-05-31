package com.spark.newbitrade.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * 首页滚动数据
 */

public class Notice {
    @SerializedName("body")
    private String body = null;
    @SerializedName("createTime")
    private Date createTime = null;
    @SerializedName("header")
    private String header = null;
    @SerializedName("id")
    private Long id = null;
    @SerializedName("sysLanguage")
    private String sysLanguage = null;
    @SerializedName("updateTime")
    private Date updateTime = null;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSysLanguage() {
        return sysLanguage;
    }

    public void setSysLanguage(String sysLanguage) {
        this.sysLanguage = sysLanguage;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
