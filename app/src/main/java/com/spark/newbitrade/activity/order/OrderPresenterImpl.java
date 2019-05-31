package com.spark.newbitrade.activity.order;

import com.android.volley.VolleyError;
import com.spark.library.otc.model.PageOrderVo;
import com.spark.library.otc.model.QueryParamOrderVo;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.model.otc.TradeControllerModel;

/**
 * Created by Administrator on 2019/3/2 0002.
 */

public class OrderPresenterImpl implements OrderContract.Presenter {
    private OrderContract.View view;
    private TradeControllerModel tradeControllerModel;

    public OrderPresenterImpl(OrderContract.View view) {
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

    /**
     * 查询我的在途订单(未付款，已付款，申诉中)
     */
    @Override
    public void findMyOrderInTransit(QueryParamOrderVo queryParam) {
        tradeControllerModel.findMyOrderInTransitUsingPOST(queryParam,
                new ResponseCallBack.SuccessListener<PageOrderVo>() {
                    @Override
                    public void onResponse(PageOrderVo response) {
                        hideLoading();
                        if (view != null)
                            view.findMyOrderInTransitSuccess(response);
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

    /**
     * 查询我的归档订单（已完成，已取消）
     */
    @Override
    public void findMyOrderAchive(QueryParamOrderVo queryParam) {
        tradeControllerModel.findMyOrderAchiveUsingPOST(queryParam,
                new ResponseCallBack.SuccessListener<PageOrderVo>() {
                    @Override
                    public void onResponse(PageOrderVo response) {
                        hideLoading();
                        if (view != null)
                            view.findMyOrderAchiveSuccess(response);
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
