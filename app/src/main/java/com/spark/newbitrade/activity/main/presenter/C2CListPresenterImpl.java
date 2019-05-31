package com.spark.newbitrade.activity.main.presenter;

import com.android.volley.VolleyError;
import com.spark.library.otc.model.PageAdvertiseShowVo;
import com.spark.library.otc.model.QueryParamAdvertiseShowVo;
import com.spark.newbitrade.activity.main.C2CListContract;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.model.otc.AdvertiseScanControllerModel;

/**
 * Created by Administrator on 2018/2/28.
 */

public class C2CListPresenterImpl implements C2CListContract.C2CListPresenter {
    private C2CListContract.C2CListView view;
    private AdvertiseScanControllerModel advertiseScanControllerModel;

    public C2CListPresenterImpl(C2CListContract.C2CListView view) {
        this.view = view;
        this.advertiseScanControllerModel = new AdvertiseScanControllerModel();
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
    public void findPageForCoin(QueryParamAdvertiseShowVo queryParam) {
        advertiseScanControllerModel.findPageForCoin(queryParam, new ResponseCallBack.SuccessListener<PageAdvertiseShowVo>() {
            @Override
            public void onResponse(PageAdvertiseShowVo response) {
                if (view != null) {
                    view.findPageForCoinSuccess(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                if (view != null)
                    view.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (view != null)
                    view.dealError(volleyError);
            }
        });
    }


}
