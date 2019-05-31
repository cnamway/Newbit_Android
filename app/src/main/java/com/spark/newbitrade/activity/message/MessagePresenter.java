package com.spark.newbitrade.activity.message;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.newbitrade.data.DataSource;
import com.spark.newbitrade.entity.Message;
import com.spark.newbitrade.factory.UrlFactory;
import com.spark.newbitrade.utils.GlobalConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.spark.newbitrade.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class MessagePresenter implements MessageContract.Presenter {
    private final DataSource dataRepository;
    private final MessageContract.View view;

    public MessagePresenter(DataSource dataRepository, MessageContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void message(HashMap<String, String> params) {
        dataRepository.doStringPost(UrlFactory.getMessageUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == GlobalConstant.SUCCESS_CODE) {
                        List<Message> objs = new Gson().fromJson(object.getJSONObject("data").getJSONArray("records").toString(), new TypeToken<List<Message>>() {
                        }.getType());
                        view.messageSuccess(objs);
                    } else {
                        view.messageFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.messageFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.messageFail(code, toastMessage);
            }
        });
    }


}
