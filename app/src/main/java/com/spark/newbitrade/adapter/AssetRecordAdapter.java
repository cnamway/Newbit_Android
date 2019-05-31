package com.spark.newbitrade.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.base.BaseConstant;
import com.spark.newbitrade.entity.AssetRecord;
import com.spark.newbitrade.utils.DateUtils;
import com.spark.newbitrade.utils.MathUtils;

import java.util.List;

/**
 * 资产记录
 */

public class AssetRecordAdapter extends BaseQuickAdapter<AssetRecord, BaseViewHolder> {

    public AssetRecordAdapter(@Nullable List<AssetRecord> data) {
        super(R.layout.item_lv_asset_record, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AssetRecord item) {
//        1充值，2提币，3转入，4转出，5资金交易-扣除，6资金交易-增加，7法币卖出，8法币买入，9.缴纳商家认证保证金，10退回商家认证保证金
        //   type
        //        <item>充币</item>1
        //        <item>提币</item>2
        //        <item>转入</item>3
        //        <item>转出</item>4
        //        <item>资金交易-扣除</item>5
        //        <item>资金交易-增加</item>6
        int type = 6;//5资金交易-扣除 6资金交易-增加
        switch (item.getType()) {
            case 1:
                helper.setText(R.id.tvType, "充值");
                type = 6;
                break;
            case 2:
                helper.setText(R.id.tvType, "提币");
                type = 5;
                break;
            case 3:
                helper.setText(R.id.tvType, "转入");
                type = 6;
                break;
            case 4:
                helper.setText(R.id.tvType, "转出");
                type = 5;
                break;
            case 5:
                helper.setText(R.id.tvType, "资金交易扣除");
                type = 5;
                break;
            case 6:
                helper.setText(R.id.tvType, "资金交易增加");
                type = 6;
                break;
            case 7:
                helper.setText(R.id.tvType, "法币卖出");
                type = 5;
                break;
            case 8:
                helper.setText(R.id.tvType, "法币买入");
                type = 6;
                break;
            case 9:
                helper.setText(R.id.tvType, "缴纳商家认证保证金");
                type = 5;
                break;
            case 10:
                helper.setText(R.id.tvType, "退回商家认证保证金");
                type = 6;
                break;
            default:
                helper.setText(R.id.tvType, "未知类型");
                type = 6;
                break;
        }
        if (type == 5) {
            helper.setTextColor(R.id.tvBuyCanUse, MyApplication.getApp().getResources().getColor(R.color.font_red));
            helper.setText(R.id.tvBuyCanUse, "- " + MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getAmount(), BaseConstant.MONEY_FORMAT, null)) );
        } else if (type == 6) {
            helper.setTextColor(R.id.tvBuyCanUse, MyApplication.getApp().getResources().getColor(R.color.bg_btn_normal));
            helper.setText(R.id.tvBuyCanUse, MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getAmount(), BaseConstant.MONEY_FORMAT, null)) );
        } else {
            helper.setTextColor(R.id.tvBuyCanUse, MyApplication.getApp().getResources().getColor(R.color.bg_btn_normal));
            helper.setText(R.id.tvBuyCanUse, MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getAmount(), BaseConstant.MONEY_FORMAT, null)) );
        }

        helper.setText(R.id.tvFrozon, DateUtils.getFormatTime("HH:mm:ss MM/dd", item.getCreateTime()));
        helper.setText(R.id.tvFee, MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getFee(), BaseConstant.MONEY_FORMAT, null)) );
    }
}
