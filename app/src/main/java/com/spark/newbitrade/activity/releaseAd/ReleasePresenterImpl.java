package com.spark.newbitrade.activity.releaseAd;

import com.android.volley.VolleyError;
import com.spark.newbitrade.model.otc.PriceControllerModel;
import com.spark.library.otc.model.AdvertiseDto;
import com.spark.library.otc.model.AuthMerchantApplyMarginType;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.Ads;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.entity.PayWaySetting;
import com.spark.newbitrade.model.otc.AdvertiseSelfControllerModel;
import com.spark.newbitrade.model.otc.PayControllerModel;
import com.spark.library.otc.model.MessageResult;

import java.util.List;

/**
 * Created by Administrator on 2019/3/13 0013.
 */

public class ReleasePresenterImpl implements ReleaseAdContract.Presenter {

    private ReleaseAdContract.View view;
    private AdvertiseSelfControllerModel advertiseSelfControllerModel;
    private PayControllerModel payControllerModel;
    private PriceControllerModel priceControllerModel;

    public ReleasePresenterImpl(ReleaseAdContract.View view) {
        this.view = view;
        this.advertiseSelfControllerModel = new AdvertiseSelfControllerModel();
        this.payControllerModel = new PayControllerModel();
        this.priceControllerModel = new PriceControllerModel();
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
    public void createAdvertise(AdvertiseDto advertiseDto) {
        showLoading();
        advertiseSelfControllerModel.createAdvertise(advertiseDto, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (view != null) {
                    view.createAdvertiseSuccess(response);
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
    public void updateAdvertise(AdvertiseDto advertiseDto, Long advertiseId) {
        advertiseSelfControllerModel.updateAdvertise(advertiseDto, advertiseId,
                new ResponseCallBack.SuccessListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoading();
                        if (view != null)
                            view.updateAdvertiseSuccess(response);
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
    public void listMerchantAdvertiseCoin() {
        showLoading();
        advertiseSelfControllerModel.listMerchantAdvertiseCoin(new ResponseCallBack.SuccessListener<List<AuthMerchantApplyMarginType>>() {
            @Override
            public void onResponse(List<AuthMerchantApplyMarginType> response) {
                hideLoading();
                if (view != null) {
                    view.listMerchantAdvertiseCoinSuccess(response);
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
    public void queryPayWayList() {
        showLoading();
        payControllerModel.queryListUsingGET(new ResponseCallBack.SuccessListener<List<PayWaySetting>>() {
            @Override
            public void onResponse(List<PayWaySetting> response) {
                hideLoading();
                if (view != null) {
                    view.queryPayWayListSuccess(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (view != null) {
                    view.dealError(httpErrorEntity);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (view != null) {
                    view.dealError(volleyError);
                }
            }
        });
    }

    @Override
    public void findAdvertiseDetail(Long advertiseId) {
        advertiseSelfControllerModel.findAdvertiseDetail(advertiseId,
                new ResponseCallBack.SuccessListener<Ads>() {
                    @Override
                    public void onResponse(Ads response) {
                        hideLoading();
                        if (view != null)
                            view.findAdvertiseDetailSuccess(response);
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
}
