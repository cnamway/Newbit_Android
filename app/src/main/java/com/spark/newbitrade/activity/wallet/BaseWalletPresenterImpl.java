package com.spark.newbitrade.activity.wallet;

import com.android.volley.VolleyError;
import com.spark.library.ac.model.MessageResult;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.entity.Wallet;
import com.spark.newbitrade.model.ac.AssetControllerModel;

import java.util.List;

/**
 * Created by Administrator on 2019/3/5 0005.
 */

public class BaseWalletPresenterImpl implements BaseWalletContract.WalletPresenter {
    private BaseWalletContract.WalletView walletView;
    private AssetControllerModel assetControllerModel;

    public BaseWalletPresenterImpl(BaseWalletContract.WalletView walletView) {
        this.walletView = walletView;
        assetControllerModel = new AssetControllerModel();
    }


    @Override
    public void getWallet(String type) {
//        showLoading();
        assetControllerModel.findWalletUsing(type, new ResponseCallBack.SuccessListener<List<Wallet>>() {
            @Override
            public void onResponse(List<Wallet> response) {
//                hideLoading();
                if (walletView != null)
                    walletView.getWalletSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
//                hideLoading();
                if (walletView != null)
                    walletView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                hideLoading();
                if (walletView != null)
                    walletView.dealError(volleyError);
            }
        });
    }

    @Override
    public void findSupportAssetUsingGET() {
//        showLoading();
        assetControllerModel.findSupportAssetUsingGET(new ResponseCallBack.SuccessListener<List<Wallet>>() {
            @Override
            public void onResponse(List<Wallet> list) {
//                hideLoading();
                if (walletView != null)
                    walletView.findSupportAssetUsingGETSuccess(list);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
//                hideLoading();
                if (walletView != null)
                    walletView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                hideLoading();
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
