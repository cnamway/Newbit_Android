package com.spark.newbitrade.activity.message;


import com.spark.newbitrade.base.Contract;
import com.spark.newbitrade.entity.Message;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface MessageContract {
    interface View extends Contract.BaseView<Presenter> {

        void messageSuccess(List<Message> obj);

        void messageFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {

        void message(HashMap<String, String> params);

    }
}
