package com.spark.newbitrade.model.uc;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.spark.library.uc.api.MemberControllerApi;
import com.spark.library.uc.model.MessageResult;
import com.spark.library.uc.model.MessageResultMemberVo;
import com.spark.library.uc.model.ResetLoginPasswordDto;
import com.spark.library.uc.model.TradePasswordSetDto;
import com.spark.library.uc.model.UpdateTradePasswordDto;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.entity.User;
import com.spark.newbitrade.factory.HttpUrls;
import com.spark.newbitrade.utils.LogUtils;

import static com.spark.newbitrade.utils.GlobalConstant.SUCCESS_CODE;


/**
 * 用户有关的请求方法
 */

public class MemberControllerModel {
    private MemberControllerApi memberControllerApi;

    public MemberControllerModel() {
        memberControllerApi = new MemberControllerApi();
        memberControllerApi.setBasePath(HttpUrls.UC_HOST);
    }

    /**
     * 获取用户信息
     */
    public void getUserInfo(final ResponseCallBack.SuccessListener<User> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                memberControllerApi.getUserInfoUsingPOST(new Response.Listener<MessageResultMemberVo>() {
                    @Override
                    public void onResponse(MessageResultMemberVo response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            Gson gson = new Gson();
                            User user = gson.fromJson(gson.toJson(response.getData()), User.class);
                            if (successListener != null)
                                successListener.onResponse(user);
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
     * 更改资金密码
     */
    public void updateTradePassword(String oldPassword, String newPassword, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        final UpdateTradePasswordDto updateTradePasswordDto = new UpdateTradePasswordDto();
        updateTradePasswordDto.setOldPassword(oldPassword);
        updateTradePasswordDto.setNewPassword(newPassword);
        new Thread(new Runnable() {
            @Override
            public void run() {
                memberControllerApi.updateTradePasswordUsingPOST(updateTradePasswordDto, new Response.Listener<MessageResult>() {
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
     * 设置资金密码
     */
    public void setTradePassword(final String tradePassword, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        final TradePasswordSetDto tradePasswordSetDto = new TradePasswordSetDto();
        tradePasswordSetDto.setTradePassword(tradePassword);
        new Thread(new Runnable() {
            @Override
            public void run() {
                memberControllerApi.setTradePasswordUsingPOST(tradePasswordSetDto, new Response.Listener<MessageResult>() {
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
     * 忘记密码
     */
    public void doForget(String phone, String password, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        final ResetLoginPasswordDto resetLoginPasswordDto = new ResetLoginPasswordDto();
        resetLoginPasswordDto.setMobilePhone(phone);
        resetLoginPasswordDto.setNewPassword(password);
        new Thread(new Runnable() {
            @Override
            public void run() {
                memberControllerApi.resetLoginPasswordUsingPOST(resetLoginPasswordDto, new Response.Listener<MessageResult>() {
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
