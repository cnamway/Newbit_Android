package com.spark.newbitrade.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.newbitrade.R;
import com.spark.newbitrade.entity.HomeNewTenBean;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.MathUtils;
import com.spark.newbitrade.utils.StringUtils;

import java.util.List;

/**
 * 首页十条列表
 * Created by Administrator on 2018/2/28.
 */

public class HomeNewTenAdapter extends BaseQuickAdapter<HomeNewTenBean, BaseViewHolder> {
    private Context context;

    public HomeNewTenAdapter(Context context, int layoutResId, @Nullable List<HomeNewTenBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeNewTenBean item) {
        helper.setText(R.id.tvName, item.getUsername())
                .setText(R.id.tvLimit, context.getString(R.string.text_quota) + ": " + StringUtils.getThousand(String.valueOf(item.getMinLimit())) + "~" + StringUtils.getThousand(String.valueOf(item.getMaxLimit())) + GlobalConstant.CNY)
                .setText(R.id.tvPrice, MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getPrice(), 2, null)) + GlobalConstant.CNY).
                setText(R.id.tvTradeAmount, /*context.getString(R.string.text_deal) + ": " + */item.getSingleCount() + "  |  " +/*context.getString(R.string.text_app_rate) + ": "+*/(StringUtils.isEmpty(item.getPercentageComplete()) ? "0.00%" : item.getPercentageComplete()))
                .setText(R.id.tvCount, context.getString(R.string.amount) + ":" + item.getRemainAmount());

        if (StringUtils.isNotEmpty(item.getAvatar()))
            Glide.with(context).load(item.getAvatar()).into((ImageView) helper.getView(R.id.ivHeader));
        else
            Glide.with(context).load(R.mipmap.icon_avatar).into((ImageView) helper.getView(R.id.ivHeader));
        String payMode = item.getPayMode();
        if (payMode.contains(GlobalConstant.alipay)) helper.setVisible(R.id.ivPay, true);
        else helper.setGone(R.id.ivPay, false);
        if (payMode.contains(GlobalConstant.wechat)) helper.setVisible(R.id.ivWeChat, true);
        else helper.setGone(R.id.ivWeChat, false);
        if (payMode.contains(GlobalConstant.card)) helper.setVisible(R.id.ivUnionPay, true);
        else helper.setGone(R.id.ivUnionPay, false);
    }
}
