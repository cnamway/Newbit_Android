package com.spark.newbitrade.activity.ads;


import com.spark.library.otc.model.QueryParamAdvertiseVo;
import com.spark.newbitrade.base.BaseContract;
import com.spark.newbitrade.entity.Ads;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface AdsContract {

    interface View extends BaseContract.BaseView {

        void findMyAdvertiseSuccess(List<Ads> obj);

        void findMyAdvertiseAchiveSuccess(List<Ads> obj);

        void onShelvesAdvertiseSuccess(String obj);

        void offShelvesAdvertiseSuccess(String obj);

        void deleteAdvertiseSuccess(String obj);
    }

    interface Presenter extends BaseContract.BasePresenter {

        void findMyAdvertise(Integer tradeType);

        void findMyAdvertiseAchive(QueryParamAdvertiseVo queryParam);

        void onShelvesAdvertise(Long advertiseId, String tradePwd);

        void offShelvesAdvertise(Long advertiseId);

        void deleteAdvertise(Long advertiseId);
    }


}
