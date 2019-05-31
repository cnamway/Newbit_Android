package com.spark.newbitrade.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.newbitrade.R;
import com.spark.newbitrade.entity.Address;
import com.spark.newbitrade.utils.StringUtils;

import java.util.List;

/**
 * 提币地址
 * Created by Administrator on 2018/3/8.
 */

public class AddressAdapter extends BaseQuickAdapter<Address, BaseViewHolder> {
    private Context context;

    public AddressAdapter(Context context,int layoutResId, @Nullable List<Address> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Address item) {
        helper.setText(R.id.tvRemark, StringUtils.isEmpty(item.getRemark()) ? context.getResources().getString(R.string.str_no_remark) : context.getResources().getString(R.string.str_remark_tip)+item.getRemark())
                .setText(R.id.tvAddress, item.getAddress())
                .setText(R.id.tvCoinName,item.getCoinId());
    }
}
