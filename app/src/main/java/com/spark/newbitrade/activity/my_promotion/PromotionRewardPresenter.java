package com.spark.newbitrade.activity.my_promotion;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.library.uc.model.MessageResultPageMemberPromotionVo;
import com.spark.library.uc.model.MessageResultPageMemberRewardRecord;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.data.DataSource;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.entity.PromotionReward;
import com.spark.newbitrade.factory.HttpUrls;
import com.spark.newbitrade.factory.UrlFactory;
import com.spark.newbitrade.model.uc.PromotionControllerModel;
import com.spark.newbitrade.utils.GlobalConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.spark.newbitrade.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public class PromotionRewardPresenter implements PromotionRewardContract.Presenter {

    private PromotionRewardContract.View view;
    private PromotionControllerModel promotionControllerModel;

    public PromotionRewardPresenter(PromotionRewardContract.View view) {
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
    public void getPromotionReward(HashMap<String, String> params) {
        showLoading();
        promotionControllerModel.getPromotionReward(params, new ResponseCallBack.SuccessListener<MessageResultPageMemberRewardRecord>() {
            @Override
            public void onResponse(MessageResultPageMemberRewardRecord response) {
                hideLoading();
                if (view != null)
                    view.getPromotionRewardSuccess(response.getData().getRecords());
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
