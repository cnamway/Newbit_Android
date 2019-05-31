package com.spark.newbitrade.model.uc;

import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.data.SendRemoteDataUtil;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.factory.HttpUrls;
import com.spark.newbitrade.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.newbitrade.utils.GlobalConstant.JSON_ERROR;
import static com.spark.newbitrade.utils.GlobalConstant.SUCCESS_CODE;


/**
 * 账户设置参数
 */

public class UcModel {

    /**
     * 上传头像
     *
     * @param url
     */
    public void avatar(final String url, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("avatar", url);
        SendRemoteDataUtil.getInstance().doStringPost(HttpUrls.getAvatarUrl(), map, new SendRemoteDataUtil.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    int code = StringUtils.getCode(object);
                    if (code == SUCCESS_CODE) {
                        if (successListener != null)
                            successListener.onResponse(StringUtils.getMessage(object));
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
                if (errorListener != null)
                    errorListener.onErrorResponse(httpErrorEntity);
            }
        });
    }

    /**
     * 绑定手机号
     *
     * @param phone
     * @param password
     * @param code
     */
    public void bindPhone(String phone, String password, final String code, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("password", password);
        map.put("code", code);
        SendRemoteDataUtil.getInstance().doStringPost(HttpUrls.getBindPhoneUrl(), map, new SendRemoteDataUtil.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    int code = StringUtils.getCode(object);
                    if (code == SUCCESS_CODE) {
                        if (successListener != null)
                            successListener.onResponse(StringUtils.getMessage(object));
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
                if (errorListener != null)
                    errorListener.onErrorResponse(httpErrorEntity);
            }
        });
    }

    /**
     * 获取当前绑定手机的手机号码
     *
     * @param type
     */
    public void sendChangePhoneCode(String type, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", type);
        SendRemoteDataUtil.getInstance().doStringPost(HttpUrls.getSendCodeAfterLoginUrl(), map, new SendRemoteDataUtil.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    int code = StringUtils.getCode(object);
                    if (code == SUCCESS_CODE) {
                        if (successListener != null)
                            successListener.onResponse(StringUtils.getMessage(object));
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
                if (errorListener != null)
                    errorListener.onErrorResponse(httpErrorEntity);
            }
        });
    }

    /**
     * 修改手机号
     */
    public void changePhone(String phone, String password, String code, String newCode, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("password", password);
        map.put("oldCode", code);
        map.put("newCode", newCode);
        SendRemoteDataUtil.getInstance().doStringPost(HttpUrls.getChangePhoneUrl(), map, new SendRemoteDataUtil.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    int code = StringUtils.getCode(object);
                    if (code == SUCCESS_CODE) {
                        if (successListener != null)
                            successListener.onResponse(StringUtils.getMessage(object));
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
                if (errorListener != null)
                    errorListener.onErrorResponse(httpErrorEntity);
            }
        });
    }


}