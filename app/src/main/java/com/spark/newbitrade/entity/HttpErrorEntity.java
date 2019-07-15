package com.spark.newbitrade.entity;


import com.spark.newbitrade.utils.StringUtils;

import org.json.JSONObject;

/**
 * 错误信息的实体类
 */

public class HttpErrorEntity {
    private int code;
    private String message;
    private String url;
    private String cid;
    private String data;

    public HttpErrorEntity(int code, String message, String url, String cid) {
        this.code = code;
        this.message = message;
        this.url = url;
        this.cid = cid;
    }

    public HttpErrorEntity(int code, String message, String url, String cid, String data) {
        this.code = code;
        this.message = message;
        this.url = url;
        this.cid = cid;
        this.data = data;
    }

    public HttpErrorEntity(JSONObject jsonObject) {
        this.code = StringUtils.getCode(jsonObject);
        this.message = StringUtils.getMessage(jsonObject);
        this.url = StringUtils.getUrl(jsonObject);
        this.cid = StringUtils.getCid(jsonObject);
        this.data = StringUtils.getData(jsonObject);
    }

    public String getData() {
        return data;
    }

    public String getCid() {
        return cid;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "HttpErrorEntity{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", url='" + url + '\'' +
                ", cid='" + cid + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
