package com.spark.newbitrade.activity.account_pwd;


import com.spark.newbitrade.data.DataSource;
import com.spark.newbitrade.factory.UrlFactory;
import com.spark.newbitrade.utils.GlobalConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.newbitrade.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class ResetAccountPwdPresenter implements AccountPwdContract.ResetPresenter {
    private final DataSource dataRepository;
    private final AccountPwdContract.ResetView view;

    public ResetAccountPwdPresenter(DataSource dataRepository, AccountPwdContract.ResetView view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void resetAccountPwd(final HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getResetAccountPwdUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    int code = object.optInt("code");
                    if (code == GlobalConstant.SUCCESS_CODE) {
                        view.resetAccountPwdSuccess(object.getString("message"));
                    } else if (code == GlobalConstant.SUCCESS_SEC_CODE) {
                        resetAccountPwd(params);
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
                view.hideLoadingPopup();
                view.doPostFail(code, toastMessage);

            }
        });
    }

    @Override
    public void resetAccountPwdCode(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getSendCodeAfterLoginUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == GlobalConstant.SUCCESS_CODE) {
                        view.resetAccountPwdCodeSuccess(object.optString("message"));
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
                view.hideLoadingPopup();
                view.doPostFail(code, toastMessage);

            }
        });
    }
}
