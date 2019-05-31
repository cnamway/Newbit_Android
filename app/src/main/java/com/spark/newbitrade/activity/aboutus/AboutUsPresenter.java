package com.spark.newbitrade.activity.aboutus;


import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.library.cms.model.MessageResultWebConfigVo;
import com.spark.library.cms.model.WebArticleVo;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.data.DataSource;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.entity.Vision;
import com.spark.newbitrade.factory.UrlFactory;
import com.spark.newbitrade.model.cms.WebConfigControllerModel;
import com.spark.newbitrade.model.otc.AppVersionModel;
import com.spark.newbitrade.utils.GlobalConstant;

import org.json.JSONObject;

import static com.spark.newbitrade.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class AboutUsPresenter implements AboutUsContract.Presenter {
    private AboutUsContract.View view;
    private WebConfigControllerModel webConfigControllerModel;
    private AppVersionModel appVersionModel;

    public AboutUsPresenter(AboutUsContract.View view) {
        this.view = view;
        webConfigControllerModel = new WebConfigControllerModel();
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
    public void getWebConfig() {
        showLoading();
        webConfigControllerModel.webConfigQuery(new ResponseCallBack.SuccessListener<MessageResultWebConfigVo>() {
            @Override
            public void onResponse(MessageResultWebConfigVo response) {
                hideLoading();
                if (view != null) {
                    view.getWebConfigSuccess(response);
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
    public void checkVersion() {
        showLoading();
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
                hideLoading();
                if (view != null)
                    view.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (view != null)
                    view.dealError(volleyError);
            }
        });
    }

}
