package com.spark.newbitrade.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.library.otc.model.AdvertiseShowVo;
import com.spark.newbitrade.R;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.MathUtils;
import com.spark.newbitrade.utils.StringUtils;

import java.util.List;

/**
 * c2c列表
 * Created by Administrator on 2018/2/28.
 */

public class C2CListAdapter extends BaseQuickAdapter<AdvertiseShowVo, BaseViewHolder> {
    private Context context;

    public C2CListAdapter(Context context, int layoutResId, @Nullable List<AdvertiseShowVo> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, AdvertiseShowVo item) {
        helper.setText(R.id.tvName, item.getRealName())
                .setText(R.id.tvLimit, context.getString(R.string.text_quota) + ": " + MathUtils.subZeroAndDot(String.valueOf(item.getMinLimit())) + "~" + MathUtils.subZeroAndDot(String.valueOf(item.getMaxLimit())) + item.getLocalCurrency())
                .setText(R.id.tvPrice, MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getPrice().doubleValue(), 2, null) + item.getLocalCurrency()))
                .setText(R.id.tvTradeAmount, item.getRangeTimeOrder() + "  |  " + MathUtils.getRate(item.getRangeTimeSuccessOrder(), item.getRangeTimeOrder()))
                .setText(R.id.tvCount, context.getString(R.string.amount) + ":" + MathUtils.subZeroAndDot(item.getRemainAmount().doubleValue() + ""));

//        if (StringUtils.isNotEmpty(item.getAvatar()))
//            Glide.with(context).load(item.getAvatar()).into((ImageView) helper.getView(R.id.ivHeader));
//        else
        Glide.with(context).load(R.mipmap.icon_avatar).into((ImageView) helper.getView(R.id.ivHeader));

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
