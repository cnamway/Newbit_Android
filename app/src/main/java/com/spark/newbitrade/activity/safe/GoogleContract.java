package com.spark.newbitrade.activity.safe;


import com.spark.newbitrade.base.Contract;
import com.spark.newbitrade.entity.GoogleCode;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/07/21
 */

public interface GoogleContract {
    interface View extends Contract.BaseView<Presenter> {

        void getInfoSuccess(GoogleCode obj);

        void getInfoFail(Integer code, String toastMessage);

    }

    interface Presenter extends Contract.BasePresenter {

        void getInfo();
    }

    interface UnBindView extends Contract.BaseView<UnBindPresenter> {

        void doUnbindSuccess(String obj);

        void doUnbindFail(Integer code, String toastMessage);

    }

    interface UnBindPresenter extends Contract.BasePresenter {

        void doUnbind(HashMap<String, String> params);
    }

    interface BindView extends Contract.BaseView<BindPresenter> {

        void doBindSuccess(String obj);

        void doBindFail(Integer code, String toastMessage);

    }

    interface BindPresenter extends Contract.BasePresenter {

        void doBind(HashMap<String, String> params);
    }
}
