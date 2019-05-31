package com.spark.newbitrade.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.newbitrade.R;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.entity.WalletDetail;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/2/5.
 */

public class WalletDetailAdapter extends BaseQuickAdapter<WalletDetail, BaseViewHolder> {
    private Context context;

    public WalletDetailAdapter(@LayoutRes int layoutResId, @Nullable List<WalletDetail> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, WalletDetail item) {
        Date date = new Date(item.getCreateTime());
        String data = String.valueOf(android.text.format.DateFormat.format("HH:mm MM/dd", date));
        String count = new BigDecimal(String.valueOf(item.getAmount())).toString();
        helper.setText(R.id.tvTime, data)
                .setText(R.id.tvCount, item.getAmount() > 0 ? "+" + count : count)
                .setTextColor(R.id.tvCount, item.getAmount() > 0 ? context.getResources().getColor(R.color.main_font_green) : context.getResources().getColor(R.color.main_font_red))
                .setText(R.id.tvUnit, item.getSymbol())
                .setText(R.id.tvCharge, new BigDecimal(String.valueOf(item.getFee())).toString());
        TextView tvTypeText = helper.getView(R.id.tvType);
        String text = "";
        switch (item.getType()) {
            case 0:
                text = MyApplication.getApp().getString(R.string.top_up);
                break;
            case 1:
                text = MyApplication.getApp().getString(R.string.withdrawal);
                break;
            case 2:
                text = MyApplication.getApp().getString(R.string.transfer);
                break;
            case 3:
                text = MyApplication.getApp().getString(R.string.coin_currency_trade);
                break;
            case 4:
                text = MyApplication.getApp().getString(R.string.fiat_money_buy);
                break;
            case 5:
                text = MyApplication.getApp().getString(R.string.fiat_money_sell);
                break;
            case 6:
                text = MyApplication.getApp().getString(R.string.activities_reward);
                break;
            case 7:
                text = MyApplication.getApp().getString(R.string.promotion_rewards);
                break;
            case 8:
                text = MyApplication.getApp().getString(R.string.share_out_bonus);
                break;
            case 9:
                text = MyApplication.getApp().getString(R.string.vote);
                break;
            case 10:
                text = MyApplication.getApp().getString(R.string.artificial_top_up);
                break;
            case 11:
                text = MyApplication.getApp().getString(R.string.match_money);
                break;
            case 12:
                text = MyApplication.getApp().getString(R.string.pay_merchant_certification);
                break;
            case 13:
                text = MyApplication.getApp().getString(R.string.repay_merchant_certification);
                break;
            case 15:
                text = MyApplication.getApp().getString(R.string.recharge_currency);
                break;
            case 16:
                text = MyApplication.getApp().getString(R.string.currency_exchange);
                break;
            case 17:
                text = MyApplication.getApp().getString(R.string.channel_promotion);
                break;
            case 19:
                text = MyApplication.getApp().getString(R.string.registration_award);
                break;
            case 20:
                text = MyApplication.getApp().getString(R.string.realname_award);
                break;
            case 21:
                text = MyApplication.getApp().getString(R.string.promotion_registration_awards);
                break;
            case 22:
                text = MyApplication.getApp().getString(R.string.promotion_realname_award);
                break;
            case 23:
                text = MyApplication.getApp().getString(R.string.promotion_currency_exchange_award);
                break;
            case 24:
                text = MyApplication.getApp().getString(R.string.promotion_currency_trading_incentives);
                break;
            case 25:
                text = MyApplication.getApp().getString(R.string.airdrop);
                break;
            case 26:
                text = MyApplication.getApp().getString(R.string.lock_osition);
                break;
        }
        tvTypeText.setText(text);
    }
}
