package com.spark.newbitrade.model.uc;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.spark.library.uc.api.CaptchaGetControllerApi;
import com.spark.library.uc.model.MessageResult;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.factory.HttpUrls;
import com.spark.newbitrade.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import static com.spark.newbitrade.utils.GlobalConstant.SUCCESS_CODE;


/**
 * 校验-uc
 */

public class CaptchaGetControllerModel {
    private CaptchaGetControllerApi captchaGetControllerApi;

    public CaptchaGetControllerModel() {
        captchaGetControllerApi = new CaptchaGetControllerApi();
        captchaGetControllerApi.setBasePath(HttpUrls.UC_HOST);
    }

    /**
     * 获取手机验证码
     */
    public void getCodeByPhone(final String phone,
                               final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                captchaGetControllerApi.getPhoneCaptchaUsingGET(phone, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null)
                                successListener.onResponse(response.getMessage());
                        } else {
                            if (errorListener != null)
                                errorListener.onErrorResponse(new HttpErrorEntity(response.getCode(), response.getMessage(), response.getUrl(), response.getCid(), (String) response.getData()));
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
     * 获取手机验证码,带有header
     */
    public void getCodeByPhone(final String phone, String check, String cid,
                               final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        captchaGetControllerApi.addHeader("check", check);
        captchaGetControllerApi.addHeader("cid", cid);
        new Thread(new Runnable() {
            @Override
            public void run() {
                captchaGetControllerApi.getPhoneCaptchaUsingGET(phone, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response);
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


    /**
     * 获取邮箱验证码
     */
    public void getCodeByEmail(final String email,
                               final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                captchaGetControllerApi.getEmailCaptchaUsingGET(email, new Response.Listener<MessageResult>() {
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

    /**
     * 极验
     */
    public void doCaptch(final ResponseCallBack.SuccessListener<JSONObject> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                captchaGetControllerApi.getGeeCaptchaUsingGET(new Response.Listener<Object>() {
                    @Override
                    public void onResponse(Object response) {
                        LogUtils.i("response==" + response.toString());
                        try {
                            JSONObject object = new JSONObject(response.toString().replace("\\", ""));
                            if (successListener != null)
                                successListener.onResponse(object);
                        } catch (JSONException e) {
                            e.printStackTrace();
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
     * 校验验证码
     */
    public void checkCaptcha(final ResponseCallBack.SuccessListener<JSONObject> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                captchaGetControllerApi.checkCaptchaUsingGET(new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response.toString());
//                        try {
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null)
                                successListener.onResponse(new JSONObject());
                        }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
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
     * 校验极限验证
     */
    public void checkCaptcha(String check, String cid,
                               final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        captchaGetControllerApi.addHeader("check", check);
        captchaGetControllerApi.addHeader("cid", cid);
        new Thread(new Runnable() {
            @Override
            public void run() {
                captchaGetControllerApi.checkCaptchaUsingGET( new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response);
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

    /**
     * 获取手机验证码
     *
     * @param phone
     * @param successListener
     * @param errorListener
     */
    public void getCodeByPhone(final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        //captchaGetControllerApi.addHeader("Cookie", SharedPreferencesUtil.getInstance(BaseApplication.getAppContext()).getUcSid());
        new Thread(new Runnable() {
            @Override
            public void run() {
                captchaGetControllerApi.getValidateCaptchaUsingGET("phone", new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null)
                                successListener.onResponse(response.getMessage());
                        } else {
                            if (errorListener != null)
                                errorListener.onErrorResponse(new HttpErrorEntity(response.getCode(), response.getMessage(), response.getUrl(), response.getCid(), (String) response.getData()));
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
     * 获取手机验证码,带有header
     *
     * @param phone
     * @param check
     * @param cid
     * @param successListener
     * @param errorListener
     */
    public void getCodeByPhoneWithHead(String check, String cid, final String type,
                                       final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        captchaGetControllerApi.addHeader("check", check);
        captchaGetControllerApi.addHeader("cid", cid);
        //captchaGetControllerApi.addHeader("Cookie", SharedPreferencesUtil.getInstance(BaseApplication.getAppContext()).getUcSid());
        new Thread(new Runnable() {
            @Override
            public void run() {
                captchaGetControllerApi.getValidateCaptchaUsingGET(type, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null)
                                successListener.onResponse(response.getMessage());
                        } else {
                            if (errorListener != null)
                                errorListener.onErrorResponse(new HttpErrorEntity(response.getCode(), response.getMessage(), response.getUrl(), response.getCid(), (String) response.getData()));
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
