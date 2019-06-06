package com.spark.newbitrade.activity.main.presenter;

import com.android.volley.VolleyError;
import com.spark.library.otc.model.PageAdvertiseShowVo;
import com.spark.library.otc.model.QueryParamAdvertiseShowVo;
import com.spark.library.otcSys.model.MessageResult;
import com.spark.library.otcSys.model.TradeDto;
import com.spark.newbitrade.activity.main.C2CListContract;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.model.otc.AdvertiseScanControllerModel;
import com.spark.newbitrade.model.otc_system.TradeControllerModel;

/**
 * Created by Administrator on 2018/2/28.
 */

public class C2CListPresenterImpl implements C2CListContract.C2CListPresenter {
    private C2CListContract.C2CListView view;
    private AdvertiseScanControllerModel advertiseScanControllerModel;
    private TradeControllerModel tradeControllerModel;

    public C2CListPresenterImpl(C2CListContract.C2CListView view) {
        this.view = view;
        this.advertiseScanControllerModel = new AdvertiseScanControllerModel();
        this.tradeControllerModel = new TradeControllerModel();
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
    public void findPageForCoin(QueryParamAdvertiseShowVo queryParam) {
        advertiseScanControllerModel.findPageForCoin(queryParam, new ResponseCallBack.SuccessListener<PageAdvertiseShowVo>() {
            @Override
            public void onResponse(PageAdvertiseShowVo response) {
                if (view != null) {
                    view.findPageForCoinSuccess(response);
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
    public void createOrder(TradeDto tradeDto) {
        tradeControllerModel.createOrder(tradeDto, new ResponseCallBack.SuccessListener<MessageResult>() {
            @Override
            public void onResponse(MessageResult response) {
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


}
