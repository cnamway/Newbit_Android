package com.spark.newbitrade.activity.myinfo;


import com.android.volley.VolleyError;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.entity.User;
import com.spark.newbitrade.model.otc.UploadControllerModel;
import com.spark.newbitrade.model.uc.MemberControllerModel;
import com.spark.newbitrade.model.uc.UcModel;


/**
 * Created by Administrator on 2017/9/25.
 */

public class MyInfoPresenterImpl implements MyInfoContract.MyInfoPresenter {
    private MyInfoContract.MyInfoView myInfoView;
    private UcModel ucModel;
    private MemberControllerModel memberControllerModel;
    private UploadControllerModel uploadControllerModel;

    public MyInfoPresenterImpl(MyInfoContract.MyInfoView myInfoView) {
        this.myInfoView = myInfoView;
        ucModel = new UcModel();
        memberControllerModel = new MemberControllerModel();
        uploadControllerModel = new UploadControllerModel();
    }

    @Override
    public void uploadBase64Pic(String base64) {
        showLoading();
        uploadControllerModel.base64UpLoadUsingPOST(base64,
                new ResponseCallBack.SuccessListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoading();
                        if (myInfoView != null)
                            myInfoView.uploadBase64PicSuccess(response);
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
    public void avatar(String url) {
        showLoading();
        ucModel.avatar(url, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (myInfoView != null)
                    myInfoView.avatarSuccess(response);
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
    public void getUserInfo() {
        showLoading();
        memberControllerModel.getUserInfo(new ResponseCallBack.SuccessListener<User>() {
            @Override
            public void onResponse(User response) {
                hideLoading();
                if (myInfoView != null)
                    myInfoView.getUserInfoSuccess(response);
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
