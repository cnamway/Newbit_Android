package com.spark.newbitrade.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.spark.newbitrade.R;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.utils.GlobalConstant;

import java.util.HashMap;


/**
 * 交易界面买入或卖出的dialog
 * Created by Administrator on 2018/3/14.
 */

public class TradeBuyOrSellConfirmDialog extends Dialog {
    private TextView tvPrice;
    private TextView tvTotal;
    private TextView tvAmount;
    private TextView tvType;
    private TextView tvOption;
    private TextView tvCancle;
    private TextView tvTitle;
    private TextView tvTag;
    private String price;
    private String amount;
    private String total;
    private String type;
    private Context context;

    public TradeBuyOrSellConfirmDialog(@NonNull Context context) {
        super(context, R.style.myDialog);
        this.context = context;
        initView();
        setListener();
    }

    public void setDataParams(HashMap<String, String> map) {
        if (map != null) {
            price = map.get("price");
            amount = map.get("amount");
            total = map.get("total");
            type = map.get("type");
            initData();
        }
    }

    public TextView getTvConfirm() {
        return tvOption;
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
        tvOption.setText(context.getText(R.string.dialog_sure));
        tvCancle = view.findViewById(R.id.tvCancle);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(context.getString(R.string.dialog_two_title_buy));
        tvTag = view.findViewById(R.id.tvTag);
        tvTag.setVisibility(View.GONE);
    }


    private void setListener() {
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    protected void initData() {
        tvPrice.setText(price);
        tvAmount.setText(amount);
        tvTotal.setText(total);
        if (type.equals(GlobalConstant.BUY)) {
            tvType.setText(context.getString(R.string.text_buy_in));
            tvType.setEnabled(false);
            tvTitle.setText(context.getString(R.string.dialog_two_title_buy));
        } else {
            tvType.setText(context.getString(R.string.text_sale_out));
            tvType.setEnabled(true);
            tvTitle.setText(context.getString(R.string.dialog_two_title_sell));
        }
    }

}
