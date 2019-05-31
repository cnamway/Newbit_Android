package com.spark.newbitrade.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.newbitrade.R;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.entity.Entrust;
import com.spark.newbitrade.utils.DateUtils;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.MathUtils;

import java.util.Date;
import java.util.List;

/**
 * 委托
 * author: wuzongjie
 * time  : 2018/4/17 0017 10:18
 * desc  : 当前委托的适配器
 */

public class TrustAdapter extends BaseQuickAdapter<Entrust.ListBean, BaseViewHolder> {
    private int baseScale;
    private int coinScale;

    public void setBaseScale(int baseScale, int coinScale) {
        this.baseScale = baseScale;
        this.coinScale = coinScale;
    }

    public TrustAdapter(@Nullable List<Entrust.ListBean> data) {
        super(R.layout.item_trust, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Entrust.ListBean item) {
        if (item.getSide() == GlobalConstant.INT_BUY) {
            helper.setText(R.id.trust_type, MyApplication.getApp().getString(R.string.text_buy_in)).setTextColor(R.id.trust_type,
                    ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_green));
        } else {
            helper.setText(R.id.trust_type, MyApplication.getApp().getString(R.string.text_sale_out)).setTextColor(R.id.trust_type,
                    ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_red));
        }
        //String[] times = DateUtils.getFormatTime(null, new Date(item.getTransactTime())).split(" ");
        //helper.setText(R.id.trust_time, times[0].substring(5, times[0].length()) + " " + times[1].substring(0, 5));
        helper.setText(R.id.trust_time, DateUtils.getFormatTime(null, new Date(item.getTransactTime())));
        if (item.getPriceType() == GlobalConstant.INT_LIMIT_PRICE) { // 限价
            helper.setText(R.id.trust_price, MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getPrice(), baseScale, null)));
            helper.setText(R.id.trust_num, MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getOrderQty(), coinScale, null)));
        } else { // 市价
            helper.setText(R.id.trust_price, String.valueOf(MyApplication.getApp().getString(R.string.text_best_prices)));
            if (item.getSide() == GlobalConstant.INT_BUY) {
                helper.setText(R.id.trust_num, String.valueOf("--"));
            } else {
                helper.setText(R.id.trust_num, MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getOrderQty(), coinScale, null)));
            }
        }
        helper.setText(R.id.trust_one, MyApplication.getApp().getString(R.string.text_number) + "(" + item.getCoinSymbol() + ")");
        helper.setText(R.id.trust_two, MyApplication.getApp().getString(R.string.text_actual_deal) + "(" + item.getCoinSymbol() + ")");
        helper.setText(R.id.trust_ones, MyApplication.getApp().getString(R.string.text_price) + "(" + item.getBaseSymbol() + ")");
        helper.setText(R.id.trust_done, MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getTradedAmount(), coinScale, null))); // 已成交
        helper.addOnClickListener(R.id.trust_back);
    }
}
