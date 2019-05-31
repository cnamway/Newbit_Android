package com.spark.newbitrade.activity.setting;


import com.android.volley.VolleyError;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.model.login.CasLoginModel;
import com.spark.newbitrade.utils.SharedPreferenceInstance;


/**
 * Created by Administrator on 2017/9/25.
 */

public class SettingPresenterImpl implements SettingContact.Presenter {
    private SettingContact.View myInfoView;
    private CasLoginModel casLoginModel;

    public SettingPresenterImpl(SettingContact.View myInfoView) {
        this.myInfoView = myInfoView;
        casLoginModel = new CasLoginModel();
    }

    @Override
    public void loginOut() {
        showLoading();
        String tgt = SharedPreferenceInstance.getInstance().getTgt();
        casLoginModel.loginOut(tgt, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (myInfoView != null)
                    myInfoView.loginOutSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (myInfoView != null)
                    myInfoView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (myInfoView != null)
                    myInfoView.dealError(volleyError);
            }
        });
    }

    @Override
    public void showLoading() {
        if (myInfoView != null)
            myInfoView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (myInfoView != null)
            myInfoView.hideLoading();
    }

    @Override
    public void destory() {
        myInfoView = null;
    }
}
