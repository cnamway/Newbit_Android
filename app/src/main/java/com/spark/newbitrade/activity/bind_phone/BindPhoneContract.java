package com.spark.newbitrade.activity.bind_phone;


import com.spark.newbitrade.base.Contract;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface BindPhoneContract {
    interface View extends Contract.BaseView<Presenter> {
        void bindPhoneSuccess(String obj);

        void sendChangePhoneCodeSuccess(String obj);

        void changePhoneSuccess(String obj);

        void sendCodeSuccess(String obj, int type);

        void doPostFail(Integer code, String toastMessage);


    }

    interface Presenter extends Contract.BasePresenter {
        void bindPhone(HashMap<String, String> params);

        void sendCode(HashMap<String, String> params, int type);

        void sendChangePhoneCode(HashMap<String, String> map);

        void changePhone(HashMap<String, String> params);
    }
}
