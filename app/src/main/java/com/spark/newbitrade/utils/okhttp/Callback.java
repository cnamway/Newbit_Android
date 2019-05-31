package com.spark.newbitrade.utils.okhttp;


import com.google.gson.Gson;
import com.spark.newbitrade.entity.HttpErrorEntity;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/9/29.
 */

public abstract class Callback<T> {
    protected Gson gson = new Gson();

    public abstract T parseNetworkResponse(Response response) throws IOException;

    public void onBefore(Request request) {
    }

    public void onAfter() {
    }

    public void inProgress(float progress) {

    }

    public abstract void onError(Request request, HttpErrorEntity httpErrorEntity);

    public abstract void onResponse(T response);

    public static Callback CALLBACK_DEFAULT = new Callback() {

        @Override
        public Object parseNetworkResponse(Response response) throws IOException {
            return null;
        }

        @Override
        public void onError(Request request, HttpErrorEntity httpErrorEntity) {

        }

        @Override
        public void onResponse(Object response) {

        }
    };
}
