package com.spark.newbitrade.model.cms;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.spark.library.cms.api.WebConfigControllerApi;
import com.spark.library.cms.api.WebOtherContentControllerApi;
import com.spark.library.cms.model.MessageResultPageWebOtherContent;
import com.spark.library.cms.model.MessageResultWebConfigVo;
import com.spark.library.cms.model.QueryParamWebOtherContent;
import com.spark.library.cms.model.WebOtherContent;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.factory.HttpUrls;
import com.spark.newbitrade.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * cms业务模块
 */

public class WebOtherContentControllerModel {
    private WebOtherContentControllerApi webOtherContentControllerApi;

    public WebOtherContentControllerModel() {
        webOtherContentControllerApi = new WebOtherContentControllerApi();
        webOtherContentControllerApi.setBasePath(HttpUrls.CMS_HOST);
    }

    /**
     * 定制内容
     */
    public void webOtherContentQuery(HashMap<String, String> params, final ResponseCallBack.SuccessListener<List<WebOtherContent>> successListener, final ResponseCallBack.ErrorListener errorListener) {
        final QueryParamWebOtherContent queryParam = new QueryParamWebOtherContent();
        queryParam.setPageIndex(Integer.parseInt(params.get("pageNo")));
        queryParam.setPageSize(Integer.parseInt(params.get("pageSize")));
        queryParam.setSortFields("createTime_d");
        new Thread(new Runnable() {
            @Override
            public void run() {
                webOtherContentControllerApi.webConfigDetailUsingPOST(queryParam, new Response.Listener<MessageResultPageWebOtherContent>() {
                    @Override
                    public void onResponse(MessageResultPageWebOtherContent response) {
                        LogUtils.i("response==" + response.toString());
                        if (successListener != null)
                            successListener.onResponse(response.getData().getRecords());
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
