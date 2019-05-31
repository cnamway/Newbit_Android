package com.spark.newbitrade.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.entity.PayWaySetting;
import com.spark.newbitrade.entity.User;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.StringUtils;

import java.util.List;

/**
 * 收款方式
 */
public class PayWayAdapter extends BaseQuickAdapter<PayWaySetting, BaseViewHolder> {

    public PayWayAdapter(@Nullable List<PayWaySetting> data) {
        super(R.layout.adapter_my_account, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PayWaySetting item) {
        switch (item.getPayType()) {
            case GlobalConstant.alipay:
                helper.setImageResource(R.id.ivType, R.mipmap.icon_pay);
                helper.setText(R.id.tvBankName, "支付宝");
                break;
            case GlobalConstant.wechat:
                helper.setImageResource(R.id.ivType, R.mipmap.icon_wechat);
                helper.setText(R.id.tvBankName, "微信");
                break;
            case GlobalConstant.card:
                helper.setImageResource(R.id.ivType, R.mipmap.icon_unionpay);
                helper.setText(R.id.tvBankName, item.getBank());
                break;
            case GlobalConstant.PAYPAL:
                helper.setImageResource(R.id.ivType, R.mipmap.icon_paypal);
                helper.setText(R.id.tvBankName, "PayPal");
                break;
            case GlobalConstant.other:
                helper.setImageResource(R.id.ivType, R.mipmap.other);
                helper.setText(R.id.tvBankName, "其他");
                break;
            default:
                helper.setImageResource(R.id.ivType, R.mipmap.other);
                helper.setText(R.id.tvBankName, "其他");
                break;
        }
        if (item.getStatus() == 1) {
            helper.getView(R.id.ivStatus).setSelected(true);
        } else {
            helper.getView(R.id.ivStatus).setSelected(false);
        }
        String name = "";
        User user = MyApplication.getApp().getCurrentUser();
        if (user != null && StringUtils.isNotEmpty(user.getRealName())) {
            name = user.getRealName();
        }
        helper.setText(R.id.tvName, name)
                .setText(R.id.tvBankNum, item.getPayAddress());

        helper.addOnClickListener(R.id.ivStatus);
    }


}
