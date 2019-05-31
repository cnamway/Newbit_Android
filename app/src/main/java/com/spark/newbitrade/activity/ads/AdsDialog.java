package com.spark.newbitrade.activity.ads;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.entity.Ads;

/**
 * Created by Administrator on 2018/3/6.
 */

public class AdsDialog extends Dialog {
    private TextView tvType;
    private TextView tvLimit;
    private TextView tvRemainAmount;
    private TextView tvEdit;
    private TextView tvReleaseAgain;
    private TextView tvDelete;
    private ImageButton ivClose;
    private TextView tvCancel;
    private TextView tvShelOff;
    private Ads ads;
    private Context context;

    public AdsDialog(@NonNull Context context) {
        super(context, R.style.myDialog);
        this.context = context;
        initView();
    }

    public void setAds(Ads ads) {
        this.ads = ads;
        initData();
    }

    public TextView getTvEdit() {
        return tvEdit;
    }

    public TextView getTvReleaseAgain() {
        return tvReleaseAgain;
    }

    public TextView getTvShelOff() {
        return tvShelOff;
    }

    public TextView getTvDelete() {
        return tvDelete;
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

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_ads, null);
        setContentView(view);
        tvType = view.findViewById(R.id.tvType);
        tvLimit = view.findViewById(R.id.tvLimit);
        tvRemainAmount = view.findViewById(R.id.tvRemainAmount);
        tvEdit = view.findViewById(R.id.tvEdit);
        tvReleaseAgain = view.findViewById(R.id.tvReleaseAgain);
        tvDelete = view.findViewById(R.id.tvDelete);
        ivClose = view.findViewById(R.id.ivClose);
        tvCancel = view.findViewById(R.id.tvCancel);
        tvShelOff = view.findViewById(R.id.tvShelOff);
        setLisenter();
    }

    private void initData() {
        tvType.setText(ads.getAdvertiseType() == 0 ? MyApplication.getApp().getString(R.string.text_buy) : MyApplication.getApp().getString(R.string.text_sell));
        tvLimit.setText(ads.getMinLimit() + "~" + ads.getMaxLimit() + " " + ads.getLocalCurrency());
        tvRemainAmount.setText(ads.getRemainAmount() + " " + ads.getCoinName());
        if (ads.getStatus() == 0) {
            tvShelOff.setVisibility(View.VISIBLE);
            tvReleaseAgain.setVisibility(View.GONE);
        } else {
            tvShelOff.setVisibility(View.GONE);
            tvReleaseAgain.setVisibility(View.VISIBLE);
        }
        tvEdit.setVisibility(ads.getStatus() == 0 ? View.GONE : View.VISIBLE);
        tvDelete.setVisibility(ads.getStatus() == 0 ? View.GONE : View.VISIBLE);
    }

    private void setLisenter() {
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

}
