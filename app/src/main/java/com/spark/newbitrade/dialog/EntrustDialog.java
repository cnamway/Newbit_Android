package com.spark.newbitrade.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.entity.Entrust;
import com.spark.newbitrade.utils.MathUtils;
import com.spark.newbitrade.utils.StringUtils;

/**
 * 点击委托列表弹出的dialog
 * Created by Administrator on 2018/1/31.
 */

public class EntrustDialog extends Dialog {
    private TextView tvCancle; // 取消
    private TextView tvType; // 类别
    private TextView tvPrice; // 价格
    private TextView tvTotal; // 总额
    private TextView tvAmount; // 数量
    private TextView tvOption; // 撤单
    private TextView tvSecOption; // 撤单后买入
    private Entrust.ListBean entrust;
    private Context context;
    private boolean isBuyEntrust;
    private int baseCoinScale;
    private int coinScale;

    public void setBaseCoinScale(int baseCoinScale, int coinScale) {
        this.baseCoinScale = baseCoinScale;
        this.coinScale = coinScale;
    }

    public EntrustDialog(Context context) {
        super(context, R.style.myDialog);
        this.context = context;
        initView();
        setListener();
    }

    public void setEntrust(Entrust.ListBean entrust) {
        this.entrust = entrust;
        initData();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int) (MyApplication.getApp().getmWidth());
        window.setAttributes(lp);
    }

    protected void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_entrust, null);
        setContentView(view);
        tvCancle = view.findViewById(R.id.tvCancle);
        tvType = view.findViewById(R.id.tvType);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvTotal = view.findViewById(R.id.tvTotal);
        tvAmount = view.findViewById(R.id.tvAmount);
        tvOption = view.findViewById(R.id.tvOption);
        tvSecOption = view.findViewById(R.id.tvSecOption);
    }

    private void initData() {
        if (StringUtils.isNotEmpty(String.valueOf(entrust.getSide()))) {
            if (entrust.getSide() == GlobalConstant.INT_BUY) {
                tvType.setText(context.getString(R.string.text_buy_in));
                tvType.setEnabled(false);
            } else {
                tvType.setText(context.getString(R.string.text_sale_out));
                tvType.setEnabled(true);
            }
        }

        if (GlobalConstant.LIMIT_PRICE.equals(entrust.getPriceType())) { // 限价
            tvPrice.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(entrust.getPrice(), baseCoinScale, null)) + entrust.getBaseSymbol());
            tvAmount.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(entrust.getOrderQty(), coinScale, null)) + entrust.getCoinSymbol());
            tvTotal.setText(String.valueOf(MathUtils.subZeroAndDot(MathUtils.getRundNumber(entrust.getPrice() * entrust.getOrderQty(), baseCoinScale, null))
                    + entrust.getBaseSymbol()));
        } else { // 市价
            tvPrice.setText(String.valueOf(context.getString(R.string.text_best_prices)));
            if (StringUtils.isNotEmpty(String.valueOf(entrust.getSide()))) {
                if (entrust.getSide() == GlobalConstant.INT_BUY) {
//                    tvAmount.setText(String.valueOf("--" + entrust.getCoinSymbol()));
                    tvAmount.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(entrust.getOrderQty(), coinScale, null)) + entrust.getCoinSymbol());
//                    tvTotal.setText(MathUtils.getRundNumber(entrust.getOrderQty(), coinScale, null) + entrust.getCoinSymbol());
                    tvTotal.setText(String.valueOf(MathUtils.subZeroAndDot(MathUtils.getRundNumber(entrust.getPrice() * entrust.getOrderQty(), baseCoinScale, null))
                            + entrust.getBaseSymbol()));
                } else {
                    tvAmount.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(entrust.getOrderQty(), coinScale, null)) + entrust.getCoinSymbol());
                    tvTotal.setText(String.valueOf(MathUtils.subZeroAndDot(MathUtils.getRundNumber(entrust.getPrice() * entrust.getOrderQty(), baseCoinScale, null))
                            + entrust.getBaseSymbol()));
                }
            }
        }
    }

    private void setListener() {
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void setOnCancelOrder(View.OnClickListener listener) {
        tvOption.setOnClickListener(listener);
    }

}
