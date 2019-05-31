package com.spark.newbitrade.model.otc;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.spark.library.otc.api.AppealControllerApi;
import com.spark.library.otc.model.AppealApplyInTransitDto;
import com.spark.library.otc.model.MessageResult;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.factory.HttpUrls;

import static com.spark.newbitrade.utils.GlobalConstant.SUCCESS_CODE;

/**
 * otc业务模块
 */

public class AppealControllerModel {
    private AppealControllerApi appealControllerApi;

    public AppealControllerModel() {
        appealControllerApi = new AppealControllerApi();
        appealControllerApi.setBasePath(HttpUrls.OTC_HOST);
    }

    /**
     * 订单申诉申请
     */
    public void appealApplyUsingPOST(final AppealApplyInTransitDto appealApplyInTransitDto, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                appealControllerApi.appealApplyUsingPOST(appealApplyInTransitDto, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null)
                                successListener.onResponse(response.getMessage());
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
