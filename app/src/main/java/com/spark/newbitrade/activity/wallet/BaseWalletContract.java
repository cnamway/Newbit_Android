package com.spark.newbitrade.activity.wallet;

import com.spark.library.ac.model.MessageResult;
import com.spark.newbitrade.base.BaseContract;
import com.spark.newbitrade.entity.Wallet;

import java.util.List;

/**
 * 钱包
 */

public interface BaseWalletContract {
    interface WalletView extends BaseContract.BaseView {
        void getWalletSuccess(List<Wallet> list);

        void findSupportAssetUsingGETSuccess(List<Wallet> list);
    }

    interface WalletPresenter extends BaseContract.BasePresenter {

        void getWallet(String type);

        void findSupportAssetUsingGET();

    }
}
