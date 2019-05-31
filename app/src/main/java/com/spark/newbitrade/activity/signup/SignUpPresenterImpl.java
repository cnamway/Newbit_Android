package com.spark.newbitrade.activity.signup;


import com.android.volley.VolleyError;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.model.uc.CaptchaGetControllerModel;
import com.spark.newbitrade.model.uc.RegisterControllerModel;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/25.
 */

public class SignUpPresenterImpl implements SignUpContract.SignPresenter {
    private SignUpContract.SignView signView;
    private RegisterControllerModel registerControllerModel;
    private CaptchaGetControllerModel captchaGetControllerModel;

    public SignUpPresenterImpl(SignUpContract.SignView signView) {
        this.signView = signView;
        registerControllerModel = new RegisterControllerModel();
        captchaGetControllerModel = new CaptchaGetControllerModel();
    }

    @Override
    public void getPhoneCode(String phone, String check, String cid) {
        showLoading();
        captchaGetControllerModel.getCodeByPhone(phone, check, cid, new ResponseCallBack.SuccessListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoading();
                        if (signView != null)
                            signView.codeSuccess(response);
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (signView != null)
                            signView.dealError(httpErrorEntity);
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (signView != null)
                            signView.dealError(volleyError);
                    }
                });
    }

    @Override
    public void getPhoneCode(String phone) {
        showLoading();
        captchaGetControllerModel.getCodeByPhone(phone, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (signView != null)
                    signView.codeSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (signView != null)
                    signView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (signView != null)
                    signView.dealError(volleyError);
            }
        });
    }

    @Override
    public void getEmailCode(String email) {
        showLoading();
        captchaGetControllerModel.getCodeByEmail(email,
                new ResponseCallBack.SuccessListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoading();
                        if (signView != null)
                            signView.codeSuccess(response);
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (signView != null)
                            signView.dealError(httpErrorEntity);
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (signView != null)
                            signView.dealError(volleyError);
                    }
                });
    }

    @Override
    public void captch() {
        showLoading();
        captchaGetControllerModel.doCaptch(new ResponseCallBack.SuccessListener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideLoading();
                if (signView != null)
                    signView.captchSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (signView != null)
                    signView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (signView != null)
                    signView.dealError(volleyError);
            }
        });
    }

    @Override
    public void sighUpByEmail(String username, String password, String country, String promotion, String email, String code) {
        showLoading();
        registerControllerModel.doSighUpByEmail(username, password, country, promotion, email, code, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (signView != null)
                    signView.sighUpSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (signView != null)
                    signView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (signView != null)
                    signView.dealError(volleyError);
            }
        });
    }

    @Override
    public void sighUpByPhone(String username, String password, String country, String promotion, String phone, String code) {
        showLoading();
        registerControllerModel.doSighUpByPhone(username, password, country, promotion, phone, code, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (signView != null)
                    signView.sighUpSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (signView != null)
                    signView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (signView != null)
                    signView.dealError(volleyError);
            }
        });
    }


    @Override
    public void showLoading() {
        if (signView != null)
            signView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (signView != null)
            signView.hideLoading();
    }

    @Override
    public void destory() {
        signView = null;
    }

}
