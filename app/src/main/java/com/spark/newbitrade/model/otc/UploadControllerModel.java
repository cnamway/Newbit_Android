package com.spark.newbitrade.model.otc;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.spark.library.otc.api.UploadControllerApi;
import com.spark.library.otc.model.MessageResult;
import com.spark.library.otc.model.OssUploadEntity;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.factory.HttpUrls;
import com.spark.newbitrade.utils.LogUtils;

/**
 * otc业务模块
 */

public class UploadControllerModel {
    private UploadControllerApi uploadControllerApi;

    public UploadControllerModel() {
        uploadControllerApi = new UploadControllerApi();
        uploadControllerApi.setBasePath(HttpUrls.UC_HOST);
    }

    /**
     * 上传base64处理后的图片
     */
    public void base64UpLoadUsingPOST(final String base64, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        final OssUploadEntity ossUploadEntity = new OssUploadEntity();
        ossUploadEntity.setBase64Data(base64);
        ossUploadEntity.setOss(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                uploadControllerApi.base64UpLoadUsingPOST(ossUploadEntity, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response.toString());
                        if (successListener != null)
                            successListener.onResponse((String) response.getData());

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
