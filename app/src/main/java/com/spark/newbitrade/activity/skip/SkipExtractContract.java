package com.spark.newbitrade.activity.skip;


import com.spark.library.ac.model.MemberWallet;
import com.spark.newbitrade.base.BaseContract;
import com.spark.newbitrade.entity.CasLoginEntity;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface SkipExtractContract {
    interface View extends BaseContract.BaseView {

        void getAddressSuccess(MemberWallet obj);

        void checkBusinessLoginSuccess(CasLoginEntity casLoginEntity);

        void doLoginBusinessSuccess(String type);
    }

    interface Presenter extends BaseContract.BasePresenter {

        void getAddress(String coinName);

        void checkBusinessLogin(String type);

        void doLoginBusiness(String tgc, String type);
    }


}
