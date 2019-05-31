package com.spark.newbitrade.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.library.uc.model.MemberPromotionVo;
import com.spark.newbitrade.R;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.entity.PromotionRecord;
import com.spark.newbitrade.utils.DateUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public class PromotionRecordAdapter extends BaseQuickAdapter<MemberPromotionVo, BaseViewHolder> {

    public PromotionRecordAdapter(int layoutResId, @Nullable List<MemberPromotionVo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberPromotionVo item) {
        helper.setText(R.id.tvRegistrationTime, DateUtils.getDateTimeFromMillisecond(item.getCreateTime().getTime()))
                .setText(R.id.tvUserName, item.getUsername());
        switch (item.getLevel()) {
            case 0:
                helper.setText(R.id.RecommendationLevel, MyApplication.getApp().getString(R.string.level_one));
                break;
            case 1:
                helper.setText(R.id.RecommendationLevel, MyApplication.getApp().getString(R.string.level_two));
                break;
            case 2:
                helper.setText(R.id.RecommendationLevel, MyApplication.getApp().getString(R.string.level_three));
                break;
            case 3:
                helper.setText(R.id.RecommendationLevel, MyApplication.getApp().getString(R.string.level_four));
                break;
        }
    }

}
