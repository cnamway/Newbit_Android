package com.spark.newbitrade.activity.wallet_coin;


import com.spark.newbitrade.base.BaseContract;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface AddAddressContract {

    interface View extends BaseContract.BaseView {
        void addWalletWithdrawAddressUsingPOSTSuccess(String obj);
    }

    interface Presenter extends BaseContract.BasePresenter {

        void addWalletWithdrawAddressUsingPOST(String address, String coinId, String remark);
    }


}
