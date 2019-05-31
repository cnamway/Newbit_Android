package com.spark.newbitrade.activity.skip;


import com.android.volley.VolleyError;
import com.spark.library.ac.model.MemberWalletVo;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.model.ac.AssetControllerModel;

import java.math.BigDecimal;

import static com.spark.newbitrade.utils.GlobalConstant.COMMON;

/**
 * 提币
 */

public class SkipPayPresnetImpl implements SkipPayContract.Presenter {
    private SkipPayContract.View extractView;
    private AssetControllerModel assetControllerModel;

    public SkipPayPresnetImpl(SkipPayContract.View extractView) {
        this.extractView = extractView;
        assetControllerModel = new AssetControllerModel();
    }

    @Override
    public void walletWithdraw(String address, BigDecimal amount, String coinName, String tradePassword) {
        showLoading();
        assetControllerModel.walletWithdraw(address, amount, coinName, tradePassword, "", "",
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
        assetControllerModel.findWalletByCoinNameUsing(COMMON, coinName, new ResponseCallBack.SuccessListener<MemberWalletVo>() {
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
