package com.spark.newbitrade.activity.safe;


import com.spark.newbitrade.data.DataSource;
import com.spark.newbitrade.factory.UrlFactory;
import com.spark.newbitrade.utils.GlobalConstant;

import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.newbitrade.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class GoogleBindPresenter implements GoogleContract.BindPresenter {
    private final DataSource dataRepository;
    private final GoogleContract.BindView view;

    public GoogleBindPresenter(DataSource dataRepository, GoogleContract.BindView view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void doBind(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getBindGoogleCode(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    int code = object.optInt("code");
                    if (code == GlobalConstant.SUCCESS_CODE) {
                        view.doBindSuccess(object.optString("message"));
                    } else {
                        view.doBindFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    view.doBindFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doBindFail(code, toastMessage);
            }
        });
    }
}
