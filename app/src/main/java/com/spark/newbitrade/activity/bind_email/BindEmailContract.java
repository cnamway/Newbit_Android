package com.spark.newbitrade.activity.bind_email;


import com.spark.newbitrade.base.Contract;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface BindEmailContract {

    interface View extends Contract.BaseView<Presenter> {

        void bindEmailSuccess(String obj);


        void sendEmailCodeSuccess(String obj);


        void doPostFail(Integer code, String toastMessage);


    }

    interface Presenter extends Contract.BasePresenter {
        void sendEmailCode(HashMap<String, String> params);

        void bindEmail(HashMap<String, String> params);
    }

    interface UnbindEmailView extends Contract.BaseView<UnbindEmailPresenter> {

        void getNewEmailCodeSuccess(String obj);

        void getOldEmailCodeSuccess(String obj);

        void unBindEmailSuccess(String obj);

        void doPostFail(Integer code, String toastMessage);

    }

    interface UnbindEmailPresenter extends Contract.BasePresenter {
        void getOldEmailCode(HashMap<String, String> params);

        void getNewEmailCode(HashMap<String, String> params);

        void unBindEmail(HashMap<String, String> params);
    }
}
