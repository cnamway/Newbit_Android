package com.spark.newbitrade.activity.feed;


import com.spark.newbitrade.base.Contract;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface FeedBackContract {

    interface View extends Contract.BaseView<Presenter> {

        void doFeedBackSuccess(String obj);

        void doFeedBackFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {

        void doFeedBack(HashMap<String, String> params);

    }
}
