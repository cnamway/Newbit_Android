package com.spark.newbitrade.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.newbitrade.R;
import com.spark.newbitrade.entity.Wallet;
import com.spark.newbitrade.utils.MathUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2018/2/5.
 */

public class WalletAdapter extends BaseQuickAdapter<Wallet, BaseViewHolder> {
    public WalletAdapter(@Nullable List<Wallet> data) {
        super(R.layout.item_lv_wallet, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Wallet item) {
        helper.setText(R.id.tvCoinUnit, item.getCoinId())
                .setText(R.id.tvCoinName, item.getCoinId())
                .setText(R.id.tvBuyCanUse, MathUtils.subZeroAndDot(String.valueOf(item.getBalance().toPlainString())));
//                .setText(R.id.tvFrozon, MathUtils.subZeroAndDot(MathUtils.getRundNumber(Double.valueOf(new BigDecimal(item.getFrozenBalance()).toString()), 8, null)));
        switch (item.getCoinId()) {
            case "BTC":
                helper.setImageResource(R.id.ivIcon, R.mipmap.btc);
                break;
            case "ETH":
                helper.setImageResource(R.id.ivIcon, R.mipmap.eth);
                break;
            case "USDT":
                helper.setImageResource(R.id.ivIcon, R.mipmap.usdt);
                break;
            case "GUSD":
                helper.setImageResource(R.id.ivIcon, R.mipmap.gusd);
                break;
            case "BCH":
                helper.setImageResource(R.id.ivIcon, R.mipmap.bch);
                break;
            case "CND":
                helper.setImageResource(R.id.ivIcon, R.mipmap.cnd);
                break;
            case "CNT":
                helper.setImageResource(R.id.ivIcon, R.mipmap.cnt);
                break;
            case "DASH":
                helper.setImageResource(R.id.ivIcon, R.mipmap.dash);
                break;
            case "DVC":
                helper.setImageResource(R.id.ivIcon, R.mipmap.dvc);
                break;
            case "ETC":
                helper.setImageResource(R.id.ivIcon, R.mipmap.etc);
                break;
            case "G":
                helper.setImageResource(R.id.ivIcon, R.mipmap.g);
                break;
            case "XNE":
                helper.setImageResource(R.id.ivIcon, R.mipmap.xne);
                break;
            case "ZEC":
                helper.setImageResource(R.id.ivIcon, R.mipmap.zec);
                break;
            case "NBTC":
                helper.setImageResource(R.id.ivIcon, R.mipmap.nbtc);
                break;
        }

    }
}
