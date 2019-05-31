package com.spark.newbitrade.model.uc;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.library.uc.api.RegisterControllerApi;
import com.spark.library.uc.model.MessageResult;
import com.spark.library.uc.model.MessageResultListCountry;
import com.spark.library.uc.model.RegisterByEmailDto;
import com.spark.library.uc.model.RegisterByPhoneDto;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.CountryEntity;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.factory.HttpUrls;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.okhttp.AppUtils;

import java.util.List;

import static com.spark.newbitrade.utils.GlobalConstant.SUCCESS_CODE;


/**
 * 注册
 */

public class RegisterControllerModel {
    private RegisterControllerApi registerControllerApi;

    public RegisterControllerModel() {
        registerControllerApi = new RegisterControllerApi();
        registerControllerApi.setBasePath(HttpUrls.UC_HOST);
    }

    /**
     * 忘记密码
     */
    public void doForget(String password, String account, String code, String mode,
                         final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
    }

    /**
     * 获取国家列表
     */
    public void getCountry(final ResponseCallBack.SuccessListener<List<CountryEntity>> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                registerControllerApi.findSupportCountryUsingPOST(new Response.Listener<MessageResultListCountry>() {
                    @Override
                    public void onResponse(MessageResultListCountry response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            Gson gson = new Gson();
                            List<CountryEntity> objList = gson.fromJson(gson.toJson(response.getData()), new TypeToken<List<CountryEntity>>() {
                            }.getType());
                            if (successListener != null)
                                successListener.onResponse(objList);
                        } else {
                            if (errorListener != null)
                                errorListener.onErrorResponse(new HttpErrorEntity(response.getCode(), response.getMessage(), response.getUrl(), response.getCid()));
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (errorListener != null)
                            errorListener.onErrorResponse(error);
                    }
                });
            }
        }).start();
    }


    /**
     * 邮箱注册
     *
     * @param username
     * @param password
     * @param country
     * @param promotion
     * @param email
     * @param code
     */
    public void doSighUpByEmail(String username, String password, String country, String promotion, String email, String code,
                                final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        registerControllerApi.addHeader("check", "email:" + email + ":" + code);
        final RegisterByEmailDto registerByEmailDto = new RegisterByEmailDto();
        registerByEmailDto.setCountry(country);
        registerByEmailDto.setEmail(email);
        registerByEmailDto.setPassword(password);
        registerByEmailDto.setPromotion(promotion);
        registerByEmailDto.setTid(AppUtils.getSerialNumber());
        registerByEmailDto.setUsername(username);
        new Thread(new Runnable() {
            @Override
            public void run() {
                registerControllerApi.registerByEmailUsingPOST(registerByEmailDto, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null)
                                successListener.onResponse(response.getMessage());
                        } else {
                            if (errorListener != null)
                                errorListener.onErrorResponse(new HttpErrorEntity(response.getCode(), response.getMessage(), response.getUrl(), response.getCid()));
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (errorListener != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * 手机注册
     *
     * @param username
     * @param password
     * @param country
     * @param promotion
     * @param phone
     * @param code
     */
    public void doSighUpByPhone(String username, String password, String country, String promotion, String phone, String code,
                                final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        registerControllerApi.addHeader("check", "phone:" + phone + ":" + code);
        final RegisterByPhoneDto registerByPhoneDto = new RegisterByPhoneDto();
        registerByPhoneDto.setCountry(country);
        registerByPhoneDto.setMobilePhone(phone);
        registerByPhoneDto.setPassword(password);
        registerByPhoneDto.setPromotion(promotion);
        registerByPhoneDto.setTid(AppUtils.getSerialNumber());
        registerByPhoneDto.setUsername(username);
        new Thread(new Runnable() {
            @Override
            public void run() {
                registerControllerApi.registerByPhoneUsingPOST(registerByPhoneDto, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null)
                                successListener.onResponse(response.getMessage());
                        } else {
                            if (errorListener != null)
                                errorListener.onErrorResponse(new HttpErrorEntity(response.getCode(), response.getMessage(), response.getUrl(), response.getCid()));
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (errorListener != null)
                            errorListener.onErrorResponse(error);
                    }
                });
            }
        }).start();
    }


}
