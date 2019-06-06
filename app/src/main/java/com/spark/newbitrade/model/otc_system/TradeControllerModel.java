package com.spark.newbitrade.model.otc_system;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.spark.library.otcSys.api.TradeControllerApi;
import com.spark.library.otcSys.model.MessageResult;
import com.spark.library.otcSys.model.TradeDto;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.factory.HttpUrls;
import com.spark.newbitrade.utils.LogUtils;


import static com.spark.newbitrade.utils.GlobalConstant.SUCCESS_CODE;

/**
 * otc-system业务模块
 */

public class TradeControllerModel {
    private TradeControllerApi tradeControllerApi;

    public TradeControllerModel() {
        tradeControllerApi = new TradeControllerApi();
        tradeControllerApi.setBasePath(HttpUrls.OTC_SYSTEM_HOST);
    }

    /**
     * 下单接口
     */
    public void createOrder(final TradeDto tradeDto, final ResponseCallBack.SuccessListener<MessageResult> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                tradeControllerApi.otcTradeUsingPOST(tradeDto, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null)
                                successListener.onResponse(response);
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
