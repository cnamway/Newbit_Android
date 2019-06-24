package com.spark.newbitrade.model.otc;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.library.otc.api.AdvertiseSelfControllerApi;
import com.spark.library.otc.model.AdvertiseDto;
import com.spark.library.otc.model.AuthMerchantApplyMarginType;
import com.spark.library.otc.model.Dict;
import com.spark.library.otc.model.MessageResult;
import com.spark.library.otc.model.MessageResultAdvertiseDetailVo;
import com.spark.library.otc.model.MessageResultListAdvertiseVo;
import com.spark.library.otc.model.MessageResultListAuthMerchantApplyMarginType;
import com.spark.library.otc.model.MessageResultListDict;
import com.spark.library.otc.model.MessageResultPageAdvertiseVo;
import com.spark.library.otc.model.QueryParamAdvertiseVo;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.Ads;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.factory.HttpUrls;

import java.util.List;

import static com.spark.newbitrade.utils.GlobalConstant.SUCCESS_CODE;

/**
 * otc业务模块
 */

public class AdvertiseSelfControllerModel {
    private AdvertiseSelfControllerApi advertiseSelfControllerApi;

    public AdvertiseSelfControllerModel() {
        advertiseSelfControllerApi = new AdvertiseSelfControllerApi();
        advertiseSelfControllerApi.setBasePath(HttpUrls.OTC_HOST);
    }

    /**
     * 创建广告
     */
    public void createAdvertise(final AdvertiseDto advertiseDto, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        LogUtils.i("创建广告advertiseDto==" + advertiseDto.toString());
        new Thread(new Runnable() {
            @Override
            public void run() {
                advertiseSelfControllerApi.createAdvertiseUsingPOST(advertiseDto, new Response.Listener<MessageResult>() {
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
     * 删除广告
     */
    public void deleteAdvertise(final Long advertiseId, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                advertiseSelfControllerApi.deleteAdvertiseUsingPOST(advertiseId, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
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
     * 查询我的广告详情
     */
    public void findAdvertiseDetail(final Long advertiseId, final ResponseCallBack.SuccessListener<Ads> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                advertiseSelfControllerApi.findAdvertiseDetailUsingGET(advertiseId, new Response.Listener<MessageResultAdvertiseDetailVo>() {
                    @Override
                    public void onResponse(MessageResultAdvertiseDetailVo response) {
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            Gson gson = new Gson();
                            Ads ads = gson.fromJson(gson.toJson(response.getData()), new TypeToken<Ads>() {
                            }.getType());
                            if (successListener != null)
                                successListener.onResponse(ads);
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
     * 广告级别类型
     */
    public void listAuthMerchantType(final ResponseCallBack.SuccessListener<List<Dict>> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                advertiseSelfControllerApi.listAuthMerchantTypeUsingGET(new Response.Listener<MessageResultListDict>() {
                    @Override
                    public void onResponse(MessageResultListDict response) {
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
     * 广告交易币种信息
     */
    public void listMerchantAdvertiseCoin(final ResponseCallBack.SuccessListener<List<AuthMerchantApplyMarginType>> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                advertiseSelfControllerApi.listMerchantAdvertiseCoinUsingGET(new Response.Listener<MessageResultListAuthMerchantApplyMarginType>() {
                    @Override
                    public void onResponse(MessageResultListAuthMerchantApplyMarginType response) {
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
     * 下架广告
     */
    public void offShelvesAdvertise(final Long advertiseId, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                advertiseSelfControllerApi.offShelvesAdvertiseUsingPOST(advertiseId, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
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
     * 上架广告
     */
    public void onShelvesAdvertise(final Long advertiseId, final String tradePwd, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                advertiseSelfControllerApi.onShelvesAdvertiseUsingPOST(advertiseId, tradePwd, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
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
     * 修改广告
     */
    public void updateAdvertise(final AdvertiseDto advertiseDto, final Long advertiseId, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                advertiseSelfControllerApi.updateAdvertiseUsingPOST(advertiseDto, advertiseId, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
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
     * 查询我的归档广告
     */
    public void findMyAdvertiseAchiveUsingPOST(final QueryParamAdvertiseVo queryParam, final ResponseCallBack.SuccessListener<List<Ads>> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                advertiseSelfControllerApi.findMyAdvertiseAchiveUsingPOST(queryParam, new Response.Listener<MessageResultPageAdvertiseVo>() {
                    @Override
                    public void onResponse(MessageResultPageAdvertiseVo response) {
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            Gson gson = new Gson();
                            List<Ads> adsList = gson.fromJson(gson.toJson(response.getData().getRecords()), new TypeToken<List<Ads>>() {
                            }.getType());
                            for (Ads ads : adsList) {
                                ads.setStatus(1);
                            }
                            if (successListener != null)
                                successListener.onResponse(adsList);
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
     * 查询我的上架广告
     */
    public void findMyAdvertiseUsingGET(final Integer tradeType, final ResponseCallBack.SuccessListener<List<Ads>> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                advertiseSelfControllerApi.findMyAdvertiseUsingGET(tradeType, new Response.Listener<MessageResultListAdvertiseVo>() {
                    @Override
                    public void onResponse(MessageResultListAdvertiseVo response) {
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            Gson gson = new Gson();
                            List<Ads> adsList = gson.fromJson(gson.toJson(response.getData()), new TypeToken<List<Ads>>() {
                            }.getType());
                            for (Ads ads : adsList) {
                                ads.setStatus(0);
                            }
                            if (successListener != null)
                                successListener.onResponse(adsList);
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
