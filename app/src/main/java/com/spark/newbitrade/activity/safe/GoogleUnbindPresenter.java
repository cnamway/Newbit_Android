package com.spark.newbitrade.activity.safe;


import com.spark.newbitrade.data.DataSource;
import com.spark.newbitrade.factory.UrlFactory;

import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.newbitrade.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class GoogleUnbindPresenter implements GoogleContract.UnBindPresenter {
    private final DataSource dataRepository;
    private final GoogleContract.UnBindView view;

    public GoogleUnbindPresenter(DataSource dataRepository, GoogleContract.UnBindView view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }


    @Override
    public void doUnbind(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getUnBindGoogleCode(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    int code = object.optInt("code");
                    if (code == 0) {
                        view.doUnbindSuccess(object.optString("message"));
                    } else {
                        view.doUnbindFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    view.doUnbindFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doUnbindFail(code, toastMessage);
            }
        });
    }
}
