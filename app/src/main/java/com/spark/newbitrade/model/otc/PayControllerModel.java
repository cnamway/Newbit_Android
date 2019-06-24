package com.spark.newbitrade.model.otc;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.library.otc.api.PayControllerApi;
import com.spark.library.otc.model.MemberPayTypeDto;
import com.spark.library.otc.model.MessageResult;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.entity.PayWaySetting;
import com.spark.newbitrade.factory.HttpUrls;
import com.spark.newbitrade.utils.LogUtils;

import java.util.List;

import static com.spark.newbitrade.utils.GlobalConstant.SUCCESS_CODE;

/**
 * Created by Administrator on 2019/3/15 0015.
 */

public class PayControllerModel {
    private PayControllerApi payControllerApi;

    public PayControllerModel() {
        this.payControllerApi = new PayControllerApi();
        this.payControllerApi.setBasePath(HttpUrls.OTC_HOST);
    }

    /**
     * 添加
     */
    public void doBindBank(String payType, String payAddress, String bank, String branch, String tradePassword, String qrCodeUrl, String name, final ResponseCallBack.SuccessListener<MessageResult> listener, final ResponseCallBack.ErrorListener errorListener) {
        final MemberPayTypeDto memberPayTypeDto = new MemberPayTypeDto();
        memberPayTypeDto.setPayType(payType);
        memberPayTypeDto.setPayAddress(payAddress);
        memberPayTypeDto.setBank(bank);
        memberPayTypeDto.setBranch(branch);
        memberPayTypeDto.setTradePwd(tradePassword);
        memberPayTypeDto.setQrCodeUrl(qrCodeUrl);
        memberPayTypeDto.setRealName(name);
        new Thread(new Runnable() {
            @Override
            public void run() {
                payControllerApi.addUsingPOST(memberPayTypeDto, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (listener != null) {
                                listener.onResponse(response);
                            }
                        } else {
                            if (errorListener != null) {
                                errorListener.onErrorResponse(new HttpErrorEntity(response.getCode(), response.getMessage(), response.getUrl(), response.getCid()));
                            }
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
     * 查询
     */
    public void queryListUsingGET(final ResponseCallBack.SuccessListener<List<PayWaySetting>> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                payControllerApi.queryListUsingGET(new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            Gson gson = new Gson();
                            List<PayWaySetting> objList = gson.fromJson(gson.toJson(response.getData()), new TypeToken<List<PayWaySetting>>() {
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
     * 修改
     */
    public void doUpdateBank(final Long id, String payType, String payAddress, String bank, String branch, String tradePassword, String qrCodeUrl, String name, final ResponseCallBack.SuccessListener<MessageResult> listener, final ResponseCallBack.ErrorListener errorListener) {
        final MemberPayTypeDto memberPayTypeDto = new MemberPayTypeDto();
        memberPayTypeDto.setPayType(payType);
        memberPayTypeDto.setPayAddress(payAddress);
        memberPayTypeDto.setBank(bank);
        memberPayTypeDto.setBranch(branch);
        memberPayTypeDto.setTradePwd(tradePassword);
        memberPayTypeDto.setQrCodeUrl(qrCodeUrl);
        memberPayTypeDto.setRealName(name);
        new Thread(new Runnable() {
            @Override
            public void run() {
                payControllerApi.updateUsingPOST(memberPayTypeDto, id, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (listener != null) {
                                listener.onResponse(response);
                            }
                        } else {
                            if (errorListener != null) {
                                errorListener.onErrorResponse(new HttpErrorEntity(response.getCode(), response.getMessage(), response.getUrl(), response.getCid()));
                            }
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
     * 修改状态
     */
    public void doUpdateStatusBank(final Long id, final Integer status, final ResponseCallBack.SuccessListener<MessageResult> listener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                payControllerApi.updateStatusUsingGET(id, status, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (listener != null) {
                                listener.onResponse(response);
                            }
                        } else {
                            if (errorListener != null) {
                                errorListener.onErrorResponse(new HttpErrorEntity(response.getCode(), response.getMessage(), response.getUrl(), response.getCid()));
                            }
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
     * 删除
     */
    public void doDeleteBank(final Long id, final String pws, final ResponseCallBack.SuccessListener<MessageResult> listener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                payControllerApi.deleteUsingPOST(id, pws, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (listener != null) {
                                listener.onResponse(response);
                            }
                        } else {
                            if (errorListener != null) {
                                errorListener.onErrorResponse(new HttpErrorEntity(response.getCode(), response.getMessage(), response.getUrl(), response.getCid()));
                            }
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


}
