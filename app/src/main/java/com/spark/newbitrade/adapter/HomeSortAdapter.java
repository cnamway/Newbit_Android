package com.spark.newbitrade.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.main.MainActivity;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.entity.Currency;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.MathUtils;

import java.util.List;

/**
 * author: wuzongjie
 * time  : 2018/4/16 0016 15:23
 * desc  : 首页涨幅榜适配器
 */

public class HomeSortAdapter extends BaseQuickAdapter<Currency, BaseViewHolder> {

    private boolean isLoad;

    public boolean isLoad() {
        return isLoad;
    }

    public void setLoad(boolean load) {
        isLoad = load;
    }

    public HomeSortAdapter(@Nullable List<Currency> data) {
        super(R.layout.item_home_sort, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Currency item) {
        if (isLoad) {
            helper.setText(R.id.tvConvert, MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getClose() * item.getBaseUsdRate() * MainActivity.rate, 2, null)) + GlobalConstant.CNY);
        } else {
            helper.setText(R.id.tvConvert, "$" + MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getUsdRate(), 2, null)));
        }
        helper.setText(R.id.tvSort, "" + (helper.getAdapterPosition() + 1));
        helper.setText(R.id.item_home_chg, (item.getChg() >= 0 ? "+" : "") + MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getChg() * 100, 2, "########0.") + "%"));
        helper.setTextColor(R.id.tvClose, item.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(),
                R.color.main_font_green) : ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_red));
        helper.getView(R.id.item_home_chg).setEnabled(item.getChg() >= 0);
        String[] strings = item.getSymbol().split("/");
        helper.setText(R.id.tvBuyCoinSymbol, strings[0]);
        helper.setText(R.id.tvBuyBaseSymbol, "/" + strings[1]);
        helper.setText(R.id.tv24HCount, MyApplication.getApp().getString(R.string.text_24_change) + MathUtils.subZeroAndDot(MathUtils.getRundNumber(Double.valueOf(item.getVolume().toString()), 2, null)));
        helper.setText(R.id.tvClose, MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getClose(), item.getBaseCoinScale(), null)));
        if (helper.getPosition() == 0) {
            helper.setBackgroundRes(R.id.tvSort, R.mipmap.bg_sort_first);
        } else if (helper.getPosition() == 1) {
            helper.setBackgroundRes(R.id.tvSort, R.mipmap.bg_sort_sec);
        } else if (helper.getPosition() == 2) {
            helper.setBackgroundRes(R.id.tvSort, R.mipmap.bg_sort_tri);
        } else if (helper.getPosition() >= 3) {
            helper.setBackgroundRes(R.id.tvSort, R.mipmap.bg_sort_four);
        }
    }
}
