package com.spark.newbitrade.utils.okhttp.get;

import android.net.Uri;

import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.utils.SharedPreferenceInstance;
import com.spark.newbitrade.utils.okhttp.OkhttpUtils;
import com.spark.newbitrade.utils.okhttp.RequestBuilder;
import com.spark.newbitrade.utils.okhttp.RequestCall;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * Created by Administrator on 2017/11/13.
 */

public class GetBuilder extends RequestBuilder {
    @Override
    public RequestCall build() {
        int code = SharedPreferenceInstance.getInstance().getLanguageCode();
        if (code == 1) {
            addHeader("Accept-Language", "zh-CN,zh");
        } else if (code == 2) {
            addHeader("Accept-Language", "en-us,en");
        } else if (code == 3) {
            addHeader("Accept-Language", "ja-JP");
        }
//        String token = EncryUtils.getInstance().decryptString(SharedPreferenceInstance.getInstance().getToken(), MyApplication.getApp().getPackageName());
//        addHeader("access-auth-token", token);
//        LogUtils.i("access-auth-token====" + token);
        if (params != null) {
            url = appendParams(url, params);
        }
        return new GetRequest(url, params, headers).build();
    }

    private String appendParams(String url, Map<String, String> params) {
        //TODO
        if (url == null || params == null || params.isEmpty()) {
            return url;
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            builder.appendQueryParameter(key, params.get(key));
        }
        return builder.build().toString();
    }

    @Override
    public GetBuilder url(String url) {
        this.url = url;
        return this;
    }


    @Override
    public GetBuilder addParams(String key, String val) {
        //TODO
        return this;
    }

    @Override
    public GetBuilder addHeader(String key, String value) {
        if (this.headers == null) headers = new HashMap<>();
        headers.put("Accept-Language", SharedPreferenceInstance.getInstance().getAddHeaderLanguage(MyApplication.getApp()));
        headers.put("User-Agent", OkhttpUtils.getUserAgent());
        headers.put(key, value);
        return this;
    }

    @Override
    public RequestBuilder addParams(Map<String, String> map) {
        if (this.params == null) params = new HashMap<>();
        params.putAll(map);
        return this;
    }
}
