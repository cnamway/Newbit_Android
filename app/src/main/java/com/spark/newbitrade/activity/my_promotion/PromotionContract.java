package com.spark.newbitrade.activity.my_promotion;

import com.spark.library.cms.model.MessageResultWebConfigVo;
import com.spark.newbitrade.base.BaseContract;
import com.spark.newbitrade.base.Contract;
import com.spark.newbitrade.entity.PromotionRecord;
import com.spark.newbitrade.entity.PromotionReward;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/10/12 0012.
 */

public interface PromotionContract {

    interface View extends BaseContract.BaseView {

        void getWebConfigSuccess(MessageResultWebConfigVo response);

    }

    interface Presenter extends BaseContract.BasePresenter {

        void getWebConfig();

    }
}
