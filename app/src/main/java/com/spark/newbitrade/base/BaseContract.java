package com.spark.newbitrade.base;


import com.android.volley.VolleyError;
import com.spark.newbitrade.entity.HttpErrorEntity;

/**
 * Contract的基类
 */

public class BaseContract {
    public interface BaseView {
        void showLoading();

        void hideLoading();

        void dealError(HttpErrorEntity httpErrorEntity);

        void dealError(VolleyError volleyError);

    }

    public interface BasePresenter {
        void showLoading();

        void hideLoading();

        void destory();
    }
}
