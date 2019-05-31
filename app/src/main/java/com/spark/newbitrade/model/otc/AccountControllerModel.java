package com.spark.newbitrade.model.otc;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.spark.library.otc.api.AccountControllerApi;
import com.spark.library.otc.model.MemberAccount;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.factory.HttpUrls;
import com.spark.newbitrade.utils.LogUtils;

/**
 * otc业务模块
 */

public class AccountControllerModel {
    private AccountControllerApi accountControllerApi;

    public AccountControllerModel() {
        accountControllerApi = new AccountControllerApi();
        accountControllerApi.setBasePath(HttpUrls.OTC_HOST);
    }

    /**
     * 获取otc账本
     */
    public void findAccount(final Long memberId, final String coinName, final ResponseCallBack.SuccessListener<MemberAccount> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                accountControllerApi.findAccountUsingGET(memberId, coinName, new Response.Listener<MemberAccount>() {
                    @Override
                    public void onResponse(MemberAccount response) {
                        LogUtils.i("response==" + response.toString());
                        if (successListener != null)
                            successListener.onResponse(response);

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
