package com.spark.newbitrade.model.otc;

import com.google.gson.Gson;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.data.SendRemoteDataUtil;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.factory.HttpUrls;
import com.spark.newbitrade.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * APP版本更新信息
 */

public class AppVersionModel {
    /**
     * 获取APP版本更新信息
     */
    public void getAppVersion(final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        HashMap<String, String> map = new HashMap<>();
        SendRemoteDataUtil.getInstance().doStringGet(HttpUrls.getAppVersionUsingGet(), map, new SendRemoteDataUtil.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    int code = StringUtils.getCode(object);
                    if (code == 200 || code == 404 || code == 500) {
                        if (successListener != null)
                            successListener.onResponse(response);
                    } else {
                        if (errorListener != null)
                            errorListener.onErrorResponse(new HttpErrorEntity(object));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (errorListener != null)
                        errorListener.onErrorResponse(new HttpErrorEntity(-9999, "", "", ""));
                }
            }

            @Override
            public void onDataNotAvailable(HttpErrorEntity httpErrorEntity) {
                if (httpErrorEntity != null) {
                    if (httpErrorEntity.getCode() == 200 || httpErrorEntity.getCode() == 404 || httpErrorEntity.getCode() == 500) {
                        if (successListener != null) {
                            String response = new Gson().toJson(httpErrorEntity);
                            successListener.onResponse(response);
                        }
                    }
                } else {
                    if (errorListener != null)
                        errorListener.onErrorResponse(httpErrorEntity);
                }

            }
        });
    }

}