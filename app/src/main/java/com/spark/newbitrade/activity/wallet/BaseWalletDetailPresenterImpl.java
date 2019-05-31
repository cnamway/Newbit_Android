package com.spark.newbitrade.activity.wallet;

import com.android.volley.VolleyError;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.AssetRecord;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.model.ac.AssetControllerModel;
import com.spark.newbitrade.utils.LogUtils;

import java.util.HashMap;
import java.util.List;

/**
 * 资产明细
 */
public class BaseWalletDetailPresenterImpl implements BaseWalletDetailContract.WalletPresenter {
    private BaseWalletDetailContract.WalletView myAssetTradeView;
    private AssetControllerModel assetControllerModel;

    public BaseWalletDetailPresenterImpl(BaseWalletDetailContract.WalletView myAssetTradeView) {
        this.myAssetTradeView = myAssetTradeView;
        assetControllerModel = new AssetControllerModel();
    }

    @Override
    public void showLoading() {
        if (myAssetTradeView != null)
            myAssetTradeView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (myAssetTradeView != null)
            myAssetTradeView.hideLoading();
    }

    @Override
    public void destory() {
        myAssetTradeView = null;
    }

    @Override
    public void getRecordList(boolean isShow, Integer type, HashMap<String, String> map, String busiType, String coinName) {
        if (isShow)
            showLoading();
        assetControllerModel.findAssetTransLog(coinName, type, map, busiType,
                new ResponseCallBack.SuccessListener<List<AssetRecord>>() {
                    @Override
                    public void onResponse(List<AssetRecord> list) {
                        LogUtils.i("response==" + list.toString());
                        hideLoading();
                        if (myAssetTradeView != null)
                            myAssetTradeView.getRecordListSuccess(list);
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (myAssetTradeView != null)
                            myAssetTradeView.dealError(httpErrorEntity);
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (myAssetTradeView != null)
                            myAssetTradeView.dealError(volleyError);
                    }
                });
    }
}
