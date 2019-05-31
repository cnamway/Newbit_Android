package com.spark.newbitrade.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2018/1/26.
 */

public class User extends DataSupport implements Serializable {

    private String area;
    private Long certifiedType;
    private String country;
    private String email;
    private Integer fundsVerified;
    private Integer googleAuthStatus;
    private Long id;
    private Long inviterId;
    private String mobilePhone;
    private String promotionCode;
    private String realName;
    private Integer realNameStatus;
    private Date registrationTime;
    private Integer status;
    private Integer transactionStatus;
    private String username;

    private boolean isLogin;
    private String gtc;
    private String avatar;
    private boolean isSelect;


    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Long getCertifiedType() {
        return certifiedType;
    }

    public void setCertifiedType(Long certifiedType) {
        this.certifiedType = certifiedType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getFundsVerified() {
        return fundsVerified;
    }

    public void setFundsVerified(Integer fundsVerified) {
        this.fundsVerified = fundsVerified;
    }

    public Integer getGoogleAuthStatus() {
        return googleAuthStatus;
    }

    public void setGoogleAuthStatus(Integer googleAuthStatus) {
        this.googleAuthStatus = googleAuthStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInviterId() {
        return inviterId;
    }

    public void setInviterId(Long inviterId) {
        this.inviterId = inviterId;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getRealNameStatus() {
        return realNameStatus;
    }

    public void setRealNameStatus(Integer realNameStatus) {
        this.realNameStatus = realNameStatus;
    }

    public Date getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Date registrationTime) {
        this.registrationTime = registrationTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(Integer transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getGtc() {
        return gtc;
    }

    public void setGtc(String gtc) {
        this.gtc = gtc;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getPromotionCode() {
        return promotionCode;
    }


    public String getPromotionPrefix() {
//        return promotionPrefix;
        return "";
    }


}
