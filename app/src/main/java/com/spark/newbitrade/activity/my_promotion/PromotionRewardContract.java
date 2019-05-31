package com.spark.newbitrade.activity.my_promotion;

import com.spark.library.uc.model.MemberRewardRecord;
import com.spark.newbitrade.base.BaseContract;
import com.spark.newbitrade.base.Contract;
import com.spark.newbitrade.entity.PromotionReward;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public interface PromotionRewardContract {

    interface View extends BaseContract.BaseView {
        void getPromotionRewardFail(Integer code, String toastMessage);

        void getPromotionRewardSuccess(List<MemberRewardRecord> obj);
    }


    interface Presenter extends BaseContract.BasePresenter {
        void getPromotionReward(HashMap<String, String> map);
    }

}
