package com.spark.newbitrade.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.newbitrade.R;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.entity.Ads;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.MathUtils;

import java.util.List;

/**
 * 广告
 * Created by Administrator on 2018/2/5.
 */

public class AdsAdapter extends BaseQuickAdapter<Ads, BaseViewHolder> {

    private Context context;

    public AdsAdapter(@LayoutRes int layoutResId, @Nullable List<Ads> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, Ads item) {
        helper.setText(R.id.tvCoinName, item.getCoinName())
                .setText(R.id.tvStatus, item.getStatus() == 0 ? MyApplication.getApp().getString(R.string.grounding) : MyApplication.getApp().getString(R.string.shelved))
                .setText(R.id.tvType, item.getAdvertiseType() == 0 ? MyApplication.getApp().getString(R.string.text_buy) : MyApplication.getApp().getString(R.string.text_sell))
                .setTextColor(R.id.tvType, item.getAdvertiseType() == 0 ? context.getResources().getColor(R.color.main_font_green) : context.getResources().getColor(R.color.main_font_red))
                .setText(R.id.tvPrice, MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getPrice(), 8, null)) + "CNY")
                .setText(R.id.tvLimit, MyApplication.getApp().getString(R.string.limit) + item.getMinLimit() + "~" + item.getMaxLimit() + item.getLocalCurrency());
        String payMode = item.getPayMode();
        if (payMode.contains(GlobalConstant.alipay)) helper.setVisible(R.id.ivPay, true);
        else helper.setGone(R.id.ivPay, false);
        if (payMode.contains(GlobalConstant.wechat)) helper.setVisible(R.id.ivWeChat, true);
        else helper.setGone(R.id.ivWeChat, false);
        if (payMode.contains(GlobalConstant.card)) helper.setVisible(R.id.ivUnionPay, true);
        else helper.setGone(R.id.ivUnionPay, false);
        if (payMode.contains(GlobalConstant.PAYPAL)) helper.setVisible(R.id.ivPalpay, true);
        else helper.setGone(R.id.ivPalpay, false);
        if (payMode.contains(GlobalConstant.other)) helper.setVisible(R.id.ivOther, true);
        else helper.setGone(R.id.ivOther, false);
    }
}
