package com.spark.newbitrade.model.otc;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.library.otc.api.TradeControllerApi;
import com.spark.library.otc.model.MemberPayType;
import com.spark.library.otc.model.MessageResult;
import com.spark.library.otc.model.MessageResultListMemberPayType;
import com.spark.library.otc.model.MessageResultOrderDetailVo;
import com.spark.library.otc.model.MessageResultPageChatMessageRecord;
import com.spark.library.otc.model.MessageResultPageOrderVo;
import com.spark.library.otc.model.OrderDetailVo;
import com.spark.library.otc.model.OrderInTransitDto;
import com.spark.library.otc.model.OrderPaymentDto;
import com.spark.library.otc.model.PageOrderVo;
import com.spark.library.otc.model.QueryCondition;
import com.spark.library.otc.model.QueryParamChatMessageRecord;
import com.spark.library.otc.model.QueryParamOrderVo;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.ChatEntity;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.factory.HttpUrls;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.spark.newbitrade.utils.GlobalConstant.SUCCESS_CODE;

/**
 * otc业务模块
 */

public class TradeControllerModel {
    private TradeControllerApi tradeControllerApi;

    public TradeControllerModel() {
        tradeControllerApi = new TradeControllerApi();
        tradeControllerApi.setBasePath(HttpUrls.OTC_HOST);
    }

    /**
     * 创建订单请求
     */
    public void createOrder(final OrderInTransitDto orderInTransitDto, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                tradeControllerApi.createOrderUsingPOST(orderInTransitDto, new Response.Listener<MessageResult>() {
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
     * 取消订单
     */
    public void cancelOrderUsingGET(final String orderId, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                tradeControllerApi.cancelOrderUsingGET(orderId, new Response.Listener<MessageResult>() {
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
     * 查询我的归档订单
     */
    public void findMyOrderAchiveUsingPOST(final QueryParamOrderVo queryParam, final ResponseCallBack.SuccessListener<PageOrderVo> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                tradeControllerApi.findMyOrderAchiveUsingPOST(queryParam, new Response.Listener<MessageResultPageOrderVo>() {
                    @Override
                    public void onResponse(MessageResultPageOrderVo response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null)
                                successListener.onResponse(response.getData());
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
     * 查询我的全部订单
     */
    public void findMyOrderAllUsingPOST(final QueryParamOrderVo queryParam, final ResponseCallBack.SuccessListener<PageOrderVo> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                tradeControllerApi.findMyOrderAllUsingPOST(queryParam, new Response.Listener<MessageResultPageOrderVo>() {
                    @Override
                    public void onResponse(MessageResultPageOrderVo response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null)
                                successListener.onResponse(response.getData());
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
     * 查询我的在途订单
     */
    public void findMyOrderInTransitUsingPOST(final QueryParamOrderVo queryParam, final ResponseCallBack.SuccessListener<PageOrderVo> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                tradeControllerApi.findMyOrderInTransitUsingPOST(queryParam, new Response.Listener<MessageResultPageOrderVo>() {
                    @Override
                    public void onResponse(MessageResultPageOrderVo response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null)
                                successListener.onResponse(response.getData());
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
     * 查询归档订单详情
     */
    public void findOrderAchiveDetailUsingGET(final String orderId, final ResponseCallBack.SuccessListener<OrderDetailVo> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                tradeControllerApi.findOrderAchiveDetailUsingGET(orderId, new Response.Listener<MessageResultOrderDetailVo>() {
                    @Override
                    public void onResponse(MessageResultOrderDetailVo response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null)
                                successListener.onResponse(response.getData());
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
     * 查询在途订单详情
     */
    public void findOrderInTransitDetailUsingGET(final String orderId, final ResponseCallBack.SuccessListener<OrderDetailVo> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                tradeControllerApi.findOrderInTransitDetailUsingGET(orderId, new Response.Listener<MessageResultOrderDetailVo>() {
                    @Override
                    public void onResponse(MessageResultOrderDetailVo response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null)
                                successListener.onResponse(response.getData());
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
     * 确认付款
     */
    public void paymentOrderUsingPOST(final OrderPaymentDto orderPaymentDto, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                tradeControllerApi.paymentOrderUsingPOST(orderPaymentDto, new Response.Listener<MessageResult>() {
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
     * 查询订单支持的聊天信息
     */
    public void queryOrderChatDataUsingPOST(final String orderId, HashMap<String, String> params, final ResponseCallBack.SuccessListener<List<ChatEntity>> successListener, final ResponseCallBack.ErrorListener errorListener) {
        final QueryParamChatMessageRecord queryParam = new QueryParamChatMessageRecord();
        queryParam.setPageIndex(Integer.parseInt(params.get("pageNo")));
        queryParam.setPageSize(Integer.parseInt(params.get("pageSize")));
        queryParam.setSortFields("sendTime_d");
        List<QueryCondition> queryConditions = new ArrayList<>();
        QueryCondition queryCondition = new QueryCondition();
        queryParam.setQueryList(queryConditions);
        new Thread(new Runnable() {
            @Override
            public void run() {
                tradeControllerApi.queryOrderChatDataUsingPOST(orderId, queryParam, new Response.Listener<MessageResultPageChatMessageRecord>() {
                    @Override
                    public void onResponse(MessageResultPageChatMessageRecord response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null) {
                                List<ChatEntity> list = new Gson().fromJson(new Gson().toJson(response.getData().getRecords()), new TypeToken<List<ChatEntity>>() {
                                }.getType());
                                successListener.onResponse(list);
                            }

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
     * 查询订单支持的支付信息
     */
    public void queryOrderPayTypeUsingGET(final String orderId, final ResponseCallBack.SuccessListener<List<MemberPayType>> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                tradeControllerApi.queryOrderPayTypeUsingGET(orderId, new Response.Listener<MessageResultListMemberPayType>() {
                    @Override
                    public void onResponse(MessageResultListMemberPayType response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null)
                                successListener.onResponse(response.getData());
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
     * 订单放行
     */
    public void releaseOrderUsingGET(final String orderId, final String tradePwd, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                tradeControllerApi.releaseOrderUsingGET(orderId, tradePwd, new Response.Listener<MessageResult>() {
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
