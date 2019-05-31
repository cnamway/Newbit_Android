package com.spark.newbitrade.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.newbitrade.R;
import com.spark.newbitrade.entity.Entrust;
import com.spark.newbitrade.utils.DateUtils;
import com.spark.newbitrade.utils.MathUtils;

import java.util.Date;
import java.util.List;

/**
 * 委托详情
 * author: wuzongjie
 * time  : 2018/4/25 0025 16:47
 * desc  :
 */

public class EntrustDetailAdapter extends BaseQuickAdapter<Entrust.ListBean.DetailBean, BaseViewHolder> {
    private int baseCoinScale;
    private int coinScale;


    public void setBaseCoinScale(int baseCoinScale, int coinScale) {
        this.baseCoinScale = baseCoinScale;
        this.coinScale = coinScale;
    }

    public EntrustDetailAdapter(@Nullable List<Entrust.ListBean.DetailBean> data) {
        super(R.layout.item_entrust_detail, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Entrust.ListBean.DetailBean item) {
        /*String[] times = DateUtils.getFormatTime(null, new Date(item.getTime())).split(" ");
        helper.setText(R.id.tvTime, times[0].substring(5, times[0].length()) + " " + times[1].substring(0, 5));*/
        helper.setText(R.id.tvTime, DateUtils.getFormatTime(null, new Date(item.getTime())));
        helper.setText(R.id.tvPrice, MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getPrice(), baseCoinScale, null)));
        helper.setText(R.id.tvEntrustCount, MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getAmount(), coinScale, null)));
        helper.setText(R.id.tvFee, MathUtils.subZeroAndDot(MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getFee(), 8, null))));
    }
}
