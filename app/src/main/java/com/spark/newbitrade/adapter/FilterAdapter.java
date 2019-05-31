package com.spark.newbitrade.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.newbitrade.R;
import com.spark.newbitrade.entity.FilterBean;

import java.util.List;

/**
 * 提币地址
 * Created by Administrator on 2018/3/8.
 */

public class FilterAdapter extends BaseQuickAdapter<FilterBean, BaseViewHolder> {
    private Context context;

    public FilterAdapter(Context context, @Nullable List<FilterBean> data) {
        super(R.layout.item_filter, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, FilterBean item) {
        TextView tvName = helper.getView(R.id.tvName);
        tvName.setText(item.getName());
        if (!item.isSelected()) {
            tvName.setBackgroundResource(R.drawable.shape_bg_ripple_btn_global_option_normal_small_radius);
            tvName.setTextColor(context.getResources().getColor(R.color.main_font_content));
        } else {
            tvName.setBackgroundResource(R.drawable.shape_bg_normal_corner_grey_enabled);
            tvName.setTextColor(context.getResources().getColor(R.color.white));
        }
    }
}
