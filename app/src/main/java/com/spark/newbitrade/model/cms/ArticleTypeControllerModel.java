package com.spark.newbitrade.model.cms;

import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.spark.library.cms.api.ArticleTypeControllerApi;
import com.spark.library.cms.api.WebArticleControllerApi;
import com.spark.library.cms.api.WebConfigControllerApi;
import com.spark.library.cms.model.ArticleType;
import com.spark.library.cms.model.ArticleTypeDto;
import com.spark.library.cms.model.MessageResultListArticleType;
import com.spark.library.cms.model.MessageResultWebArticleVo;
import com.spark.library.cms.model.MessageResultWebConfigVo;
import com.spark.library.cms.model.WebArticleVo;
import com.spark.newbitrade.callback.ResponseCallBack;
import com.spark.newbitrade.factory.HttpUrls;
import com.spark.newbitrade.utils.LanguageUtil;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.SharedPreferenceInstance;

import java.util.List;

/**
 * cms业务模块
 */

public class ArticleTypeControllerModel {
    private ArticleTypeControllerApi articleTypeControllerApi;
    private WebArticleControllerApi webArticleControllerApi;

    public ArticleTypeControllerModel() {
        articleTypeControllerApi = new ArticleTypeControllerApi();
        articleTypeControllerApi.setBasePath(HttpUrls.CMS_HOST);
        webArticleControllerApi = new WebArticleControllerApi();
        webArticleControllerApi.setBasePath(HttpUrls.CMS_HOST);
    }

    /**
     * 帮助中心
     */
    public void articleQuery(Integer id, final ResponseCallBack.SuccessListener<List<ArticleType>> successListener, final ResponseCallBack.ErrorListener errorListener) {
        int languageCode = SharedPreferenceInstance.getInstance().getLanguageCode();

        final ArticleTypeDto articleType = new ArticleTypeDto();
        articleType.setParentType(id);
        if (languageCode == 1) articleType.setSysLanguage("cn");
        else if (languageCode == 2) articleType.setSysLanguage(LanguageUtil.ENGLISH);
        else if (languageCode == 3) articleType.setSysLanguage(LanguageUtil.ENGLISH);
        new Thread(new Runnable() {
            @Override
            public void run() {
                articleTypeControllerApi.queryUsingPOST(articleType, new Response.Listener<MessageResultListArticleType>() {
                    @Override
                    public void onResponse(MessageResultListArticleType response) {
                        LogUtils.i("response==" + response.toString());
                        if (successListener != null)
                            successListener.onResponse(response.getData());

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (errorListener != null)
                            errorListener.onErrorResponse(error);
                    }
                });
            }
        }).start();
    }


    /**
     * 帮助中心详情
     */
    public void webArticleQuery(final Long articleTypeId, final ResponseCallBack.SuccessListener<WebArticleVo> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                webArticleControllerApi.getArticleTypeUsingGET1(articleTypeId, new Response.Listener<MessageResultWebArticleVo>() {
                    @Override
                    public void onResponse(MessageResultWebArticleVo response) {
                        LogUtils.i("response==" + response.toString());
                        if (successListener != null)
                            successListener.onResponse(response.getData());

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (errorListener != null)
                            errorListener.onErrorResponse(error);
                    }
                });
            }
        }).start();
    }


}
