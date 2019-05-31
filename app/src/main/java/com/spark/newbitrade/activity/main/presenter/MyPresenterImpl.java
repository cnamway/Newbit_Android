package com.spark.newbitrade.activity.main.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.activity.main.MainContract;
import com.spark.newbitrade.data.DataSource;
import com.spark.newbitrade.entity.Wallet;
import com.spark.newbitrade.factory.UrlFactory;
import com.spark.newbitrade.utils.GlobalConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.spark.newbitrade.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2018/2/24.
 */

public class MyPresenterImpl implements MainContract.MyPresenter {
    private MainContract.MyView view;
    private DataSource dataRepository;

    public MyPresenterImpl(DataSource dataRepository, MainContract.MyView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void myWallet() {
        HashMap<String, String> map = new HashMap<>();
        map.put("memberId", MyApplication.getApp().getCurrentUser().getId() + "");
        dataRepository.doStringPost(UrlFactory.getWalletUrl(), map, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    int code = object.optInt("code");
                    if (code == GlobalConstant.SUCCESS_CODE) {
                        List<Wallet> coins = new Gson().fromJson(object.getJSONArray("data").toString(), new TypeToken<List<Wallet>>() {
                        }.getType());
                        view.myWalletSuccess(coins);
                    } else if (code == GlobalConstant.SUCCESS_SEC_CODE) {
                        myWallet();
                    } else {
                        view.doPostFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doPostFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.doPostFail(code, toastMessage);
            }
        });
    }




}
