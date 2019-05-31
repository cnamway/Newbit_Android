package com.spark.newbitrade.data;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.HashMap;

/**
 * 当需要使用缓存等时 需要使用Local该类加载数据
 */

public class LocalDataSource implements DataSource {
    private static LocalDataSource INSTANCE;
    private Handler handler = new Handler(Looper.getMainLooper());

    public LocalDataSource(Context context) {
    }

    public static LocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void doStringPost(String url, HashMap<String, String> map, DataCallback dataCallback) {

    }

    @Override
    public void doStringPost(String url, DataCallback dataCallback) {

    }

    @Override
    public void doStringPost(String url, String json, DataCallback dataCallback) {

    }

    @Override
    public void doStringGet(String url, HashMap<String, String> params, DataCallback dataCallback) {

    }


    @Override
    public void doStringGet(String params, DataCallback dataCallback) {

    }


    @Override
    public void doDownload(String Url, DataCallback dataCallback) {

    }

    @Override
    public void doUploadFile(String url, File file, DataCallback dataCallback) {

    }

}
