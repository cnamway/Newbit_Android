package com.spark.newbitrade.activity.edit_login_pwd;


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

public class EditLoginPwdPresenter implements EditLoginPwdContract.Presenter {
    private final DataSource dataRepository;
    private final EditLoginPwdContract.View view;

    public EditLoginPwdPresenter(DataSource dataRepository, EditLoginPwdContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    public void sendEditLoginPwdCode(HashMap<String, String> map) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getSendCodeAfterLoginUrl(), map, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == GlobalConstant.SUCCESS_CODE) {
                        view.sendEditLoginPwdCodeSuccess(object.optString("message"));
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
    public void editPwd(HashMap<String, String> map) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getEditPwdUrl(), map, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == GlobalConstant.SUCCESS_CODE) {
                        view.editPwdSuccess(object.optString("message"));
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
    public void doLoginOut() {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getUcLoginOutUrl(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                try {
                    String response = (String) obj;
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == GlobalConstant.SUCCESS_SEC_CODE) {
                        view.doLoginOutSuccess(object.optString("message"));
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
