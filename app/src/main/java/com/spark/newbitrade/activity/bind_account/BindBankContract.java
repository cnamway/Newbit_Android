package com.spark.newbitrade.activity.bind_account;


import com.spark.library.otc.model.MessageResult;
import com.spark.newbitrade.base.BaseContract;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface BindBankContract {
    interface View extends BaseContract.BaseView {

        void doBindBankSuccess(MessageResult obj);

        void doUpdateBankSuccess(MessageResult obj);
    }

    interface Presenter extends BaseContract.BasePresenter {

        void doBindBank(String payType, String account, String bank, String branch, String tradePassword);

        void doUpdateBank(Long id, String payType, String accout, String bank, String branch, String tradePassword);

    }


}
