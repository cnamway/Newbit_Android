package com.spark.newbitrade.activity.country;


import com.spark.newbitrade.base.BaseContract;
import com.spark.newbitrade.entity.Country;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface CountryContract {

    interface View extends BaseContract.BaseView {

        void countrySuccess(List<Country> obj);
    }

    interface Presenter extends BaseContract.BasePresenter {

        void country();
    }


}
