package com.spark.newbitrade.activity.appeal;


import com.spark.library.otc.model.AppealApplyInTransitDto;
import com.spark.newbitrade.base.BaseContract;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface AppealContract {
//    interface View extends Contract.BaseView<Presenter> {
//
//        void appealSuccess(String obj);
//
//        void appealFail(Integer code, String toastMessage);
//
//        void uploadBase64PicSuccess(String obj, int type);
//
//        void doPostFail(Integer code, String toastMessage);
//
//    }
//
//    interface Presenter extends Contract.BasePresenter {
//
//        void appeal(HashMap<String, String> params);
//
//        void uploadBase64Pic(HashMap<String, String> params, int type);
//
//        void uploadImageFile(File file, int type);
//    }

    interface View extends BaseContract.BaseView {

        void appealApplySuccess(String obj);

        void base64UpLoadSuccess(String obj);
    }

    interface Presenter extends BaseContract.BasePresenter {

        void appealApply(AppealApplyInTransitDto appealApplyInTransitDto);

        void base64UpLoad(String base64);
    }


}
