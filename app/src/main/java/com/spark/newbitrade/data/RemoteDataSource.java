package com.spark.newbitrade.data;


import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.factory.UrlFactory;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.okhttp.OkhttpUtils;
import com.spark.newbitrade.utils.okhttp.StringCallback;

import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/9/25.
 */
public class RemoteDataSource implements DataSource {
    private static RemoteDataSource INSTANCE;

    public static RemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource();
        }
        return INSTANCE;
    }

    private RemoteDataSource() {

    }

    /**
     * 有参数的post请求
     *
     * @param url
     * @param params
     * @param dataCallback
     */
    @Override
    public void doStringPost(final String url, HashMap<String, String> params, final DataCallback dataCallback) {
        String strCookie = "";
        OkhttpUtils.post().url(url).addParams(params).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, HttpErrorEntity httpErrorEntity) {
//                dataCallback.onDataNotAvailable(httpErrorEntity);
            }

            @Override
            public void onResponse(String response) {
                LogUtils.i("onResponse：" + response.toString());
                dataCallback.onDataLoaded(response);
            }
        });
        if (params != null) {
            LogUtils.i("head   access-auth-token===" + strCookie);
            String strParams = "";
            for (String key : params.keySet()) {
                strParams = strParams + key + "=" + params.get(key) + "&";
            }
        }
    }

    /**
     * 无参数的post请求
     *
     * @param url
     * @param dataCallback
     */
    @Override
    public void doStringPost(final String url, final DataCallback dataCallback) {
        String strCookie = "";
        if (url.equals(UrlFactory.getNewVision())) {
            OkhttpUtils.post().url(url).build().execute(new StringCallback() {
                @Override
                public void onError(Request request, HttpErrorEntity httpErrorEntity) {
//                    dataCallback.onDataNotAvailable(httpErrorEntity);
                }

                @Override
                public void onResponse(String response) {
                    LogUtils.i("onResponse：" + response.toString());
                    dataCallback.onDataLoaded(response);
                }
            });
        } else {
            OkhttpUtils.post().url(url).build().execute(new StringCallback() {
                @Override
                public void onError(Request request, HttpErrorEntity httpErrorEntity) {
//                    dataCallback.onDataNotAvailable(httpErrorEntity);
                }

                @Override
                public void onResponse(String response) {
                    LogUtils.i("onResponse：" + response.toString());
                    dataCallback.onDataLoaded(response);
                }
            });
        }
    }


    /**
     * 传递json
     */
    @Override
    public void doStringPost(String url, String json, final DataCallback dataCallback) {
        LogUtils.i("请求链接==" + url + "?" + json);
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json"), json);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (e.getClass().equals(SocketTimeoutException.class)) {
//                    dataCallback.onDataNotAvailable(GlobalConstant.TIME_OUT, null);
                } else {
                    dataCallback.onDataNotAvailable(GlobalConstant.SERVER_ERROR, null);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseStr = response.body().string();
                LogUtils.i("返回数据===" + responseStr);
                dataCallback.onDataLoaded(responseStr);
            }
        });
    }


    /**
     * get请求
     *
     * @param params
     * @param dataCallback
     */
    @Override
    public void doStringGet(final String url, HashMap<String, String> params, final DataCallback dataCallback) {
        String strCookie = "";
//        if (MyApplication.getApp().getCurrentUser() != null)
//            strCookie = StringUtils.isNotEmpty(MyApplication.getApp().getCurrentUser().getAccessToken()) ? MyApplication.getApp().getCurrentUser().getAccessToken() : "";
        LogUtils.i("地址==" + url);
        OkhttpUtils.get().url(url).addParams(params).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, HttpErrorEntity httpErrorEntity) {
//                dataCallback.onDataNotAvailable(httpErrorEntity);
            }

            @Override
            public void onResponse(String response) {
                LogUtils.i("onResponse：" + response.toString());
                dataCallback.onDataLoaded(response);
            }
        });
        if (params != null) {
            String strParams = "";
            for (String key : params.keySet()) {
                strParams = strParams + key + "=" + params.get(key) + "&";
            }
        }
    }

    @Override
    public void doStringGet(String url, final DataCallback dataCallback) {
        String strCookie = "";
//        if (MyApplication.getApp().getCurrentUser() != null)
//            strCookie = StringUtils.isNotEmpty(MyApplication.getApp().getCurrentUser().getAccessToken()) ? MyApplication.getApp().getCurrentUser().getAccessToken() : "";
        LogUtils.i("地址==" + url);
        OkhttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, HttpErrorEntity httpErrorEntity) {
//                dataCallback.onDataNotAvailable(httpErrorEntity);
            }

            @Override
            public void onResponse(String response) {
                LogUtils.i("onResponse：" + response.toString());
                dataCallback.onDataLoaded(response);
            }
        });
    }

    /**
     * 下载
     */
    @Override
    public void doDownload(String url, final DataCallback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onDataNotAvailable(GlobalConstant.SERVER_ERROR, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onDataLoaded(response);
            }
        });
    }

    /**
     * 上传文件
     *
     * @param url
     * @param file
     */
    @Override
    public void doUploadFile(String url, File file, final DataCallback dataCallback) {
//        String strCookie = "";
//        if (MyApplication.getApp().getCurrentUser() != null)
//            strCookie = StringUtils.isNotEmpty(MyApplication.getApp().getCurrentUser().getAccessToken()) ? MyApplication.getApp().getCurrentUser().getAccessToken() : "";
        RequestParams params = new RequestParams(url);
        //  params.addHeader("Cookie", strCookie);
        List<KeyValue> list = new ArrayList<>();
        list.add(new KeyValue("file", file));
        MultipartBody body = new MultipartBody(list, "UTF-8");
        params.setConnectTimeout(60000);
        params.setReadTimeout(60000);
        params.setRequestBody(body);
        x.http().post(params, new Callback.ProgressCallback<String>() {
            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("result==" + result);
                dataCallback.onDataLoaded(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dataCallback.onDataNotAvailable(GlobalConstant.SERVER_ERROR, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                dataCallback.onDataNotAvailable(GlobalConstant.REQUEST_CANCEL, cex.toString());
            }

            @Override
            public void onFinished() {
            }
        });
    }

}
