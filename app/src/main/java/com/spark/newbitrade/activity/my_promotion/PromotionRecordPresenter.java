package com.spark.newbitrade.activity.my_promotion;

import com.android.volley.VolleyError;
import com.spark.library.uc.model.MessageResultPageMemberPromotionVo;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.model.uc.PromotionControllerModel;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public class PromotionRecordPresenter implements PromotionRecordContract.Presenter {

    private PromotionRecordContract.View view;
    private PromotionControllerModel promotionControllerModel;

    public PromotionRecordPresenter(PromotionRecordContract.View view) {
        this.view = view;
        promotionControllerModel = new PromotionControllerModel();
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
        if (view != null)
            view = null;
    }

    @Override
    public void getPromotion(HashMap<String, String> params) {
        showLoading();
        promotionControllerModel.getPromotion(params, new ResponseCallBack.SuccessListener<MessageResultPageMemberPromotionVo>() {
            @Override
            public void onResponse(MessageResultPageMemberPromotionVo response) {
                hideLoading();
                if (view != null)
                    view.getPromotionSuccess(response.getData().getRecords(), response.getData().getTotal());
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
