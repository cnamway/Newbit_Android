package com.spark.newbitrade.activity.skip;


import com.android.volley.VolleyError;
import com.spark.library.ac.model.MemberWalletVo;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.ExtractInfo;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.model.ac.AssetControllerModel;
import com.spark.newbitrade.model.ac.CaptchaGetControllerModel;

import java.math.BigDecimal;
import java.util.List;

import static com.spark.newbitrade.utils.GlobalConstant.COMMON;
import static com.spark.newbitrade.utils.GlobalConstant.OTC;

/**
 * 提币
 */

public class SkipPayPresnetImpl implements SkipPayContract.Presenter {
    private SkipPayContract.View extractView;
    private AssetControllerModel assetControllerModel;
    private CaptchaGetControllerModel captchaGetControllerModel;

    public SkipPayPresnetImpl(SkipPayContract.View extractView) {
        this.extractView = extractView;
        assetControllerModel = new AssetControllerModel();
        captchaGetControllerModel = new CaptchaGetControllerModel();
    }

    @Override
    public void walletWithdraw(String address, BigDecimal amount, String coinName, String tradePassword, String code, String phone) {
        showLoading();
        assetControllerModel.walletWithdraw(address, amount, coinName, tradePassword, code, phone,
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
    public void getCoinMessage(String coinName) {
        showLoading();
        assetControllerModel.findWalletByCoinNameUsing(OTC, coinName, new ResponseCallBack.SuccessListener<MemberWalletVo>() {
            @Override
            public void onResponse(MemberWalletVo response) {
                hideLoading();
                if (extractView != null)
                    extractView.getCoinMessageSuccess(response);
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
