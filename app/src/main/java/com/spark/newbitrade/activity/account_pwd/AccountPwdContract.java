package com.spark.newbitrade.activity.account_pwd;


import com.spark.newbitrade.base.BaseContract;
import com.spark.newbitrade.base.Contract;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface AccountPwdContract {
    interface View extends BaseContract.BaseView {

        void accountPwdSuccess(String obj);

        void editAccountPwdSuccess(String obj);

        void doPostFail(Integer code, String toastMessage);
    }

    interface Presenter extends BaseContract.BasePresenter {
        void accountPwd(String tradePassword);

        void editAccountPwd(String oldPassword, String newPassword);
    }



    interface ResetView extends Contract.BaseView<ResetPresenter> {

        void resetAccountPwdSuccess(String obj);

        void doPostFail(Integer code, String toastMessage);

        void resetAccountPwdCodeSuccess(String obj);

    }

    interface ResetPresenter extends Contract.BasePresenter {

        void resetAccountPwd(HashMap<String, String> params);

        void resetAccountPwdCode(HashMap<String, String> params);
    }

}
