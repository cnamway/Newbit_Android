package com.spark.newbitrade.activity.edit_login_pwd;


import com.android.volley.VolleyError;
import com.spark.newbitrade.activity.forgot_pwd.ForgotPwdContract;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.data.DataSource;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.factory.UrlFactory;
import com.spark.newbitrade.model.login.CasLoginModel;
import com.spark.newbitrade.model.uc.CaptchaGetControllerModel;
import com.spark.newbitrade.model.uc.MemberControllerModel;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.SharedPreferenceInstance;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.newbitrade.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class EditLoginPwdPresenter implements EditLoginPwdContract.Presenter {
    private EditLoginPwdContract.View view;
    private CaptchaGetControllerModel captchaGetControllerModel;
    private MemberControllerModel memberControllerModel;
    private CasLoginModel casLoginModel;

    public EditLoginPwdPresenter(EditLoginPwdContract.View view) {
        this.view = view;
        captchaGetControllerModel = new CaptchaGetControllerModel();
        memberControllerModel = new MemberControllerModel();
        casLoginModel = new CasLoginModel();
    }

    @Override
    public void updateForget(String phone, String code, String oldPassword, String newPassword) {
        showLoading();
        memberControllerModel.updateLoginPasswordUsing(phone, code, oldPassword, newPassword,
                new ResponseCallBack.SuccessListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoading();
                        if (view != null)
                            view.updateForgetSuccess(response);
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
    public void captch() {
        showLoading();
        captchaGetControllerModel.doCaptch(new ResponseCallBack.SuccessListener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideLoading();
                if (view != null)
                    view.captchSuccess(response);
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
    public void getPhoneCode(String phone) {
        captchaGetControllerModel.getCodeByPhone(phone, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (view != null)
                    view.getPhoneCodeSuccess(response);
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
    public void getPhoneCode(String phone, String check, String cid) {
        showLoading();
        captchaGetControllerModel.getCodeByPhone(phone, check, cid, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (view != null)
                    view.codeSuccess(response);
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
    public void loginOut() {
        showLoading();
        String tgt = SharedPreferenceInstance.getInstance().getTgt();
        casLoginModel.loginOut(tgt, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (view != null)
                    view.loginOutSuccess(response);
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
}


