package com.spark.newbitrade.activity.safe;


import com.google.gson.Gson;
import com.spark.newbitrade.data.DataSource;
import com.spark.newbitrade.entity.GoogleCode;
import com.spark.newbitrade.factory.UrlFactory;

import org.json.JSONObject;

import static com.spark.newbitrade.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class GooglePresenter implements GoogleContract.Presenter {
    private final DataSource dataRepository;
    private final GoogleContract.View view;

    public GooglePresenter(DataSource dataRepository, GoogleContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void getInfo() {
        view.displayLoadingPopup();
        dataRepository.doStringGet(UrlFactory.getGoogleCode(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    int code = object.optInt("code");
                    if (code == 0) {
                        GoogleCode googleCode = new Gson().fromJson(object.getJSONObject("data").toString(), GoogleCode.class);
                        view.getInfoSuccess(googleCode);
                    } else {
                        view.getInfoFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    view.getInfoFail(JSON_ERROR, null);
                }

            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.getInfoFail(code, toastMessage);
            }
        });
    }
}
