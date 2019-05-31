package com.spark.newbitrade.activity.wallet_coin;


import com.spark.newbitrade.base.BaseContract;
import com.spark.newbitrade.entity.Address;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface AddressContract {
    interface View extends BaseContract.BaseView {
        void findWalletWithdrawAddressSuccess(List<Address> obj);

        void delWalletWithdrawAddressUsingGETSuccess(String obj);
    }

    interface Presenter extends BaseContract.BasePresenter {

        void findWalletWithdrawAddress(String coinName);

        void delWalletWithdrawAddressUsingGET(String id);
    }


}
