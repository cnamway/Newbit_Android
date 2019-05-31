package com.spark.newbitrade.data;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface DataSource {

    void doStringPost(String url, HashMap<String, String> params, DataCallback dataCallback);

    void doStringPost(String url, DataCallback dataCallback);

    void doStringPost(String url, String json, DataCallback dataCallback);

    void doStringGet(String url, HashMap<String, String> params, DataCallback dataCallback);

    void doStringGet(String url, DataCallback dataCallback);

    void doDownload(String Url, DataCallback dataCallback);

    void doUploadFile(String url, File file, DataCallback dataCallback);

    interface DataCallback {

        void onDataNotAvailable(Integer code, String toastMessage);

        void onDataLoaded(Object obj);
    }
}
