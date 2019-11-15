package com.spark.newbitrade.model.login;


import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.data.SendRemoteDataUtil;
import com.spark.newbitrade.entity.CasLoginEntity;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.factory.HttpUrls;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.SharedPreferenceInstance;
import com.spark.newbitrade.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.newbitrade.utils.GlobalConstant.JSON_ERROR;
import static com.spark.newbitrade.utils.GlobalConstant.SUCCESS_CODE;


/**
 * casLogin业务封装
 */
public class CasLoginModel {

    /**
     * 登录cas
     *
     * @param username
     * @param password
     */
    public void casLogn(String username, String password, String rememberMe, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("rememberMe", rememberMe);
        SendRemoteDataUtil.getInstance().doStringPost(HttpUrls.getInstance().getCasLogin(), params, new SendRemoteDataUtil.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                try {
                    String response = (String) obj;
                    JSONObject object = new JSONObject(response);
                    String tgt = object.optString("tgc");
                    if (StringUtils.isEmpty(tgt)) {
                        if (errorListener != null)
                            errorListener.onErrorResponse(new HttpErrorEntity(object));
                    } else {
                        SharedPreferenceInstance.getInstance().setTgt(tgt);
                        if (successListener != null)
                            successListener.onResponse(tgt);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    if (errorListener != null)
                        errorListener.onErrorResponse(new HttpErrorEntity(JSON_ERROR, "", "", ""));
                }
            }

            @Override
            public void onDataNotAvailable(HttpErrorEntity httpErrorEntity) {
                if (errorListener != null) {
                    errorListener.onErrorResponse(httpErrorEntity);
                }
            }
        });
    }

    /**
     * 发送验证码
     */
    public void sendVertifyCode(String phone, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("type", "phone");
        SendRemoteDataUtil.getInstance().doStringGetWithHead(HttpUrls.getInstance().getSendVertifyCodeUrl(), params, new SendRemoteDataUtil.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                LogUtils.i(obj.toString());
                successListener.onResponse((String) obj);
            }

            @Override
            public void onDataNotAvailable(HttpErrorEntity httpErrorEntity) {
                if (errorListener != null) {
                    errorListener.onErrorResponse(httpErrorEntity);
                }
            }
        });
    }

    /**
     * 验证短信验证码
     */
    public void phoneCodeCheck(String code, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        HashMap<String, String> params = new HashMap<>();
        SendRemoteDataUtil.getInstance().doStringGetWithHeadAndCheck(HttpUrls.getInstance().getVertifyCodeUrl(), code, params, new SendRemoteDataUtil.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                LogUtils.i(obj.toString());
                successListener.onResponse((String) obj);
            }

            @Override
            public void onDataNotAvailable(HttpErrorEntity httpErrorEntity) {
                if (errorListener != null) {
                    errorListener.onErrorResponse(httpErrorEntity);
                }
            }
        });
    }

    /**
     * 获取ticket
     *
     * @param gtc
     * @param type
     */
    public void getBussinessTicket(String gtc, final String type, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("service", HttpUrls.getInstance().getService(type));
        SendRemoteDataUtil.getInstance().doStringPost(HttpUrls.getInstance().getCasLogin() + "/" + gtc, params, new SendRemoteDataUtil.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    int code = StringUtils.getCode(object);
                    if (code != SUCCESS_CODE) {
                        if (errorListener != null)
                            errorListener.onErrorResponse(new HttpErrorEntity(object));
                    }
                } catch (JSONException e) {
                    doBussionLogin((String) obj, type, successListener, errorListener);
                }
            }

            @Override
            public void onDataNotAvailable(HttpErrorEntity httpErrorEntity) {
                if (errorListener != null) {
                    errorListener.onErrorResponse(httpErrorEntity);
                }
            }
        });
    }

    /**
     * 登录业务系统
     */
    public void doBussionLogin(String ticket, final String type, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("ticket", ticket);
        SendRemoteDataUtil.getInstance().doStringGet(HttpUrls.getInstance().getCasTickets(type), params, new SendRemoteDataUtil.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    int code = StringUtils.getCode(object);
                    if (code == SUCCESS_CODE) {
                        if (successListener != null)
                            successListener.onResponse(type);
                    } else {
                        if (errorListener != null)
                            errorListener.onErrorResponse(new HttpErrorEntity(object));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (errorListener != null)
                        errorListener.onErrorResponse(new HttpErrorEntity(JSON_ERROR, "", "", ""));
                }
            }

            @Override
            public void onDataNotAvailable(HttpErrorEntity httpErrorEntity) {
                if (errorListener != null) {
                    errorListener.onErrorResponse(httpErrorEntity);
                }
            }
        });
    }

    /**
     * 2.4 业务系统登录状态查询接口
     */
    public void checkBusinessLogin(final String type, final ResponseCallBack.SuccessListener<CasLoginEntity> successListener, final ResponseCallBack.ErrorListener errorListener) {
        SendRemoteDataUtil.getInstance().doStringPost(HttpUrls.getInstance().getCheckTicket(type), new HashMap<String, String>(), new SendRemoteDataUtil.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                if (StringUtils.isNotEmpty((String) obj)) {
                    if (obj.equals("true")) {
                        if (successListener != null)
                            successListener.onResponse(new CasLoginEntity(true, type));
                    } else {
                        if (successListener != null)
                            successListener.onResponse(new CasLoginEntity(false, type));
                    }
                }
            }

            @Override
            public void onDataNotAvailable(HttpErrorEntity httpErrorEntity) {
                if (errorListener != null) {
                    errorListener.onErrorResponse(httpErrorEntity);
                }
            }
        });
    }

    /**
     * 登出
     */
    public void loginOut(String tgt, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        HashMap<String, String> params = new HashMap<>();
        SendRemoteDataUtil.getInstance().doStringDelete(HttpUrls.getInstance().getCasLogin() + "/" + tgt, params, new SendRemoteDataUtil.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                if (successListener != null)
                    successListener.onResponse((String) obj);
            }

            @Override
            public void onDataNotAvailable(HttpErrorEntity httpErrorEntity) {
                if (errorListener != null) {
                    errorListener.onErrorResponse(httpErrorEntity);
                }
            }
        });
    }

}
