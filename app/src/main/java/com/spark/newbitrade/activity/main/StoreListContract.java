package com.spark.newbitrade.activity.main;

import com.spark.library.otc.model.MessageResultAuthMerchantFrontVo;
import com.spark.newbitrade.base.BaseContract;
import com.spark.newbitrade.entity.Ads;
import com.spark.newbitrade.entity.PayWaySetting;
import com.spark.library.otc.model.AdvertiseDto;
import com.spark.library.otc.model.AuthMerchantApplyMarginType;
import com.spark.library.otc.model.MessageResult;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface StoreListContract {

    interface View extends BaseContract.BaseView {

        void priceFindSuccess(MessageResult obj);

        void findAuthMerchantStatusSuccess(MessageResultAuthMerchantFrontVo response);
    }

    interface Presenter extends BaseContract.BasePresenter {

        void priceFind(String coinName, String currency);

        void findAuthMerchantStatus();
    }

}
