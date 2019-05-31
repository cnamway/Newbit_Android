package com.spark.newbitrade.activity.chat;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.library.cms.model.MessageResultWebConfigVo;
import com.spark.library.otc.model.ChatMessageRecord;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.data.DataSource;
import com.spark.newbitrade.entity.ChatEntity;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.factory.UrlFactory;
import com.spark.newbitrade.model.otc.TradeControllerModel;
import com.spark.newbitrade.utils.GlobalConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.spark.newbitrade.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2018/4/16 0016.
 */

public class ChatPresenter implements ChatContact.Presenter {
    private ChatContact.View view;
    private TradeControllerModel tradeControllerModel;

    public ChatPresenter(ChatContact.View view) {
        this.view = view;
        tradeControllerModel = new TradeControllerModel();
    }

    @Override
    public void getHistoryMessage(String orderId, HashMap<String, String> params) {
        tradeControllerModel.queryOrderChatDataUsingPOST(orderId, params, new ResponseCallBack.SuccessListener<List<ChatEntity>>() {
            @Override
            public void onResponse(List<ChatEntity> response) {
                hideLoading();
                if (view != null) {
                    view.getHistoryMessageSuccess(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (view != null) {
                    view.getHistoryMessageFail(httpErrorEntity.getCode(), httpErrorEntity.getMessage());
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (view != null) {
                    view.dealError(volleyError);
                }
            }
        });
    }

    @Override
    public void showLoading() {
        if (view != null) {
            view.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (view != null) {
            view.hideLoading();
        }
    }

    @Override
    public void destory() {
        view = null;
    }
}
