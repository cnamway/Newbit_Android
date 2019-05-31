package com.spark.newbitrade.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.main.MainActivity;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.entity.Currency;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.MathUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/1/29.
 */

public class HomeOneAdapter extends BaseQuickAdapter<Currency, BaseViewHolder> {


    public HomeOneAdapter(@LayoutRes int layoutResId, @Nullable List<Currency> data) {
        super(layoutResId, data);

    }


    @Override
    protected void convert(BaseViewHolder helper, Currency item) {
        helper.setImageResource(R.id.ivCollect, item.isCollect() ? R.mipmap.icon_collect_hover : R.mipmap.icon_collect_normal);
        helper.addOnClickListener(R.id.ivCollect);
        helper.setText(R.id.tvCurrencyName, item.getSymbol())
                .setText(R.id.tvClose, MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getClose(), item.getBaseCoinScale(), null)))
                .setText(R.id.tvAddPercent, (item.getChg() >= 0 ? "+" : "") + MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getChg() * 100, 2, "########0.")) + "%")
                .setText(R.id.tvVol, "≈" + MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getClose() * item.getBaseUsdRate() * MainActivity.rate, 2, null)) + GlobalConstant.CNY)
                .setTextColor(R.id.tvAddPercent, item.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_green) : ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_red))
                .setTextColor(R.id.tvClose, item.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_green) : ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_red));
    }



}
