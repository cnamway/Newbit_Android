package com.spark.newbitrade.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;

/**
 * 点击委托列表弹出的dialog
 * Created by Administrator on 2018/1/31.
 */

public class PayWaySelectDialog extends Dialog {

    private ImageView ivBank;
    private LinearLayout llBank;
    private ImageView ivAli;
    private LinearLayout llAli;
    private ImageView ivWeChat;
    private LinearLayout llWeChat;
    private ImageView ivPalpay;
    private LinearLayout llPalpay;
    private ImageView ivOther;
    private LinearLayout llOther;
    private TextView tvOption;
    private TextView tvCancle;
    private Context context;
    private PayWaySelectListener selectListener;
    private String select = "";


    public PayWaySelectDialog(Context context,PayWaySelectListener selectListener) {
        super(context, R.style.myDialog);
        this.context = context;
        this.selectListener = selectListener;
        initView();
        setListener();
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
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_payway_view, null);
        setContentView(view);
        llAli = view.findViewById(R.id.llAli);
        llBank = view.findViewById(R.id.llBank);
        llWeChat = view.findViewById(R.id.llWeChat);
        llPalpay = view.findViewById(R.id.llPalpay);
        llOther = view.findViewById(R.id.llOther);
        ivAli = view.findViewById(R.id.ivAli);
        ivBank = view.findViewById(R.id.ivBank);
        ivWeChat = view.findViewById(R.id.ivWeChat);
        ivPalpay= view.findViewById(R.id.ivPalpay);
        ivOther = view.findViewById(R.id.ivOther);
        tvOption = view.findViewById(R.id.tvOption);
        tvCancle = view.findViewById(R.id.tvCancle);
    }

    private void setListener() {
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        llAli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = "支付宝";
                ivBank.setImageResource(R.mipmap.icon_uncheck);
                ivAli.setImageResource(R.mipmap.icon_checked);
                ivWeChat.setImageResource(R.mipmap.icon_uncheck);
                ivPalpay.setImageResource(R.mipmap.icon_uncheck);
                ivOther.setImageResource(R.mipmap.icon_uncheck);
                selectListener.getSelectPayWay(select);
            }
        });
        llBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = "银行卡";
                ivBank.setImageResource(R.mipmap.icon_checked);
                ivAli.setImageResource(R.mipmap.icon_uncheck);
                ivWeChat.setImageResource(R.mipmap.icon_uncheck);
                ivPalpay.setImageResource(R.mipmap.icon_uncheck);
                ivOther.setImageResource(R.mipmap.icon_uncheck);
                selectListener.getSelectPayWay(select);
            }
        });
        llWeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = "微信";
                ivBank.setImageResource(R.mipmap.icon_uncheck);
                ivAli.setImageResource(R.mipmap.icon_uncheck);
                ivWeChat.setImageResource(R.mipmap.icon_checked);
                ivPalpay.setImageResource(R.mipmap.icon_uncheck);
                ivOther.setImageResource(R.mipmap.icon_uncheck);
                selectListener.getSelectPayWay(select);
            }
        });

        llPalpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = "Palpay";
                ivBank.setImageResource(R.mipmap.icon_uncheck);
                ivAli.setImageResource(R.mipmap.icon_uncheck);
                ivWeChat.setImageResource(R.mipmap.icon_uncheck);
                ivPalpay.setImageResource(R.mipmap.icon_checked);
                ivOther.setImageResource(R.mipmap.icon_uncheck);
                selectListener.getSelectPayWay(select);
            }
        });

        llOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = "其他";
                ivBank.setImageResource(R.mipmap.icon_uncheck);
                ivAli.setImageResource(R.mipmap.icon_uncheck);
                ivWeChat.setImageResource(R.mipmap.icon_uncheck);
                ivPalpay.setImageResource(R.mipmap.icon_uncheck);
                ivOther.setImageResource(R.mipmap.icon_checked);
                selectListener.getSelectPayWay(select);
            }
        });
    }

    public TextView getTvOption() {
        return tvOption;
    }

    public void setView(boolean isAli,boolean isWechat,boolean isBank) {
        if (isAli) {
            llAli.setVisibility(View.VISIBLE);
        }
        if (isBank) {
            llBank.setVisibility(View.VISIBLE);
        }
        if (isWechat) {
            llWeChat.setVisibility(View.VISIBLE);
        }
    }

    public interface PayWaySelectListener{
            void getSelectPayWay(String payWay);
    }
}
