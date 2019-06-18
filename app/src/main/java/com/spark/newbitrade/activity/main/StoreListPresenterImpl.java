package com.spark.newbitrade.activity.main;

import com.android.volley.VolleyError;
import com.spark.library.otc.model.MessageResultAuthMerchantFrontVo;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.model.otc.AuthMerchantControllerModel;
import com.spark.newbitrade.model.otc.PriceControllerModel;
import com.spark.library.otc.model.MessageResult;

/**
 * Created by Administrator on 2019/3/13 0013.
 */

public class StoreListPresenterImpl implements StoreListContract.Presenter {

    private StoreListContract.View view;
    private PriceControllerModel priceControllerModel;
    private AuthMerchantControllerModel authMerchantControllerModel;

    public StoreListPresenterImpl(StoreListContract.View view) {
        this.view = view;
        this.priceControllerModel = new PriceControllerModel();
        this.authMerchantControllerModel = new AuthMerchantControllerModel();
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
    public void priceFind(String coinName, String currency) {
        priceControllerModel.priceFind(coinName, currency,
                new ResponseCallBack.SuccessListener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        hideLoading();
                        if (view != null)
                            view.priceFindSuccess(response);
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
    public void findAuthMerchantStatus() {
        showLoading();
        authMerchantControllerModel.findAuthMerchantStatus(new ResponseCallBack.SuccessListener<MessageResultAuthMerchantFrontVo>() {
            @Override
            public void onResponse(MessageResultAuthMerchantFrontVo response) {
                hideLoading();
                if (view != null) {
                    view.findAuthMerchantStatusSuccess(response);
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
}
