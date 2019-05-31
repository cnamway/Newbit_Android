package com.spark.newbitrade.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.releaseAd.PubAdsActivity;

/**
 * 购买或者买入广告dialog
 * Created by Administrator on 2018/1/31.
 */

public class BuyOrSellDialog extends Dialog {
    private LinearLayout llSell;
    private LinearLayout llBuy;
    private ImageView ivClose;
    private Context context;

    public BuyOrSellDialog(Context context) {
        super(context, R.style.myDialog);
        this.context = context;
        initView();
        setLisener();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (MyApplication.getApp().getmWidth() * 0.8);
        dialogWindow.setAttributes(lp);
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_buy_or_sell_ads, null);
        setContentView(view);
        llSell = view.findViewById(R.id.llSell);
        llBuy = view.findViewById(R.id.llBuy);
        ivClose = view.findViewById(R.id.ivClose);
    }

    private void setLisener() {
        llBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doPubSellOrrBuy(0);
            }
        });

        llSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doPubSellOrrBuy(1);
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    /**
     * @param intType 0-buy ，1-sell
     */
    private void doPubSellOrrBuy(int intType) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", intType);
        Intent intent = new Intent(context, PubAdsActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
        dismiss();
    }

}
