package com.spark.newbitrade.activity.forgot_pwd;


import com.spark.newbitrade.base.Contract;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface ForgotPwdContract {
    interface View extends Contract.BaseView<Presenter> {
        void forgotCodeSuccess(String obj);

        void forgotCodeFail(Integer code, String toastMessage);

        void captchSuccess(JSONObject obj);

        void captchFail(Integer code, String toastMessage);

        void doForgetSuccess(String obj);

        void doForgetFail(Integer code, String toastMessage);


    }

    interface Presenter extends Contract.BasePresenter {

        void forgotCode(String url, HashMap<String, String> params);

        void captch();

        void doForget(HashMap<String, String> params);

    }

}
