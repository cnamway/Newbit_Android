package com.spark.newbitrade.activity.ads;

import com.android.volley.VolleyError;
import com.spark.library.otc.model.QueryParamAdvertiseVo;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.Ads;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.model.otc.AdvertiseSelfControllerModel;

import java.util.List;

/**
 * Created by Administrator on 2019/3/2 0002.
 */

public class AdsPresenterImpl implements AdsContract.Presenter {
    private AdsContract.View view;
    private AdvertiseSelfControllerModel advertiseSelfControllerModel;

    public AdsPresenterImpl(AdsContract.View view) {
        this.view = view;
        advertiseSelfControllerModel = new AdvertiseSelfControllerModel();
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
    public void findMyAdvertise(Integer tradeType) {
        advertiseSelfControllerModel.findMyAdvertiseUsingGET(tradeType, new ResponseCallBack.SuccessListener<List<Ads>>() {
            @Override
            public void onResponse(List<Ads> response) {
                hideLoading();
                if (view != null)
                    view.findMyAdvertiseSuccess(response);
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
    public void findMyAdvertiseAchive(QueryParamAdvertiseVo queryParam) {
        advertiseSelfControllerModel.findMyAdvertiseAchiveUsingPOST(queryParam,
                new ResponseCallBack.SuccessListener<List<Ads>>() {
                    @Override
                    public void onResponse(List<Ads> response) {
                        hideLoading();
                        if (view != null)
                            view.findMyAdvertiseAchiveSuccess(response);
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
    public void onShelvesAdvertise(Long advertiseId, String tradePwd) {
        advertiseSelfControllerModel.onShelvesAdvertise(advertiseId, tradePwd,
                new ResponseCallBack.SuccessListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoading();
                        if (view != null)
                            view.onShelvesAdvertiseSuccess(response);
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
    public void offShelvesAdvertise(Long advertiseId) {
        advertiseSelfControllerModel.offShelvesAdvertise(advertiseId,
                new ResponseCallBack.SuccessListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoading();
                        if (view != null)
                            view.offShelvesAdvertiseSuccess(response);
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
    public void deleteAdvertise(Long advertiseId) {
        advertiseSelfControllerModel.deleteAdvertise(advertiseId,
                new ResponseCallBack.SuccessListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoading();
                        if (view != null)
                            view.deleteAdvertiseSuccess(response);
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
