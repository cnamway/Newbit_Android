package com.spark.newbitrade.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.newbitrade.R;
import com.spark.newbitrade.entity.Country;
import com.spark.newbitrade.entity.CountryEntity;
import com.spark.newbitrade.utils.SharedPreferenceInstance;

import java.util.List;

/**
 * 国家
 * Created by Administrator on 2018/3/1.
 */

public class CountryAdapter extends BaseQuickAdapter<CountryEntity, BaseViewHolder> {

    public CountryAdapter(int layoutResId, @Nullable List<CountryEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CountryEntity item) {
        switch (SharedPreferenceInstance.getInstance().getLanguageCode()) {
            case 1:
                helper.setText(R.id.tvname, item.getZhName());
                break;
            case 2:
                helper.setText(R.id.tvname, item.getEnName());
                break;
        }

    }
}
