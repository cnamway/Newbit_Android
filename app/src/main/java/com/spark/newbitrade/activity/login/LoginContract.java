package com.spark.newbitrade.activity.login;


import com.spark.newbitrade.base.BaseContract;
import com.spark.newbitrade.entity.User;

import org.json.JSONObject;

/**
 * 登录
 */

public interface LoginContract {
    interface LoginView extends BaseContract.BaseView {
        void captchSuccess(JSONObject obj);

        void casLoginSuccess(Object o);

        void codeSuccess(Object o);

        void ucLoginSuccess(String response);

        void getUserInfoSuccess(User user);

        void checkPhoneCodeSuccess(String response);

        void checkCaptchaSuccess(String response);
    }

    interface LoginPresenter extends BaseContract.BasePresenter {
        void getPhoneCode(String phone);

        void captch();

        void casLogn(String username, String password, String remember);

        void doUcLogin(String gtc, String type);

        void getUserInfo();

        void getPhoneCode(String phone, String check, String cid);

        void checkPhoneCode(String code);

        void checkCaptcha(String check, String cid);

    }
}
