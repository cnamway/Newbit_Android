package com.spark.newbitrade.activity.setting;

import com.spark.newbitrade.base.BaseContract;

/**
 * Created by Administrator on 2018/4/24 0024.
 */

public class SettingContact {

    public interface View extends BaseContract.BaseView {

        void loginOutSuccess(String obj);
    }

    interface Presenter extends BaseContract.BasePresenter {

        void loginOut();
    }

}
