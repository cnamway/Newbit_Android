package com.spark.newbitrade.activity.main.presenter;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.newbitrade.activity.main.MainContract;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.data.DataSource;
import com.spark.newbitrade.entity.CasLoginEntity;
import com.spark.newbitrade.entity.Currency;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.entity.Vision;
import com.spark.newbitrade.factory.UrlFactory;
import com.spark.newbitrade.model.login.CasLoginModel;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.spark.newbitrade.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2018/2/25.
 */

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View view;
    private DataSource dataRepository;
    private CasLoginModel casLoginModel;

    public MainPresenter(DataSource dataRepository, MainContract.View view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
        this.casLoginModel = new CasLoginModel();
    }

    @Override
    public void allCurrency() {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getAllCurrency(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    List<Currency> currencies = new Gson().fromJson(response, new TypeToken<List<Currency>>() {
                    }.getType());
                    view.allCurrencySuccess(currencies);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (StringUtils.isNotEmpty(response)) { // 请求出错且有返回数据的状况，用此种方法解析
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            view.allCurrencyFail(jsonObject.optInt("code"), jsonObject.optInt("code") + "：" + jsonObject.optString("message"));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        view.allCurrencyFail(JSON_ERROR, null);
                    }

                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.allCurrencyFail(JSON_ERROR, null);
            }
        });
    }

    @Override
    public void tradeLogin() {
        view.displayLoadingPopup();
        dataRepository.doStringGet(UrlFactory.getTradeLogin(), new DataSource.DataCallback() { // 携带固定的cookie
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    if (object.optInt("code") == GlobalConstant.SUCCESS_SEC_CODE) {
                        String code = object.getString("data");
                        view.tradeLoginSuccess(code);
                    } else {
                        view.doPostFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    view.allCurrencyFail(JSON_ERROR, null);
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
    public void getNewVersion() {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getNewVision(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == GlobalConstant.SUCCESS_CODE) {
                        Vision vision = new Gson().fromJson(object.toString(), new TypeToken<Vision>() {
                        }.getType());
                        view.getNewVersionSuccess(vision);
                    } else {
                        //view.doPostFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    view.doPostFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doPostFail(JSON_ERROR, null);
            }
        });
    }

    @Override
    public void getUserInfo() {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getUserInfo(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                try {
                    String response = (String) obj;
                    JSONObject object = new JSONObject(response);
                    int code = object.optInt("code");
                    if (code == GlobalConstant.SUCCESS_SEC_CODE || code == GlobalConstant.SUCCESS_CODE) { //  exchange登录涉及到发指令登录，所以不管cookie是否被清除，都触发一次交易登录
                        view.getUserInfoSuccess();
                    } else {
                        LogUtils.i("code==" + object.optInt("code") + ",,message==" + object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtils.i("解析失败");
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                LogUtils.i("onDataNotAvailable");
            }
        });
    }


    @Override
    public void checkBusinessLogin(String type) {
        view.displayLoadingPopup();
        casLoginModel.checkBusinessLogin(type, new ResponseCallBack.SuccessListener<CasLoginEntity>() {
            @Override
            public void onResponse(CasLoginEntity response) {
                view.hideLoadingPopup();
                if (view != null)
                    view.checkBusinessLoginSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                view.hideLoadingPopup();
                if (view != null)
                    view.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                view.hideLoadingPopup();
                if (view != null)
                    view.dealError(volleyError);
            }
        });
    }

    @Override
    public void doLoginBusiness(String tgc, String type) {
        view.displayLoadingPopup();
        casLoginModel.getBussinessTicket(tgc, type, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                view.hideLoadingPopup();
                if (view != null)
                    view.doLoginBusinessSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                view.hideLoadingPopup();
                if (view != null)
                    view.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                view.hideLoadingPopup();
                if (view != null)
                    view.dealError(volleyError);
            }
        });
    }

}
