package com.spark.newbitrade.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Administrator on 2018/4/12.
 */

public class ChatEntity {

    private Type type;
    private String fromAvatar;
    private String content;
    private String messageType;
    private String nameFrom;
    private String nameTo;
    private String orderId;
    private Integer partitionKey;
    private String sendTime;
    private String uidFrom;
    private String uidTo;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getFromAvatar() {
        return fromAvatar;
    }

    public void setFromAvatar(String fromAvatar) {
        this.fromAvatar = fromAvatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getNameFrom() {
        return nameFrom;
    }

    public void setNameFrom(String nameFrom) {
        this.nameFrom = nameFrom;
    }

    public String getNameTo() {
        return nameTo;
    }

    public void setNameTo(String nameTo) {
        this.nameTo = nameTo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getPartitionKey() {
        return partitionKey;
    }

    public void setPartitionKey(Integer partitionKey) {
        this.partitionKey = partitionKey;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getUidFrom() {
        return uidFrom;
    }

    public void setUidFrom(String uidFrom) {
        this.uidFrom = uidFrom;
    }

    public String getUidTo() {
        return uidTo;
    }

    public void setUidTo(String uidTo) {
        this.uidTo = uidTo;
    }

    public enum Type {
        LEFT, RIGHT;
    }
}
