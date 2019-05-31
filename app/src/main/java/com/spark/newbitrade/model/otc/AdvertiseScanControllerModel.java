package com.spark.newbitrade.model.otc;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.library.otc.api.AdvertiseScanControllerApi;
import com.spark.library.otc.model.AuthMerchantFrontVo;
import com.spark.library.otc.model.Coin;
import com.spark.library.otc.model.MessageResultAuthMerchantFrontVo;
import com.spark.library.otc.model.MessageResultListCoin;
import com.spark.library.otc.model.MessageResultListCountry;
import com.spark.library.otc.model.MessageResultPageAdvertiseShowVo;
import com.spark.library.otc.model.PageAdvertiseShowVo;
import com.spark.library.otc.model.QueryParamAdvertiseShowVo;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.Country;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.factory.HttpUrls;
import com.spark.newbitrade.utils.LogUtils;

import java.util.List;

import static com.spark.newbitrade.utils.GlobalConstant.SUCCESS_CODE;

/**
 * otc业务模块
 */

public class AdvertiseScanControllerModel {
    private AdvertiseScanControllerApi advertiseScanControllerApi;

    public AdvertiseScanControllerModel() {
        advertiseScanControllerApi = new AdvertiseScanControllerApi();
        advertiseScanControllerApi.setBasePath(HttpUrls.OTC_HOST);
    }

    /**
     * 查询所有的币种
     */
    public void listOtcTradeCoin(final ResponseCallBack.SuccessListener<List<Coin>> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                advertiseScanControllerApi.listOtcTradeCoinUsingGET(new Response.Listener<MessageResultListCoin>() {
                    @Override
                    public void onResponse(MessageResultListCoin response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            List<Coin> coinList = response.getData();
                            if (successListener != null)
                                successListener.onResponse(coinList);
                        } else {
                            if (errorListener != null)
                                errorListener.onErrorResponse(new HttpErrorEntity(response.getCode(), response.getMessage(), response.getUrl(), response.getCid()));
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (errorListener != null)
                            errorListener.onErrorResponse(error);
                    }
                });
            }
        }).start();
    }

    /**
     * 按指定币种查询上架的广告
     */
    public void findPageForCoin(final QueryParamAdvertiseShowVo queryParam, final ResponseCallBack.SuccessListener<PageAdvertiseShowVo> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                advertiseScanControllerApi.findPageForCoinUsingPOST(queryParam, new Response.Listener<MessageResultPageAdvertiseShowVo>() {
                    @Override
                    public void onResponse(MessageResultPageAdvertiseShowVo response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            PageAdvertiseShowVo pageAdvertiseShowVo = response.getData();
                            if (successListener != null)
                                successListener.onResponse(pageAdvertiseShowVo);
                        } else {
                            if (errorListener != null)
                                errorListener.onErrorResponse(new HttpErrorEntity(response.getCode(), response.getMessage(), response.getUrl(), response.getCid()));
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (errorListener != null)
                            errorListener.onErrorResponse(error);
                    }
                });
            }
        }).start();
    }

    /**
     * 认证商家信息查询
     */
    public void findAuthMerchant(final Long memberId, final ResponseCallBack.SuccessListener<AuthMerchantFrontVo> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                advertiseScanControllerApi.findAuthMerchantUsingGET(memberId, new Response.Listener<MessageResultAuthMerchantFrontVo>() {
                    @Override
                    public void onResponse(MessageResultAuthMerchantFrontVo response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            Gson gson = new Gson();
                            AuthMerchantFrontVo authMerchantFrontVo = response.getData();
                            if (successListener != null)
                                successListener.onResponse(authMerchantFrontVo);
                        } else {
                            if (errorListener != null)
                                errorListener.onErrorResponse(new HttpErrorEntity(response.getCode(), response.getMessage(), response.getUrl(), response.getCid()));
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (errorListener != null)
                            errorListener.onErrorResponse(error);
                    }
                });
            }
        }).start();
    }

    /**
     * 查询所有的交易区
     */
    public void listOtcTradeAreaUsingGET(final ResponseCallBack.SuccessListener<List<Country>> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                advertiseScanControllerApi.listOtcTradeAreaUsingGET(new Response.Listener<MessageResultListCountry>() {
                    @Override
                    public void onResponse(MessageResultListCountry response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            Gson gson = new Gson();
                            List<Country> countryList = gson.fromJson(gson.toJson(response.getData()), new TypeToken<List<Country>>() {
                            }.getType());
                            if (successListener != null)
                                successListener.onResponse(countryList);
                        } else {
                            if (errorListener != null)
                                errorListener.onErrorResponse(new HttpErrorEntity(response.getCode(), response.getMessage(), response.getUrl(), response.getCid()));
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (errorListener != null)
                            errorListener.onErrorResponse(error);
                    }
                });
            }
        }).start();
    }


}
