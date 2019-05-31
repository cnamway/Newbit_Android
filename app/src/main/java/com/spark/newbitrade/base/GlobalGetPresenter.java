package com.spark.newbitrade.base;


import com.google.gson.Gson;
import com.spark.newbitrade.data.DataSource;
import com.spark.newbitrade.entity.Message;
import com.spark.newbitrade.factory.UrlFactory;
import com.spark.newbitrade.utils.GlobalConstant;

import org.json.JSONException;
import org.json.JSONObject;

import static com.spark.newbitrade.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by daiyy on 2018/07/10.
 */

public class GlobalGetPresenter implements ContractGet.BasePresenter {
    private final DataSource dataRepository;
    private final ContractGet.BaseView view;

    public GlobalGetPresenter(DataSource dataRepository, ContractGet.BaseView view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void doStringGet(String params) {
        view.displayLoadingPopup();
        dataRepository.doStringGet(UrlFactory.getMessageUrl() + params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == GlobalConstant.SUCCESS_CODE) {
                        Message message = new Gson().fromJson(object.getJSONObject("data").toString(), Message.class);
                        view.doGetSuccess(message.getContent());
                    } else {
                        view.doGetFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doGetFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doGetFail(code, toastMessage);
            }
        });
    }
}
