package com.spark.newbitrade.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.library.otc.model.OrderVo;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.utils.MathUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/2/5.
 */

public class OrderAdapter extends BaseQuickAdapter<OrderVo, BaseViewHolder> {
    private Context context;

    public OrderAdapter(Context context, @Nullable List<OrderVo> data) {
        super(R.layout.item_order, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderVo item) {

        if (MyApplication.getApp().getCurrentUser() != null && item.getMemberId().equals(MyApplication.getApp().getCurrentUser().getId())) {
            helper.setText(R.id.tvType, (item.getOrderType().equals("1") ? context.getString(R.string.text_buy) : context.getString(R.string.text_sell)))
                    .setTextColor(R.id.tvType, item.getOrderType().equals("1") ? context.getResources().getColor(R.color.main_font_green) : context.getResources().getColor(R.color.main_font_red))
                    .setText(R.id.tvCount, MathUtils.subZeroAndDot(String.valueOf(item.getNumber().toPlainString())) + " " + item.getCoinName())
                    .setText(R.id.tvTotal, MathUtils.subZeroAndDot(String.valueOf(item.getMoney().toPlainString())) + " CNY");
//                    .setText(R.id.tvState, (item.getOrderType().equals("1") ? context.getString(R.string.sell_side) + ": " : context.getString(R.string.buy_side) + ": "));
        } else {
            helper.setText(R.id.tvType, (item.getOrderType().equals("0") ? context.getString(R.string.text_buy) : context.getString(R.string.text_sell)))
                    .setTextColor(R.id.tvType, item.getOrderType().equals("0") ? context.getResources().getColor(R.color.main_font_green) : context.getResources().getColor(R.color.main_font_red))
                    .setText(R.id.tvCount, MathUtils.subZeroAndDot(String.valueOf(item.getNumber().toPlainString())) + " " + item.getCoinName())
                    .setText(R.id.tvTotal, MathUtils.subZeroAndDot(String.valueOf(item.getMoney().toPlainString())) + " CNY");
//                    .setText(R.id.tvState, (item.getOrderType().equals("0") ? context.getString(R.string.sell_side) + ": " : context.getString(R.string.buy_side) + ": "));
        }


    }


}
