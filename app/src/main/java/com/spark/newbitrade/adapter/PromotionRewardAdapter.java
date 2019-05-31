package com.spark.newbitrade.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.library.uc.model.MemberRewardRecord;
import com.spark.newbitrade.R;
import com.spark.newbitrade.utils.DateUtils;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public class PromotionRewardAdapter extends BaseQuickAdapter<MemberRewardRecord, BaseViewHolder> {
    public PromotionRewardAdapter(int layoutResId, @Nullable List<MemberRewardRecord> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberRewardRecord item) {
        helper.setText(R.id.tvReleaseTime, DateUtils.getDateTimeFromMillisecond(item.getRewardDate().getTime()))
                .setText(R.id.tvCurrency, item.getCoin())
                .setText(R.id.tvAmount, NumberFormat.getInstance().format(item.getAmount()))
                .setText(R.id.tvRemarks, "");
    }

}
