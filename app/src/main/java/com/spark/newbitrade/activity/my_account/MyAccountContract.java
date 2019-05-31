package com.spark.newbitrade.activity.my_account;


import com.spark.library.otc.model.MessageResult;
import com.spark.newbitrade.base.BaseContract;
import com.spark.newbitrade.entity.PayWaySetting;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface MyAccountContract {
    interface View extends BaseContract.BaseView {

        void queryPayWayListSuccess(List<PayWaySetting> response);

        void updateSuccess(MessageResult response);

    }

    interface Presenter extends BaseContract.BasePresenter {

        void queryPayWayList();

        void doUpdateStatusBank(Long id, Integer status);

        void doDeleteBank(Long id, String tradePassword);
    }


}
