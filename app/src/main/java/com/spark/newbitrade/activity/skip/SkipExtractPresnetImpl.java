package com.spark.newbitrade.activity.skip;


import com.android.volley.VolleyError;
import com.spark.library.ac.model.MemberWallet;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.CasLoginEntity;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.model.ac.AssetControllerModel;
import com.spark.newbitrade.model.login.CasLoginModel;

public class SkipExtractPresnetImpl implements SkipExtractContract.Presenter {
    private SkipExtractContract.View extractView;
    private AssetControllerModel assetControllerModel;
    private CasLoginModel casLoginModel;

    public SkipExtractPresnetImpl(SkipExtractContract.View extractView) {
        this.extractView = extractView;
        assetControllerModel = new AssetControllerModel();
        casLoginModel = new CasLoginModel();
    }

    @Override
    public void getAddress(String coinName) {
        showLoading();
        assetControllerModel.findWalletAddressUsing(coinName, new ResponseCallBack.SuccessListener<MemberWallet>() {
            @Override
            public void onResponse(MemberWallet response) {
                hideLoading();
                if (extractView != null)
                    extractView.getAddressSuccess(response);
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
    public void checkBusinessLogin(String type) {
        showLoading();
        casLoginModel.checkBusinessLogin(type, new ResponseCallBack.SuccessListener<CasLoginEntity>() {
            @Override
            public void onResponse(CasLoginEntity response) {
                hideLoading();
                if (extractView != null)
                    extractView.checkBusinessLoginSuccess(response);
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
    public void doLoginBusiness(String tgc, String type) {
        showLoading();
        casLoginModel.getBussinessTicket(tgc, type, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (extractView != null)
                    extractView.doLoginBusinessSuccess(response);
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
