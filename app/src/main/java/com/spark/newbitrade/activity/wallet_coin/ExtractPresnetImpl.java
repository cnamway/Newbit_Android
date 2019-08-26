package com.spark.newbitrade.activity.wallet_coin;

import com.android.volley.VolleyError;
import com.spark.library.ac.model.MessageResult;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.ExtractInfo;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.model.ac.AssetControllerModel;
import com.spark.newbitrade.model.ac.CaptchaGetControllerModel;
import com.spark.newbitrade.model.ac.CheckControllerModel;
import com.spark.newbitrade.model.login.CasLoginModel;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.List;

/**
 * 提币
 */

public class ExtractPresnetImpl implements ExtractContract.ExtractPresenter {
    private ExtractContract.ExtractView extractView;
    private AssetControllerModel assetControllerModel;
    private CaptchaGetControllerModel captchaGetControllerModel;
    private CasLoginModel casLoginModel;
    private CheckControllerModel chcekControllerModel;

    public ExtractPresnetImpl(ExtractContract.ExtractView extractView) {
        this.extractView = extractView;
        assetControllerModel = new AssetControllerModel();
        captchaGetControllerModel = new CaptchaGetControllerModel();
        casLoginModel = new CasLoginModel();
        chcekControllerModel = new CheckControllerModel();
    }

    @Override
    public void walletWithdraw(String address, BigDecimal amount, String coinName, String tradePassword, String code, String phone) {
        showLoading();
        assetControllerModel.walletWithdraw(address, amount, coinName, tradePassword, code, phone, "",
                new ResponseCallBack.SuccessListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoading();
                        if (extractView != null)
                            extractView.walletWithdrawSuccess(response);
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (extractView != null)
                            extractView.dealError(httpErrorEntity);
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (extractView != null)
                            extractView.dealError(volleyError);
                    }
                });
    }


    @Override
    public void getExtractInfo(String coinName) {
        showLoading();
        assetControllerModel.findSupportAsset(coinName, new ResponseCallBack.SuccessListener<List<ExtractInfo>>() {
            @Override
            public void onResponse(List<ExtractInfo> response) {
                hideLoading();
                if (extractView != null)
                    extractView.getExtractInfoSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (extractView != null)
                    extractView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (extractView != null)
                    extractView.dealError(volleyError);
            }
        });
    }


    @Override
    public void getPhoneCode(String phone) {
        captchaGetControllerModel.getCodeByPhone(phone, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (extractView != null)
                    extractView.getPhoneCodeSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (extractView != null)
                    extractView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (extractView != null)
                    extractView.dealError(volleyError);
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
                if (extractView != null)
                    extractView.captchSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (extractView != null)
                    extractView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (extractView != null)
                    extractView.dealError(volleyError);
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
                if (extractView != null)
                    extractView.codeSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (extractView != null)
                    extractView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (extractView != null)
                    extractView.dealError(volleyError);
            }
        });
    }

    @Override
    public void checkAddress(String address) {
        showLoading();
        chcekControllerModel.checkAddress(address, new ResponseCallBack.SuccessListener<MessageResult>() {
            @Override
            public void onResponse(MessageResult response) {
                hideLoading();
                if (extractView != null)
                    extractView.checkAddressSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (extractView != null)
                    extractView.checkAddressFail(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (extractView != null)
                    extractView.dealError(volleyError);
            }
        });
    }

    @Override
    public void showLoading() {
        if (extractView != null)
            extractView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (extractView != null)
            extractView.hideLoading();
    }

    @Override
    public void destory() {
        extractView = null;
    }
}
