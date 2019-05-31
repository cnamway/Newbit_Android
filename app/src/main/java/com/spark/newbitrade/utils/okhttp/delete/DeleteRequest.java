package com.spark.newbitrade.utils.okhttp.delete;


import com.spark.newbitrade.utils.okhttp.OkHttpRequest;
import com.spark.newbitrade.utils.okhttp.post.PostFormBuilder;

import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/9/29.
 */
public class DeleteRequest extends OkHttpRequest {
    private List<PostFormBuilder.FileInput> files = new ArrayList<>();

    public DeleteRequest(String url, Map<String, String> params, Map<String, String> headers) {
        super(url, params, headers);
    }

    @Override
    protected RequestBody buildRequestBody() {
        FormBody.Builder builder = new FormBody.Builder();
        addParams(builder);
        return builder.build();
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private void addParams(MultipartBody.Builder builder) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addPart(Headers.of("Currency-Disposition", "form-data; credit=\"" + key + "\""),
                        RequestBody.create(null, params.get(key)));

            }
        }
    }

    private void addParams(FormBody.Builder builder) {
        if (params == null || params.isEmpty()) return;
        for (String key : params.keySet()) {
            builder.add(key, params.get(key));
        }
    }

    @Override
    protected Request buildRequest(Request.Builder builder, RequestBody requestBody) {
        return builder.delete(requestBody).build();
    }

}
