package com.spark.newbitrade.activity.order;


import com.spark.library.otc.model.PageOrderVo;
import com.spark.library.otc.model.QueryParamOrderVo;
import com.spark.newbitrade.base.BaseContract;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface OrderContract {
    interface View extends BaseContract.BaseView {

        void findMyOrderInTransitSuccess(PageOrderVo obj);

        void findMyOrderAchiveSuccess(PageOrderVo obj);

    }

    interface Presenter extends BaseContract.BasePresenter {

        /**
         * 查询我的在途订单(未付款，已付款，申诉中)
         */
        void findMyOrderInTransit(QueryParamOrderVo queryParam);

        /**
         * 查询我的归档订单（已完成，已取消）
         */
        void findMyOrderAchive(QueryParamOrderVo queryParam);
    }


}
