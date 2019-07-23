package com.spark.newbitrade.activity.my.help;

import com.android.volley.VolleyError;
import com.spark.library.cms.model.ArticleType;
import com.spark.library.cms.model.WebArticleVo;
import com.spark.library.otc.model.MessageResult;
import com.spark.newbitrade.activity.my_account.MyAccountContract;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.entity.PayWaySetting;
import com.spark.newbitrade.model.cms.ArticleTypeControllerModel;
import com.spark.newbitrade.model.otc.PayControllerModel;

import java.util.List;

/**
 * Created by Administrator on 2019/3/15 0015.
 */

public class HelpPresenterImpl implements HelpContract.Presenter {

    private HelpContract.View view;
    private ArticleTypeControllerModel articleTypeControllerModel;

    public HelpPresenterImpl(HelpContract.View view) {
        this.view = view;
        this.articleTypeControllerModel = new ArticleTypeControllerModel();
    }

    @Override
    public void queryList(int id) {
        showLoading();
        articleTypeControllerModel.articleQuery(id, new ResponseCallBack.SuccessListener<List<ArticleType>>() {
            @Override
            public void onResponse(List<ArticleType> response) {
                hideLoading();
                if (view != null) {
                    view.queryListSuccess(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (view != null) {
                    view.dealError(httpErrorEntity);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (view != null) {
                    view.dealError(volleyError);
                }
            }
        });
    }

    @Override
    public void queryList2(Integer id) {
        showLoading();
        articleTypeControllerModel.articleQuery(id, new ResponseCallBack.SuccessListener<List<ArticleType>>() {
            @Override
            public void onResponse(List<ArticleType> response) {
                hideLoading();
                if (view != null) {
                    view.queryListSuccess2(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (view != null) {
                    view.dealError(httpErrorEntity);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (view != null) {
                    view.dealError(volleyError);
                }
            }
        });
    }

    @Override
    public void webArticleQuery(Long articleTypeId) {
        showLoading();
        articleTypeControllerModel.webArticleQuery(articleTypeId, new ResponseCallBack.SuccessListener<WebArticleVo>() {
            @Override
            public void onResponse(WebArticleVo response) {
                hideLoading();
                if (view != null) {
                    view.webArticleQuerySuccess(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (view != null) {
                    view.dealError(httpErrorEntity);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (view != null) {
                    view.dealError(volleyError);
                }
            }
        });
    }


    @Override
    public void showLoading() {
        if (view != null) {
            view.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (view != null) {
            view.hideLoading();
        }
    }

    @Override
    public void destory() {
        view = null;
    }
}
