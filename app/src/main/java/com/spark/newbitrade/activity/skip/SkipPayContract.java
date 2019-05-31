package com.spark.newbitrade.activity.skip;


import com.spark.library.ac.model.MemberWalletVo;
import com.spark.newbitrade.base.BaseContract;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface SkipPayContract {
    interface View extends BaseContract.BaseView {
        void walletWithdrawSuccess(String response);

        void getCoinMessageSuccess(MemberWalletVo obj);
    }

    interface Presenter extends BaseContract.BasePresenter {
        void walletWithdraw(String address, BigDecimal amount, String coinName, String tradePassword);

        void getCoinMessage(String coinName);
    }


}
