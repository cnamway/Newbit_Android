package com.spark.newbitrade.activity.account_pwd_reset;

import com.android.volley.VolleyError;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.model.login.CasLoginModel;
import com.spark.newbitrade.model.uc.CaptchaGetControllerModel;
import com.spark.newbitrade.model.uc.MemberControllerModel;

import org.json.JSONObject;

/**
 * 重置资金密码
 */

public class AccountPwdResetPresenterImpl implements AccountPwdResetContract.ForgotPwdPresenter {
    private AccountPwdResetContract.ForgotPwdView forgotPwdView;
    private CaptchaGetControllerModel captchaGetControllerModel;
    private MemberControllerModel memberControllerModel;
    private CasLoginModel casLoginModel;

    public AccountPwdResetPresenterImpl(AccountPwdResetContract.ForgotPwdView forgotPwdView) {
        this.forgotPwdView = forgotPwdView;
        captchaGetControllerModel = new CaptchaGetControllerModel();
        memberControllerModel = new MemberControllerModel();
        casLoginModel = new CasLoginModel();
    }

    @Override
    public void getPhoneCode() {
        captchaGetControllerModel.getCodeByPhone(new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (forgotPwdView != null)
                    forgotPwdView.getPhoneCodeSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (forgotPwdView != null)
                    forgotPwdView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (forgotPwdView != null)
                    forgotPwdView.dealError(volleyError);
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
                if (forgotPwdView != null)
                    forgotPwdView.captchSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (forgotPwdView != null)
                    forgotPwdView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (forgotPwdView != null)
                    forgotPwdView.dealError(volleyError);
            }
        });
    }


    @Override
    public void getPhoneCode(String phone, String check, String cid) {
        showLoading();
        captchaGetControllerModel.getCodeByPhoneWithHead(check, cid, "phone", new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (forgotPwdView != null)
                    forgotPwdView.codeSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (forgotPwdView != null)
                    forgotPwdView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (forgotPwdView != null)
                    forgotPwdView.dealError(volleyError);
            }
        });
    }

    @Override
    public void checkPhoneCode(String code) {
        showLoading();
        casLoginModel.phoneCodeCheck(code, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (forgotPwdView != null)
                    forgotPwdView.checkPhoneCodeSuccess(response.toString());
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (forgotPwdView != null)
                    forgotPwdView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (forgotPwdView != null)
                    forgotPwdView.dealError(volleyError);
            }
        });
    }

    @Override
    public void updateForget(String phone, String password, String code) {
        showLoading();
        memberControllerModel.doForgetTrade(phone, password, code,
                new ResponseCallBack.SuccessListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoading();
                        if (forgotPwdView != null)
                            forgotPwdView.updateForgetSuccess(response);
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (forgotPwdView != null)
                            forgotPwdView.dealError(httpErrorEntity);
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (forgotPwdView != null)
                            forgotPwdView.dealError(volleyError);
                    }
                });
    }

    @Override
    public void showLoading() {
        if (forgotPwdView != null)
            forgotPwdView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (forgotPwdView != null)
            forgotPwdView.hideLoading();
    }

    @Override
    public void destory() {
        forgotPwdView = null;
    }

}
