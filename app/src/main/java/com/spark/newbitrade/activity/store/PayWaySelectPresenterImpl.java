package com.spark.newbitrade.activity.store;

import com.android.volley.VolleyError;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.entity.PayWaySetting;
import com.spark.newbitrade.model.otc.PayControllerModel;

import java.util.List;

/**
 * Created by Administrator on 2019/3/15 0015.
 */

public class PayWaySelectPresenterImpl implements PayWaySelectContract.Presenter {

    private PayWaySelectContract.View view;
    private PayControllerModel payControllerModel;

    public PayWaySelectPresenterImpl(PayWaySelectContract.View view) {
        this.view = view;
        this.payControllerModel = new PayControllerModel();
    }

    @Override
    public void queryPayWayList() {
        showLoading();
        payControllerModel.queryListUsingGET(new ResponseCallBack.SuccessListener<List<PayWaySetting>>() {
            @Override
            public void onResponse(List<PayWaySetting> response) {
                hideLoading();
                if (view != null) {
                    view.queryPayWayListSuccess(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (view != null) {
                    view.dealError(httpErrorEntity);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (view != null) {
                    view.dealError(volleyError);
                }
            }
        });
    }

    @Override
    public void showLoading() {
        if (view != null) {
            view.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (view != null) {
            view.hideLoading();
        }
    }

    @Override
    public void destory() {
        view = null;
    }
}
