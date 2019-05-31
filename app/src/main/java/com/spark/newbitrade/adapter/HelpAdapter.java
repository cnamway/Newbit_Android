package com.spark.newbitrade.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.library.cms.model.ArticleType;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.entity.PayWaySetting;
import com.spark.newbitrade.entity.User;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.StringUtils;

import java.util.List;

/**
 * 帮助中心
 */
public class HelpAdapter extends BaseQuickAdapter<ArticleType, BaseViewHolder> {

    public HelpAdapter(@Nullable List<ArticleType> data) {
        super(R.layout.adapter_my_help, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ArticleType item) {
        helper.setText(R.id.tvName, item.getName());

    }


}
