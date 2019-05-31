package com.spark.newbitrade.model.otc;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.factory.HttpUrls;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.library.otc.api.PriceControllerApi;
import com.spark.library.otc.model.MessageResult;


import static com.spark.newbitrade.utils.GlobalConstant.SUCCESS_CODE;

/**
 * Created by Administrator on 2019/3/15 0015.
 */

public class PriceControllerModel {
    private PriceControllerApi priceControllerApi;

    public PriceControllerModel() {
        this.priceControllerApi = new PriceControllerApi();
        this.priceControllerApi.setBasePath(HttpUrls.OTC_HOST);
    }

    /**
     * 价格获取接口
     */
    public void priceFind(final String coinName, final String currency, final ResponseCallBack.SuccessListener<MessageResult> listener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                priceControllerApi.getOtcPriceUsingGET(coinName, currency, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (listener != null) {
                                listener.onResponse(response);
                            }
                        } else {
                            if (errorListener != null) {
                                errorListener.onErrorResponse(new HttpErrorEntity(response.getCode(), response.getMessage(), response.getUrl(), response.getCid()));
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (errorListener != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                });
            }
        }).start();
    }


}
