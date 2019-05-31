package com.spark.newbitrade.activity.my_promotion;

import com.spark.library.uc.model.MemberPromotionVo;
import com.spark.newbitrade.base.BaseContract;
import com.spark.newbitrade.base.Contract;
import com.spark.newbitrade.entity.PromotionRecord;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public interface PromotionRecordContract {

    interface View extends BaseContract.BaseView {
        void getPromotionFail(Integer code, String toastMessage);

        void getPromotionSuccess(List<MemberPromotionVo> list, int total);
    }


    interface Presenter extends BaseContract.BasePresenter {
        void getPromotion(HashMap<String, String> map);
    }

}
