package com.spark.newbitrade.activity.edit_login_pwd;


import com.spark.newbitrade.base.BaseContract;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface EditLoginPwdContract {
    interface View extends BaseContract.BaseView {
        void getPhoneCodeSuccess(String obj);

        void captchSuccess(JSONObject obj);

        void updateForgetSuccess(String obj);

        void codeSuccess(String obj);

        void loginOutSuccess(String obj);
    }

    interface Presenter extends BaseContract.BasePresenter {

        void getPhoneCode(String phone);

        void captch();

        void getPhoneCode(String phone, String check, String cid);

        void updateForget(String phone, String code, String oldPassword, String newPassword);

        void loginOut();
    }
}
