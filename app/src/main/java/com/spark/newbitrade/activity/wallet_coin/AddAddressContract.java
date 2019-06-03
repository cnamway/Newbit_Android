package com.spark.newbitrade.activity.wallet_coin;


import com.spark.newbitrade.base.BaseContract;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface AddAddressContract {

    interface View extends BaseContract.BaseView {
        void addWalletWithdrawAddressUsingPOSTSuccess(String obj);

        void getPhoneCodeSuccess(String obj);

        void captchSuccess(JSONObject obj);

        void codeSuccess(String obj);
    }

    interface Presenter extends BaseContract.BasePresenter {

        void addWalletWithdrawAddressUsingPOST(String address, String coinId, String remark, String code, String phone);

        void getPhoneCode(String phone);

        void captch();

        void getPhoneCode(String phone, String check, String cid);
    }


}
