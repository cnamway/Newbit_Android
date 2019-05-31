package com.spark.newbitrade.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.newbitrade.R;
import com.spark.newbitrade.entity.User;

import java.util.List;

/**
 * Created by Administrator on 2018/2/9.
 */

public class SwitchUserAdapter extends BaseQuickAdapter<User, BaseViewHolder> {
    public SwitchUserAdapter(int layoutResId, @Nullable List<User> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, User item) {
        helper.setText(R.id.etAccount, item.getUsername()).setVisible(R.id.ivSellect, item.isSelect());
    }
}
