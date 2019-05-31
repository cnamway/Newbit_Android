package com.spark.newbitrade.activity.switch_user;


import com.spark.newbitrade.base.Contract;
import com.spark.newbitrade.entity.User;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface SwitchUserContract {
    interface View extends Contract.BaseView<Presenter> {

        void userFail(Integer code, String toastMessage);

        void userSuccess(List<User> obj);
    }

    interface Presenter extends Contract.BasePresenter {
        void users();
    }
}
