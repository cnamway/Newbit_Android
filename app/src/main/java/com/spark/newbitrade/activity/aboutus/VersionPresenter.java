package com.spark.newbitrade.activity.aboutus;


import com.android.volley.VolleyError;
import com.spark.library.cms.model.MessageResultWebConfigVo;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.model.cms.WebConfigControllerModel;
import com.spark.newbitrade.model.otc.AppVersionModel;

/**
 * Created by Administrator on 2017/9/25.
 */

public class VersionPresenter implements VersionContract.Presenter {
    private VersionContract.View view;
    private AppVersionModel appVersionModel;

    public VersionPresenter(VersionContract.View view) {
        this.view = view;
        appVersionModel = new AppVersionModel();
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

    @Override
    public void checkVersion() {
        appVersionModel.getAppVersion(new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (view != null)
                    view.checkVersionSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                if (view != null)
                    view.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (view != null)
                    view.dealError(volleyError);
            }
        });
    }

}
