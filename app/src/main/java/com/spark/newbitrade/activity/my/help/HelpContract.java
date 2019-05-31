package com.spark.newbitrade.activity.my.help;


import com.spark.library.cms.model.ArticleType;
import com.spark.library.cms.model.WebArticleVo;
import com.spark.library.otc.model.MessageResult;
import com.spark.newbitrade.base.BaseContract;
import com.spark.newbitrade.entity.PayWaySetting;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface HelpContract {
    interface View extends BaseContract.BaseView {

        void queryListSuccess(List<ArticleType> response);

        void queryListSuccess2(List<ArticleType> response);

        void webArticleQuerySuccess(WebArticleVo response);

    }

    interface Presenter extends BaseContract.BasePresenter {

        void queryList(int id);

        void queryList2(int id);

        void webArticleQuery(Long articleTypeId);
    }


}
