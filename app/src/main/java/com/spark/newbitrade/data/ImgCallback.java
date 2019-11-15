package com.spark.newbitrade.data;



import com.spark.newbitrade.utils.okhttp.Callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * 图片验证码
 */

public abstract class ImgCallback extends Callback<Response> {
    @Override
    public Response parseNetworkResponse(Response response) throws IOException {
        return response;
    }
}
