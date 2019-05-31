package com.spark.newbitrade.model.cms;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.spark.library.cms.api.WebConfigControllerApi;
import com.spark.library.cms.model.MessageResultWebConfigVo;
import com.spark.library.cms.model.WebOtherContent;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.factory.HttpUrls;
import com.spark.newbitrade.utils.LogUtils;

/**
 * cms业务模块
 */

public class WebConfigControllerModel {
    private WebConfigControllerApi webConfigControllerApi;

    public WebConfigControllerModel() {
        webConfigControllerApi = new WebConfigControllerApi();
        webConfigControllerApi.setBasePath(HttpUrls.CMS_HOST);
    }

    /**
     * 站点配置信息
     */
    public void webConfigQuery(final ResponseCallBack.SuccessListener<MessageResultWebConfigVo> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                webConfigControllerApi.webConfigDetailUsingGET( new Response.Listener<MessageResultWebConfigVo>() {
                    @Override
                    public void onResponse(MessageResultWebConfigVo response) {
                        LogUtils.i("response==" + response.toString());
                        if (successListener != null)
                            successListener.onResponse(response);

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
