package com.spark.newbitrade.activity.wallet;

import com.spark.newbitrade.base.BaseContract;
import com.spark.newbitrade.entity.AssetRecord;
import com.spark.newbitrade.entity.Wallet;

import java.util.HashMap;
import java.util.List;


public interface BaseWalletDetailContract {
    interface WalletView extends BaseContract.BaseView {

        void getRecordListSuccess(List<AssetRecord> list);

    }

    interface WalletPresenter extends BaseContract.BasePresenter {

        void getRecordList(boolean isShow, Integer type, HashMap<String, String> params, String busiType, String coinName);

    }
}
