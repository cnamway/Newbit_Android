package com.spark.newbitrade.activity.appeal;

import com.android.volley.VolleyError;
import com.spark.library.otc.model.AppealApplyInTransitDto;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.model.otc.AppealControllerModel;
import com.spark.newbitrade.model.otc.UploadControllerModel;

/**
 * Created by Administrator on 2019/3/2 0002.
 */

public class AppealPresenterImpl implements AppealContract.Presenter {
    private AppealContract.View view;
    private AppealControllerModel appealControllerModel;
    private UploadControllerModel uploadControllerModel;

    public AppealPresenterImpl(AppealContract.View view) {
        this.view = view;
        appealControllerModel = new AppealControllerModel();
        uploadControllerModel = new UploadControllerModel();
    }

    @Override
    public void showLoading() {
        if (view != null)
            view.showLoading();
    }

    @Override
    public void hideLoading() {
        if (view != null)
            view.hideLoading();
    }

    @Override
    public void destory() {
        view = null;
    }

    @Override
    public void appealApply(AppealApplyInTransitDto appealApplyInTransitDto) {
        appealControllerModel.appealApplyUsingPOST(appealApplyInTransitDto,
                new ResponseCallBack.SuccessListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoading();
                        if (view != null)
                            view.appealApplySuccess(response);
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

    @Override
    public void base64UpLoad(String base64) {
        showLoading();
        uploadControllerModel.base64UpLoadUsingPOST(base64,
                new ResponseCallBack.SuccessListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoading();
                        if (view != null)
                            view.base64UpLoadSuccess(response);
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
