package com.spark.newbitrade.activity.main.presenter;

import com.android.volley.VolleyError;
import com.spark.library.otc.model.Coin;
import com.spark.newbitrade.activity.main.C2CContract;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.Country;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.model.otc.AdvertiseScanControllerModel;

import java.util.List;

/**
 * Created by Administrator on 2018/2/24.
 */

public class C2CPresenterImpl implements C2CContract.C2CPresenter {
    private C2CContract.C2CView view;
    private AdvertiseScanControllerModel advertiseScanControllerModel;

    public C2CPresenterImpl(C2CContract.C2CView view) {
        this.view = view;
        this.advertiseScanControllerModel = new AdvertiseScanControllerModel();
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

    @Override
    public void listOtcTradeCoin() {
        showLoading();
        advertiseScanControllerModel.listOtcTradeCoin(new ResponseCallBack.SuccessListener<List<Coin>>() {
            @Override
            public void onResponse(List<Coin> response) {
                hideLoading();
                if (view != null) {
                    view.listOtcTradeCoinSuccess(response);
                }
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


}
