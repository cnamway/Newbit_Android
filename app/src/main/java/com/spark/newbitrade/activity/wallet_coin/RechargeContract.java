package com.spark.newbitrade.activity.wallet_coin;

import com.spark.library.ac.model.MemberWallet;
import com.spark.newbitrade.base.BaseContract;



public interface RechargeContract {
    interface WalletView extends BaseContract.BaseView {
        void getAddressSuccess(MemberWallet obj);
    }

    interface WalletPresenter extends BaseContract.BasePresenter {

        void getAddress(String coinName);

    }
}
