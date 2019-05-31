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
 * author: wuzongjie
 * time  : 2018/4/17 0017 10:56
 * desc  :历史记录的适配器
 */

public class TrustHistoryAdapter extends BaseQuickAdapter<Entrust.ListBean, BaseViewHolder> {
    private int baseScale;
    private int coinScale;

    public void setBaseScale(int baseScale, int coinScale) {
        this.baseScale = baseScale;
        this.coinScale = coinScale;
    }

    public TrustHistoryAdapter(@Nullable List<Entrust.ListBean> data) {
        super(R.layout.item_history_trust, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Entrust.ListBean item) {
        if (GlobalConstant.INT_BUY == (item.getSide())) {
            helper.setText(R.id.tvType, MyApplication.getApp().getString(R.string.text_buy_in)).setTextColor(R.id.tvType,
                    ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_green));
        } else {
            helper.setText(R.id.tvType, MyApplication.getApp().getString(R.string.text_sale_out)).setTextColor(R.id.tvType,
                    ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_red));
        }
        if (5 == (item.getStatus())) { // 已成交
            helper.setVisible(R.id.tvCancel, false);
            helper.setVisible(R.id.tvSuccess, true);
        } else {
            helper.setVisible(R.id.tvCancel, true);
            helper.setVisible(R.id.tvSuccess, false);
        }

        helper.setText(R.id.tvVolumeTag, MyApplication.getApp().getString(R.string.text_volume) + "(" + item.getCoinSymbol() + ")");
        //String[] times = DateUtils.getFormatTime(null, new Date(item.getTransactTime())).split(" ");
        //helper.setText(R.id.tvEntrustTime, times[0].substring(5, times[0].length()) + " " + times[1].substring(0, 5));
        helper.setText(R.id.tvEntrustTime, DateUtils.getFormatTime(null, new Date(item.getTransactTime())));
        if (GlobalConstant.INT_LIMIT_PRICE == (item.getPriceType())) { // 限价
            helper.setText(R.id.tvEntrustCountTag, MyApplication.getApp().getString(R.string.text_entrust_num) + "(" + item.getCoinSymbol() + ")");
            helper.setText(R.id.tvEntrustPrice, MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getPrice(), baseScale, null)));
        } else { // 市价
            if (item.getSide() == GlobalConstant.INT_BUY) {
                helper.setText(R.id.tvEntrustPrice, String.valueOf(MyApplication.getApp().getString(R.string.text_best_prices)));
                helper.setText(R.id.tvEntrustCountTag, MyApplication.getApp().getString(R.string.text_total_entrust) + "(" + item.getBaseSymbol() + ")");
            } else {
                helper.setText(R.id.tvEntrustCountTag, MyApplication.getApp().getString(R.string.text_entrust_num) + "(" + item.getCoinSymbol() + ")");
                helper.setText(R.id.tvEntrustPrice, String.valueOf(MyApplication.getApp().getString(R.string.text_best_prices)));
            }
        }
        helper.setText(R.id.tvEntrustPriceTag, MyApplication.getApp().getString(R.string.text_entrust_price) + "(" + item.getBaseSymbol() + ")");
        // 委托量
        helper.setText(R.id.tvEntrustCount, MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getOrderQty(), coinScale, null)));
        // 成交总额
        helper.setText(R.id.tvTotal, MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getTurnover(), 8, null)));
        helper.setText(R.id.tvTotalTag, MyApplication.getApp().getString(R.string.text_total_deal) + "(" + item.getBaseSymbol() + ")");
        // 成交均价
        if (item.getTradedAmount() == 0.0 || item.getTurnover() == 0.0) {
            helper.setText(R.id.tvAverage, String.valueOf(0.0));
        } else {
            helper.setText(R.id.tvAverage, MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getTurnover() / item.getTradedAmount(),
                    8, null)));
        }
        helper.setText(R.id.tvAverageTag, MyApplication.getApp().getString(R.string.text_average_price) + "(" + item.getBaseSymbol() + ")");
        // 成交量
        //helper.setText(R.id.history_six,String.valueOf(item.getTradedAmount()));
        helper.setText(R.id.tvVolume, MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getTradedAmount(), coinScale, null)));
    }
}