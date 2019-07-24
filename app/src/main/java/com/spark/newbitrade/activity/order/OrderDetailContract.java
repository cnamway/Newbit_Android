package com.spark.newbitrade.activity.order;


import com.spark.library.otc.model.MemberPayType;
import com.spark.library.otc.model.OrderDetailVo;
import com.spark.library.otc.model.OrderPaymentDto;
import com.spark.newbitrade.base.BaseContract;
import com.spark.newbitrade.entity.HttpErrorEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface OrderDetailContract {
//    interface View extends Contract.BaseView<Presenter> {
//
//        void orderDetailSuccess(OrderDetial obj);
//
//        void payDoneSuccess(String obj);
//
//        void cancleSuccess(String obj);
//
//        void releaseSuccess(String obj);
//
//        void downloadSuccess(Response obj);
//
//        void doPostFail(Integer code, String toastMessage);
//    }
//
//    interface Presenter extends Contract.BasePresenter {
//        void orderDetail(HashMap<String, String> params);
//
//        void payDone(HashMap<String, String> params);
//
//        void cancle(HashMap<String, String> params);
//
//        void release(HashMap<String, String> params);
//
//        void doDownload(String url);
//    }

    interface View extends BaseContract.BaseView {

        void cancelOrderUsingGETSuccess(String obj);

        void releaseOrderUsingGETSuccess(String obj);

        void paymentOrderUsingPOSTSuccess(String obj);

        void findOrderInTransitDetailUsingGETSuccess(OrderDetailVo obj);

        void findOrderAchiveDetailUsingGETSuccess(OrderDetailVo obj);

        void queryOrderPayTypeUsingGETSuccess(List<MemberPayType> obj);

        void findOrderAchiveDetailUsingGETFail(HttpErrorEntity httpErrorEntity);

        void findOrderInTransitDetailUsingGETFail(HttpErrorEntity httpErrorEntity);

    }

    interface Presenter extends BaseContract.BasePresenter {

        void cancelOrderUsingGET(String orderId);

        void releaseOrderUsingGET(String orderId, String tradePwd);

        void paymentOrderUsingPOST(OrderPaymentDto orderPaymentDto);

        void findOrderInTransitDetailUsingGET(String orderId);

        void findOrderAchiveDetailUsingGET(String orderId);

        void queryOrderPayTypeUsingGET(String orderId);
    }


}
