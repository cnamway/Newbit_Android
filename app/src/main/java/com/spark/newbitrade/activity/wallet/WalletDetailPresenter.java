package com.spark.newbitrade.activity.wallet;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.newbitrade.data.DataSource;
import com.spark.newbitrade.entity.WalletDetail;
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

public class WalletDetailPresenter implements WalletDetailContract.Presenter {
    private final DataSource dataRepository;
    private final WalletDetailContract.View view;

    public WalletDetailPresenter(DataSource dataRepository, WalletDetailContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void allTransaction(final HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getAllTransactionUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    int code = object.optInt("code");
                    if (code == GlobalConstant.SUCCESS_CODE) {
                        List<WalletDetail> objs = new Gson().fromJson(object.getJSONObject("data").getJSONArray("records").toString(), new TypeToken<List<WalletDetail>>() {
                        }.getType());
                        view.allTransactionSuccess(objs);
                    } else if (code == GlobalConstant.SUCCESS_SEC_CODE) {
                        allTransaction(params);
                    } else {
                        view.allTransactionFail(code, object.optString("message"));
                    }
                } catch (JSONException e) {
                    view.allTransactionFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.allTransactionFail(code, toastMessage);

            }
        });
    }

}
