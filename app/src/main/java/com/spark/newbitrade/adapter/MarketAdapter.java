package com.spark.newbitrade.adapter;

import android.support.annotation.Nullable;

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
 * 行情adapter
 * author: wuzongjie
 * time  : 2018/4/16 0016 18:18
 */

public class MarketAdapter extends BaseQuickAdapter<Currency, BaseViewHolder> {
    private int type;
    private boolean isLoad;

    public boolean isLoad() {
        return isLoad;
    }

    public void setLoad(boolean load) {
        isLoad = load;
    }

    public MarketAdapter(@Nullable List<Currency> data, int type) {
        super(R.layout.item_market, data);
        this.type = type;
    }


    @Override
    protected void convert(BaseViewHolder helper, Currency item) {
        if (isLoad) {
            helper.setText(R.id.tvConvert, String.valueOf(MathUtils.subZeroAndDot(MathUtils.getRundNumber(
                    item.getBaseUsdRate() * item.getClose() * MainActivity.rate, 2, null))) + GlobalConstant.CNY);
        } else {
            helper.setText(R.id.tvConvert, "$" + String.valueOf(MathUtils.subZeroAndDot(MathUtils.getRundNumber(
                    item.getBaseUsdRate() * item.getClose(), 2, null))));
        }
        helper.setText(R.id.item_home_chg, (item.getChg() >= 0 ? "+" : "") + MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getChg() * 100, 2, "########0.")) + "%");
        helper.getView(R.id.item_home_chg).setEnabled(item.getChg() >= 0);
        if (type == 2) {
            helper.setText(R.id.tvBuySymbol, item.getSymbol().split("/")[0]);
            helper.setText(R.id.tvSecSymbol, "/" + item.getSymbol().split("/")[1]);
        } else {
            helper.setText(R.id.tvBuySymbol, item.getOtherCoin());
            helper.setText(R.id.tvSecSymbol, item.getSymbol().substring(item.getSymbol().indexOf("/"), item.getSymbol().length()));
        }
        helper.setText(R.id.tvClose, MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getClose(), item.getBaseCoinScale(), null)));
        /*helper.setTextColor(R.id.tvClose, item.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(),
                R.color.main_font_green) : ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_red));*/
        helper.setText(R.id.tv24HCount, MyApplication.getApp().getString(R.string.text_24_change) + item.getVolume());
    }
}