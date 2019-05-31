package com.spark.newbitrade.activity.buy_or_sell;

import com.android.volley.VolleyError;
import com.spark.library.otc.model.AuthMerchantFrontVo;
import com.spark.library.otc.model.OrderInTransitDto;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.model.otc.AdvertiseScanControllerModel;
import com.spark.newbitrade.model.otc.TradeControllerModel;

/**
 * Created by Administrator on 2018/2/28.
 */

public class C2CBuyOrSellPresenterImpl implements C2CBuyOrSellContract.Presenter {
    private C2CBuyOrSellContract.View view;
    private TradeControllerModel tradeControllerModel;
    private AdvertiseScanControllerModel advertiseScanControllerModel;

    public C2CBuyOrSellPresenterImpl(C2CBuyOrSellContract.View view) {
        this.view = view;
        this.tradeControllerModel = new TradeControllerModel();
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
    public void createOrder(OrderInTransitDto orderInTransitDto) {
        tradeControllerModel.createOrder(orderInTransitDto, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                if (view != null) {
                    view.createOrderSuccess(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                if (view != null)
                    view.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (view != null)
                    view.dealError(volleyError);
            }
        });
    }

    @Override
    public void getAvgTime(Long memberId) {
        advertiseScanControllerModel.findAuthMerchant(memberId, new ResponseCallBack.SuccessListener<AuthMerchantFrontVo>() {
            @Override
            public void onResponse(AuthMerchantFrontVo response) {
                if (view != null) {
                    view.getAvgTimeSuccess(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                if (view != null)
                    view.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (view != null)
                    view.dealError(volleyError);
            }
        });
    }



}
