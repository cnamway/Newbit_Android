package com.spark.newbitrade.activity.wallet_coin;

import com.spark.library.ac.model.MemberWallet;
import com.spark.newbitrade.base.BaseContract;
import com.spark.newbitrade.entity.ExtractInfo;

import java.util.List;


public interface RechargeContract {
    interface WalletView extends BaseContract.BaseView {
        void getAddressSuccess(MemberWallet obj);

        void getExtractInfoSuccess(List<ExtractInfo> list);
    }

    interface WalletPresenter extends BaseContract.BasePresenter {

        void getAddress(String coinName);

        void getExtractInfo(String coinName);
    }
}
