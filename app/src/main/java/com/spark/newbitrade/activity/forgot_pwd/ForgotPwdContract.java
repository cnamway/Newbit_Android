package com.spark.newbitrade.activity.forgot_pwd;


import com.spark.newbitrade.base.BaseContract;
import com.spark.newbitrade.base.Contract;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface ForgotPwdContract {
    interface View extends BaseContract.BaseView {
        void getPhoneCodeSuccess(String obj);

        void captchSuccess(JSONObject obj);

        void updateForgetSuccess(String obj);

        void codeSuccess(String obj);

        void checkPhoneCodeSuccess(String response);
    }

    interface Presenter extends BaseContract.BasePresenter {

        void getPhoneCode(String phone);

        void captch();

        void getPhoneCode(String phone, String check, String cid);

        void checkPhoneCode(String code);

        void updateForget(String mobilePhone, String newPassword, String code, String countryCode);

    }

}
