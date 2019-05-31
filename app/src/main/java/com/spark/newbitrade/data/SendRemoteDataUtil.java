package com.spark.newbitrade.data;


import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.SharedPreferenceInstance;
import com.spark.newbitrade.utils.okhttp.AppUtils;
import com.spark.newbitrade.utils.okhttp.OkhttpUtils;
import com.spark.newbitrade.utils.okhttp.StringCallback;

import java.util.HashMap;

import okhttp3.Request;

/**
 * 发送数据
 */
public class SendRemoteDataUtil {
    private static SendRemoteDataUtil INSTANCE;
    private DataCallback dataCallback;
    private FileDownloadDataCallback downloadDataCallback;

    /**
     * 普通的数据请求回调
     */
    public interface DataCallback {
        void onDataLoaded(Object obj);

        void onDataNotAvailable(HttpErrorEntity httpErrorEntity);
    }

    /**
     * 下载文件回调
     */
    public interface FileDownloadDataCallback extends DataCallback {
        void intProgress(float progress);
    }

    public static SendRemoteDataUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SendRemoteDataUtil();
        }
        return INSTANCE;
    }

    /**
     * 有参数的post请求
     */
    public void doStringPost(String url, HashMap<String, String> params, DataCallback callback) {
        this.dataCallback = callback;
        String tgt = SharedPreferenceInstance.getInstance().getTgt();
        OkhttpUtils.post().url(url).addHeader("tgt", tgt).addHeader("tid", AppUtils.getSerialNumber()).addParams(params).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, HttpErrorEntity httpErrorEntity) { // 获得response响应
                dataCallback.onDataNotAvailable(httpErrorEntity);
            }

            @Override
            public void onResponse(String response) {
                LogUtils.i("onResponse：" + response.toString());
                dataCallback.onDataLoaded(response);
            }

        });
        String strParams = url;
        if (params != null) {
            strParams = strParams + "?";
            for (String key : params.keySet()) {
                strParams = strParams + key + "=" + params.get(key) + "&";
            }
        }
        LogUtils.i("传参==" + strParams);
    }

    /**
     * get请求
     */
    public void doStringGet(String url, HashMap<String, String> params, final DataCallback dataCallback) {
        OkhttpUtils.get().url(url).addHeader("tid", AppUtils.getSerialNumber()).addParams(params).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, HttpErrorEntity httpErrorEntity) {
                dataCallback.onDataNotAvailable(httpErrorEntity);
            }

            @Override
            public void onResponse(String response) {
                LogUtils.i("onResponse：" + response.toString());
                dataCallback.onDataLoaded(response);
            }
        });
        String strParams = url;
        if (params != null) {
            strParams = url + "?";
            for (String key : params.keySet()) {
                strParams = strParams + key + "=" + params.get(key) + "&";
            }
        }
        LogUtils.i("传参==" + strParams);
    }

    /**
     * get请求
     *
     * @param params
     * @param dataCallback
     */
    public void doStringGetWithHead(String url, HashMap<String, String> params, final DataCallback dataCallback) {
        this.dataCallback = dataCallback;
        String tgt = SharedPreferenceInstance.getInstance().getTgt();
        OkhttpUtils.get().url(url).addHeader("tgt", tgt).addHeader("Connection", "close").addParams(params).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, HttpErrorEntity httpErrorEntity) { // 获得response响应
                dataCallback.onDataNotAvailable(httpErrorEntity);
                if (httpErrorEntity != null)
                    LogUtils.i("request：" + request.toString() + "error:" + httpErrorEntity.getCode());

            }

            @Override
            public void onResponse(String response) {
                LogUtils.i("onResponse：" + response.toString());
                dataCallback.onDataLoaded(response);
            }

        });
        String strParams = url;
        if (params != null) {
            strParams = strParams + "?";
            for (String key : params.keySet()) {
                strParams = strParams + key + "=" + params.get(key) + "&";
            }
        }
        LogUtils.i("传参==" + strParams);
    }

    /**
     * get请求
     *
     * @param params
     * @param dataCallback
     */
    public void doStringGetWithHeadAndCheck(String url, String code, HashMap<String, String> params, final DataCallback dataCallback) {
        this.dataCallback = dataCallback;
        String tgt = SharedPreferenceInstance.getInstance().getTgt();
        OkhttpUtils.get().url(url).addHeader("tgt", tgt).addHeader("check", "phone::" + code).
                addParams(params).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, HttpErrorEntity httpErrorEntity) { // 获得response响应
                dataCallback.onDataNotAvailable(httpErrorEntity);
            }

            @Override
            public void onResponse(String response) {
                LogUtils.i("onResponse：" + response.toString());
                dataCallback.onDataLoaded(response);
            }

        });
        String strParams = url;
        if (params != null) {
            strParams = strParams + "?";
            for (String key : params.keySet()) {
                strParams = strParams + key + "=" + params.get(key) + "&";
            }
        }
        LogUtils.i("传参==" + strParams);
    }

    /**
     * delete请求
     */
    public void doStringDelete(String url, HashMap<String, String> params, final DataCallback dataCallback) {
        this.dataCallback = dataCallback;
        String tgt = SharedPreferenceInstance.getInstance().getTgt();
        OkhttpUtils.delete().url(url).addHeader("tgt", tgt).addParams(params).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, HttpErrorEntity httpErrorEntity) {
                dataCallback.onDataNotAvailable(httpErrorEntity);
            }

            @Override
            public void onResponse(String response) {
                LogUtils.i("onResponse：" + response.toString());
                dataCallback.onDataLoaded(response);
            }
        });
    }


}
