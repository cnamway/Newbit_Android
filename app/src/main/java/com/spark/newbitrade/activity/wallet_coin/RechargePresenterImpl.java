package com.spark.newbitrade.activity.wallet_coin;

import com.android.volley.VolleyError;
import com.spark.library.ac.model.MemberWallet;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.model.ac.AssetControllerModel;

/**
 * Created by Administrator on 2019/3/5 0005.
 */

public class RechargePresenterImpl implements RechargeContract.WalletPresenter {
    private RechargeContract.WalletView walletView;
    private AssetControllerModel assetControllerModel;

    public RechargePresenterImpl(RechargeContract.WalletView walletView) {
        this.walletView = walletView;
        assetControllerModel = new AssetControllerModel();
    }

    @Override
    public void getAddress(String coinName) {
        showLoading();
        assetControllerModel.findWalletAddressUsing(coinName, new ResponseCallBack.SuccessListener<MemberWallet>() {
            @Override
            public void onResponse(MemberWallet response) {
                hideLoading();
                if (walletView != null)
                    walletView.getAddressSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (walletView != null)
                    walletView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (walletView != null)
                    walletView.dealError(volleyError);
            }
        });
    }


    @Override
    public void showLoading() {
        if (walletView != null)
            walletView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (walletView != null)
            walletView.hideLoading();
    }

    @Override
    public void destory() {
        walletView = null;
    }
}
