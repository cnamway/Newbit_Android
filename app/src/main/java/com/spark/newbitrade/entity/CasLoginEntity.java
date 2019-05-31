package com.spark.newbitrade.entity;

/**
 * CasLoginEntity
 */

public class CasLoginEntity {
    private boolean isLogin;
    private String type;

    public CasLoginEntity(boolean isLogin, String type) {
        this.isLogin = isLogin;
        this.type = type;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
