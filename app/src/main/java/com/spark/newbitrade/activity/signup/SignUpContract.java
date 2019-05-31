package com.spark.newbitrade.activity.signup;


import com.spark.newbitrade.base.BaseContract;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface SignUpContract {

    interface SignView extends BaseContract.BaseView {

        void codeSuccess(String obj);

        void sighUpSuccess(String obj);

        void captchSuccess(JSONObject obj);
    }

    interface SignPresenter extends BaseContract.BasePresenter {
        void getPhoneCode(String phone, String check, String cid);

        void getPhoneCode(String phone);

        void getEmailCode(String email);

        void captch();

        void sighUpByEmail(String username, String password, String country, String promotion, String email, String code);

        void sighUpByPhone(String username, String password, String country, String promotion, String phone, String code);

    }



}
