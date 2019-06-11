package com.spark.newbitrade.activity.login;

import com.android.volley.VolleyError;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.entity.User;
import com.spark.newbitrade.model.login.CasLoginModel;
import com.spark.newbitrade.model.uc.CaptchaGetControllerModel;
import com.spark.newbitrade.model.uc.MemberControllerModel;
import com.spark.newbitrade.utils.StringUtils;

import org.json.JSONObject;

/**
 * 登录
 */

public class LoginPresenterImpl implements LoginContract.LoginPresenter {
    private LoginContract.LoginView loginView;
    private CasLoginModel casLoginModel;
    private MemberControllerModel memberControllerModel;
    private CaptchaGetControllerModel captchaGetControllerModel;

    public LoginPresenterImpl(LoginContract.LoginView loginView) {
        this.loginView = loginView;
        casLoginModel = new CasLoginModel();
        memberControllerModel = new MemberControllerModel();
        captchaGetControllerModel = new CaptchaGetControllerModel();
    }

    @Override
    public void getPhoneCode(String phone) {
        showLoading();
        casLoginModel.sendVertifyCode(phone, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (loginView != null)
                    loginView.codeSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (loginView != null)
                    loginView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (loginView != null)
                    loginView.dealError(volleyError);
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
                if (loginView != null)
                    loginView.codeSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (loginView != null)
                    loginView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (loginView != null)
                    loginView.dealError(volleyError);
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
                if (loginView != null)
                    loginView.checkPhoneCodeSuccess(response.toString());
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (loginView != null)
                    loginView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (loginView != null)
                    loginView.dealError(volleyError);
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
                if (loginView != null)
                    loginView.captchSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (loginView != null)
                    loginView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (loginView != null)
                    loginView.dealError(volleyError);
            }
        });
    }

    /**
     * 登录cas
     *
     * @param username
     * @param password
     */
    @Override
    public void casLogn(String username, String password, String remember) {
        showLoading();
        if (StringUtils.isEmpty(remember)) {
            remember = "true";
        }
        casLoginModel.casLogn(username, password, remember, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (loginView != null)
                    loginView.casLoginSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (loginView != null)
                    loginView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (loginView != null)
                    loginView.dealError(volleyError);
            }
        });
    }


    @Override
    public void doUcLogin(String gtc, String type) {
        casLoginModel.getBussinessTicket(gtc, type, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (loginView != null)
                    loginView.ucLoginSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (loginView != null)
                    loginView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (loginView != null)
                    loginView.dealError(volleyError);
            }
        });
    }

    @Override
    public void getUserInfo() {
        memberControllerModel.getUserInfo(new ResponseCallBack.SuccessListener<User>() {
            @Override
            public void onResponse(User response) {
                hideLoading();
                if (loginView != null)
                    loginView.getUserInfoSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (loginView != null)
                    loginView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (loginView != null)
                    loginView.dealError(volleyError);
            }
        });
    }

    @Override
    public void checkCaptcha(String check, String cid) {
        showLoading();
        captchaGetControllerModel.checkCaptcha(check, cid, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (loginView != null)
                    loginView.checkCaptchaSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (loginView != null)
                    loginView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (loginView != null)
                    loginView.dealError(volleyError);
            }
        });
    }

    @Override
    public void showLoading() {
        if (loginView != null)
            loginView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (loginView != null)
            loginView.hideLoading();
    }

    @Override
    public void destory() {
        loginView = null;
    }
}
