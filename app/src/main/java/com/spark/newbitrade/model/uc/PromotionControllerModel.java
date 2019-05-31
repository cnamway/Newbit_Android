package com.spark.newbitrade.model.uc;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.spark.library.uc.api.MemberControllerApi;
import com.spark.library.uc.api.PromotionControllerApi;
import com.spark.library.uc.model.MemberPromotionVo;
import com.spark.library.uc.model.MemberRewardRecord;
import com.spark.library.uc.model.MessageResult;
import com.spark.library.uc.model.MessageResultMemberVo;
import com.spark.library.uc.model.MessageResultPageMemberPromotionVo;
import com.spark.library.uc.model.MessageResultPageMemberRewardRecord;
import com.spark.library.uc.model.PageMemberPromotionVo;
import com.spark.library.uc.model.QueryParamMemberPromotionVo;
import com.spark.library.uc.model.QueryParamMemberRewardRecord;
import com.spark.library.uc.model.TradePasswordSetDto;
import com.spark.library.uc.model.UpdateTradePasswordDto;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.entity.User;
import com.spark.newbitrade.factory.HttpUrls;
import com.spark.newbitrade.utils.LogUtils;

import java.util.HashMap;
import java.util.List;

import static com.spark.newbitrade.utils.GlobalConstant.SUCCESS_CODE;


/**
 * 推广有关的请求方法
 */

public class PromotionControllerModel {
    private PromotionControllerApi promotionControllerApi;

    public PromotionControllerModel() {
        promotionControllerApi = new PromotionControllerApi();
        promotionControllerApi.setBasePath(HttpUrls.UC_HOST);
    }

    /**
     * 查询推广记录
     */
    public void getPromotion(HashMap<String, String> params, final ResponseCallBack.SuccessListener<MessageResultPageMemberPromotionVo> successListener, final ResponseCallBack.ErrorListener errorListener) {
        final QueryParamMemberPromotionVo queryParam = new QueryParamMemberPromotionVo();
        queryParam.setPageIndex(Integer.parseInt(params.get("pageNo")));
        queryParam.setPageSize(Integer.parseInt(params.get("pageSize")));
        queryParam.setSortFields("createTime_d");
        new Thread(new Runnable() {
            @Override
            public void run() {
                promotionControllerApi.pagePromotionRecordUsingPOST(queryParam, new Response.Listener<MessageResultPageMemberPromotionVo>() {
                    @Override
                    public void onResponse(MessageResultPageMemberPromotionVo response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null)
                                successListener.onResponse(response);
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
     * 查询佣金记录
     */
    public void getPromotionReward(HashMap<String, String> params, final ResponseCallBack.SuccessListener<MessageResultPageMemberRewardRecord> successListener, final ResponseCallBack.ErrorListener errorListener) {
        final QueryParamMemberRewardRecord queryParam = new QueryParamMemberRewardRecord();
        queryParam.setPageIndex(Integer.parseInt(params.get("pageNo")));
        queryParam.setPageSize(Integer.parseInt(params.get("pageSize")));
        queryParam.setSortFields("createTime_d");
        new Thread(new Runnable() {
            @Override
            public void run() {
                promotionControllerApi.promotionUsingPOST(queryParam, new Response.Listener<MessageResultPageMemberRewardRecord>() {
                    @Override
                    public void onResponse(MessageResultPageMemberRewardRecord response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null)
                                successListener.onResponse(response);
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
