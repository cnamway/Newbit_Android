package com.spark.newbitrade.activity.my_account;

import com.android.volley.VolleyError;
import com.spark.library.otc.model.MessageResult;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.entity.PayWaySetting;
import com.spark.newbitrade.model.otc.PayControllerModel;

import java.util.List;

/**
 * Created by Administrator on 2019/3/15 0015.
 */

public class MyAccountPresenterImpl implements MyAccountContract.Presenter {

    private MyAccountContract.View view;
    private PayControllerModel payControllerModel;

    public MyAccountPresenterImpl(MyAccountContract.View view) {
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
    public void doUpdateStatusBank(Long id, final Integer status) {
        showLoading();
        payControllerModel.doUpdateStatusBank(id, status, new ResponseCallBack.SuccessListener<MessageResult>() {
            @Override
            public void onResponse(MessageResult response) {
                hideLoading();
                if (view != null) {
                    view.updateSuccess(response);
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
    public void doDeleteBank(Long id, String pws) {
        showLoading();
        payControllerModel.doDeleteBank(id, pws, new ResponseCallBack.SuccessListener<MessageResult>() {
            @Override
            public void onResponse(MessageResult response) {
                hideLoading();
                if (view != null) {
                    view.updateSuccess(response);
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
