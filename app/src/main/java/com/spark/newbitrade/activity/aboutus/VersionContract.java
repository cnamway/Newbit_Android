package com.spark.newbitrade.activity.aboutus;


import com.spark.library.cms.model.MessageResultWebConfigVo;
import com.spark.newbitrade.base.BaseContract;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface VersionContract {

    interface View extends BaseContract.BaseView {

        void checkVersionSuccess(String obj);

    }

    interface Presenter extends BaseContract.BasePresenter {

        void checkVersion();

    }
}
