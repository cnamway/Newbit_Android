package com.spark.newbitrade.activity.wallet_coin;

import com.android.volley.VolleyError;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.model.ac.AssetControllerModel;

/**
 * Created by Administrator on 2019/3/2 0002.
 */

public class AddAddresssPresenterImpl implements AddAddressContract.Presenter {
    private AddAddressContract.View view;
    private AssetControllerModel assetControllerModel;

    public AddAddresssPresenterImpl(AddAddressContract.View view) {
        this.view = view;
        assetControllerModel = new AssetControllerModel();
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

    @Override
    public void addWalletWithdrawAddressUsingPOST(String address, String coinId, String remark) {
        assetControllerModel.addWalletWithdrawAddressUsingPOST(address, coinId, remark,
                new ResponseCallBack.SuccessListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoading();
                        if (view != null)
                            view.addWalletWithdrawAddressUsingPOSTSuccess(response);
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




}
