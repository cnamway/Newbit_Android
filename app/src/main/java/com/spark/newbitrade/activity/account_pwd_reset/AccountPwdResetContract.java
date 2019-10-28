package com.spark.newbitrade.activity.account_pwd_reset;

import com.spark.newbitrade.base.BaseContract;

import org.json.JSONObject;

/**
 * 重置资金密码
 */

public interface AccountPwdResetContract {
    interface ForgotPwdView extends BaseContract.BaseView {
        void getPhoneCodeSuccess(String obj);

        void captchSuccess(JSONObject obj);

        void updateForgetSuccess(String obj);

        void codeSuccess(String obj);

        void checkPhoneCodeSuccess(String response);
    }

    interface ForgotPwdPresenter extends BaseContract.BasePresenter {

        void getPhoneCode();

        void captch();

        void getPhoneCode(String phone, String check, String cid);

        void checkPhoneCode(String code);

        void updateForget(String mobilePhone, String newPassword, String code);

    }

}
