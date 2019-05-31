package com.spark.newbitrade.activity.country;


import com.android.volley.VolleyError;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.Country;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.model.otc.AdvertiseScanControllerModel;

import java.util.List;


/**
 * Created by Administrator on 2017/9/25.
 */

public class CountryPresenterImpl implements CountryContract.Presenter {
    private CountryContract.View view;
    private AdvertiseScanControllerModel advertiseScanControllerModel;

    public CountryPresenterImpl(CountryContract.View view) {
        this.view = view;
        advertiseScanControllerModel = new AdvertiseScanControllerModel();
    }

    @Override
    public void country() {
        showLoading();
        advertiseScanControllerModel.listOtcTradeAreaUsingGET(new ResponseCallBack.SuccessListener<List<Country>>() {
            @Override
            public void onResponse(List<Country> response) {
                hideLoading();
                if (view != null)
                    view.countrySuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (view != null)
                    view.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (view != null)
                    view.dealError(volleyError);
            }
        });
    }

    @Override
    public void showLoading() {
        if (view != null)
            view.showLoading();
    }

    @Override
    public void hideLoading() {
        if (view != null)
            view.hideLoading();
    }

    @Override
    public void destory() {
        view = null;
    }
}
