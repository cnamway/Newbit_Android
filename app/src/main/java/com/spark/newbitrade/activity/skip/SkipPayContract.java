package com.spark.newbitrade.activity.skip;


import com.spark.library.ac.model.MemberWalletVo;
import com.spark.newbitrade.base.BaseContract;
import com.spark.newbitrade.entity.CasLoginEntity;
import com.spark.newbitrade.entity.ExtractInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface SkipPayContract {
    interface View extends BaseContract.BaseView {
        void walletWithdrawSuccess(String response);

        void getCoinMessageSuccess(MemberWalletVo obj);

        void getPhoneCodeSuccess(String obj);

        void getExtractInfoSuccess(List<ExtractInfo> list);

        void checkBusinessLoginSuccess(CasLoginEntity casLoginEntity);

        void doLoginBusinessSuccess(String type);
    }

    interface Presenter extends BaseContract.BasePresenter {
        void walletWithdraw(String address, BigDecimal amount, String coinName, String tradePassword, String code, String phone, String orderNo);

        void getCoinMessage(String coinName);

        void getPhoneCode(String phone);

        void getExtractInfo(String coinName);

        void checkBusinessLogin(String type);

        void doLoginBusiness(String tgc, String type);
    }

}
