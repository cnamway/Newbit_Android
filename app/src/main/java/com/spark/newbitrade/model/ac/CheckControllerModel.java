package com.spark.newbitrade.model.ac;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.spark.library.ac.api.CheckControllerApi;
import com.spark.library.ac.model.MessageResult;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.factory.HttpUrls;

import static com.spark.newbitrade.utils.GlobalConstant.SUCCESS_CODE;


/**
 * 资产
 */

public class CheckControllerModel {
    private CheckControllerApi checkControllerApi;

    public CheckControllerModel() {
        checkControllerApi = new CheckControllerApi();
        checkControllerApi.setBasePath(HttpUrls.AC_HOST);
    }

    /**
     * 用户选择地址后校验提币地址是否内部地址
     */
    public void checkAddress(final String address, final ResponseCallBack.SuccessListener<MessageResult> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                checkControllerApi.checkAddressUsingGET(address, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
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
