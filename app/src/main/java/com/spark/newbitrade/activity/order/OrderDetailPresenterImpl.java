package com.spark.newbitrade.activity.order;

import com.android.volley.VolleyError;
import com.spark.library.otc.model.MemberPayType;
import com.spark.library.otc.model.OrderDetailVo;
import com.spark.library.otc.model.OrderPaymentDto;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.model.otc.TradeControllerModel;

import java.util.List;

/**
 * Created by Administrator on 2019/3/2 0002.
 */

public class OrderDetailPresenterImpl implements OrderDetailContract.Presenter {
    private OrderDetailContract.View view;
    private TradeControllerModel tradeControllerModel;

    public OrderDetailPresenterImpl(OrderDetailContract.View view) {
        this.view = view;
        tradeControllerModel = new TradeControllerModel();
    }

    @Override
    public void showLoading() {
        if (view != null)
            view.showLoading();
    }

    @Override
    public void hideLoading() {
        if (view != null)
            view.hideLoading();
    }

    @Override
    public void destory() {
        view = null;
    }

    @Override
    public void cancelOrderUsingGET(String orderId) {
        showLoading();
        tradeControllerModel.cancelOrderUsingGET(orderId,
                new ResponseCallBack.SuccessListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoading();
                        if (view != null)
                            view.cancelOrderUsingGETSuccess(response);
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (view != null)
                            view.dealError(httpErrorEntity);
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (view != null)
                            view.dealError(volleyError);
                    }
                });
    }

    @Override
    public void releaseOrderUsingGET(String orderId, String tradePwd) {
        showLoading();
        tradeControllerModel.releaseOrderUsingGET(orderId, tradePwd,
                new ResponseCallBack.SuccessListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoading();
                        if (view != null)
                            view.releaseOrderUsingGETSuccess(response);
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (view != null)
                            view.dealError(httpErrorEntity);
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (view != null)
                            view.dealError(volleyError);
                    }
                });
    }

    @Override
    public void paymentOrderUsingPOST(OrderPaymentDto orderPaymentDto) {
        showLoading();
        tradeControllerModel.paymentOrderUsingPOST(orderPaymentDto,
                new ResponseCallBack.SuccessListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoading();
                        if (view != null)
                            view.paymentOrderUsingPOSTSuccess(response);
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (view != null)
                            view.dealError(httpErrorEntity);
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (view != null)
                            view.dealError(volleyError);
                    }
                });
    }

    @Override
    public void findOrderInTransitDetailUsingGET(String orderId) {
        showLoading();
        tradeControllerModel.findOrderInTransitDetailUsingGET(orderId,
                new ResponseCallBack.SuccessListener<OrderDetailVo>() {
                    @Override
                    public void onResponse(OrderDetailVo response) {
                        hideLoading();
                        if (view != null)
                            view.findOrderInTransitDetailUsingGETSuccess(response);
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (view != null)
                            view.findOrderInTransitDetailUsingGETFail(httpErrorEntity);
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (view != null)
                            view.dealError(volleyError);
                    }
                });
    }

    @Override
    public void findOrderAchiveDetailUsingGET(String orderId) {
        showLoading();
        tradeControllerModel.findOrderAchiveDetailUsingGET(orderId,
                new ResponseCallBack.SuccessListener<OrderDetailVo>() {
                    @Override
                    public void onResponse(OrderDetailVo response) {
                        hideLoading();
                        if (view != null)
                            view.findOrderAchiveDetailUsingGETSuccess(response);
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (view != null)
                            view.findOrderAchiveDetailUsingGETFail(httpErrorEntity);
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (view != null)
                            view.dealError(volleyError);
                    }
                });
    }

    @Override
    public void queryOrderPayTypeUsingGET(String orderId) {
        showLoading();
        tradeControllerModel.queryOrderPayTypeUsingGET(orderId,
                new ResponseCallBack.SuccessListener<List<MemberPayType>>() {
                    @Override
                    public void onResponse(List<MemberPayType> response) {
                        hideLoading();
                        if (view != null)
                            view.queryOrderPayTypeUsingGETSuccess(response);
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (view != null)
                            view.dealError(httpErrorEntity);
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (view != null)
                            view.dealError(volleyError);
                    }
                });
    }


}
