package com.spark.newbitrade.activity.releaseAd;

import com.spark.library.otc.model.AdvertiseDto;
import com.spark.library.otc.model.AuthMerchantApplyMarginType;
import com.spark.newbitrade.base.BaseContract;
import com.spark.newbitrade.entity.Ads;
import com.spark.newbitrade.entity.PayWaySetting;
import com.spark.library.otc.model.MessageResult;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface ReleaseAdContract {

    interface View extends BaseContract.BaseView {

        void createAdvertiseSuccess(String obj);

        void updateAdvertiseSuccess(String obj);

        void listMerchantAdvertiseCoinSuccess(List<AuthMerchantApplyMarginType> obj);

        void queryPayWayListSuccess(List<PayWaySetting> obj);

        void findAdvertiseDetailSuccess(Ads obj);

        void priceFindSuccess(MessageResult obj);
    }

    interface Presenter extends BaseContract.BasePresenter {
        void createAdvertise(AdvertiseDto advertiseDto);

        void updateAdvertise(AdvertiseDto advertiseDto, Long advertiseId);

        void listMerchantAdvertiseCoin();

        void queryPayWayList();

        void findAdvertiseDetail(Long advertiseId);

        void priceFind(String coinName, String currency);
    }


}
