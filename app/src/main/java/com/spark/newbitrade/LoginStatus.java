package com.spark.newbitrade;

/**
 * 记录登录状态
 */

public class LoginStatus {
    private boolean isAcLogin;
    private boolean isOtcLogin;
    private boolean isUcLogin;

    public boolean isAcLogin() {
        return isAcLogin;
    }

    public void setAcLogin(boolean acLogin) {
        isAcLogin = acLogin;
    }

    public boolean isOtcLogin() {
        return isOtcLogin;
    }

    public void setOtcLogin(boolean otcLogin) {
        isOtcLogin = otcLogin;
    }

    public boolean isUcLogin() {
        return isUcLogin;
    }

    public void setUcLogin(boolean ucLogin) {
        isUcLogin = ucLogin;
    }
}
