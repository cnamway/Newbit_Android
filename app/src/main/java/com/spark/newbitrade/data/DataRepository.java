package com.spark.newbitrade.data;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */
public class DataRepository implements DataSource {
    private static DataRepository INSTANCE = null;
    private final DataSource mRemoteDataSource;
    private final DataSource mLocalDataSource;
    private boolean isLocal = false;

    private DataRepository(DataSource mRemoteDataSource, DataSource mLocalDataSource) {
        this.mRemoteDataSource = mRemoteDataSource;
        this.mLocalDataSource = mLocalDataSource;
    }

    public static DataRepository getInstance(DataSource mRemoteDataSource, DataSource mLocalDataSource) {
        if (INSTANCE == null) INSTANCE = new DataRepository(mRemoteDataSource, mLocalDataSource);
        return INSTANCE;
    }


    @Override
    public void doStringPost(String url, HashMap<String, String> map, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.doStringPost(url, map, dataCallback);
        else mRemoteDataSource.doStringPost(url, map, dataCallback);
    }

    @Override
    public void doStringPost(String url, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.doStringPost(url, dataCallback);
        else mRemoteDataSource.doStringPost(url, dataCallback);
    }

    @Override
    public void doStringPost(String url, String json, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.doStringPost(url, json, dataCallback);
        else mRemoteDataSource.doStringPost(url, json, dataCallback);
    }

    @Override
    public void doStringGet(String url, HashMap<String, String> params, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.doStringGet(url,params, dataCallback);
        else mRemoteDataSource.doStringGet(url,params, dataCallback);
    }


    @Override
    public void doStringGet(String params, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.doStringGet(params, dataCallback);
        else mRemoteDataSource.doStringGet(params, dataCallback);
    }

    @Override
    public void doDownload(String url, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.doDownload(url, dataCallback);
        else mRemoteDataSource.doDownload(url, dataCallback);
    }

    @Override
    public void doUploadFile(String url, File file, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.doUploadFile(url, file, dataCallback);
        else mRemoteDataSource.doUploadFile(url, file, dataCallback);
    }

}

